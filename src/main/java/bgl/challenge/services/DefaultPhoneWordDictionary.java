package bgl.challenge.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	public List<String> getPossibleWords(String encodedNumber) {
		List<String> possibleWords = new ArrayList<>();

		if (dictionary.containsKey(encodedNumber)) {
			List<String> words = dictionary.get(encodedNumber);
			// Capitalize the word
			return words;
		}

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

		for (int i = 0; i < subStrings.size(); i++) {
			SubString subString = subStrings.get(i);

			//
			String encodedString = subString.getValue();
			List<String> correspondingDictionaryWords = dictionary.get(encodedString);
			correspondingDictionaryWords.stream().forEach(dicWord -> {
				String temp = new StringBuilder(" ").append(dicWord).append(" ").toString();
				String possibleWord = originalEncodedString.replace(subString.getValue(), temp);
				// Replace spaces with dashes
				possibleWord = possibleWord.trim().replace(" ", "-");

				// Checking if this is a valid phoneword
				boolean isValidWord = phonewordSyntaxChecker.isValid(possibleWord);
				if (isValidWord) {
					possibleWords.add(possibleWord);
				}
			});
		}

		return possibleWords;
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
				words.stream().forEach(prod -> {
					SubString subString = new SubString(encodedOriginalString, subStr, start, end);
					result.add(subString);
				});
			}
		}

		return result;
	}

	void setShortestWordLength(int length) {
		this.shortestWordLength = length;
	}

	public int getShortestWordLength() {
		return this.shortestWordLength;
	}
}
