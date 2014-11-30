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
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

import phsym.argparse.exceptions.ArgParseException;
import phsym.argparse.exceptions.InvalidValueException;
import phsym.argparse.exceptions.ValueRequiredException;

public abstract class Argument<E> implements IHelpString {

	private String name;
	private String description;
	private Consumer<E> action;
	private boolean processed = false;
	private boolean required = false;
	private E defaultValue;
	private List<String> choices;
	
	public Argument() {
		
	}
	
	public boolean hasBeenProcessed() {
		return processed;
	}
	
	public boolean hasNotBeenProcessed() {
		return !processed;
	}
	
	public String getName() {
		return name;
	}
	
	public Argument<E> name(String shortName) {
		this.name = shortName;
		return this;
	}
	
	public String getHelp() {
		return description;
	}
	
	public Argument<E> help(String description) {
		this.description = description;
		return this;
	}
	
	public Argument<E> choices(String ... choices) {
		if(!this.requireValue())
			throw new RuntimeException("Argument " + this.name + " can't have choices since no value is required");
		this.choices = Arrays.asList(choices);
		return this;
	}
	
	public E getDefault() {
		return defaultValue;
	}
	
	public Argument<E> setDefault(E defaultValue) {
		this.defaultValue = defaultValue;
		return this;
	}
	
	public boolean hasDefault() {
		return defaultValue != null;
	}
	
	public Argument<E> required(boolean required) {
		this.required = required;
		return this;
	}
	
	public boolean isRequired() {
		return required;
	}
	
	public Argument<E> consume(Consumer<E> action) {
		if(this.action == null)
			this.action = action;
		else
			this.action = this.action.andThen(action);
		return this;
	}
	
	public Argument<E> action(Runnable action) {
		consume((x) -> action.run());
		return this;
	}

	@Override
	public String helpStr() {
		StringBuilder help = new StringBuilder("    ");
		help.append(name);
		help.append("\t");
		if(requireValue()) {
			if(choices == null || choices.size() == 0)
				help.append("<").append(typeDesc()).append(">");
			else {
				help.append("<");
				Iterator<String> it = choices.iterator();
				while(it.hasNext()) {
					help.append(it.next());
					if(it.hasNext())
						help.append(" | ");
				}
				help.append(">");
			}
		} else
			help.append("\t");
		help.append("\t").append(description);
		if(hasDefault())
			help.append(" (default: ").append(defaultValue).append(")");
		return help.toString();
	}

	private E callDirect(E value) {
		if(action != null)
			action.accept(value);
		processed = true;
		return value;
	}
	
	public E call() throws ValueRequiredException {
		return callDirect((E)null);
	}
	
	public E call(String value) throws ArgParseException {
		if(value == null && requireValue())
			throw new ValueRequiredException(name);
		if(choices != null && choices.size() > 0 && !choices.contains(value))
			throw new InvalidValueException(this.name, value, this.choices);
		return callDirect(parse(value));
	}
	
	public E callDefault() {
		return callDirect(defaultValue);
	}
	
	public abstract E parse(String value) throws ArgParseException;
	public abstract boolean requireValue();
	public abstract String typeDesc();
	
}
