package bgl.challenge.phoneword.services;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import bgl.challenge.phoneword.services.PhonewordSyntaxChecker;

public class PhonewordSyntaxCheckerTest {

	PhonewordSyntaxChecker syntaxChecker;

	@BeforeEach
	protected void beforeEach() {
		syntaxChecker = new PhonewordSyntaxChecker();
	}

	@Test
	public void testIsValid_NoConsecutiveDigits_ReturnTrue() {
		// Given
		String phoneword = "KITTY-1";
		boolean expected = true;

		// When
		boolean actual = syntaxChecker.isValid(phoneword);

		// Then
		assertThat(actual).isEqualTo(expected);
	}
	
	@Test
	public void testIsValid_ContainsConsecutiveDigits_ReturnFlase() {
		// Given
		String phoneword = "KITTY-12";
		boolean expected = false;

		// When
		boolean actual = syntaxChecker.isValid(phoneword);

		// Then
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void testContainConsecutiveDigits_NoConsecutiveDigits() {
		// given
		String phoneword = "KITTY-1";
		boolean expected = false;

		// when
		boolean actual = syntaxChecker.containConsecutiveDigits(phoneword);

		// then
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void testContainConsecutiveDigits_ContainsConsecutiveDigits() {
		// given
		String phoneword = "KITTY-11";
		boolean expected = true;

		// when
		boolean actual = syntaxChecker.containConsecutiveDigits(phoneword);

		// then
		assertThat(actual).isEqualTo(expected);
	}
}
