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
package phsym.argparse.arguments.impl;

import java.util.regex.Pattern;

import phsym.argparse.arguments.Argument;
import phsym.argparse.exceptions.ArgParseException;
import phsym.argparse.exceptions.InvalidValueException;

/**
 * Represent a String argument
 * @author phsym
 *
 */
public class StringArgument extends Argument<String> {
	
	private Pattern pattern = null;
	
	/**
	 * Default constructor
	 */
	public StringArgument() {
		super();
	}

	/**
	 * Add a pattern constraint on the value
	 * @param regex The pattern constraint
	 * @return this
	 */
	public StringArgument pattern(String regex) {
		pattern = Pattern.compile(regex);
		return this;
	}
	
	@Override
	public String parse(String value) throws ArgParseException {
		if(pattern != null && !pattern.matcher(value).matches())
			throw new InvalidValueException(getNames()[0], value, " does not match pattern " + pattern.pattern());
		return value;
	}

	@Override
	public boolean requireValue() {
		return true;
	}

	@Override
	public String typeDesc() {
		return "string";
	}

}
