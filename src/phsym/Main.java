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
import phsym.argparse.exceptions.UnknownArgumentException;
import phsym.argparse.exceptions.ValueRequiredException;

public class Main {

	public static void main(String[] args) throws UnknownArgumentException, ValueRequiredException {
		ArgParse parser = new ArgParse("Test", "1.0", "This is a simple test with java 8");
		parser.onString("-t", "The t option", System.out::println);
		parser.onString("-o", "The o option", (x) -> System.out.println("The o option has been passed : " + x));
		parser.onInt("-i", "Integer value", (i) -> System.out.println(i+1));
		parser.onBool("-b", "Boolean value", System.out::println);
		parser.onStringArray("-l", "List of strings", System.out::println);
		parser.onStringMap("-m", "Map value", System.out::println);
		parser.onBool("-h", "Print this help", (h) -> {parser.printHelp();System.exit(1);});
		parser.<String>on(Type.STRING, "--str", "String test", (s) -> System.out.println(s.trim()));

		System.out.println("Running it");
		Map<String, Object> x = parser.parse(Arrays.asList("-t", "coucou", "-o", "toto", "-i", "12", "-b", "-l", "az,ze, er , rt", "-m", "tata:yoyo, titi: tutu", "--str", "coucou"));
		System.out.println(x);
		parser.printHelp();
	}
}
