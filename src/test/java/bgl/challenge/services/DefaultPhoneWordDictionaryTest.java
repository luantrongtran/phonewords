package bgl.challenge.services;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DefaultPhoneWordDictionaryTest {

	DefaultPhoneWordDictionary dictionary;

	@BeforeEach
	protected void beforeEach() throws Exception {
		WordEncoder wordEncoder = new PhoneWordEncoder();
		dictionary = new DefaultPhoneWordDictionary(wordEncoder);
	}
	
	@Test
	public void testGetPossibleWords_GivenANotExactWord_WhenFindAValidNumber_ThenReturnCorrectWord_1() {
		//Given
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
	public void testGetPossibleWords_GivenAExactWord_WhenFindAValidNumber_ThenReturnCorrectWord_1() {
		//Given
		// a 1-word Dictionary
		String dictionaryWord = "Hello";
		dictionary.addNewWord(dictionaryWord);
		
		String input = "43556";
		
		// When
		List<String> actualWords = dictionary.getPossibleWords(input);
		
		// Then
		assertThat(actualWords.size()).isEqualTo(1);
		
		// Assert that the actual word is as expected; also the actual word is all capital
		String expectedWord = "HELLO";
		String actualWord = actualWords.get(0);
		assertThat(actualWord).isEqualTo(expectedWord);
	}
	
	@Test
	public void testGetPossibleWords_GivenAExactWord_WhenFindAValidNumber_ThenReturnCorrectWord_2() {
		//Given
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
		assertThat(dictionary.contain(expextedEncodedNumber)).isTrue();
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
		assertThat(dictionary.contain(expextedEncodedNumber)).isTrue();
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
		assertThat(dictionary.contain(expectedEncodedNumber1)).isTrue();
		
		assertThat(is2ndWordAdded).isTrue();
		assertThat(dictionary.contain(expectedEncodedNumber2));
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
