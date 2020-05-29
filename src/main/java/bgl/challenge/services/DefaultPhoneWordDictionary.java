package bgl.challenge.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import bgl.challenge.exception.UnknownCharacterException;
import bgl.challenge.models.SubString;

public class DefaultPhoneWordDictionary implements PhoneWordDictionary {

	/**
	 * For encoding a word.
	 */
	WordEncoder wordEncoder;

	SyntaxChecker phonewordSyntaxChecker;

	/**
	 * Key is the encoded number, value is the corresponding words in dictionary
	 */
	Map<String, List<String>> dictionary;

	/**
	 * A pivotal recording the shortest word length
	 */
	int shortestWordLength = 0;

	public DefaultPhoneWordDictionary(WordEncoder wordEncoder, SyntaxChecker phonewordSyntaxChecker) {
		this.wordEncoder = wordEncoder;
		this.phonewordSyntaxChecker = phonewordSyntaxChecker;
		dictionary = new HashMap<>();
	}

	/**
	 * Add a new word into the dictionary
	 * 
	 * @param word
	 */
	public boolean addNewWord(String word) {
		// Not accept null or empty string
		if (word == null || word.isEmpty()) {
			return false;
		}

		if (dictionary == null) {
			dictionary = new HashMap<String, List<String>>();
		}

		try {
			// store upper case
			word = word.toUpperCase();
			String encoded = wordEncoder.encode(word) + "";
			if (!dictionary.containsKey(encoded)) {
				List<String> words = new ArrayList<>();
				words.add(word);
				dictionary.put(encoded, words);
			} else {
				List<String> words = dictionary.get(encoded);
				words.add(word);
			}

			if (this.getShortestWordLength() == 0) {
				// if it is the first word
				this.setShortestWordLength(word.length());
			} else if (word.length() < this.shortestWordLength) {
				// if the new word's length is shorter
				setShortestWordLength(word.length());
			}
		} catch (UnknownCharacterException ex) {
			String warnMsg = String.format("Warning - Cannot add word [%s] into the library - ", word);
			warnMsg += ex.getMessage();
			System.out.println(warnMsg);
			return false;
		}
		return true;
	}

	/**
	 * Checking if a number is in the dictionary
	 * 
	 * @param encodedNumber
	 *            this is the encoded number not the word
	 * @return
	 */
	public boolean contains(String encodedNumber) {
		if (dictionary == null) {
			return false;
		}
		return dictionary.containsKey(encodedNumber);
	}

	@Override
	public List<String> findPhonewords(String encodedNumber) {
		List<SubString> subStrings = findAllPossibleWords(encodedNumber);
		List<List<SubString>> groupsOfPossibleWords = groupPossibleEncodedString(subStrings);

		List<String> possibleWords = groupsOfPossibleWords.stream().map(lstSubStrings -> {
			List<String> possibleWordsInGroup = constructPossibleWords(new String(encodedNumber), lstSubStrings);
			return possibleWordsInGroup;
		}).reduce(new ArrayList<>(), (partial, lstWords) -> {
			partial.addAll(lstWords);
			return partial;
		});

		return possibleWords;
	}

	/**
	 * Construct all possible words (not encoded)
	 * 
	 * @param originalEncodedString
	 * @param subStrings
	 * @return
	 */
	List<String> constructPossibleWords(String originalEncodedString, List<SubString> subStrings) {
		List<String> possibleWords = new ArrayList<>();

		List<List<SubString>> groupOfPossibleWords = groupPossibleEncodedString(subStrings);

		groupOfPossibleWords.stream().forEach(lstSubStrings -> {
			// calculate the number of possible words
			int noOfPossibleWords = lstSubStrings.stream().map(subString -> {
				String encodedString = subString.getValue();
				List<String> dictionaryWords = dictionary.get(encodedString);
				return dictionaryWords.size();
			}).reduce(1, (total, noOfWordsPerEncoding) -> {
				total *= noOfWordsPerEncoding;
				return total;
			});

			// Create a list containing possible word and
			final List<String> possibleWordsOfCurrentGroup = new ArrayList<>(noOfPossibleWords);
			for (int i = 0; i < noOfPossibleWords; i++) {
				// initialize all the value to be the originalEncodedString
				possibleWordsOfCurrentGroup.add(new String(originalEncodedString));
			}

			lstSubStrings.stream().forEach(subString -> {
				String encodedString = subString.getValue();
				List<String> dictionaryWords = dictionary.get(encodedString);
				for (int i = 0; i < possibleWordsOfCurrentGroup.size();) {
					for (int j = 0; j < dictionaryWords.size(); j++, i++) {
						String correspondingWord = dictionaryWords.get(j);
						String newStr = this.replaceSubString(possibleWordsOfCurrentGroup.get(i), subString,
								correspondingWord);
						possibleWordsOfCurrentGroup.set(i, newStr);
					}
				}
			});

			/*
			 * Format the word to meet requirements - e.g. dashes between words; or no 2
			 * consecutive digites
			 */
			possibleWordsOfCurrentGroup.stream().filter(phonewordSyntaxChecker::isValid).forEach(word -> {
				Set<Integer> dashIndexes = new TreeSet<>(new Comparator<Integer>() {
					@Override
					public int compare(Integer o1, Integer o2) {
						return -o1.compareTo(o2);
					}
				});

				lstSubStrings.forEach(subString -> {
					dashIndexes.add(subString.getStart());
					dashIndexes.add(subString.getEnd());
				});

				Iterator<Integer> iter = dashIndexes.iterator();
				boolean rightDash = true;
				while (iter.hasNext()) {
					int dashIndex = iter.next();
					if (dashIndex == word.length() - 1) {
						rightDash = !rightDash;
						continue;
					} else if (dashIndex == 0) {
						rightDash = !rightDash;
						continue;
					}

					if (rightDash) {
						String temp = word.substring(0, dashIndex+1) + "-" + word.substring(dashIndex + 1);
						word = temp;
					} else {
						String temp = word.substring(0, dashIndex) + "-" + word.substring(dashIndex);
						word = temp;
					}
					 
					rightDash = !rightDash;
				}

				// The word may contain 2 consecutive dashes
				word = word.replace("--", "-");
				possibleWords.add(word);
			});
		});

		return possibleWords;
	}

