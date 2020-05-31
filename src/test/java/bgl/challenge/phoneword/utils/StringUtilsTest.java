package bgl.challenge.phoneword.utils;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class StringUtilsTest {

	@Test
	void testRemoveSpacesAndPunctuations_ThenRemoveAllSpaces() throws Exception {
		// Given
		String input = "111 222 333  444";
		String expected = "111222333444";

		// When
		String actual = StringUtils.removeSpacesAndPunctuations(input);

		// Then
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	void testRemoveSpacesAndPunctuations_ThenRemoveAllPunctuations() throws Exception {
		// Given
		String input = "111,222,333.444";
		String expected = "111222333444";

		// When
		String actual = StringUtils.removeSpacesAndPunctuations(input);

		// Then
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	void testRemoveSpacesAndPunctuations_ThenRemoveAllPunctuationsAndSpaces() throws Exception {
		// Given
		String input = "111,222 333.444";
		String expected = "111222333444";

		// When
		String actual = StringUtils.removeSpacesAndPunctuations(input);

		// Then
		assertThat(actual).isEqualTo(expected);
	}
}
