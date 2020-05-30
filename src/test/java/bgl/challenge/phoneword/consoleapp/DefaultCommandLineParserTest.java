package bgl.challenge.phoneword.consoleapp;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import bgl.challenge.phoneword.consoleapp.CommandLineInfo;
import bgl.challenge.phoneword.consoleapp.DefaultCommandLineParser;
import bgl.challenge.phoneword.exception.ArgumentMissingException;

public class DefaultCommandLineParserTest {

	DefaultCommandLineParser parser;

	@BeforeEach
	protected void beforeEach() throws Exception {
		parser = Mockito.spy(DefaultCommandLineParser.class);
	}

	@Test
	void testParse_GivenCommandLineArgs_ThenReturnCorrectInfo_1() throws Exception {
		// Given
		String inputFileName = "listOfNumbers.txt";
		String dictionaryFileName = "dictionaryWords.txt";
		String[] commandLineArgs = { DefaultCommandLineParser.INPUT_FILE_INDICATOR, inputFileName,
				DefaultCommandLineParser.DICTIONARY_FILE_INDICATOR, dictionaryFileName };
		doReturn(inputFileName).when(parser).extractInputFileName(commandLineArgs);
		doReturn(dictionaryFileName).when(parser).extractDictionaryFileName(commandLineArgs);

		// When
		CommandLineInfo actualInfo = parser.parse(commandLineArgs);

		// Then
		assertThat(actualInfo).isNotNull();
		assertThat(actualInfo.getInputFileName()).isEqualTo(inputFileName);
		assertThat(actualInfo.getDictionaryFileName()).isEqualTo(dictionaryFileName);
	}

	@Test
	public void testParse_GivenCommandLineArgs_ThenReturnCorrectInfo_2() {
		// Given
		String inputFileName = "input.txt";
		String dictionaryFileName = "dictionary.txt";
		String[] commandLineArgs = { DefaultCommandLineParser.INPUT_FILE_INDICATOR, inputFileName,
				DefaultCommandLineParser.DICTIONARY_FILE_INDICATOR, dictionaryFileName };

		doReturn(inputFileName).when(parser).extractInputFileName(commandLineArgs);
		doReturn(dictionaryFileName).when(parser).extractDictionaryFileName(commandLineArgs);

		// When
		CommandLineInfo actualInfo = parser.parse(commandLineArgs);

		// Then
		assertThat(actualInfo).isNotNull();
		assertThat(actualInfo.getInputFileName()).isEqualTo(inputFileName);
		assertThat(actualInfo.getDictionaryFileName()).isEqualTo(dictionaryFileName);
	}

	@Test
	public void testExtractInputFileName_1() {
		// Given
		String indicator = DefaultCommandLineParser.INPUT_FILE_INDICATOR;
		String inputFileName = "input.txt";
		String[] commandLineArgs = { indicator, inputFileName };

		doReturn(inputFileName).when(parser).extractParameter(commandLineArgs, indicator);

		// When
		String actualInputFileName = parser.extractInputFileName(commandLineArgs);

		// Then
		assertThat(actualInputFileName).isNotNull();
		assertThat(actualInputFileName).isEqualTo(inputFileName);
	}

	@Test
	public void testExtractInputFileName_2() {
		// Given
		String indicator = DefaultCommandLineParser.INPUT_FILE_INDICATOR;
		String inputFileName = "listOfNumbers.txt";
		String[] commandLineArgs = { indicator, inputFileName };
		doReturn(inputFileName).when(parser).extractParameter(commandLineArgs, indicator);

		// When
		String actualInputFileName = parser.extractInputFileName(commandLineArgs);

		// Then
		assertThat(actualInputFileName).isNotNull();
		assertThat(actualInputFileName).isEqualTo(inputFileName);
	}

	@Test
	public void testExtractInputFileName_GivenEmptyInputFileName_ThenThrowException() {
		// Given
		String indicator = DefaultCommandLineParser.INPUT_FILE_INDICATOR;
		String emptyInputFileName = "";
		String[] commandLineArgs = { indicator, emptyInputFileName };

		doThrow(ArgumentMissingException.class).when(parser).extractParameter(commandLineArgs, indicator);

		// When
		assertThrows(ArgumentMissingException.class, () -> {
			parser.extractInputFileName(commandLineArgs);
		});
	}

