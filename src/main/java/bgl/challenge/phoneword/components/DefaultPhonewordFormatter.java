package bgl.challenge.phoneword.components;

import java.util.Iterator;
import java.util.Set;

import bgl.challenge.phoneword.models.Pattern;
import bgl.challenge.phoneword.utils.CollectionUtils;

public class DefaultPhonewordFormatter implements PhonewordFormatter {

	@Override
	public String format(String word, Pattern pattern) {
		/*
		 * Find all indexes need to insert dashes in the original phone number.
		 */
		Set<Integer> dashIndexes = CollectionUtils.createIntegerDescendingSet();
		pattern.stream().forEach(subString -> {
			dashIndexes.add(subString.getStart());
			dashIndexes.add(subString.getEnd());
		});

		/*
		 * Insert dashes into the pattern.
		 * 
		 * Insert dashes from the end of the original phone number to the start so it
		 * won't change the original indexes of the other dashes.
		 */
		Iterator<Integer> iter = dashIndexes.iterator();
		/*
		 * the variable is to simulate end and start of a substring; this is because if
		 * it is the start position, the dash is inserted to the left of the index when
		 * with the end position, the dash is inserted to the right.
		 */
		boolean rightDash = true;
		while (iter.hasNext()) {
			int dashIndex = iter.next();
			if (dashIndex == word.length() - 1) {
				rightDash = !rightDash;
				continue;
			} else if (dashIndex == 0) {
				rightDash = !rightDash;
				continue;
			}

			if (rightDash) {
				String temp = word.substring(0, dashIndex + 1) + "-" + word.substring(dashIndex + 1);
				word = temp;
			} else {
				String temp = word.substring(0, dashIndex) + "-" + word.substring(dashIndex);
				word = temp;
			}

			rightDash = !rightDash;
		}

		// The formated word may contain 2 consecutive dashes
		word = word.replace("--", "-");

		return word;
	}
}
