package bgl.challenge.phoneword.models;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SubStringTest {

	@BeforeEach
	protected void setUpBeforeClass() throws Exception {
	}

	@Test
	public void isConflictWith_Given2olverlapSubStrings_ThenReturnTrue() {
		// Given
		String originalEncodedString = "222";
		String subEncodedString1 = "22";
		int start1 = 0;
		int end1 = 1;
		SubString subString1 = new SubString(originalEncodedString, subEncodedString1, start1, end1);
		
		String subEncodedString2 = "22";
		int start2 = 1;
		int end2 = 2;
		
		// When
		SubString subString2 = new SubString(originalEncodedString, subEncodedString2, start2, end2);
		
		// Then
		assertThat(subString1.conflictWith(subString2)).isTrue();
		assertThat(subString2.conflictWith(subString1)).isTrue();
	}
	
	@Test
	public void isConflictWith_Given2SeparateSubStrings_ThenReturnFalse() {
		// Given
		String originalEncodedString = "1122";
		String subEncodedString1 = "11";
		int start1 = 0;
		int end1 = 1;
		SubString subString1 = new SubString(originalEncodedString, subEncodedString1, start1, end1);
		
		String subEncodedString2 = "22";
		int start2 = 2;
		int end2 = 3;
		
		// When
		SubString subString2 = new SubString(originalEncodedString, subEncodedString2, start2, end2);
		
		// Then
		assertThat(subString1.conflictWith(subString2)).isFalse();
		assertThat(subString2.conflictWith(subString1)).isFalse();
	}
	
	@Test
	public void isConflictWith_Given2CompletelyOverllaped_ThenReturnTrue() {
		// Given
		String originalEncodedString = "1122";
		String subEncodedString1 = "1122";
		int start1 = 0;
		int end1 = 3;
		SubString subString1 = new SubString(originalEncodedString, subEncodedString1, start1, end1);
		
		String subEncodedString2 = "12";
		int start2 = 1;
		int end2 = 2;
		
		// When
		SubString subString2 = new SubString(originalEncodedString, subEncodedString2, start2, end2);
		
		// Then
		assertThat(subString1.conflictWith(subString2)).isTrue();
		assertThat(subString2.conflictWith(subString1)).isTrue();
	}
}
