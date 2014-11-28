package phsym.argparse.arguments;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public abstract class MapArgument<V> extends Argument<Map<String, V>> {
	
	public MapArgument(String name, String description) {
		super(name, description);
	}

	public MapArgument(String name, String description,	Consumer<Map<String, V>> action) {
		super(name, description, action);
	}

	@Override
	public Map<String, V> parse(String value) {
		return Arrays.stream(value.split("\\s*,\\s*"))
			.map((s) -> s.split("\\s*:\\s*"))
			.reduce(new HashMap<>(), (r,e) -> {r.put(e[0], parseValue(e[1])); return r;}, (a, b) -> {a.putAll(b); return a;});
	}

	@Override
	public final boolean requireValue() {
		return true;
	}
	
	protected abstract V parseValue(String value);
}
