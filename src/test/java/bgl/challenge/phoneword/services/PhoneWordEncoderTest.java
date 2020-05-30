package bgl.challenge.phoneword.services;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PhoneWordEncoderTest {

	PhoneWordEncoder translator;

	@BeforeEach
	protected void setUpBeforeClass() throws Exception {
		this.translator = new PhoneWordEncoder();
	}

	@Test
	public void testTranslateString() {
		// Given
		String originalString = "KITTY";
		int expectedNumber = 54889;

		// When
		int actualNumber = translator.encode(originalString);

		// Then
		assertThat(actualNumber).isEqualTo(expectedNumber);
	}

	@Test
	public void testTranslateString_1() {
		// Given
		String originalString = "Hello";
		int expectedNumber = 43556;

		// When
		int actualNumber = translator.encode(originalString);

		// Then
		assertThat(actualNumber).isEqualTo(expectedNumber);
	}
}
