package phsym.argparse;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import phsym.argparse.arguments.Argument;
import phsym.argparse.arguments.Type;
import phsym.argparse.exceptions.UnknownArgumentException;
import phsym.argparse.exceptions.ValueRequiredException;

public class ArgParse {

	private LinkedHashMap<String, Argument<?>> map;
	private String prog;
	private String version;
	private String description;

	public ArgParse(String prog, String version, String description) {
		this.prog = prog;
		this.version = version;
		this.description = description;
		map = new LinkedHashMap<>();
	}

	public <E> void add(Argument<E> arg) {
		map.put(arg.getName(), arg);
	}
	
	public void add(Type type, String name, String help) {
		add(type.newInstance(name, help));
	}
	
	public <E> void on(Type type, String name, String help, Consumer<E> action) {
		add(type.newInstance(name, help, action));
	}
	
	public void onInt(String name, String help, Consumer<Integer> action) {
		on(Type.INT, name, help, action);
	}
	
	public void onString(String name, String help, Consumer<String> action) {
		on(Type.STRING, name, help, action);
	}
	
	public void onBool(String name, String help, Consumer<Boolean> action) {
		on(Type.BOOL, name, help, action);
	}
	
	public void onStringArray(String name, String help, Consumer<List<String>> action) {
		on(Type.STRING_ARRAY, name, help, action);
	}
	
	public void onStringMap(String name, String help, Consumer<Map<String, String>> action) {
		on(Type.STRING_MAP, name, help, action);
	}

	public Map<String, Object> parse(List<String> args) throws UnknownArgumentException, ValueRequiredException {
		Map<String, Object> values = new HashMap<>();
		Iterator<String> it = args.iterator();
		while (it.hasNext()) {
			Object value = null;
			String n = it.next();
			Argument<?> arg = map.get(n);
			if (arg == null)
				throw new UnknownArgumentException(n);
			if(arg.requireValue()) {
				if (it.hasNext())
					value = arg.call(it.next());
			}
			else
				value = arg.call();
			values.put(arg.getName(), value);
		}
		return values;
	}

	public void printHelp() {
		System.out.println("Usage for : " + prog + " version " + version);
		System.out.println(description);
		map.values().stream().map(Argument::helpStr).forEach(System.out::println);
	}
}
