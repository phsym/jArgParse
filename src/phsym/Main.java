package phsym;
import java.util.Arrays;
import java.util.Map;

import phsym.argparse.ArgParse;
import phsym.argparse.arguments.Type;
import phsym.argparse.exceptions.UnknownArgumentException;
import phsym.argparse.exceptions.ValueRequiredException;

public class Main {

	public static void main(String[] args) throws UnknownArgumentException, ValueRequiredException {
		ArgParse parser = new ArgParse("Test", "1.0", "This is a simple test with java 8");
		parser.onString("-t", "The t option", System.out::println);
		parser.onString("-o", "The o option", (x) -> System.out.println("The o option has been passed : " + x));
		parser.onInt("-i", "Integer value", (i) -> System.out.println(i+1));
		parser.onBool("-b", "Boolean value", System.out::println);
		parser.onStringArray("-l", "List of strings", System.out::println);
		parser.onStringMap("-m", "Map value", System.out::println);
		parser.onBool("-h", "Print this help", (h) -> {parser.printHelp();System.exit(1);});
		parser.<String>on(Type.STRING, "--str", "String test", (s) -> System.out.println(s.trim()));

		System.out.println("Running it");
		Map<String, Object> x = parser.parse(Arrays.asList("-t", "coucou", "-o", "toto", "-i", "12", "-b", "-l", "az,ze, er , rt", "-m", "tata:yoyo, titi: tutu", "--str", "coucou"));
		System.out.println(x);
		parser.printHelp();
	}
}
