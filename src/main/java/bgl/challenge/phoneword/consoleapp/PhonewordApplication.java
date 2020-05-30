package bgl.challenge.phoneword.consoleapp;

import java.io.IOException;

import bgl.challenge.phoneword.exception.ArgumentMissingException;

public interface PhonewordApplication {
	public void run(String[] commandLineArgs) throws ArgumentMissingException, IOException;
}
