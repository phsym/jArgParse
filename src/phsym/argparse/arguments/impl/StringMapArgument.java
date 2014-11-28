package phsym.argparse.arguments.impl;

import java.util.Map;
import java.util.function.Consumer;

import phsym.argparse.arguments.MapArgument;

public class StringMapArgument extends MapArgument<String> {
	
	public StringMapArgument(String name, String description) {
		super(name, description);
	}

	public StringMapArgument(String name, String description, Consumer<Map<String, String>> action) {
		super(name, description, action);
	}

	@Override
	protected String parseValue(String value) {
		return value;
	}

	@Override
	public String typeDesc() {
		return "key:value[, ...]";
	}

}
