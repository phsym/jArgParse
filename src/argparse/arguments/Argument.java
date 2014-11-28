package argparse.arguments;
import java.util.function.Consumer;

import argparse.exceptions.ValueRequiredException;

public abstract class Argument<E> {

	private String name;
	private String description;
	private Consumer<E> action;
	
	public Argument(String name, String description) {
		this.name = name;
		this.description = description;
	}
	
	public Argument(String name, String description, Consumer<E> action) {
		this(name, description);
		this.action = action;
	}
	
	public String getName() {
		return name;
	}

	public String helpStr() {
		StringBuilder help = new StringBuilder(" ");
		help.append(name);
		if(requireValue())
			help.append(" <").append(typeDesc()).append(">");
		else
			help.append("\t");
		help.append("\t").append(description);
		return help.toString();
	}

	public E call() throws ValueRequiredException {
		return call(null);
	}
	
	public E call(String value) throws ValueRequiredException {
		if(value == null && requireValue())
			throw new ValueRequiredException(name);
		E parsedVal = parse(value);
		if(action != null)
			action.accept(parsedVal);
		return parsedVal;
	}
	
	public abstract E parse(String value);
	public abstract boolean requireValue();
	public abstract String typeDesc();
	
}
