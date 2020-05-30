package bgl.challenge.phoneword.services;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public interface PhoneWordDictionary {
	public List<String> findPhonewords(String encodedNumber);
	public void importFromFile(File f) throws FileNotFoundException, IOException;
}
