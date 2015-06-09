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
package phsym.argparse;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;











import org.junit.Test;











import phsym.argparse.arguments.Type;
import phsym.argparse.exceptions.ArgParseException;
import phsym.argparse.exceptions.ArgumentConflictException;
import phsym.argparse.exceptions.InvalidValueException;
import phsym.argparse.exceptions.MissingArgumentException;
import phsym.argparse.exceptions.UnknownArgumentException;
import phsym.argparse.exceptions.ValueRequiredException;

public class ArgParseTest implements Type {

	@Test
	public void test_with_no_arg() {
		ArgParse argparse = new ArgParse("Test");
		String[] help = argparse.help().split("\n");
		assertEquals(help.length, 1);
		assertTrue(help[0].trim().equals("Usage: Test [options]"));
	}

	
	@Test
	public void test_help_string() {
		ArgParse argparse = new ArgParse("Test")
			.description("Description text")
			.version("1.0")
			.defaultHelp()
			.addDefaultErrorHandler()
			.epilog("Epilog");
		String[] help = argparse.help().split("\n");
		assertEquals(help.length, 5);
		assertTrue(help[0].trim().equals("Usage: Test [options]"));
		assertTrue(help[1].trim().equals("Description text"));
		assertTrue(help[2].contains("-v"));
		assertTrue(help[2].contains("Print version"));
		assertTrue(help[3].trim().contains("-h"));
		assertTrue(help[3].trim().contains("Print this help"));
		assertTrue(help[4].trim().equals("Epilog"));
	}
	
	@Test
	public void test_error_handler() {
		ArgParse argparse = new ArgParse("Test")
			.onError((e) -> {throw new RuntimeException("test");});
		try {
			argparse.parse(Arrays.asList("-u"));
			fail("No exception thrown");
		} catch(RuntimeException e) {
			assertEquals(e.getMessage(), "test");
		}
	}
	
	@Test
	public void test_required() throws ArgParseException {
		ArgParse parser = new ArgParse("Test");
		parser.add(INT, "-i")
			.required(true);
		
		String[] help = parser.help().split("\n");
		assertEquals(help.length, 2);
		assertTrue(help[0].trim().equals("Usage: Test -i <int> [options]"));
		try {
			parser.parseThrow(Arrays.asList());
			fail("Exception was not thrown");
		} catch(MissingArgumentException e) {}
	}
	
	@Test
	public void test_unknown_argument() throws ArgParseException {
		ArgParse parser = new ArgParse("Test");
		try {
			parser.parseThrow(Arrays.asList("-u"));
			fail("Exception was not thrown");
		} catch(UnknownArgumentException e){}
	}
	
	@Test
	public void test_callbacks() throws ArgParseException {
		ArgParse parser = new ArgParse("Test");
		parser.add(STRING, "-s")
			.action(() -> {throw new RuntimeException("test");});
		parser.add(STRING, "-t")
			.consume((str) -> {throw new RuntimeException(str);});
		
		try {
			parser.parseThrow(Arrays.asList("-s", "foo"));
			fail("Exception was not thrown");
		} catch(RuntimeException e){
			assertEquals(e.getMessage(), "test");
		}
		try {
			parser.parseThrow(Arrays.asList("-t", "bar"));
			fail("Exception was not thrown");
		} catch(RuntimeException e){
			assertEquals(e.getMessage(), "bar");
		}
	}
	
	@Test
	public void test_default() {
		ArgParse parser = new ArgParse("Test");
		parser.add(INT, "-i").setDefault(13);
		Object res = parser.parse(Arrays.asList()).get("i");
		assertNotNull(res);
		assertEquals(res, 13);
	}
	
	@Test
	public void test_conflict() {
		ArgParse parser = new ArgParse("Test");
		parser.add(INT, "-i");
		try {
			parser.add(INT, "-i");
			fail("No ArgumentConflictException thrown");
		} catch(ArgumentConflictException e) {
			
		}
	}
	
	@Test
	public void test_int() throws ArgParseException {
		ArgParse parser = new ArgParse("Test");
		parser.add(INT, "-i")
			.positive()
			.lt(20)
			.andAssert((i) -> i % 2 == 0)
			.help("The i option");
		String[] help = parser.help().split("\n");
		assertEquals(help.length, 2);
		assertTrue(help[0].trim().equals("Usage: Test [options]"));
		assertTrue(help[1].contains("-i"));
		assertTrue(help[1].contains("The i option"));
		
		try {
			parser.parseThrow(Arrays.asList("-i"));
			fail("Exception was not thrown");
		} catch(ValueRequiredException e){}
		
		try {
			parser.parseThrow(Arrays.asList("-i", "foo"));
			fail("Exception was not thrown");
		} catch(InvalidValueException e){}
		
		try {
			parser.parseThrow(Arrays.asList("-i", "-2"));
			fail("Exception was not thrown");
		} catch(InvalidValueException e){}
		
		try {
			parser.parseThrow(Arrays.asList("-i", "20"));
			fail("Exception was not thrown");
		} catch(InvalidValueException e){}
		
		try {
			parser.parseThrow(Arrays.asList("-i", "13"));
			fail("Exception was not thrown");
		} catch(InvalidValueException e){}
		
		Map<String, Object> res = parser.parseThrow(Arrays.asList("-i", "12"));
		assertNotNull(res);
		assertEquals(res.get("i"), 12);
	}
	
	@Test
	public void test_file() throws IOException, ArgParseException {
		ArgParse parser = new ArgParse("Test");
		parser.add(FILE, "-d")
			.exists(true)
			.directory(true)
			.help("A Directory");
		parser.add(FILE, "-f")
			.create(false, true, true)
			.help("A File");
		
		File tmpFile = File.createTempFile("tmp", "test");
		tmpFile.delete();
		File tmpDir = File.createTempFile("tmpdir", "test");
		tmpDir.delete();
		tmpDir.mkdirs();
		
		String[] args = new String[]{"-f", tmpFile.getAbsolutePath(), "-d", tmpDir.getAbsolutePath()};
		Map<String, Object> res = parser.parseThrow(args);
		assertEquals(res.get("f"), tmpFile);
		assertEquals(res.get("d"), tmpDir);
	}
	
	@Test
	public void test_bool() {
		ArgParse parser = new ArgParse("Test");
		parser.add(BOOL, "-b")
			.setDefault(false);
		Map<String, Object> res = parser.parse(new String[]{"-b"});
		assertTrue((Boolean)res.get("b"));
		
		parser = new ArgParse("Test");
		parser.add(BOOL, "-b")
			.setDefault(false);
		res = parser.parse(new String[]{});
		assertFalse((Boolean)res.get("b"));
	}
}
