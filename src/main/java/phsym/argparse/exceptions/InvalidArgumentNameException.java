package phsym.argparse.exceptions;

/**
 * An exception thrown if an argument name is invalid. This is a {@link RuntimeException}
 * since most of the time, argument names are hardcoded
 * @author phsym
 *
 */
public class InvalidArgumentNameException extends RuntimeException {

	private static final long serialVersionUID = -6944725241956686887L;

	public InvalidArgumentNameException(String message) {
		super(message);
	}
}
