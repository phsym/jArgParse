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
		ArgParse parser = new ArgParse("Test", "1.0", "This is a simple test with java 8");
		parser.add(INT, "-i")
			.setDescription("The i option")
			.setRequired(true)
			.addAction(System.out::println);
		
		parser.add(STRING, "-o")
			.setDefault("Default String")
			.setDescription("The o option")
			.addAction((x) -> System.out.println("The o option has been passed : " + x))
			.addAction((x) -> System.out.println("And again : " + x));
		
		parser.add(STRING_ARRAY, "-l")
			.setSeparators(',', ':')
			.setDescription("List of strings")
			.addAction(System.out::println);
		
		parser.add(STRING_MAP, "-m")
			.setSeparators(',', ':')
			.setDescription("Map value")
			.addAction(System.out::println);
			
		parser.add(STRING, "-c")
			.setDescription("Multiple choices option")
			.choices("AB", "CD", "EF")
			.addAction((x) -> System.out.println("Choice : " + x));
		
		parser.addHelpFlag();
		parser.addVersionFlag();
		parser.addDefaultErrorHandler();
		
		Map<String, Object> x = parser.parse(args);

		/* ..... */
	}
}
```
