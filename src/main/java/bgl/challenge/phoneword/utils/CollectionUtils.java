package bgl.challenge.phoneword.utils;

import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

public class CollectionUtils {
	public static Set<Integer> createIntegerDescendingSet() {
		Set<Integer> dashIndexes = new TreeSet<>(new Comparator<Integer>() {
			@Override
			public int compare(Integer o1, Integer o2) {
				// descending order
				return -o1.compareTo(o2);
			}
		});
		
		return dashIndexes;
	}
}
