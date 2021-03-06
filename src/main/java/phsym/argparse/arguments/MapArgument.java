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
import java.util.HashMap;
import java.util.Map;

import phsym.argparse.exceptions.ArgParseException;

/**
 * Represent an argument that should be parsed as a map of <String, E>
 * @author phsym
 *
 * @param <E> The type of elements stored in the parsed map
 */
public abstract class MapArgument<V> extends Argument<Map<String, V>> {
	
	protected char entrySeparator = ',';
	protected char keySeparator = ':';
	
	/**
	 * Default contructor
	 */
	public MapArgument() {
	}
	
	/**
	 * Set the separator character used in list to separate elements
	 * @param keySeparator The separator used for separating keys and entries (default to ':')
	 * @param entrySeparator The separator used to separate groups odfkey/entry (default to ',')
	 * @return this
	 */
	public MapArgument<V> separators(char entrySeparator, char keySeparator) {
		this.entrySeparator = entrySeparator;
		this.keySeparator = keySeparator;
		return this;
	}

	@Override
	public Map<String, V> parse(String value) throws ArgParseException {
		return Arrays.stream(value.split("\\s*" + entrySeparator + "\\s*"))
			.map((s) -> s.split("\\s*" + keySeparator + "\\s*"))
			.reduce(new HashMap<>(), (r,e) -> {r.put(e[0], parseValue(e[1])); return r;}, (a, b) -> {a.putAll(b); return a;});
	}

	@Override
	public final boolean requireValue() {
		return true;
	}
	
	@Override
	public Argument<Map<String, V>> choices(String... choices) {
		throw new RuntimeException(getClass().getName() + " cannot have multiple choices");
	}
	
	/**
	 * Parse the value to store in the map
	 * @param value The string representation of the value
	 * @return The parsed value
	 */
	protected abstract V parseValue(String value);
}
