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
package phsym.argparse.arguments;

import java.util.Arrays;
import java.util.List;

import phsym.argparse.exceptions.ArgParseException;

/**
 * Represent an argument that should be parsed as an array of E
 * @author phsym
 *
 * @param <E> The type of elements stored in the parsed array
 */
public abstract class ArrayArgument<E> extends Argument<List<E>> {
	
	protected char separator = ',';
	
	/**
	 * Default contructor
	 */
	public ArrayArgument() {
		super();
	}
	
	/**
	 * Set the separator character used in list to separate elements
	 * @param separator The separator to use (default is set to ',')
	 * @return this
	 */
	public ArrayArgument<E> separator(char separator) {
		this.separator = separator;
		return this;
	}

	@Override
	public final List<E> parse(String value) throws ArgParseException {
		return Arrays.asList(Arrays.stream(value.split("\\s*" + separator + "\\s*"))
			.map(this::parseElement)
			.toArray(this::createArray));
	}

	@Override
	public boolean requireValue() {
		return true;
	}
	
	@Override
	public Argument<List<E>> choices(String... choices) {
		throw new RuntimeException(getClass().getName() + " cannot have multiple choices");
	}
	
	/**
	 * Create an uninitialized array of elements
	 * @param len The size of the array to create
	 * @return The created array
	 */
	protected abstract E[] createArray(int len);
	
	/**
	 * Parse individual aray elements
	 * @param element The string to parse
	 * @return The parsed element
	 */
	protected abstract E parseElement(String element);
}
