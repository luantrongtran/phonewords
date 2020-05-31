package bgl.challenge.phoneword.components;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import bgl.challenge.phoneword.exception.UnknownCharacterException;
import bgl.challenge.phoneword.models.Pattern;
import bgl.challenge.phoneword.models.SubString;
import bgl.challenge.phoneword.utils.StringUtils;

public class DefaultPhoneWordDictionary implements PhoneWordDictionary {

	WordEncoder wordEncoder;

	SyntaxChecker phonewordSyntaxChecker;

	PhonewordFormatter phonewordFormatter;

	/**
	 * Key is the encoded number, value is the corresponding words in dictionary
	 */
	Map<String, List<String>> dictionary;

	/**
	 * A pivotal recording the shortest word length
	 */
	int shortestWordLength = 0;

	public DefaultPhoneWordDictionary(WordEncoder wordEncoder, SyntaxChecker phonewordSyntaxChecker,
			PhonewordFormatter phonewordFormatter) {
		this.wordEncoder = wordEncoder;
		this.phonewordSyntaxChecker = phonewordSyntaxChecker;
		this.phonewordFormatter = phonewordFormatter;
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
		PhonewordFormatter phonewordFormatter = new DefaultPhonewordFormatter();
		DefaultPhoneWordDictionary instance = new DefaultPhoneWordDictionary(wordEncoder, phonewordSyntaxChecker,
				phonewordFormatter);
		return instance;
	}

	/**
	 * Add a new word into the dictionary
	 * 
	 * @param word
	 * @return true if the given word was successfully encoded and added into the
	 *         dictionary
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
			/*
			 * Adding the word into the dictionary
			 */
			word = word.toUpperCase();
			String correspondingNumber = encodesWord(word);
			if (!containsNumber(correspondingNumber)) {
				List<String> words = new ArrayList<>();
				words.add(word);
				dictionary.put(correspondingNumber, words);
			} else {
				// If duplicated number, add it into the existing bucket
				List<String> words = dictionary.get(correspondingNumber);
				words.add(word);
			}

