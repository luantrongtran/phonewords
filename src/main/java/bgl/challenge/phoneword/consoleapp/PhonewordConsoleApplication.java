package bgl.challenge.phoneword.consoleapp;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import bgl.challenge.phoneword.components.DefaultPhoneWordDictionary;
import bgl.challenge.phoneword.components.PhoneWordDictionary;

public class PhonewordConsoleApplication implements PhonewordApplication {

	private static final String DEFAULT_OUTPUT_FILE_NAME = "output.txt";

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
		 * Find all possible phonewords
		 */
		List<String> allPhonewords = findAllPhonewords(commandInfo.getInputFileName());

		/*
		 * Writing to file
		 */
		writingToFile(allPhonewords, DEFAULT_OUTPUT_FILE_NAME);
	}

	private void loadDictionaryFromFile(String dictionaryFileName) throws IOException {
		File f = new File(dictionaryFileName);
		phoneWordDictionary.importFromFile(f);
	}

	private List<String> findAllPhonewords(String inputFileName) throws IOException {
		List<String> lstNumbers = Files.readAllLines(Paths.get(inputFileName));
		List<String> allPhonewords = lstNumbers.parallelStream().flatMap(phoneNumber -> {
			List<String> phonewords = phoneWordDictionary.findPhonewords(phoneNumber);
			return phonewords.stream();
		}).collect(Collectors.toList());
		return allPhonewords;
	}

	private void writingToFile(List<String> phonewords, String outputFileName) throws IOException {
		BufferedWriter writer = null;
		try {
			File f = new File(outputFileName);
			FileWriter fileWriter = new FileWriter(f);
			writer = new BufferedWriter(fileWriter);
			for (String word : phonewords) {
				writer.write(word);
				writer.newLine();
			}
		} finally {
			if (writer != null) {
				writer.close();
			}
		}
	}
}
