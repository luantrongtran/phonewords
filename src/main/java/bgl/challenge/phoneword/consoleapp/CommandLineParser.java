package bgl.challenge.phoneword.consoleapp;

import bgl.challenge.phoneword.exception.ArgumentMissingException;

public interface CommandLineParser {
	/**
	 * @param commandLineArgs
	 *            the arguments passed into the main method
	 * @return
	 */
	public CommandLineInfo parse(String[] commandLineArgs) throws ArgumentMissingException;
}
