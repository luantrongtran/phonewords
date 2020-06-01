package bgl.challenge.phoneword.components;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import bgl.challenge.phoneword.models.Pattern;
import bgl.challenge.phoneword.utils.CollectionUtils;

public class DefaultPhonewordFormatter implements PhonewordFormatter {

	@Override
	public String format(String word, Pattern pattern) {
		/*
		 * Find all indexes need to insert dashes in the original phone number.
		 */
		List<Integer> dashIndexes = new ArrayList<>();
		pattern.stream().forEach(subString -> {
			dashIndexes.add(subString.getStart());
			dashIndexes.add(subString.getEnd());
		});
		// sorting the index descending
		CollectionUtils.sortDescending(dashIndexes);

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

			if (rightDash) {
				// insert dash to the right
				if (dashIndex != word.length() - 1) {
					// if not the last index
					String temp = word.substring(0, dashIndex + 1) + "-" + word.substring(dashIndex + 1);
					word = temp;
				}
			} else {
				// insert dash to the left
				if (dashIndex != 0) {
					// if not the first index
					String temp = word.substring(0, dashIndex) + "-" + word.substring(dashIndex);
					word = temp;
				}
			}

			rightDash = !rightDash;
		}

		// The formated word may contain 2 consecutive dashes
		word = word.replace("--", "-");

		return word;
	}
}
