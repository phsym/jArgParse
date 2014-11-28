package argparse.arguments.impl;

import java.util.function.Consumer;

import argparse.arguments.Argument;

public class StringArgument extends Argument<String> {
	
	public StringArgument(String name, String description) {
		super(name, description);
	}

	public StringArgument(String name, String description, Consumer<String> action) {
		super(name, description, action);
	}

	@Override
	public String parse(String value) {
		return value;
	}

	@Override
	public boolean requireValue() {
		return true;
	}

	@Override
	public String typeDesc() {
		return "string";
	}

}
