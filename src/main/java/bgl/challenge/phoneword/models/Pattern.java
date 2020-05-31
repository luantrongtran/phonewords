package bgl.challenge.phoneword.models;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * @formatter:off
 * 
 * For example: given: 
 * - A dictionary has 3 words: 'AAA', 'BBB' and 'DDD'; and
 * the encoded numbers are '222' for 'AAA' and 'BBB;' and '333' for 'DDD' 
 * - the input number: 2222 333
 * 
 * The possible patterns of the input number against the dictionary are:
 * - {222}2-333 : contain 1 substring 
 * - {222}2{333}  : contain 2 substrings
 * - 2{222}{333}  : contain 2 substrings
 * - {2{22}2}333: contain 2 substrings overlapped each other
 * 
 * numbers inside curly brackets are not only substrings of the input number but they also exist 
 * in the given dictionary, e.g. 222 is a substring of '2222 33', and is 'AAA' or 'BBB' in the
 * dictionary  
 * 
 * and a list of SubString represent a Pattern.
 * 
 * @formatter:on
 * @author luant
 *
 */
public class Pattern {
	private String originalPhoneNumber;
	private List<SubString> subStrings;

	public Pattern() {

	}

	public Pattern(String originalPhoneNumber, List<SubString> subStrings) {
		this.subStrings = subStrings;
		this.originalPhoneNumber = originalPhoneNumber;
	}

	public Stream<SubString> stream() {
		return this.subStrings.stream();
	}

	public void add(SubString subString) {
		if (subStrings == null) {
			subStrings = new ArrayList<>();
		}
		subStrings.add(subString);
	}

	public void addAll(Pattern subStrings2) {
		if (subStrings == null) {
			subStrings = new ArrayList<>();
		}
		subStrings.addAll(subStrings2.getSubStrings());
	}

	public List<SubString> getSubStrings() {
		return subStrings;
	}

	public void setSubStrings(List<SubString> subStrings) {
		this.subStrings = subStrings;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((subStrings == null) ? 0 : subStrings.hashCode());
		return result;
	}
	
	public boolean isValid() {
		boolean containsConflictedSubString = this.stream().map(subString -> {
			boolean conflictEmerged = this.stream().map(otherSubString -> {
				if (subString.equals(otherSubString)) {
					// the same substring
					return false;
				}
				return subString.conflictWith(otherSubString);
			}).reduce(false, (conflictOccurredWithSubString, currentConflict) -> {
				return conflictOccurredWithSubString | currentConflict;
			});
			return conflictEmerged;
		}).reduce(false, (anyConflict, conf) -> {
			return anyConflict | conf;
		});
		
		boolean isValid = !containsConflictedSubString;
		
		return isValid;
	}

	/**
	 * 2 patterns are duplicated/equal when their subStrings are the same regardless
	 * the order
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Pattern other = (Pattern) obj;
		if (subStrings == null) {
			if (other.subStrings != null)
				return false;
		} else if (!subStrings.equals(other.subStrings)) {
			return false;
		}

		if (this.subStrings.size() != other.subStrings.size()) {
			return false;
		}

		boolean equal = this.subStrings.containsAll(other.getSubStrings());

		return equal;
	}

	public String getOriginalPhoneNumber() {
		return originalPhoneNumber;
	}

	public void setOriginalPhoneNumber(String originalPhoneNumber) {
		this.originalPhoneNumber = originalPhoneNumber;
	}
}
