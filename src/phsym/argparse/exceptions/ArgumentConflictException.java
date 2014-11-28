package phsym.argparse.exceptions;

public class ArgumentConflictException extends RuntimeException {

	private static final long serialVersionUID = -4920296464316982123L;

	public ArgumentConflictException() {
	}

	public ArgumentConflictException(String message) {
		super(message);
	}

	public ArgumentConflictException(Throwable cause) {
		super(cause);
	}

	public ArgumentConflictException(String message, Throwable cause) {
		super(message, cause);
	}

	public ArgumentConflictException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
