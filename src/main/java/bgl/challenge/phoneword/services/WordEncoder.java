package bgl.challenge.phoneword.services;

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
