[![Build Status](https://travis-ci.org/phsym/jArgParse.svg)](https://travis-ci.org/phsym/jArgParse)

# jArgParse.

*Copyright &copy; 2014 Pierre-Henri Symoneaux*

> THIS SOFTWARE IS DISTRIBUTED WITHOUT ANY WARRANTY <br>
> See the license section for more information. <br>


**jArgParse** is a command line argument parser for Java 8.
It's currently a work in progress

## How To Use :

```java
ArgParse parser = new ArgParse("Test", "1.0", "This is a simple test with java 8");`<br>
parser.onString("-o", "The o option", (x) -> System.out.println("The o option has been passed : " + x));
parser.onInt("-i", "Integer value", (i) -> System.out.println(i+1));
parser.onBool("-h", "Print this help", (h) -> {parser.printHelp();System.exit(1);});
parser.parse(args);
```
