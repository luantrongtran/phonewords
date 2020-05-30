package bgl.challenge.phoneword.services;

import java.util.List;

public interface PhoneWordDictionary {
	public List<String> findPhonewords(String encodedNumber);
}
