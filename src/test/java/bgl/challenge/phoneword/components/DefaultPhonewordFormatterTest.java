package bgl.challenge.phoneword.components;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import bgl.challenge.phoneword.models.Pattern;
import bgl.challenge.phoneword.models.SubString;

public class DefaultPhonewordFormatterTest {
	DefaultPhonewordFormatter formatter = new DefaultPhonewordFormatter();

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

		// When
		String actualFormated = formatter.format(word, pattern);

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

		// When
		String actualFormated = formatter.format(word, pattern);

		// Then
		assertThat(actualFormated).isEqualTo(expectedFormatedWord);
	}

}
