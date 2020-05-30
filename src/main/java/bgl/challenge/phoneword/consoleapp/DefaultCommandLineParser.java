package bgl.challenge.phoneword.consoleapp;

import bgl.challenge.phoneword.exception.ArgumentMissingException;

public class DefaultCommandLineParser implements CommandLineParser {

	public static final String INPUT_FILE_INDICATOR = "-f";
	public static final String DICTIONARY_FILE_INDICATOR = "-d";

	/**
	 * @return an instance of DefaultCommandLineParser
	 */
	public static DefaultCommandLineParser getInstance() {
		DefaultCommandLineParser instance = new DefaultCommandLineParser();
		return instance;
	}

	@Override
	public CommandLineInfo parse(String[] commandLineArgs) {
		String inputFileName = extractInputFileName(commandLineArgs);
		String dictionaryFileName = extractDictionaryFileName(commandLineArgs);
		CommandLineInfo info = new CommandLineInfo(inputFileName, dictionaryFileName);
		return info;
	}

	/**
	 * Extract the file name containing dictionary words
	 * 
	 * @param commandLineArgs
	 * @return
	 */
	String extractDictionaryFileName(String[] commandLineArgs) {
		String indicator = DICTIONARY_FILE_INDICATOR;
		String inputFileName = extractParameter(commandLineArgs, indicator);
		return inputFileName;
	}

	/**
	 * Extract the file name containing list of phone numbers
	 * 
	 * @param commandLineArgs
	 * @return
	 * @throws ArgumentMissingException
	 */
	String extractInputFileName(String[] commandLineArgs) throws ArgumentMissingException {
		String indicator = INPUT_FILE_INDICATOR;
		String inputFileName = extractParameter(commandLineArgs, indicator);
		return inputFileName;
	}

	/**
	 * Exrtact a parameter value based on the indicator
	 * 
	 * @param commandLineArgs
	 * @param indicator
	 * @return
	 */
	String extractParameter(String[] commandLineArgs, String indicator) {
		String errMsg = String.format("Missing argument [%s]", indicator);
		int length = commandLineArgs.length;
		for (int i = 0; i < length; i++) {
			String arg = commandLineArgs[i];
			if (arg.equals(indicator)) {
				if (i < length - 1) {
					// if not the last index
					String inputFileName = commandLineArgs[i + 1];
					if (inputFileName.trim().isEmpty()) {
						throw new ArgumentMissingException(errMsg);
					}
					return inputFileName;
				}
			}
		}
		throw new ArgumentMissingException(errMsg);
	}
}
