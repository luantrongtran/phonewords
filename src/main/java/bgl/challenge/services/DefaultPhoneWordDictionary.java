package bgl.challenge.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bgl.challenge.exception.UnknownCharacterException;

public class DefaultPhoneWordDictionary implements PhoneWordDictionary {

	WordEncoder wordEncoder;

	Map<Integer, String> dictionary;

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
			dictionary = new HashMap<Integer, String>();
		}

		try {
			int encoded = wordEncoder.encode(word);
			dictionary.put(encoded, word);

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
	 * @param number
	 * @return
	 */
	public boolean contain(int number) {
		if (dictionary == null) {
			return false;
		}
		return dictionary.containsKey(number);
	}

	@Override
	public List<String> getPossibleWords(int number) {
		List<String> possibleWords = new ArrayList<>();

		if (dictionary.containsKey(number)) {
			String word = dictionary.get(number);
			// Capitalize the word
			word = word.toUpperCase();
			possibleWords.add(word);
		}

		return possibleWords;
	}

//	List<String> recursiveTranslate(int number) {
//		
//		
//		int length = input.length();
//		
//		while (length >= shortestWordLength) {
//			String subStr = input.substring(0, 0 + length);
//		}
//
//		return null;
//	}

	void setShortestWordLength(int length) {
		this.shortestWordLength = length;
	}

	public int getShortestWordLength() {
		return this.shortestWordLength;
	}
}
