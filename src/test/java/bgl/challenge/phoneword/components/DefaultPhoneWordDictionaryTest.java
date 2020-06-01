package bgl.challenge.phoneword.components;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import bgl.challenge.phoneword.models.Pattern;
import bgl.challenge.phoneword.models.SubString;

public class DefaultPhoneWordDictionaryTest {

	DefaultPhoneWordDictionary dictionary;
	WordEncoder wordEncoder;
	SyntaxChecker syntaxChecker;
	PhonewordFormatter phonewordFormatter;

	@BeforeEach
	protected void beforeEach() throws Exception {
		wordEncoder = Mockito.spy(PhoneWordEncoder.class);
		syntaxChecker = Mockito.spy(PhonewordSyntaxChecker.class);
		phonewordFormatter = Mockito.spy(DefaultPhonewordFormatter.class);
		dictionary = Mockito.spy(new DefaultPhoneWordDictionary(wordEncoder, syntaxChecker, phonewordFormatter));
	}

	@Test
	public void testFindPhoneWords_OneWordDictionary() {
		// Given
		dictionary.addNewWord("A");
		String input = "23";
		String expectedWord1 = "A-3";
		int expectedNoOfWords = 1;

		// When
		List<String> phonewords = dictionary.findPhonewords(input);

		// Then
		assertThat(phonewords.size()).isEqualByComparingTo(expectedNoOfWords);
		assertThat(phonewords.get(0)).isEqualTo(expectedWord1);
	}
	
	@Test
	public void testFindPhoneWords_4() {
		// Given
		dictionary.addNewWord("AAA");
		dictionary.addNewWord("DDD");
		String input = "22223333";
		String expectedWord1 = "AAA-2-DDD-3";
		String expectedWord3 = "2-AAA-3-DDD";
		String expectedWord4 = "2-AAA-DDD-3";
		int expectedNoOfWords = 3;

		// When
		List<String> phonewords = dictionary.findPhonewords(input);

		// Then
		assertThat(phonewords.size()).isEqualByComparingTo(expectedNoOfWords);
		assertThat(phonewords.contains(expectedWord1)).isTrue();
		assertThat(phonewords.contains(expectedWord3)).isTrue();
		assertThat(phonewords.contains(expectedWord4)).isTrue();
	}

	@Test
	public void testFindPhoneWords_3() {
		// Given
		dictionary.addNewWord("AAA");
		dictionary.addNewWord("BBB");
		dictionary.addNewWord("DDD");
		String input = "122213331";
		String expectedWord1 = "1-AAA-1-DDD-1";
		String expectedWord2 = "1-BBB-1-DDD-1";
		int expectedNoOfWords = 2;

		// When
		List<String> phonewords = dictionary.findPhonewords(input);

		// Then
		assertThat(phonewords.size()).isEqualByComparingTo(expectedNoOfWords);
		assertThat(phonewords.contains(expectedWord1)).isTrue();
		assertThat(phonewords.contains(expectedWord2)).isTrue();
	}

	@Test
	public void testFindPhoneWords_2() {
		// Given
		dictionary.addNewWord("AAA");
		dictionary.addNewWord("BBB");
		dictionary.addNewWord("DDD");
		String input = "222333";
		String expectedWord1 = "AAA-DDD";
		String expectedWord2 = "BBB-DDD";
		int expectedNoOfWords = 2;

		// When
		List<String> phonewords = dictionary.findPhonewords(input);

		// Then
		assertThat(phonewords.size()).isEqualByComparingTo(expectedNoOfWords);
		assertThat(phonewords.contains(expectedWord1)).isTrue();
		assertThat(phonewords.contains(expectedWord2)).isTrue();
	}

	@Test
	public void testFindPhonewords_GivenAExactWord_WhenFindAValidNumber_ThenReturnCorrectWord_1() {
		// Given
		// a 1-word Dictionary
		String dictionaryWord = "Hello";
		dictionary.addNewWord(dictionaryWord);

		String input = "43556";

		// When
		List<String> actualWords = dictionary.findPhonewords(input);

		// Then
		assertThat(actualWords.size()).isEqualTo(1);

		// Assert that the actual word is as expected; also the actual word is all
		// capital
		String expectedWord = "HELLO";
		String actualWord = actualWords.get(0);
		assertThat(actualWord).isEqualTo(expectedWord);
	}

	@Test
	public void testFindPhonewords_GivenAExactWord_WhenFindAValidNumber_ThenReturnACorrectWord_2() {
		// Given
		// a 1-word Dictionary
		String dictionaryWord = "KItty";
		dictionary.addNewWord(dictionaryWord);

		// When
		String input = "54889";
		List<String> actualWords = dictionary.findPhonewords(input);

		// Then
		String expectedWord = "KITTY";
		assertThat(actualWords.size()).isEqualTo(1);
		String actualWord = actualWords.get(0);

		assertThat(actualWord).isEqualTo(expectedWord);
	}

