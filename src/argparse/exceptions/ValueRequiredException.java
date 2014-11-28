package argparse.exceptions;

public class ValueRequiredException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public ValueRequiredException(String argument) {
		super("Argument " + argument + " require a value");
	}
}
