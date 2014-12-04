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
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

import phsym.argparse.arguments.Argument;
import phsym.argparse.arguments.IHelpString;
import phsym.argparse.arguments.Type;
import phsym.argparse.exceptions.ArgParseException;
import phsym.argparse.exceptions.ArgumentConflictException;
import phsym.argparse.exceptions.MissingArgumentException;
import phsym.argparse.exceptions.UnknownArgumentException;
import phsym.argparse.exceptions.ValueRequiredException;

public class ArgParse {
	
	private List<Argument<?>> arguments;
	private List<IHelpString> helpers;
	private String prog;
	private String version;
	private String description;
	private String epilog;
	private Consumer<Exception> exceptionHandler;

	public ArgParse(String prog) {
		this.prog = prog;
		arguments = new LinkedList<>();
		helpers = new LinkedList<>();
	}
	
	private Optional<Argument<?>> findByName(String name) {
		return arguments.stream()
			.filter((a) -> a.hasName(name))
			.findFirst();
	}
	
	private Optional<Argument<?>> findOneByNames(String ... names) {
		return arguments.stream()
			.filter((a) -> a.hasOneOfNames(names))
			.findFirst();
	}
	
	private void processDefault(Map<String, Object> values) {
		arguments.stream()
			.filter(Argument::hasNotBeenProcessed)
			.filter(Argument::hasDefault)
			.forEach((a) -> values.put(a.getDestination(), a.callDefault()));
	}
	
	private void checkRequired() throws MissingArgumentException {
		Optional<Argument<?>> missing = arguments.stream()
			.filter(Argument::hasNotBeenProcessed)
			.filter(Argument::isRequired)
			.findFirst();
		if(missing.isPresent())
			throw new MissingArgumentException(missing.get());
	}

	public <E, T extends Argument<E>> T add(T arg) {
		Objects.requireNonNull(arg, "arg must be non null");
		String[] names = arg.getNames();
		Objects.requireNonNull(names, "Argument needs at least 1 name");

		if(findOneByNames(names).isPresent())
			throw new ArgumentConflictException("Argument " + names[0] + " is already registered");
		arguments.add(arg);
		helpers.add(arg);
		return arg;
	}
	
	public <E, T extends Argument<E>> T add(Class<T> type, String name, String ... otherNames) {
		try {
			T arg = type.newInstance();
			arg.names(name, otherNames);
			return add(arg);
		} catch (InstantiationException | IllegalAccessException e) {
			throw new RuntimeException("FATAL : unexpected error", e);
		}
	}
	
	public ArgParse description(String description) {
		this.description = description;
		return this;
	}
	
	public ArgParse version(String version) {
		this.version = version;
		add(Type.BOOL, "-v")
			.help("Print version")
			.action(this::printVersion)
			.action(() -> System.exit(1));
		return this;
	}
	
	public ArgParse epilog(String epilog) {
		this.epilog = epilog;
		return this;
	}
	
	public ArgParse onError(Consumer<Exception> handler) {
		if(exceptionHandler == null)
			exceptionHandler = handler;
		else
			exceptionHandler = exceptionHandler.andThen(handler);
		return this;
	}
	
	public ArgParse addDefaultErrorHandler() {
		onError((e) -> System.err.println(e.getMessage()))
			.onError((e) -> printHelp())
			.onError((e) -> System.exit(1));
		return this;
	}
	
	public ArgParse defaultHelp() {
		add(Type.BOOL, "-h")
			.help("Print this help")
			.action(this::printHelp)
			.action(() -> System.exit(1));
		return this;
	}
	
	public ArgParse label(String label) {
		helpers.add(() -> label);
		return this;
	}
	
	public ArgParse space() {
		return label(" ");
	}
	
	public Map<String, Object> parseTrhow(String[] args) throws ArgParseException {
		return parseThrow(Arrays.asList(args));
	}
	
	public Map<String, Object> parse(String[] args) {
		return parse(Arrays.asList(args));
	}
	
	public Map<String, Object> parseThrow(List<String> args) throws ArgParseException {
		Map<String, Object> values = new HashMap<>();
		Iterator<String> it = args.iterator();
		while (it.hasNext()) {
			Object value = null;
			String n = it.next();
			Argument<?> arg = findByName(n)
					.orElseThrow(() -> new UnknownArgumentException(n));
			if(arg.requireValue()) {
				if (it.hasNext())
					value = arg.call(it.next());
				else
					throw new ValueRequiredException(arg);
			}
			else
				value = arg.call();
			values.put(arg.getDestination(), value);
		}
		processDefault(values);
		checkRequired();
		return values;
	}
	
	public Map<String, Object> parse(List<String> args) {
		try {
			return parseThrow(args);
		} catch(ArgParseException e) {
			if(exceptionHandler != null)
				exceptionHandler.accept(e);
			else
				e.printStackTrace();
		}
		return new HashMap<>();
	}

	public void printVersion() {
		System.out.print(prog);
		if(version != null)
			System.out.print(" " + version);
		System.out.println();
	}
	
	public String help() {
		ByteArrayOutputStream str = new ByteArrayOutputStream();
		PrintStream out = new PrintStream(str);
		
		out.print("Usage: " + prog + " ");
		arguments.stream()
			.filter((x) -> x.isRequired())
			.forEach((x) -> {
				out.print(x.getNames()[0] + " ");
				if(x.requireValue())
					out.print(x.typeDesc() + " ");
			});
		out.println("<options>");
		
		
		if(description != null)
			out.println(description);
		helpers.stream()
			.map(IHelpString::helpStr)
			.forEach(out::println);
		if(epilog != null && epilog.length() > 0)
			out.println(epilog);
		out.flush();
		return str.toString();
	}
	
	public void printHelp() {
		System.out.print(help());
	}
}
