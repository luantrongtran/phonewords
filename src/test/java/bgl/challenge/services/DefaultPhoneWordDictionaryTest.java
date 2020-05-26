package bgl.challenge.services;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import bgl.challenge.models.SubString;

public class DefaultPhoneWordDictionaryTest {

	DefaultPhoneWordDictionary dictionary;

	@BeforeEach
	protected void beforeEach() throws Exception {
		WordEncoder wordEncoder = new PhoneWordEncoder();
		dictionary = new DefaultPhoneWordDictionary(wordEncoder);
	}

	@Test
	public void testGetPossibleWords_GivenANotExactWord_WhenFindAValidNumber_ThenReturnCorrectWord_1() {
		// Given
		// a 1-word Dictionary
		String dictionaryWord = "KItty";
		dictionary.addNewWord(dictionaryWord);

		// When
		String input = "54889";
		List<String> actualWords = dictionary.getPossibleWords(input);

		// Then
		String expectedWord = "KITTY-1";
		assertThat(actualWords.size()).isEqualTo(1);
		String actualWord = actualWords.get(0);

		assertThat(actualWord).isEqualTo(expectedWord);
	}

	@Test
	public void testFindAllPossibleWords() {
		// Given
		String firstDictionaryWord = "Star";
		String encodedFirstDictionaryWord = "7827";
		dictionary.addNewWord(firstDictionaryWord);
		String secondDictionaryWord = "wars";
		String encodedSecondDictionaryWord = "9277";
		dictionary.addNewWord(secondDictionaryWord);

		String encodedOriginalString = "78279277";
		// when
		List<SubString> subStrings = dictionary.findAllPossibleWords(encodedOriginalString);

		// then
		int expectedNoOfSubStrings = 2;
		assertThat(subStrings.size()).isEqualTo(expectedNoOfSubStrings);
		
		SubString first = subStrings.get(0);
		assertThat(first.getValue()).isEqualTo(encodedFirstDictionaryWord);
		
		SubString second = subStrings.get(1);
		assertThat(second.getValue()).isEqualTo(encodedSecondDictionaryWord);
	}

	@Test
	public void testFindAllPossibleWordsWithSpecificLength() {
		// given
		String dictionaryWord = "abc";
		String encodedDictionaryWord = "222";
		dictionary.addNewWord(dictionaryWord);

		String encodedOriginalString = "222333";
		int wordLength = 3;

		// when
		List<SubString> lstSubStrings = dictionary.findAllPossibleWordsWithSpecificLength(encodedOriginalString,
				wordLength);

		// then
		/// Should find 1 substring
		int expectedNoOfSubStrings = 1;
		assertThat(lstSubStrings.size()).isEqualTo(expectedNoOfSubStrings);
		/// The substring info should
		SubString subString = lstSubStrings.get(0);
		/// should match the word in dictionary
		assertThat(subString.getValue()).isEqualTo(encodedDictionaryWord);
		/// The substring start index is 0
		assertThat(subString.getStart()).isEqualTo(0);
		/// and end at index 2
		assertThat(subString.getEnd()).isEqualTo(2);
	}

	@Test
	public void testFindAllPossibleWordsWithSpecificLength_2() {
		// given
		String dictionaryWord1 = "Hello";
		String encodedDictionaryWord1 = "43556";
		String dictionaryWord2 = "Kitty";
		String encodedDictionaryWord2 = "54889";
		dictionary.addNewWord(dictionaryWord1);
		dictionary.addNewWord(dictionaryWord2);

		String encodedOriginalString = "4355611154889";
		int wordLength = 5;

		// when
		List<SubString> lstSubStrings = dictionary.findAllPossibleWordsWithSpecificLength(encodedOriginalString,
				wordLength);

		// then
		/// Should have 2 substrings
		int expectedNoOfSubStrings = 2;
		assertThat(lstSubStrings.size()).isEqualTo(expectedNoOfSubStrings);
		/// 1st substring
		SubString first = lstSubStrings.get(0);
		assertThat(first.getValue()).isEqualTo(encodedDictionaryWord1);
		assertThat(first.getStart()).isEqualTo(0);
		assertThat(first.getEnd()).isEqualTo(4);

		SubString second = lstSubStrings.get(1);
		assertThat(second.getValue()).isEqualTo(encodedDictionaryWord2);
		assertThat(second.getStart()).isEqualTo(8);
		assertThat(second.getEnd()).isEqualTo(12);
	}

