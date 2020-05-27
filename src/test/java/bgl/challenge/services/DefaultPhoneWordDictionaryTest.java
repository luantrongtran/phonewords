package bgl.challenge.services;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import bgl.challenge.models.SubString;

public class DefaultPhoneWordDictionaryTest {

	DefaultPhoneWordDictionary dictionary;

	@BeforeEach
	protected void beforeEach() throws Exception {
		WordEncoder wordEncoder = new PhoneWordEncoder();
		SyntaxChecker syntaxChecker = new PhonewordSyntaxChecker();
		dictionary = new DefaultPhoneWordDictionary(wordEncoder, syntaxChecker);
	}

	public void testConstructPossibleWords_2Encoding_2PossibleWord() {
		// Overlapping encoding produces only 1 possible word
	}
	
	@Test
	public void testConstructPossibleWords_2Encoding_1PossibleWord() {
		// Given
		dictionary.addNewWord("STAR");
		dictionary.addNewWord("WAR");

		String originalEncodedString = "782792771";
		String expectedPossibleWord = "STAR-WAR-1";

		SubString subString1 = new SubString(originalEncodedString, "7827", 0, 3);
		SubString subString2 = new SubString(originalEncodedString, "927", 5, 6);
		List<SubString> subStrings = new ArrayList<>();
		subStrings.add(subString1);
		subStrings.add(subString2);

		// when
		List<String> possibleWords = dictionary.constructPossibleWords(originalEncodedString, subStrings);

		// then
		int expectedNoOfPossibleWords = 1;
		assertThat(possibleWords.size()).isEqualTo(expectedNoOfPossibleWords);
	}

	@Test
	public void testConstructPossibleWords_1Encoding_2PossibleWords_2() {
		// Given

		//// A dictionary having 2 words with the same encoding
		dictionary.addNewWord("ADGJ");
		dictionary.addNewWord("BEHK");
		/// original string
		String originalEncodedString = "123451";
		String expectedPossibleWord1 = "1-ADGJ-1";
		String expectedPossibleWord2 = "1-BEHK-1";
		/// Substrings - the provided number contain substring of the 2 words in the
		/// dictionary
		List<SubString> subStrings = new ArrayList<>();
		String subStringValue = "2345";
		int start = 1;
		int end = 4;
		SubString subString = new SubString(originalEncodedString, subStringValue, start, end);
		subStrings.add(subString);

		// when
		List<String> possibleWords = dictionary.constructPossibleWords(originalEncodedString, subStrings);

		// then
		int expectedNoOfPossibleWords = 2;
		assertThat(possibleWords.size()).isEqualTo(expectedNoOfPossibleWords);
		assertThat(possibleWords.contains(expectedPossibleWord1)).isTrue();
		assertThat(possibleWords.contains(expectedPossibleWord2)).isTrue();
	}

	@Test
	public void testConstructPossibleWords_1Encoding_2PossibleWords_1() {
		// Given

		//// A dictionary having 2 words with the same encoding
		dictionary.addNewWord("AAAA");
		dictionary.addNewWord("BBBB");
		/// original string
		String originalEncodedString = "122221";
		String expectedPossibleWord1 = "1-AAAA-1";
		String expectedPossibleWord2 = "1-BBBB-1";
		/// Substrings - the provided number contain substring of the 2 words in the
		/// dictionary
		List<SubString> subStrings = new ArrayList<>();
		String subStringValue = "2222";
		int start = 1;
		int end = 4;
		SubString subString = new SubString(originalEncodedString, subStringValue, start, end);
		subStrings.add(subString);

		// when
		List<String> possibleWords = dictionary.constructPossibleWords(originalEncodedString, subStrings);

		// then
		int expectedNoOfPossibleWords = 2;
		assertThat(possibleWords.size()).isEqualTo(expectedNoOfPossibleWords);
		assertThat(possibleWords.contains(expectedPossibleWord1)).isTrue();
		assertThat(possibleWords.contains(expectedPossibleWord2)).isTrue();
	}

	@Test
	public void testConstructPossibleWords_1Encoding_NoPossibleWord_DueToConsecutiveDigits() {
		// Given
		dictionary.addNewWord("KITTY");
		/// original string
		String originalEncodedString = "1154889";
		/// Substrings
		List<SubString> subStrings = new ArrayList<>();
		String subStringValue = "54889";
		int start = 0;
		int end = 4;
		SubString subString = new SubString(originalEncodedString, subStringValue, start, end);
		subStrings.add(subString);

		// when
		List<String> possibleWords = dictionary.constructPossibleWords(originalEncodedString, subStrings);

		// then
		int expectedNoOfPossibleWords = 0;
		assertThat(possibleWords.size()).isEqualTo(expectedNoOfPossibleWords);
	}

	@Test
	public void testConstructPossibleWords_1Encoding_1PossibleWord_1() {
		// Given
		dictionary.addNewWord("KITTY");
		/// original string
		String originalEncodedString = "154889";
		/// Substrings
		List<SubString> subStrings = new ArrayList<>();
		String subStringValue = "54889";
		int start = 0;
		int end = 4;
		SubString subString = new SubString(originalEncodedString, subStringValue, start, end);
		subStrings.add(subString);

		// when
		List<String> possibleWords = dictionary.constructPossibleWords(originalEncodedString, subStrings);

		// then
		int expectedNoOfPossibleWords = 1;
		assertThat(possibleWords.size()).isEqualTo(expectedNoOfPossibleWords);
		String possibleWord = possibleWords.get(0);
		Object expectedWord = "1-KITTY";
		assertThat(possibleWord).isEqualTo(expectedWord);
	}

	// @Test
	// public void
	// testGetPossibleWords_GivenANotExactWord_WhenFindAValidNumber_ThenReturnCorrectWord_1()
	// {
	// // Given
	// // a 1-word Dictionary
	// String dictionaryWord = "KItty";
	// dictionary.addNewWord(dictionaryWord);
	//
	// // When
	// String input = "548891";
	// List<String> actualWords = dictionary.getPossibleWords(input);
	//
	// // Then
	// String expectedWord = "KITTY-1";
	// assertThat(actualWords.size()).isEqualTo(1);
	// String actualWord = actualWords.get(0);
	//
	// assertThat(actualWord).isEqualTo(expectedWord);
	// }

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
