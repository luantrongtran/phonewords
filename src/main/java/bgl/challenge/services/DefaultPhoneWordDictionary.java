package bgl.challenge.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bgl.challenge.exception.UnknownCharacterException;

public class DefaultPhoneWordDictionary implements PhoneWordDictionary {

	WordEncoder wordEncoder;

	/**
	 * Key is the encoded number, value is the corresponding words in dictionary
	 */
	Map<String, List<String>> dictionary;

	/**
	 * A pivotal recording the shortest word length
	 */
	int shortestWordLength = 0;

	public DefaultPhoneWordDictionary(WordEncoder wordEncoder) {
		this.wordEncoder = wordEncoder;
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
			// store uppercase
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
				// if first word
				this.setShortestWordLength(word.length());
			} else if (word.length() < this.shortestWordLength) {
				// if new word length shorter
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
	public boolean contain(String encodedNumber) {
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

	// List<String> getAllPossibleWordsOfSpecificLength(String originalString, int
	// wordLength) {
	// for (int i = 0; i <= originalString.length() - wordLength; i++) {
	// String subStr = originalString.substring(i, i + wordLength);
	// if (dictionary.containsKey(key)) {
	//
	// }
	// }
	//
	// return null;
	// }

	void setShortestWordLength(int length) {
		this.shortestWordLength = length;
	}

	public int getShortestWordLength() {
		return this.shortestWordLength;
	}
}
