package bgl.challenge.phoneword.components;

import bgl.challenge.phoneword.models.Pattern;

public interface PhonewordFormatter {
	/**
	 * 
	 * @param phoneword
	 *            unformatted phoneword
	 * @param pattern
	 *            the pattern which generates the phoneword
	 * @return
	 */
	public String format(String phoneword, Pattern pattern);
}