	@Test
	public void testFindAllValidPatterns_2SubStringsNotOveralling_ThenReturn1Group() {
		// Given
		dictionary.addNewWord("STAR");
		dictionary.addNewWord("WARS");
		
		String originalEncodedString = "782792771";

		// When
		List<Pattern> patterns = dictionary.findAllValidPatterns(originalEncodedString);

		// Then
		int actualNoOfPatterns = patterns.size();
		int expectedNoOfPatterns = 3;
		assertThat(actualNoOfPatterns).isEqualTo(expectedNoOfPatterns);
	}

	@Test
	public void testFindAllValidPatterns_3SubStringsOverllaping_ThenReturn4Group() {
		// Given
		dictionary.addNewWord("AAA");
		dictionary.addNewWord("DDD");
		String originalEncodedString = "22223333";
		int expectedNoOfPatterns = 8;

		// When
		List<Pattern> patterns = dictionary.findAllValidPatterns(originalEncodedString);

		// Then
		int actualNoOfPatterns = patterns.size();
		assertThat(actualNoOfPatterns).isEqualTo(expectedNoOfPatterns);
	}

	@Test
	public void testFindAllValidPatterns_2overlappingEncodings_2PossibleWords() {
		// Given
		dictionary.addNewWord("KITTY");
		dictionary.addNewWord("KIT");
		String originalEncodedString = "54889";

		// When
		List<Pattern> patterns = dictionary.findAllValidPatterns(originalEncodedString);

		// Then
		int expectedNoOfPattern = 2;
		assertThat(patterns.size()).isEqualTo(expectedNoOfPattern);
	}

	@Test
	public void testConstructPhonewordsPerPattern_2Encoding_1PossibleWord() {
		// Given
		dictionary.addNewWord("STAR");
		dictionary.addNewWord("WARS");

		String originalEncodedString = "782792771";
		String expectedPossibleWord = "STAR-WARS-1";

		SubString subString1 = new SubString(originalEncodedString, "7827", 0, 3);
		SubString subString2 = new SubString(originalEncodedString, "9277", 4, 7);
		List<SubString> subStrings = new ArrayList<>();
		subStrings.add(subString1);
		subStrings.add(subString2);
		Pattern pattern = new Pattern(originalEncodedString, subStrings);

		// when
		List<String> possibleWords = dictionary.constructPhonewordsPerPattern(originalEncodedString, pattern);

		// then
		int expectedNoOfPossibleWords = 1;
		assertThat(possibleWords.size()).isEqualTo(expectedNoOfPossibleWords);
		assertThat(possibleWords.contains(expectedPossibleWord)).isTrue();
	}

	@Test
	public void testConstructPhonewordsPerPattern_1Encoding_2PossibleWords_2() {
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
		Pattern pattern = new Pattern(originalEncodedString, subStrings);

		// when
		List<String> possibleWords = dictionary.constructPhonewordsPerPattern(originalEncodedString, pattern);

		// then
		int expectedNoOfPossibleWords = 2;
		assertThat(possibleWords.size()).isEqualTo(expectedNoOfPossibleWords);
		assertThat(possibleWords.contains(expectedPossibleWord1)).isTrue();
		assertThat(possibleWords.contains(expectedPossibleWord2)).isTrue();
	}

	@Test
	public void testConstructPhonewordsPerPattern_1Encoding_2PossibleWords_1() {
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
		Pattern pattern = new Pattern(originalEncodedString, subStrings);

		// when
		List<String> possibleWords = dictionary.constructPhonewordsPerPattern(originalEncodedString, pattern);

		// then
		int expectedNoOfPossibleWords = 2;
		assertThat(possibleWords.size()).isEqualTo(expectedNoOfPossibleWords);
		assertThat(possibleWords.contains(expectedPossibleWord1)).isTrue();
		assertThat(possibleWords.contains(expectedPossibleWord2)).isTrue();
	}

	@Test
	public void testConstructPhonewordsPerPattern_1Encoding_NoPossibleWord_DueToConsecutiveDigits() {
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
		Pattern pattern = new Pattern(originalEncodedString, subStrings);

		// when
		List<String> possibleWords = dictionary.constructPhonewordsPerPattern(originalEncodedString, pattern);

		// then
		int expectedNoOfPossibleWords = 0;
		int actualNoOfPossibleWords = possibleWords.size();
		assertThat(actualNoOfPossibleWords).isEqualTo(expectedNoOfPossibleWords);
	}
	