	/**
	 * Given a list of subStrings which are encoded strings, the method will group
	 * subStrings which are not overlapped each other.
	 * 
	 * The main point of this method is it lists all valid groups of substrings (and
	 * unnecessary strings which are not a concerned for now)
	 * 
	 * @param originalEncoded
	 * @param subStrings
	 * @return
	 */
	List<List<SubString>> groupPossibleEncodedString(List<SubString> subStrings) {
		final List<List<SubString>> patterns = new ArrayList<>();

		subStrings.forEach(subString -> {
			patterns.add(Arrays.asList(subString));
		});

		for (int noOfSubStrings = 2; noOfSubStrings <= subStrings.size(); noOfSubStrings++) {
			final int constNoOfSubStrings = noOfSubStrings;
			for (int i = 0; i < subStrings.size(); i++) {
				SubString subString = subStrings.get(i);
				for (int j = i + 1; j < subStrings.size(); j++) {
					List<SubString> group = new ArrayList<>();
					group.add(subString);
					for (int k = 0; (k < constNoOfSubStrings && (j + k) < subStrings.size()); k++) {
						SubString nextSubString = subStrings.get(j + k);
						if (group.size() == constNoOfSubStrings) {
							break;
						}
						if (!subString.equals(nextSubString)) {
							group.add(nextSubString);
						}
					}

					if (group.size() == constNoOfSubStrings) {
						patterns.add(group);
					}
				}
			}
		}

		/*
		 * Filter out patterns containing conflicting sub strings.
		 */
		List<List<SubString>> validPatterns = patterns.stream().filter(pattern -> {
			boolean isValidPattern = !pattern.stream().map(subString -> {
				boolean conflictEmerged = pattern.stream().map(otherSubString -> {
					if (subString.equals(otherSubString)) {
						// the same substring
						return false;
					}
					return subString.conflictWith(otherSubString);
				}).reduce(false, (conflictOccurredWithSubString, currentConflict) -> {
					return conflictOccurredWithSubString | currentConflict;
				});
				return conflictEmerged;
			}).reduce(false, (anyConflict, conf) -> {
				return anyConflict | conf;
			});
			return isValidPattern;
		}).collect(Collectors.toList());

		/*
		 * validPatterns may contain duplicate pairs which have same substrings but
		 * different order.
		 */
		for (int i = 0; i < validPatterns.size() - 1; i++) {
			List<SubString> pattern = validPatterns.get(i);
			for (int j = i + 1; j < validPatterns.size(); j++) {
				List<SubString> nextPattern = validPatterns.get(j);
				if (pattern.size() == nextPattern.size()) {
					if (pattern.containsAll(nextPattern)) {
						// duplicated
						validPatterns.remove(j);
						j--;
					}
				}
			}
		}

		return validPatterns;
	}

	/**
	 * Find all possible encoded string (of the words in the dictionary) which are
	 * substrings of the provided encoded string
	 * 
	 * @param encodedOriginalString
	 * @return
	 */
	List<SubString> findAllPossibleWords(String encodedOriginalString) {
		List<SubString> result = new ArrayList<>();
		if (encodedOriginalString.length() < shortestWordLength) {
			/*
			 * if the looked up encoded string is shorter than the shortest word in the
			 * dictionary
			 */
			return result;
		}

		for (int i = shortestWordLength; i <= encodedOriginalString.length(); i++) {
			List<SubString> subStrings = findAllPossibleWordsWithSpecificLength(encodedOriginalString, i);
			result.addAll(subStrings);
		}

		return result;
	}

	/**
	 * Find all possible encoded string (of the words in the dictionary) which are
	 * substrings of the provided encoded string
	 * 
	 * @param encodedOriginalString
	 *            this is an encoded string not the original word.
	 * @param wordLength
	 * @return
	 */

	List<SubString> findAllPossibleWordsWithSpecificLength(String encodedOriginalString, int wordLength) {
		List<SubString> result = new ArrayList<>();
		for (int i = 0; i <= encodedOriginalString.length() - wordLength; i++) {
			String subStr = encodedOriginalString.substring(i, i + wordLength);
			/*
			 * The start index (inclusive) of the substring within the original string
			 */
			final int start = i;
			/*
			 * The end index (inclusive) of the substring within the original string
			 */
			final int end = i + wordLength - 1;
			if (dictionary.containsKey(subStr)) {
				// if the substring match any word in the dictionary
				List<String> words = dictionary.get(subStr);
				/*
				 * add all the dictionary words of the corresponding encoded into the returned
				 * result
				 */
				SubString subString = new SubString(encodedOriginalString, subStr, start, end);
				result.add(subString);
			}
		}

		return result;
	}

	String replaceSubString(String originalEncodedString, SubString subString, String newString) {
		String result = originalEncodedString.substring(0, subString.getStart()) + newString
				+ originalEncodedString.substring(subString.getEnd() + 1);
		return result;
	}

	void setShortestWordLength(int length) {
		this.shortestWordLength = length;
	}

	public int getShortestWordLength() {
		return this.shortestWordLength;
	}
}
