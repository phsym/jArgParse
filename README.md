![License](http://img.shields.io/badge/license-BSD-lightgrey.svg)
[![Build Status](https://travis-ci.org/phsym/jArgParse.svg)](https://travis-ci.org/phsym/jArgParse)

# jArgParse

*Copyright &copy; 2014 Pierre-Henri Symoneaux*

> THIS SOFTWARE IS DISTRIBUTED WITHOUT ANY WARRANTY <br>
> Check LICENSE.txt file for more information. <br>


**jArgParse** is a command line argument parser for Java 8.
It's currently a work in progress

## How To Use :

```java
public class Main implements Type {

	public static void main(String[] args) {
		ArgParse parser = new ArgParse("Test")
			.description("This is a simple test with java 8")
			.version("1.0")
			.defaultHelp()
			.addDefaultErrorHandler()
			.epilog("That's all you need");
			
		parser.label("Options:");
			
		parser.add(INT, "-i", "--int")
			.positive()
			.lt(20)
			.andAssert((i) -> i % 2 == 0)
			.help("The i option")
			.required(true)
			.consume(System.out::println);
		
		parser.add(STRING, "-o")
			.pattern("^t.t.$")
			.setDefault("titi")
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
			
		parser.add(BOOL, "-b")
			.help("Boolean value")
			.action(() -> System.out.println("true"));
		
		parser.space();
			
		parser.add(STRING, "-c")
			.help("Multiple choices option")
			.choices("AB", "CD", "EF")
			.consume((x) -> System.out.println("Choice : " + x));
			
		parser.add(FILE, "-d")
			.create(false, true, true)
			.directory(true)
			.help("A Directory");
		
		Map<String, Object> x = parser.parse(args);

		/* ..... */
	}
}
```
