/*
Copyright (c) 2014, Pierre-Henri Symoneaux
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

* Redistributions of source code must retain the above copyright notice, this
  list of conditions and the following disclaimer.

* Redistributions in binary form must reproduce the above copyright notice,
  this list of conditions and the following disclaimer in the documentation
  and/or other materials provided with the distribution.

* Neither the name of jArgParse nor the names of its
  contributors may be used to endorse or promote products derived from
  this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package phsym;
import java.util.Arrays;
import java.util.Map;

import phsym.argparse.ArgParse;
import phsym.argparse.arguments.Type;

public final class Main implements Type {

	public static void main(String[] args) {
		ArgParse parser = new ArgParse("Test")
			.description("This is a simple test with java 8")
			.version("1.0")
			.defaultHelp()
			.addDefaultErrorHandler()
			.epilog("That's all you need");
		
		parser.label("Options :");
		
		parser.add(INT, "-i")
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
		
//		parser.add(INT, "-r")
//			.setDescription("Required")
//			.setRequired(true);
		
		Map<String, Object> x = parser.parse(Arrays.asList("-o", "toto", "-i", "12", "-l", "az,ze, er , rt", "-m", "tata:yoyo, titi: tutu"));;
		System.out.println(x);
		parser.printHelp();
	}
}
