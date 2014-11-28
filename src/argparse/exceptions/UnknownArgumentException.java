package argparse.exceptions;

public class UnknownArgumentException extends Exception {
	private static final long serialVersionUID = -8723495139166161294L;
	
	public UnknownArgumentException(String argument) {
		super("Unknown argument : " + argument);
	}
}