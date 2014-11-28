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
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import phsym.argparse.arguments.Argument;
import phsym.argparse.arguments.Type;
import phsym.argparse.exceptions.ArgumentConflictException;
import phsym.argparse.exceptions.UnknownArgumentException;
import phsym.argparse.exceptions.ValueRequiredException;

public class ArgParse {

	private LinkedHashMap<String, Argument<?>> map;
	private String prog;
	private String version;
	private String description;

	public ArgParse(String prog, String version, String description) {
		this.prog = prog;
		this.version = version;
		this.description = description;
		map = new LinkedHashMap<>();
	}

	public <E> void add(Argument<E> arg) {
		String name = arg.getName();
		if(map.containsKey(name))
			throw new ArgumentConflictException("Argument " + name + " is already registered");
		map.put(name, arg);
	}
	
	public void add(Type type, String name, String help) {
		add(type.newInstance(name, help));
	}
	
	public <E> void on(Type type, String name, String help, Consumer<E> action) {
		add(type.newInstance(name, help, action));
	}
	
	public void onInt(String name, String help, Consumer<Integer> action) {
		on(Type.INT, name, help, action);
	}
	
	public void onString(String name, String help, Consumer<String> action) {
		on(Type.STRING, name, help, action);
	}
	
	public void onBool(String name, String help, Consumer<Boolean> action) {
		on(Type.BOOL, name, help, action);
	}
	
	public void onStringArray(String name, String help, Consumer<List<String>> action) {
		on(Type.STRING_ARRAY, name, help, action);
	}
	
	public void onStringMap(String name, String help, Consumer<Map<String, String>> action) {
		on(Type.STRING_MAP, name, help, action);
	}

	public Map<String, Object> parse(List<String> args) throws UnknownArgumentException, ValueRequiredException {
		Map<String, Object> values = new HashMap<>();
		Iterator<String> it = args.iterator();
		while (it.hasNext()) {
			Object value = null;
			String n = it.next();
			Argument<?> arg = map.get(n);
			if (arg == null)
				throw new UnknownArgumentException(n);
			if(arg.requireValue()) {
				if (it.hasNext())
					value = arg.call(it.next());
			}
			else
				value = arg.call();
			values.put(arg.getName(), value);
		}
		return values;
	}

	public void printHelp() {
		System.out.println("Usage for : " + prog + " version " + version);
		System.out.println(description);
		map.values().stream().map(Argument::helpStr).forEach(System.out::println);
	}
}
