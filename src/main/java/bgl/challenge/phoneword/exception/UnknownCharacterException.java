package bgl.challenge.phoneword.exception;

public class UnknownCharacterException extends RuntimeException {
	public UnknownCharacterException() {
		super();
	}

	public UnknownCharacterException(String msg) {
		super(msg);
	}
}
