package phsym.argparse;

import static org.junit.Assert.*;

import org.junit.Test;

public class ArgParseTest {

	@Test
	public void test() {
		ArgParse argparse = new ArgParse("Test")
			.description("Description text")
			.version("1.0")
			.defaultHelp()
			.addDefaultErrorHandler()
			.epilog("Epilog");
	}

}
