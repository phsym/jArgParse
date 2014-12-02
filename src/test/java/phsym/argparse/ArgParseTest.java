package phsym.argparse;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Map;

import org.junit.Test;

import phsym.argparse.arguments.Type;
import phsym.argparse.exceptions.ArgParseException;
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
		assertTrue(help[0].trim().equals("Usage: Test <options>"));
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
		assertTrue(help[0].trim().equals("Usage: Test <options>"));
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
		assertTrue(help[0].trim().equals("Usage: Test -i int <options>"));
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
	public void test_int() throws ArgParseException {
		ArgParse parser = new ArgParse("Test");
		parser.add(INT, "-i")
			.positive()
			.lt(20)
			.andAssert((i) -> i % 2 == 0)
			.help("The i option");
		String[] help = parser.help().split("\n");
		assertEquals(help.length, 2);
		assertTrue(help[0].trim().equals("Usage: Test <options>"));
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
		
		Map<String, Object> res = parser.parseThrow(Arrays.asList("-i", "12"));
		assertNotNull(res);
		assertEquals(res.get("-i"), 12);
	}
}
