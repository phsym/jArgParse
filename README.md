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

Build result is a *jArgParse.jar* file
Of course Java 8 is required to build and run the library

## How To Use :

```java
public class Main implements Type {

	public static void main(String[] args) {
		ArgParse parser = new ArgParse("Test", "1.0", "This is a simple test with java 8");
		parser.add(INT)
			.setShortName("-i")
			.setDescription("The i option")
			.setRequired(true)
			.setAction(System.out::println);
		
		parser.add(STRING)
			.setShortName("-o")
			.setDefault("Default String")
			.setDescription("The o option")
			.setAction((x) -> System.out.println("The o option has been passed : " + x));
		
		parser.add(STRING_ARRAY)
			.setShortName("-l")
			.setDescription("List of strings")
			.setAction(System.out::println);
		
		parser.add(STRING_MAP)
			.setShortName("-m")
			.setDescription("Map value")
			.setAction(System.out::println);
		
		parser.addHelpFlag();
		
		parser.onError((e) -> System.err.println(e.getMessage()))
			.onError((e) -> parser.printHelp())
			.onError((e) -> System.exit(1));
		
		Map<String, Object> x = null;
		x = parser.parse(args);

		/* ..... */
	}
}
```
