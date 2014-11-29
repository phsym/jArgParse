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
		
		Map<String, Object> x = null;
		try {
			x = parser.parse(args);
		} catch (UnknownArgumentException | ValueRequiredException | MissingArgumentException e) {
			System.err.println(e.getMessage());
			parser.printHelp();
			System.exit(1);
		}
		/* ..... */
	}
}
```
