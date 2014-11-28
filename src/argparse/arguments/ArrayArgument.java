package argparse.arguments;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public abstract class ArrayArgument<E> extends Argument<List<E>> {
	
	public ArrayArgument(String name, String description) {
		super(name, description);
	}

	public ArrayArgument(String name, String description, Consumer<List<E>> action) {
		super(name, description, action);
	}

	@Override
	public final List<E> parse(String value) {
		return Arrays.asList(Arrays.stream(value.split("\\s*,\\s*"))
			.map(this::parseElement)
			.toArray(this::createArray));
	}

	@Override
	public boolean requireValue() {
		return true;
	}
	
	protected abstract E[] createArray(int len);
	protected abstract E parseElement(String element);
}
