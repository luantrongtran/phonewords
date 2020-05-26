package bgl.challenge.services;

import java.util.List;

public interface PhoneWordDictionary {
	public List<String> getPossibleWords(String encodedNumber);
}
