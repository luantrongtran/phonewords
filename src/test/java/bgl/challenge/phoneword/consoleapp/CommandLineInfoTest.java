package bgl.challenge.phoneword.consoleapp;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class CommandLineInfoTest {

	@Test
	void testConstructorWithParameters() throws Exception {
		// Given
		String inputFilenName = "input.txt";
		String dictionaryFileName = "dictionary.txt";

		// When
		CommandLineInfo commandInfo = new CommandLineInfo(inputFilenName, dictionaryFileName);

		// Then
		assertThat(commandInfo.getDictionaryFileName()).isEqualTo(dictionaryFileName);
		assertThat(commandInfo.getInputFileName()).isEqualTo(inputFilenName);
	}
}
