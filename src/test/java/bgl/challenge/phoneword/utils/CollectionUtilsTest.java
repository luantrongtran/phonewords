package bgl.challenge.phoneword.utils;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

public class CollectionUtilsTest {

	@Test
	void testSortDescending_Given3IntegerList_ThenSortCorrectly() throws Exception {
		// Given
		List<Integer> unorderedList = new ArrayList<Integer>();
		int smallest = 1;
		int secondLargest = 2;
		int largest = 3;
		unorderedList.add(smallest);
		unorderedList.add(secondLargest);
		unorderedList.add(largest);
		
		// When
		CollectionUtils.sortDescending(unorderedList);

		// Then
		assertThat(unorderedList.get(0)).isEqualTo(largest);
		assertThat(unorderedList.get(1)).isEqualTo(secondLargest);
		assertThat(unorderedList.get(2)).isEqualTo(smallest);
	}
	
	@Test
	void testSortDescending_Given2IntegerList_ThenSortCorrectly() throws Exception {
		// Given
		List<Integer> unorderedList = new ArrayList<Integer>();
		int smallest = 1;
		int largest = 3;
		unorderedList.add(smallest);
		unorderedList.add(largest);
		
		// When
		CollectionUtils.sortDescending(unorderedList);

		// Then
		assertThat(unorderedList.get(0)).isEqualTo(largest);
		assertThat(unorderedList.get(1)).isEqualTo(smallest);
	}

	@Test
	void testCreateIntegerDescendingSet() throws Exception {
		// Given
		int smallest = 1;
		int secondSmallest = 2;
		int largest = 3;

		// When
		Set<Integer> actualSet = CollectionUtils.createIntegerDescendingSet();
		actualSet.add(smallest);
		actualSet.add(secondSmallest);
		actualSet.add(largest);

		// Then
		int expectedSize = 3;
		assertThat(actualSet.size()).isEqualTo(expectedSize);
		Iterator<Integer> iter = actualSet.iterator();
		int actualFirstNumber = iter.next();
		assertThat(actualFirstNumber).isEqualTo(largest);
		int actualSecondNumber = iter.next();
		assertThat(actualSecondNumber).isEqualTo(secondSmallest);
		int actualThirdNumber = iter.next();
		assertThat(actualThirdNumber).isEqualTo(smallest);
	}

	@Test
	void testCreateIntegerDescendingSet_2() throws Exception {
		// Given
		int smallest = 11;
		int secondSmallest = 12;
		int largest = 13;

		// When
		Set<Integer> actualSet = CollectionUtils.createIntegerDescendingSet();
		actualSet.add(secondSmallest);
		actualSet.add(smallest);
		actualSet.add(largest);

		// Then
		int expectedSize = 3;
		assertThat(actualSet.size()).isEqualTo(expectedSize);
		Iterator<Integer> iter = actualSet.iterator();
		int actualFirstNumber = iter.next();
		assertThat(actualFirstNumber).isEqualTo(largest);
		int actualSecondNumber = iter.next();
		assertThat(actualSecondNumber).isEqualTo(secondSmallest);
		int actualThirdNumber = iter.next();
		assertThat(actualThirdNumber).isEqualTo(smallest);
	}

}