	@Test
	public void testExtractInputFileName_GivenNoInputFileNameIndicator_ThenThrowException() {
		// Given
		String missingIndicator = DefaultCommandLineParser.INPUT_FILE_INDICATOR;
		String[] commandLineArgs = { DefaultCommandLineParser.DICTIONARY_FILE_INDICATOR, "dictionary.txt" };

		doThrow(ArgumentMissingException.class).when(parser).extractParameter(commandLineArgs, missingIndicator);

		// When
		assertThrows(ArgumentMissingException.class, () -> {
			parser.extractInputFileName(commandLineArgs);
		});
	}

	@Test
	void testExtractDictionaryFileName_GivenDictionaryFileName_ThenReturnCorrectFileName() throws Exception {
		// Given
		String expectedDictionaryFileName = "dictionary.txt";
		String indicator = DefaultCommandLineParser.DICTIONARY_FILE_INDICATOR;
		String[] commandLineArgs = { indicator, expectedDictionaryFileName };
		doReturn(expectedDictionaryFileName).when(parser).extractParameter(commandLineArgs, indicator);

		// When
		String actualDictionaryFilename = parser.extractDictionaryFileName(commandLineArgs);

		// Then
		assertThat(actualDictionaryFilename).isEqualTo(expectedDictionaryFileName);
	}

	@Test
	void testExtractDictionaryFileName_GivenDictionaryFileName_ThenReturnCorrectFileName_1() throws Exception {
		// Given
		String expectedDictionaryFileName = "dictionaryWords.txt";
		String indicator = DefaultCommandLineParser.DICTIONARY_FILE_INDICATOR;
		String[] commandLineArgs = { indicator, expectedDictionaryFileName };
		doReturn(expectedDictionaryFileName).when(parser).extractParameter(commandLineArgs, indicator);

		// When
		String actualDictionaryFilename = parser.extractDictionaryFileName(commandLineArgs);

		// Then
		assertThat(actualDictionaryFilename).isEqualTo(expectedDictionaryFileName);
	}

	@Test
	void testExtractDictionaryFileName_GivenNoDictionaryFileNameIndicator_ThenThrowException() throws Exception {
		// Given
		String[] commandLineArgs = { DefaultCommandLineParser.INPUT_FILE_INDICATOR, "input.txt" };

		String missingIndicator = DefaultCommandLineParser.DICTIONARY_FILE_INDICATOR;
		doThrow(ArgumentMissingException.class).when(parser).extractParameter(commandLineArgs, missingIndicator);

		// When & Then
		assertThrows(ArgumentMissingException.class, () -> {
			parser.extractDictionaryFileName(commandLineArgs);
		});
	}

	@Test
	void testExtractParameter_ThenReturnProperArgValue_1() throws Exception {
		// Given
		String indicator = "-p";
		String paramValue = "paraValue";
		String[] commandLineArgs = { indicator, paramValue };

		// When
		String actualParamValue = parser.extractParameter(commandLineArgs, indicator);

		// Then
		assertThat(actualParamValue).isEqualTo(paramValue);
	}

	@Test
	void testExtractParameter_ThenReturnProperArgValue_2() throws Exception {
		// Given
		String indicator = "-d";
		String paramValue = "directory";
		String[] commandLineArgs = { indicator, paramValue };

		// When
		String actualParamValue = parser.extractParameter(commandLineArgs, indicator);

		// Then
		assertThat(actualParamValue).isEqualTo(paramValue);
	}

	@Test
	void testExtractParameter_GivenEmptyParamValue_ThenThrowException() throws Exception {
		// Given
		String indicator = "-d";
		String emptyValue = "";
		String[] commandLineArgs = { indicator, emptyValue };

		// When
		assertThrows(ArgumentMissingException.class, () -> {
			parser.extractParameter(commandLineArgs, indicator);
		});
	}

	@Test
	void testExtractParameter_GivenNoIndicator_ThenThrowException() throws Exception {
		// Given
		String missingIndicator = "-d";
		String existIndicator = "-x";
		String[] commandLineArgs = { existIndicator, "exist Indicator's value" };

		// When
		assertThrows(ArgumentMissingException.class, () -> {
			parser.extractParameter(commandLineArgs, missingIndicator);
		});
	}

	@Test
	public void testExtractParameter_GivenNoArgs_ThenThrowException() {
		// Given
		String anyIndicator = "-d";
		String[] commandLineArgs = {};

		// When & Then
		assertThrows(ArgumentMissingException.class, () -> {
			parser.extractParameter(commandLineArgs, anyIndicator);
		});
	}
}