	@Test
	public void testConstructPhonewordsPerPattern_1Encoding_1LetterWord() {
		// Given
		dictionary.addNewWord("A");
		String originalEncodedNumber = "23";
		int expectedNoOfWords = 1;
		String expectedWord = "A-3";
		
		List<SubString> subStrings = new ArrayList<>();
		SubString subString1 = new SubString(originalEncodedNumber, "2", 0, 0);
		subStrings.add(subString1 );
		Pattern pattern = new Pattern(originalEncodedNumber, subStrings );
		
		
		// When
		List<String> possibleWords = dictionary.constructPhonewordsPerPattern(originalEncodedNumber, pattern);
		
		// Then
		assertThat(possibleWords.size()).isEqualTo(expectedNoOfWords);
		assertThat(possibleWords.get(0)).isEqualTo(expectedWord);
	}

	@Test
	public void testConstructPhonewordsPerPattern_1Encoding_1PossibleWord_1() {
		// Given
		dictionary.addNewWord("KITTY");
		/// original string
		String originalEncodedString = "154889";
		/// Substrings
		List<SubString> subStrings = new ArrayList<>();
		String subStringValue = "54889";
		int start = 1;
		int end = 5;
		SubString subString = new SubString(originalEncodedString, subStringValue, start, end);
		subStrings.add(subString);
		Pattern pattern = new Pattern(originalEncodedString, subStrings);

		// when
		List<String> possibleWords = dictionary.constructPhonewordsPerPattern(originalEncodedString, pattern);

		// then
		int expectedNoOfPossibleWords = 1;
		assertThat(possibleWords.size()).isEqualTo(expectedNoOfPossibleWords);
		String possibleWord = possibleWords.get(0);
		Object expectedWord = "1-KITTY";
		assertThat(possibleWord).isEqualTo(expectedWord);
	}

	@Test
	public void testFindAllPossibleSubStrings() {
		// Given
		String firstDictionaryWord = "Star";
		String encodedFirstDictionaryWord = "7827";
		dictionary.addNewWord(firstDictionaryWord);
		String secondDictionaryWord = "wars";
		String encodedSecondDictionaryWord = "9277";
		dictionary.addNewWord(secondDictionaryWord);

		String encodedOriginalString = "78279277";
		// when
		List<SubString> subStrings = dictionary.findAllPossibleSubStrings(encodedOriginalString);

		// then
		int expectedNoOfSubStrings = 2;
		assertThat(subStrings.size()).isEqualTo(expectedNoOfSubStrings);

		SubString first = subStrings.get(0);
		assertThat(first.getValue()).isEqualTo(encodedFirstDictionaryWord);

		SubString second = subStrings.get(1);
		assertThat(second.getValue()).isEqualTo(encodedSecondDictionaryWord);
	}

