package bgl.challenge.phoneword.consoleapp;

import java.io.IOException;
import java.net.URL;

import org.junit.jupiter.api.Test;

public class PhonewordConsoleApplicationTest {
	@Test
	public void testRun() throws IOException {
		String dictionaryFileName = "dictionary.txt";
		URL dictionaryUrl = this.getClass().getResource("/" + dictionaryFileName);

		String phoneNumbersFileName = "input_1.txt";
		URL phoneNumbersUrl = this.getClass().getResource("/" + phoneNumbersFileName);

		String[] commandLineArgs = { "-input", phoneNumbersUrl.getFile(), "-dictionary", dictionaryUrl.getFile() };

		PhonewordConsoleApplication app = new PhonewordConsoleApplication();

		app.run(commandLineArgs);
	}
}