			/*
			 * Monitoring and updating the shortest word length
			 */
			if (this.getShortestWordLength() == 0) {
				// if it is the first word
				setShortestWordLength(word.length());
			} else if (word.length() < this.shortestWordLength) {
				// if the new word's length is shorter
				setShortestWordLength(word.length());
			}
		} catch (UnknownCharacterException ex) {
			/*
			 * If the word contains characters which cannot be encoded
			 */
			String warnMsg = String.format("Warning - Cannot add word [%s] into the library - %s", word,
					ex.getMessage());
			System.out.println(warnMsg);
			return false;
		}
		return true;
	}

	/**
	 * Find all possible phonewords of the given number
	 */
	@Override
	public List<String> findPhonewords(String phoneNumber) {
		final String formatedPhoneNumber = StringUtils.removeSpacesAndPunctuations(phoneNumber);
		/*
		 * First find all possible patterns
		 */
		List<Pattern> patterns = findAllValidPatterns(formatedPhoneNumber);

		/*
		 * find all phonewords based on the patterns and the words in the dictionary
		 */
		List<String> possibleWords = patterns.stream().flatMap(pattern -> {
			// for each pattern construct possible words
			List<String> possibleWordsPerPattern = constructPhonewordsPerPattern(formatedPhoneNumber, pattern);
			return possibleWordsPerPattern.stream();
		}).collect(Collectors.toList());

		return possibleWords;
	}

	/**
	 * Construct all possible phonewords of the given pattern based on the words in
	 * dictionary
	 * 
	 * @param originalPhoneNumber
	 * @param subStrings
	 * @return
	 */
	List<String> constructPhonewordsPerPattern(String originalPhoneNumber, Pattern pattern) {
		/*
		 * Create a list to save possible words.
		 */
		final List<String> possibleWordsOfTheGivenPattern = calculateNumberOfPossibleWords(pattern);

		/*
		 * Composing possible words by replacing dictionary words with the substrings of
		 * of the given Pattern
		 */
		pattern.stream().forEach(subString -> {
			String encodedString = subString.getValue();
			List<String> dictionaryWords = dictionary.get(encodedString);
			for (int i = 0; i < possibleWordsOfTheGivenPattern.size();) {
				for (int j = 0; j < dictionaryWords.size(); j++, i++) {
					String correspondingWord = dictionaryWords.get(j);
					String newStr = this.replaceSubString(possibleWordsOfTheGivenPattern.get(i), subString,
							correspondingWord);
					possibleWordsOfTheGivenPattern.set(i, newStr);
				}
			}
		});

		/*
		 * Process the possible words found in previous step
		 */
		List<String> phonewords = possibleWordsOfTheGivenPattern.stream()
				// filter invalid words
				.filter(this::isValidPhoneword)
				// format valid phonewords - e.g. adding dashes between words
				.map(word -> {
					String formattedWord = formatPhoneword(word, pattern);
					return formattedWord;
				}).collect(Collectors.toList());

		return phonewords;
	}

	/**
	 * Find all valid patterns of the given phone number
	 * 
	 * @param originalEncoded
	 * @param subStrings
	 * @return
	 */
	List<Pattern> findAllValidPatterns(String originalPhoneNumber) {
		List<Pattern> patterns = new ArrayList<>();

		/*
		 * Firstly, find all possible patterns of the given phone number
		 */
		patterns = findAllPossiblePatterns(originalPhoneNumber);

		/*
		 * Filter out patterns containing conflicting sub strings.
		 */
		List<Pattern> validPatterns = patterns.stream().filter(pattern -> pattern.isValid())
				.collect(Collectors.toList());

		/*
		 * remove duplicated patterns
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
	 * Find all possible patterns of the given phone number
	 * 
	 * @param originalPhoneNumber
	 * @param subStrings
	 * @return
	 */
	List<Pattern> findAllPossiblePatterns(String originalPhoneNumber) {
		final List<Pattern> allPossiblePatterns = new ArrayList<>();

		/*
		 * Find all the substrings within the given phone numbers which exist in the
		 * dictionary
		 */
		List<SubString> subStrings = findAllPossibleSubStrings(originalPhoneNumber);

		/*
		 * Find patterns which contains only 1 substring
		 */
		subStrings.forEach(subString -> {
			Pattern p = new Pattern(originalPhoneNumber, Arrays.asList(subString));
			allPossiblePatterns.add(p);
		});

		/*
		 * Find patterns which contains > 1 substring
		 */
		for (int noOfSubStrings = 2; noOfSubStrings <= subStrings.size(); noOfSubStrings++) {
			// for each length
			final int constNoOfSubStrings = noOfSubStrings;
			for (int i = 0; i < subStrings.size(); i++) {
				// for each substring in the pattern
				SubString subStringMustBeInPattern = subStrings.get(i);
				// find the pattern containing the substring
				for (int j = i + 1; j < subStrings.size(); j++) {
					List<SubString> groupOfSubStrings = new ArrayList<>();
					groupOfSubStrings.add(subStringMustBeInPattern);
					for (int k = 0; (k < constNoOfSubStrings && (j + k) < subStrings.size()); k++) {
						SubString nextSubString = subStrings.get(j + k);
						if (groupOfSubStrings.size() == constNoOfSubStrings) {
							break;
						}
						if (!subStringMustBeInPattern.equals(nextSubString)) {
							groupOfSubStrings.add(nextSubString);
						}
					}

					if (groupOfSubStrings.size() == constNoOfSubStrings) {
						Pattern p = new Pattern(originalPhoneNumber, groupOfSubStrings);
						allPossiblePatterns.add(p);
					}
				}
			}
		}

		return allPossiblePatterns;
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
	 * Encode a word
	 * 
	 * @param word
	 * @return
	 */
	String encodesWord(String word) {
		String encodedNumber = wordEncoder.encode(word) + "";
		return encodedNumber.toUpperCase();
	}

	/**
	 * @param phoneword
	 * @return
	 */
	boolean isValidPhoneword(String phoneword) {
		boolean result = phonewordSyntaxChecker.isValid(phoneword);
		return result;
	}

	/**
	 * Import all words in a text file into the dictionary
	 */
	@Override
	public void importFromFile(File f) throws IOException {
		if (!f.exists()) {
			String errMsg = String.format("Error: failed to load the dictionary file - File not found - [%s] doesn't exist",
					f.getAbsoluteFile());
			throw new FileNotFoundException(errMsg);
		}

		/*
		 * counting lines in dictionary files
		 */
		int noOfLines = 30;
		try (Stream<String> lines = Files.lines(Paths.get(f.getAbsoluteFile().toURI()))) {
			noOfLines = (int)lines.count();
		}
		
		/*
		 * initialize the hash map the same size as the number of lines.
		 */
		dictionary = new HashMap<>(noOfLines);
		try (Stream<String> lines = Files.lines(Paths.get(f.getAbsoluteFile().toURI()))) {
			lines.forEach(this::addNewWord);
		}
		
	}

	/**
	 * Given a pattern -> calculate the number of possible words can be generated
	 * from the dictionary
	 * 
	 * @param pattern
	 * @return a List which has the length is the number of possible words; also
	 *         each element is the original phone number.
	 */
	List<String> calculateNumberOfPossibleWords(Pattern pattern) {
		/*
		 * calculate the number of possible words
		 */
		int noOfPossibleWords = pattern.stream().map(subString -> {
			String encodedString = subString.getValue();
			List<String> dictionaryWords = dictionary.get(encodedString);
			return dictionaryWords.size();
		}).reduce(1, (total, noOfWordsPerEncoding) -> {
			total *= noOfWordsPerEncoding;
			return total;
		});

		/*
		 * Create a list has the length which is the number of possible words; and each
		 * element is the original phone number
		 */
		final List<String> possibleWordsOfTheGivenPattern = new ArrayList<>(noOfPossibleWords);
		for (int i = 0; i < noOfPossibleWords; i++) {
			// and initialize all the value to be the originalEncodedString
			possibleWordsOfTheGivenPattern.add(new String(pattern.getOriginalPhoneNumber()));
		}

		return possibleWordsOfTheGivenPattern;
	}

	/**
	 * Format a phoneword - for example, adding dashes between words.
	 * 
	 * @param word
	 *            the word need formatting
	 * @param pattern
	 *            the pattern from which the word was generated
	 * @return a formatted phone words
	 * 
	 *         <p>
	 *         For example:
	 *         </p>
	 *         </p>
	 *         - Given a pattern [{222},{333}] and the phoneword is AAADDD
	 *         </p>
	 *         <p>
	 *         - The formatted word is AAA-DDD
	 *         </p>
	 */
	String formatPhoneword(String word, Pattern pattern) {
		String formattedWord = phonewordFormatter.format(word, pattern);
		return formattedWord;
	}

	/**
	 * Checking if a number is in the dictionary
	 * 
	 * @param encodedNumber
	 *            this is the encoded/phone number not the word
	 * @return
	 */
	boolean containsNumber(String encodedNumber) {
		if (dictionary == null) {
			return false;
		}
		return dictionary.containsKey(encodedNumber);
	}

	/*
	 * Getter - Setter
	 */
	void setShortestWordLength(int length) {
		this.shortestWordLength = length;
	}

	public int getShortestWordLength() {
		return this.shortestWordLength;
	}
}
