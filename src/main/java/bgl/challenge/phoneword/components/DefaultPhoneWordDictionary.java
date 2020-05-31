package bgl.challenge.phoneword.components;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
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
import java.util.stream.Stream;

import bgl.challenge.phoneword.exception.UnknownCharacterException;
import bgl.challenge.phoneword.models.Pattern;
import bgl.challenge.phoneword.models.SubString;

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
	 * Factory pattern
	 * 
	 * @return
	 */
	public static DefaultPhoneWordDictionary getInstance() {
		WordEncoder wordEncoder = new PhoneWordEncoder();
		SyntaxChecker phonewordSyntaxChecker = new PhonewordSyntaxChecker();
		DefaultPhoneWordDictionary instance = new DefaultPhoneWordDictionary(wordEncoder, phonewordSyntaxChecker);
		return instance;
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
			// convert to upper case
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

	/**
	 * Find all possible phonewords of the given number
	 */
	@Override
	public List<String> findPhonewords(String phoneNumber) {
		List<SubString> subStrings = findAllPossibleSubStrings(phoneNumber);

		List<Pattern> patterns = findAllValidPatterns(subStrings);

		List<String> possibleWords = patterns.stream().map(pattern -> {
			// for each pattern construct possible words
			List<String> possibleWordsPerPattern = constructPossibleWordsPerPattern(new String(phoneNumber), pattern);
			return possibleWordsPerPattern;
		}).reduce(new ArrayList<>(), (partial, lstWords) -> {
			// add all possible words per pattern into a single list
			partial.addAll(lstWords);
			return partial;
		});

		return possibleWords;
	}

	/**
	 * Construct all possible words
	 * 
	 * @param originalEncodedString
	 * @param subStrings
	 * @return
	 */
	List<String> constructPossibleWordsPerPattern(String originalEncodedString, Pattern pattern) {
		List<String> possibleWords = new ArrayList<>();

		// calculate the number of possible words
		int noOfPossibleWords = pattern.stream().map(subString -> {
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

		pattern.stream().forEach(subString -> {
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

			pattern.stream().forEach(subString -> {
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
					String temp = word.substring(0, dashIndex + 1) + "-" + word.substring(dashIndex + 1);
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
	List<Pattern> findAllValidPatterns(List<SubString> subStrings) {
		final List<Pattern> patterns = new ArrayList<>();

		/**
		 * Patterns which contains only 1 substring
		 */
		subStrings.forEach(subString -> {
			Pattern p = new Pattern(Arrays.asList(subString));
			patterns.add(p);
		});

		/**
		 * Patterns which contains > 1 substring
		 */
		for (int noOfSubStrings = 2; noOfSubStrings <= subStrings.size(); noOfSubStrings++) {
			final int constNoOfSubStrings = noOfSubStrings;
			for (int i = 0; i < subStrings.size(); i++) {
				SubString subString = subStrings.get(i);
				for (int j = i + 1; j < subStrings.size(); j++) {
					List<SubString> groupOfSubStrings = new ArrayList<>();
					groupOfSubStrings.add(subString);
					for (int k = 0; (k < constNoOfSubStrings && (j + k) < subStrings.size()); k++) {
						SubString nextSubString = subStrings.get(j + k);
						if (groupOfSubStrings.size() == constNoOfSubStrings) {
							break;
						}
						if (!subString.equals(nextSubString)) {
							groupOfSubStrings.add(nextSubString);
						}
					}

					if (groupOfSubStrings.size() == constNoOfSubStrings) {
						Pattern p = new Pattern(groupOfSubStrings);
						patterns.add(p);
					}
				}
			}
		}

		/*
		 * Filter out patterns containing conflicting sub strings.
		 */
		List<Pattern> validPatterns = patterns.stream().filter(pattern -> {
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
		 * validPatterns may contain duplicated patterns which have same substrings but
		 * different order.
		 */
		for (int i = 0; i < validPatterns.size() - 1; i++) {
			Pattern pattern = validPatterns.get(i);
			for (int j = i + 1; j < validPatterns.size(); j++) {
				Pattern nextPattern = validPatterns.get(j);
				if (pattern.equals(nextPattern)) {
					// remove duplicated
					validPatterns.remove(j);
					j--;
				}
			}
		}

		return validPatterns;
	}

	/**
	 * Find all possible numbers which are substrings of the originalPhoneNumber,
	 * and exist in the Dictionary.
	 * 
	 * @param originalPhoneNumber
	 * @return
	 */
	List<SubString> findAllPossibleSubStrings(String originalPhoneNumber) {
		List<SubString> result = new ArrayList<>();
		if (originalPhoneNumber.length() < shortestWordLength) {
			/*
			 * if the looked up encoded string is shorter than the shortest word in the
			 * dictionary
			 */
			return result;
		}

		for (int i = shortestWordLength; i <= originalPhoneNumber.length(); i++) {
			List<SubString> subStrings = findAllPossibleSubStringsWithSpecificLength(originalPhoneNumber, i);
			result.addAll(subStrings);
		}

		return result;
	}

	/**
	 * Find all possible fixed-length numbers which are substrings of the
	 * originalPhoneNumber, and exist in the Dictionary.
	 * 
	 * @param originalPhoneNumber
	 *            this is an encoded string not the original word.
	 * @param wordLength
	 * @return
	 */

	List<SubString> findAllPossibleSubStringsWithSpecificLength(String originalPhoneNumber, int wordLength) {
		List<SubString> result = new ArrayList<>();
		for (int i = 0; i <= originalPhoneNumber.length() - wordLength; i++) {
			String subStr = originalPhoneNumber.substring(i, i + wordLength);
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
				// List<String> words = dictionary.get(subStr);
				/*
				 * add all the dictionary words of the corresponding encoded into the returned
				 * result
				 */
				SubString subString = new SubString(originalPhoneNumber, subStr, start, end);
				result.add(subString);
			}
		}

		return result;
	}

	/**
	 * This is to modify the original number by replacing its substring with new
	 * string
	 * 
	 * @param originalPhoneNumber
	 * @param subString
	 * @param newString
	 * @return
	 */
	String replaceSubString(String originalPhoneNumber, SubString subString, String newString) {
		String result = originalPhoneNumber.substring(0, subString.getStart()) + newString
				+ originalPhoneNumber.substring(subString.getEnd() + 1);
		return result;
	}

	/**
	 * Import all words in a text file into the dictionary
	 */
	@Override
	public void importFromFile(File f) throws IOException {
		if (!f.exists()) {
			String errMsg = String.format("Error: failed to load the dictionary file - [%s] doesn't exist",
					f.getAbsoluteFile());
			throw new FileNotFoundException(errMsg);
		}

		try (Stream<String> lines = Files.lines(Paths.get(f.getAbsoluteFile().toURI()))) {
			lines.forEach(this::addNewWord);
		}
	}

	void setShortestWordLength(int length) {
		this.shortestWordLength = length;
	}

	public int getShortestWordLength() {
		return this.shortestWordLength;
	}
}
