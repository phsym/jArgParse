package phsym.argparse.arguments.impl;

import java.util.List;
import java.util.function.Consumer;

import phsym.argparse.arguments.ArrayArgument;

public class StringArrayArgument extends ArrayArgument<String> {
	
	public StringArrayArgument(String name, String description) {
		super(name, description);
	}

	public StringArrayArgument(String name, String description, Consumer<List<String>> action) {
		super(name, description, action);
	}

	@Override
	protected String parseElement(String element) {
		return element;
	}
	
	@Override
	protected String[] createArray(int len) {
		return new String[len];
	}

	@Override
	public String typeDesc() {
		return "string[, ...]";
	}

}