	@Test
	public void testGetPossibleWords_GivenAExactWord_WhenFindAValidNumber_ThenReturnCorrectWord_1() {
		// Given
		// a 1-word Dictionary
		String dictionaryWord = "Hello";
		dictionary.addNewWord(dictionaryWord);

		String input = "43556";

		// When
		List<String> actualWords = dictionary.getPossibleWords(input);

		// Then
		assertThat(actualWords.size()).isEqualTo(1);

		// Assert that the actual word is as expected; also the actual word is all
		// capital
		String expectedWord = "HELLO";
		String actualWord = actualWords.get(0);
		assertThat(actualWord).isEqualTo(expectedWord);
	}

	@Test
	public void testGetPossibleWords_GivenAExactWord_WhenFindAValidNumber_ThenReturnCorrectWord_2() {
		// Given
		// a 1-word Dictionary
		String dictionaryWord = "KItty";
		dictionary.addNewWord(dictionaryWord);

		// When
		String input = "54889";
		List<String> actualWords = dictionary.getPossibleWords(input);

		// Then
		String expectedWord = "KITTY";
		assertThat(actualWords.size()).isEqualTo(1);
		String actualWord = actualWords.get(0);

		assertThat(actualWord).isEqualTo(expectedWord);
	}

	@Test
	public void testAddNewWord_WhenAddingAWord_ThenFoundItInDictionary() {
		// Given
		String word = "Hello";
		String expextedEncodedNumber = "43556";

		// When
		boolean isWordAdded = dictionary.addNewWord(word);

		// Then
		assertThat(isWordAdded).isTrue();
		// double check the word is in the dictionary
		assertThat(dictionary.contains(expextedEncodedNumber)).isTrue();
	}

	@Test
	public void testAddNewWord_WhenAddingAWord_ThenFoundItInDictionary_2() {
		// Given
		String word = "aAaA";
		String expextedEncodedNumber = "2222";

		// When
		boolean isWordAdded = dictionary.addNewWord(word);

		// Then
		assertThat(isWordAdded).isTrue();
		// double check the word is in the dictionary
		assertThat(dictionary.contains(expextedEncodedNumber)).isTrue();
	}

	@Test
	public void testAdd2NewWords_WhenAddingAWord_ThenFoundItInDictionary_2() {
		// Given
		String dictionaryWord1 = "aAaA";
		String expectedEncodedNumber1 = "2222";

		String dictionaryWord2 = "aBc";
		String expectedEncodedNumber2 = "223";

		// When
		boolean is1stWordAdded = dictionary.addNewWord(dictionaryWord1);
		boolean is2ndWordAdded = dictionary.addNewWord(dictionaryWord2);

		// Then
		assertThat(is1stWordAdded).isTrue();
		// double check the word is in the dictionary
		assertThat(dictionary.contains(expectedEncodedNumber1)).isTrue();

		assertThat(is2ndWordAdded).isTrue();
		assertThat(dictionary.contains(expectedEncodedNumber2));
	}

	@Test
	public void testAddNewWord_GivenEmptyString_ThenReturnFalse() {
		// Given
		String emptyString = "";

		// When
		boolean isWordAdded = dictionary.addNewWord(emptyString);

		// Then
		assertThat(isWordAdded).isFalse();
	}

	@Test
	public void testAddNewWord_GivenNull_ThenReturnFalse() {
		// Given
		String emptyString = null;

		// When
		boolean isWordAdded = dictionary.addNewWord(emptyString);

		// Then
		assertThat(isWordAdded).isFalse();
	}

	@Test
	public void testGetShortestWordLength_TheShortestLengthIs2() {
		dictionary.addNewWord("aa");
		dictionary.addNewWord("TETE");

		int expectedShortestLength = 2;
		assertThat(dictionary.getShortestWordLength()).isEqualTo(expectedShortestLength);
	}

	@Test
	public void testGetShortestWordLength_TheShortestLengthIs3() {
		dictionary.addNewWord("ABDSDS");
		dictionary.addNewWord("ABC");
		dictionary.addNewWord("ZYEWRTE");

		int expectedShortestLength = 3;
		assertThat(dictionary.getShortestWordLength()).isEqualTo(expectedShortestLength);
	}
}
