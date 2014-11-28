package phsym.argparse.arguments;

import java.lang.reflect.InvocationTargetException;
import java.util.function.Consumer;

import phsym.argparse.arguments.impl.BoolArgument;
import phsym.argparse.arguments.impl.IntArgument;
import phsym.argparse.arguments.impl.StringArgument;
import phsym.argparse.arguments.impl.StringArrayArgument;
import phsym.argparse.arguments.impl.StringMapArgument;

public enum Type {
	BOOL(BoolArgument.class),
	INT(IntArgument.class),
	STRING(StringArgument.class),
	STRING_ARRAY(StringArrayArgument.class),
	STRING_MAP(StringMapArgument.class);
	
	private Class<? extends Argument<?>> clazz;
	
	Type(Class<? extends Argument<?>> clazz) {
		this.clazz = clazz;
	}
	
	
	public Argument<?> newInstance(String name, String help, Consumer<?> action) {
		try {
			return clazz.getConstructor(String.class, String.class, Consumer.class).newInstance(name, help, action);
		} catch (InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			throw new RuntimeException("FATAL unexpected error", e);
		}
	}
	
	public Argument<?> newInstance(String name, String help) {
//		return this.newInstance(name, help, null);
		try {
			return clazz.getConstructor(String.class, String.class).newInstance(name, help);
		} catch (InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			throw new RuntimeException("FATAL unexpected error", e);
		}
	}
}
