package bgl.challenge.phoneword.consoleapp;

public interface CommandLineParser {
	/**
	 * @param commandLineArgs
	 *            the arguments passed into the main method
	 * @return
	 */
	public CommandLineInfo parse(String[] commandLineArgs);
}
