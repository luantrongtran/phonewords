package bgl.challenge.phoneword.models;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

public class PatternTest {

	@Test
	void testIsValid() throws Exception {
		String originalPhoneNumber = "2222";
		List<SubString> subStrings = new ArrayList<>();
		SubString subString1 = new SubString(originalPhoneNumber, "222", 0, 2);
		SubString subString2 = new SubString(originalPhoneNumber, "222", 1, 3);
		subStrings.add(subString1);
		subStrings.add(subString2);
		boolean expectedCheck = false;
		
		// Given
		Pattern pattern = new Pattern(originalPhoneNumber, subStrings);

		// When
		boolean actualCheck = pattern.isValid();

		// Then
		assertThat(actualCheck).isEqualTo(expectedCheck);
	}

}
