package bgl.challenge.models;

public class SubString {
	/**
	 * The original String
	 */
	private String originalString;

	/**
	 * The value of the SubString
	 */
	private String value;

	/**
	 * The beginning index of the substring in the original string. Inclusive
	 */
	private int start;

	/**
	 * The last index of the substring in the original string
	 */
	private int end;

	public SubString() {

	}

	public SubString(String original, String value, int start, int end) {
		this.originalString = original;
		this.value = value;
		this.start = start;
		this.end = end;
	}

	public String getOriginalString() {
		return originalString;
	}

	public void setOriginalString(String originalString) {
		this.originalString = originalString;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getEnd() {
		return end;
	}

	public void setEnd(int end) {
		this.end = end;
	}
}