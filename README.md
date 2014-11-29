[![Build Status](https://travis-ci.org/phsym/jArgParse.svg)](https://travis-ci.org/phsym/jArgParse)

# jArgParse.

*Copyright &copy; 2014 Pierre-Henri Symoneaux*

> THIS SOFTWARE IS DISTRIBUTED WITHOUT ANY WARRANTY <br>
> See the license section for more information. <br>


**jArgParse** is a command line argument parser for Java 8.
It's currently a work in progress

## How To Use :

```java
public class Main implements Type {

	public static void main(String[] args) throws UnknownArgumentException, ValueRequiredException {
		ArgParse parser = new ArgParse("Test", "1.0", "This is a simple test with java 8");
		parser.add(INT)
			.setShortName("-i")
			.setDescription("The i option")
			.setAction(System.out::println);
		
		parser.add(STRING)
			.setShortName("-o")
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
		
		parser.add(BOOL)
			.setShortName("-h")
			.setDescription("Print this help")
			.setAction((h) -> {parser.printHelp();System.exit(1);});
		
		Map<String, Object> x = parser.parse(args);
		/* ..... */
	}
}
```
