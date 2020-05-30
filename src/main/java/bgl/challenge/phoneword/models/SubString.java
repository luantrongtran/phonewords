package bgl.challenge.phoneword.models;

/**
 * Data structure saving info of a sub string
 * 
 * @author luant
 *
 */
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
	 * The beginning index of the substring in the original string. Inclusive.
	 */
	private int start;

	/**
	 * The last index of the substring in the original string. Inclusive.
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

	public boolean conflictWith(SubString subString) {
		int start1 = this.start;
		int end1 = this.end;

		int start2 = subString.start;
		int end2 = subString.end;

		if ((start1 <= end2) && (end1 >= start2)) {
			return true;
		}

		return false;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + end;
		result = prime * result + ((originalString == null) ? 0 : originalString.hashCode());
		result = prime * result + start;
		result = prime * result + ((value == null) ? 0 : value.hashCode());
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
		SubString other = (SubString) obj;
		if (end != other.end)
			return false;
		if (originalString == null) {
			if (other.originalString != null)
				return false;
		} else if (!originalString.equals(other.originalString))
			return false;
		if (start != other.start)
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}
}
