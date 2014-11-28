package phsym.argparse.arguments.impl;

import java.util.function.Consumer;

import phsym.argparse.arguments.Argument;

public class IntArgument extends Argument<Integer> {

	public IntArgument(String name, String description) {
		super(name, description);
	}
	
	public IntArgument(String name, String description, Consumer<Integer> action) {
		super(name, description, action);
	}

	@Override
	public Integer parse(String value) {
		return Integer.parseInt(value);
	}

	@Override
	public boolean requireValue() {
		return true;
	}

	@Override
	public String typeDesc() {
		return "int";
	}

}
