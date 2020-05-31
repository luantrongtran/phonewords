package bgl.challenge.phoneword.consoleapp;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import bgl.challenge.phoneword.components.DefaultPhoneWordDictionary;
import bgl.challenge.phoneword.components.PhoneWordDictionary;

public class PhonewordConsoleApplication implements PhonewordApplication {

	CommandLineParser commandParser;
	PhoneWordDictionary phoneWordDictionary;

	public PhonewordConsoleApplication() {
		commandParser = DefaultCommandLineParser.getInstance();
		phoneWordDictionary = DefaultPhoneWordDictionary.getInstance();
	}

	@Override
	public void run(String[] commandLineArgs) throws IOException {
		/*
		 * Parse arguments
		 */
		CommandLineInfo commandInfo = commandParser.parse(commandLineArgs);

		/*
		 * Load dictionary file
		 */
		loadDictionaryFromFile(commandInfo.getDictionaryFileName());

		/*
		 * Print out all possible phonewords
		 */
		/// Get all the numbers
		List<String> lstNumbers = Files.readAllLines(Paths.get(commandInfo.getInputFileName()));
		lstNumbers.parallelStream().forEach(phoneNumber -> {
			List<String> phonewords = phoneWordDictionary.findPhonewords(phoneNumber);
			// print
			phonewords.stream().forEach(System.out::println);
		});
	}

	private void loadDictionaryFromFile(String dictionaryFileName) throws IOException {
		File f = new File(dictionaryFileName);
		phoneWordDictionary.importFromFile(f);
	}
}
