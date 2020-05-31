package bgl.challenge.phoneword.consoleapp;

/**
 * This is to save command line details
 * 
 * @author luant
 *
 */
public class CommandLineInfo {
	
	public CommandLineInfo() {

	}

	public CommandLineInfo(String inputFileName, String dictionaryFileName) {
		setInputFileName(inputFileName);
		setDictionaryFileName(dictionaryFileName);
	}

	/**
	 * The name of the file containing all phone numbers
	 */
	private String inputFileName;

	/**
	 * The name of the file containing dictionary words.
	 */
	private String dictionaryFileName;
	
	private String outputFileName;

	public String getInputFileName() {
		return inputFileName;
	}

	public void setInputFileName(String inputFileName) {
		this.inputFileName = inputFileName;
	}

	public String getDictionaryFileName() {
		return dictionaryFileName;
	}

	public void setDictionaryFileName(String dictionaryFileName) {
		this.dictionaryFileName = dictionaryFileName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dictionaryFileName == null) ? 0 : dictionaryFileName.hashCode());
		result = prime * result + ((inputFileName == null) ? 0 : inputFileName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CommandLineInfo other = (CommandLineInfo) obj;
		if (dictionaryFileName == null) {
			if (other.dictionaryFileName != null)
				return false;
		} else if (!dictionaryFileName.equals(other.dictionaryFileName))
			return false;
		if (inputFileName == null) {
			if (other.inputFileName != null)
				return false;
		} else if (!inputFileName.equals(other.inputFileName))
			return false;
		return true;
	}

	public String getOutputFileName() {
		return outputFileName;
	}

	public void setOutputFileName(String outputFileName) {
		this.outputFileName = outputFileName;
	}
	
	
}
