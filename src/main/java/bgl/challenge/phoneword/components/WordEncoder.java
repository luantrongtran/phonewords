package bgl.challenge.phoneword.components;

/**
 * 
 * @author luant
 *
 */
public interface WordEncoder {
	/**
	 * Translates a string into a number
	 * @param word
	 * @return
	 */
	public int encode(String word);
}