	@Test
	public void testFindAllPossibleSubStringsWithSpecificLength() {
		// given
		String dictionaryWord = "abc";
		String encodedDictionaryWord = "222";
		dictionary.addNewWord(dictionaryWord);

		String encodedOriginalString = "222333";
		int wordLength = 3;

		// when
		List<SubString> lstSubStrings = dictionary.findAllPossibleSubStringsWithSpecificLength(encodedOriginalString,
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
	public void testFindAllPossibleSubStringsWithSpecificLength_2() {
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
		List<SubString> lstSubStrings = dictionary.findAllPossibleSubStringsWithSpecificLength(encodedOriginalString,
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
	public void testAddNewWord_WhenAddingAWord_ThenFoundItInDictionary() {
		// Given
		String word = "Hello";
		String expextedEncodedNumber = "43556";

		// When
		boolean isWordAdded = dictionary.addNewWord(word);

		// Then
		assertThat(isWordAdded).isTrue();
		// double check the word is in the dictionary
		assertThat(dictionary.containsNumber(expextedEncodedNumber)).isTrue();
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
		assertThat(dictionary.containsNumber(expextedEncodedNumber)).isTrue();
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
		assertThat(dictionary.containsNumber(expectedEncodedNumber1)).isTrue();

		assertThat(is2ndWordAdded).isTrue();
		assertThat(dictionary.containsNumber(expectedEncodedNumber2));
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

	@Test
	public void testReplaceSubString() {
		// Given
		String originalEncodedString = "22223333";
		String replacedString = "222";
		SubString subString = new SubString(originalEncodedString, replacedString, 1, 3);
		String newString = "ABC";
		String expectedResult = "2ABC3333";

		// When
		String actualResult = dictionary.replaceSubString(originalEncodedString, subString, newString);

		// Then
		assertThat(actualResult).isEqualTo(expectedResult);
	}

	@Test
	public void testReplaceSubString_1() {
		// Given
		String originalEncodedString = "2ABC3333";
		String replacedString = "333";
		SubString subString = new SubString(originalEncodedString, replacedString, 5, 7);
		String newString = "EDF";
		String expectedResult = "2ABC3EDF";

		// When
		String actualResult = dictionary.replaceSubString(originalEncodedString, subString, newString);

		// Then
		assertThat(actualResult).isEqualTo(expectedResult);
	}

	@Test
	void testImportFromFile_GivenNotExistFile_ThenThrowException() throws Exception {
		// When
		assertThrows(FileNotFoundException.class, () -> dictionary.importFromFile(new File("nonExistFile")));
	}

	@Test
	void testImportFromFile_GivenExistFile_ThenImportAllWords_1() throws Exception {
		// Given
		String dictionaryFile = "dictionary_test_1.txt";
		URL url = this.getClass().getResource("/" + dictionaryFile);
		File f = new File(url.getFile());
		assertThat(f.exists()).isTrue();

		// When
		dictionary.importFromFile(f);

		// Then
		WordEncoder wordEncoder = new PhoneWordEncoder();
		/*
		 * assert that the dictionary contains all the encoded value of the words in
		 * text file
		 */
		try (Stream<String> stream = Files.lines(Paths.get(url.getFile()))) {
			stream.forEach(line -> {
				String word = line;
				String encoded = wordEncoder.encode(word) + "";
				assertThat(dictionary.containsNumber(encoded)).overridingErrorMessage("Word [%s] not added", word)
						.isTrue();
			});
		}
	}

	@Test
	void testImportFromFile_GivenExistFile_ThenImportAllWords_2() throws Exception {
		// Given
		String dictionaryFile = "dictionary_test_2.txt";
		URL url = this.getClass().getResource("/" + dictionaryFile);
		File f = new File(url.getFile());
		assertThat(f.exists()).isTrue();

		// When
		dictionary.importFromFile(f);

		// Then
		WordEncoder wordEncoder = new PhoneWordEncoder();
		/*
		 * assert that the dictionary contains all the encoded value of the words in
		 * text file
		 */
		try (Stream<String> stream = Files.lines(Paths.get(url.getFile()))) {
			stream.forEach(line -> {
				String word = line;
				String encoded = wordEncoder.encode(word) + "";
				assertThat(dictionary.containsNumber(encoded)).overridingErrorMessage("Word [%s] not added", word)
						.isTrue();
			});
		}
	}

	@Test
	void testEncodesWord_GivenProperWord_ThenReturnCorrectEncoding_1() throws Exception {
		// Given
		String word = "ADG";
		int expectedEncoded = 234;
		doReturn(expectedEncoded).when(wordEncoder).encode(word);

		// When
		String actualEncoded = dictionary.encodesWord(word);

		// Then
		assertThat(actualEncoded).isEqualTo(expectedEncoded + "");
	}

	@Test
	void testEncodesWord_GivenProperWord_ThenReturnCorrectEncoding_2() throws Exception {
		// Given
		String word = "TEST";
		int expectedEncoded = 8278;
		doReturn(expectedEncoded).when(wordEncoder).encode(word);

		// When
		String actualEncoded = dictionary.encodesWord(word);

		// Then
		assertThat(actualEncoded).isEqualTo(expectedEncoded + "");
	}

	@Test
	void testFormatPhoneword() throws Exception {
		// Given
		String word = "AAADDD";
		String originalPhoneNumber = "222333";
		List<SubString> subStrings = new ArrayList<>();
		SubString subString1 = new SubString(originalPhoneNumber, "222", 0, 2);
		SubString subString2 = new SubString(originalPhoneNumber, "333", 3, 5);
		subStrings.add(subString1);
		subStrings.add(subString2);
		Pattern pattern = new Pattern(originalPhoneNumber, subStrings);
		String expectedFormatedWord = "AAA-DDD";

		doReturn(expectedFormatedWord).when(phonewordFormatter).format(word, pattern);

		// When
		String actualFormated = dictionary.formatPhoneword(word, pattern);

		// Then
		assertThat(actualFormated).isEqualTo(expectedFormatedWord);
	}

	@Test
	void testFormatPhoneword_2() throws Exception {
		// Given
		String word = "AAADDDEEE";
		String originalPhoneNumber = "222333444";
		List<SubString> subStrings = new ArrayList<>();
		SubString subString1 = new SubString(originalPhoneNumber, "222", 0, 2);
		SubString subString2 = new SubString(originalPhoneNumber, "333", 3, 5);
		SubString subString3 = new SubString(originalPhoneNumber, "444", 6, 8);
		subStrings.add(subString1);
		subStrings.add(subString2);
		subStrings.add(subString3);
		Pattern pattern = new Pattern(originalPhoneNumber, subStrings);
		String expectedFormatedWord = "AAA-DDD-EEE";

		doReturn(expectedFormatedWord).when(phonewordFormatter).format(word, pattern);

		// When
		String actualFormated = dictionary.formatPhoneword(word, pattern);

		// Then
		assertThat(actualFormated).isEqualTo(expectedFormatedWord);
	}
}
