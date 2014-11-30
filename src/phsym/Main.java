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
		ArgParse parser = new ArgParse("Test", "1.0", "This is a simple test with java 8");
		parser.add(INT, "-i")
			.setRequired(true)
			.setDescription("The i option")
			.addAction(System.out::println);
		
		parser.add(STRING, "-o")
			.setDescription("The o option")
			.addAction((x) -> System.out.println("The o option has been passed : " + x))
			.addAction((x) -> System.out.println("And again : " + x));
		
		parser.add(STRING_ARRAY, "-l")
			.setSeparator(',')
			.setDescription("List of strings")
			.addAction(System.out::println);
		
		parser.add(STRING_MAP, "-m")
			.setSeparators(',', ':')
			.setDescription("Map value")
			.addAction(System.out::println);
		
		parser.add(INT, "--default")
			.setDescription("Test with default")
			.setDefault(12)
			.addAction((i) -> System.out.println("Default : " + i));
		
		parser.add(STRING, "-c")
		.setDescription("Multiple choices option")
		.choices("AB", "CD", "EF")
		.addAction((x) -> System.out.println("Choice : " + x));
		
//		parser.add(INT, "-r")
//			.setDescription("Required")
//			.setRequired(true);
		
		parser.addHelpFlag();
		parser.addVersionFlag();
		parser.addDefaultErrorHandler();
		
		Map<String, Object> x = parser.parse(Arrays.asList("-o", "toto", "-i", "12", "-l", "az,ze, er , rt", "-m", "tata:yoyo, titi: tutu"));;
		System.out.println(x);
		parser.printHelp();
	}
}
