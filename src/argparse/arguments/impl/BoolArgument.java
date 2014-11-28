package argparse.arguments.impl;

import java.util.function.Consumer;

import argparse.arguments.Argument;

public class BoolArgument extends Argument<Boolean> {
	
	public BoolArgument(String name, String description) {
		super(name, description);
	}

	public BoolArgument(String name, String description, Consumer<Boolean> action) {
		super(name, description, action);
	}

	@Override
	public Boolean parse(String value) {
		return true;
	}
	
	@Override
	public boolean requireValue() {
		return false;
	}

	@Override
	public String typeDesc() {
		return null;
	}

}
