package bgl.challenge.phoneword.components;

import java.util.HashMap;
import java.util.Map;

import bgl.challenge.phoneword.exception.UnknownCharacterException;

public class PhoneWordEncoder implements WordEncoder {

	Map<Character, Integer> dictionary;

	public PhoneWordEncoder() {
		initDictionaryMap();
	}

	@Override
	public int encode(String originalWord) {
		String word = originalWord.toLowerCase();
		int result = 0;
		for (int i = 0; i < word.length(); i++) {
			Character c = word.charAt(i);

			try {
				int code = dictionary.get(c);
				result = 10 * result + code;
			} catch (NullPointerException exception) {
				String errMsg = String.format("Cannot encode string [%s] which contain " + "unsupported character [%c]",
						originalWord, c);
				throw new UnknownCharacterException(errMsg);
			}
		}
		return result;
	}

	private void initDictionaryMap() {
		dictionary = new HashMap<Character, Integer>();
		dictionary.put('a', 2);
		dictionary.put('b', 2);
		dictionary.put('c', 2);

		dictionary.put('d', 3);
		dictionary.put('e', 3);
		dictionary.put('f', 3);

		dictionary.put('g', 4);
		dictionary.put('h', 4);
		dictionary.put('i', 4);

		dictionary.put('j', 5);
		dictionary.put('k', 5);
		dictionary.put('l', 5);

		dictionary.put('m', 6);
		dictionary.put('n', 6);
		dictionary.put('o', 6);

		dictionary.put('p', 7);
		dictionary.put('q', 7);
		dictionary.put('r', 7);
		dictionary.put('s', 7);

		dictionary.put('t', 8);
		dictionary.put('u', 8);
		dictionary.put('v', 8);

		dictionary.put('w', 9);
		dictionary.put('x', 9);
		dictionary.put('y', 9);
		dictionary.put('z', 9);
	}
}
