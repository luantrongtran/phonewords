package bgl.challenge.phoneword;

import java.net.URL;

import org.junit.jupiter.api.Test;

public class MainTest {
	@Test
	public void testMain() {
		String dictionaryFileName = "dictionary.txt";
		URL dictionaryUrl = this.getClass().getResource("/" + dictionaryFileName);

		String phoneNumbersFileName = "input.txt";
		URL phoneNumbersUrl = this.getClass().getResource("/" + phoneNumbersFileName);

		String[] args = { "-input", phoneNumbersUrl.getFile(), "-dictionary", dictionaryUrl.getFile() };
		Main.main(args);
	}
}
