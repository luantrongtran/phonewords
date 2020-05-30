package bgl.challenge.phoneword.services;

public class PhonewordSyntaxChecker implements SyntaxChecker {

	@Override
	public boolean isValid(String phoneword) {
		boolean result = true;

		// Fail if contain consecutive digits
		result &= !containConsecutiveDigits(phoneword);

		return result;
	}

	boolean containConsecutiveDigits(String phoneword) {
		boolean b = phoneword.matches(".*\\d\\d.*");
		return b;
	}
}