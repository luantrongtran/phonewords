package bgl.challenge.phoneword;

import bgl.challenge.phoneword.consoleapp.PhonewordApplication;
import bgl.challenge.phoneword.consoleapp.PhonewordConsoleApplication;

public class Main {
	public static void main(String[] args) {
		PhonewordApplication app = new PhonewordConsoleApplication();
		try {
			app.run(args);
		} catch (Exception exception) {
			String errMsg = String.format("Error - %s", exception.getMessage());
			System.out.println(errMsg);
		}
	}
}
