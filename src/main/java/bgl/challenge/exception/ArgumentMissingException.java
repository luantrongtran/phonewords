package bgl.challenge.exception;

public class ArgumentMissingException extends RuntimeException {
	public ArgumentMissingException() {
		super();
	}

	public ArgumentMissingException(String msg) {
		super(msg);
	}
}
