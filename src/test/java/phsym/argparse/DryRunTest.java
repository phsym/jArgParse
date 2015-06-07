package phsym.argparse;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.Test;

import phsym.argparse.ArgParse;
import phsym.argparse.arguments.Type;

//TODO : Split those into smaller tests
public class DryRunTest implements Type {

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Test
	public void dry_run() {
		ArgParse parser = new ArgParse("Test")
			.description("This is a simple test with java 8")
			.version("1.0")
			.defaultHelp()
			.addDefaultErrorHandler()
			.epilog("That's all you need");
		
		parser.label("Options :");
		
		parser.add(INT, "-i", "--int")
			.positive()
			.lt(20)
			.andAssert((i) -> i % 2 == 0)
			.required(true)
			.help("The i option")
			.consume(System.out::println);
			
		parser.add(STRING, "-o")
			.pattern("^t.t.$")
			.help("The o option")
			.consume((x) -> System.out.println("The o option has been passed : " + x))
			.consume((x) -> System.out.println("And again : " + x));
		
		parser.add(STRING_ARRAY, "-l")
			.separator(',')
			.help("List of strings")
			.consume(System.out::println);
		
		parser.add(STRING_MAP, "-m")
			.separators(',', ':')
			.dest("map")
			.help("Map value")
			.consume(System.out::println);
		
		parser.add(INT, "--default")
			.help("Test with default")
			.setDefault(12)
			.consume((i) -> System.out.println("Default : " + i));
		
		parser.space();
		
		parser.add(BOOL, "-b")
			.help("Boolean value")
			.action(() -> System.out.println("true"));
		
		parser.add(STRING, "-c")
			.help("Multiple choices option")
			.choices("AB", "CD", "EF")
			.consume((x) -> System.out.println("Choice : " + x));
		
		parser.add(FILE, "-d")
			.create(false, true, true)
			.directory(true)
			.help("A Directory");
		
	//	parser.add(INT, "-r")
	//		.setDescription("Required")
	//		.setRequired(true);
		
		Map<String, Object> x = parser.parse(Arrays.asList("-o", "toto", "--int", "12", "-l", "az,ze, er , rt", "-m", "tata:yoyo, titi: tutu"));;
		System.out.println(x);
		parser.printHelp();
	}

}
