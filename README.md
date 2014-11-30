[![Build Status](https://travis-ci.org/phsym/jArgParse.svg)](https://travis-ci.org/phsym/jArgParse)

# jArgParse

*Copyright &copy; 2014 Pierre-Henri Symoneaux*

> THIS SOFTWARE IS DISTRIBUTED WITHOUT ANY WARRANTY <br>
> Check LICENSE.txt file for more information. <br>


**jArgParse** is a command line argument parser for Java 8.
It's currently a work in progress

## How to build
Checkout the project, then in a terminal, go to the project directory and invoke the Ant builder with 

> ant

Build result is a **jArgParse.jar** file <br>
Of course Java 8 is required to build and run the library

## How To Use :

```java
public class Main implements Type {

	public static void main(String[] args) {
		ArgParse parser = new ArgParse("Test")
			.description("This is a simple test with java 8")
			.version("1.0")
			.defautHelp()
			.addDefaultErrorHandler()
			.epilog("That's all you need");
			
		parser.add(INT, "-i")
			.help("The i option")
			.required(true)
			.consume(System.out::println);
		
		parser.add(STRING, "-o")
			.setDefault("Default String")
			.help("The o option")
			.consume((x) -> System.out.println("The o option has been passed : " + x))
			.consume((x) -> System.out.println("And again : " + x));
		
		parser.add(STRING_ARRAY, "-l")
			.separator(',')
			.help("List of strings")
			.consume(System.out::println);
		
		parser.add(STRING_MAP, "-m")
			.separators(',', ':')
			.help("Map value")
			.consume(System.out::println);
			
		parser.add(BOOL, "-b")
			.help("Boolean value")
			.action(() -> System.out.println("true"));
			
		parser.add(STRING, "-c")
			.help("Multiple choices option")
			.choices("AB", "CD", "EF")
			.consume((x) -> System.out.println("Choice : " + x));
		
		Map<String, Object> x = parser.parse(args);

		/* ..... */
	}
}
```
