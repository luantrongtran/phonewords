package bgl.challenge.phoneword.utils;

import java.util.stream.Collectors;

public class StringUtils {
	public static String removeSpacesAndPunctuations(String str) {
		return str.chars().filter(Character::isLetterOrDigit).mapToObj(c-> Character.toString((char)c))
				.collect(Collectors.joining());
	}
}
