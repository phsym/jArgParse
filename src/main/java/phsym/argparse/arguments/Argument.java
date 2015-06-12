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
import java.util.function.Predicate;
import java.util.regex.Pattern;

import phsym.argparse.ArgParse;
import phsym.argparse.exceptions.ArgParseException;
import phsym.argparse.exceptions.InvalidArgumentNameException;
import phsym.argparse.exceptions.InvalidValueException;
import phsym.argparse.exceptions.ValueRequiredException;

/**
 * Represent an argument configuration for {@link ArgParse} class
 * @author phsym
 *
 * @param <E> The type of the argument value resulting from parsing
 */
public abstract class Argument<E> implements IHelpString {
	
	private static Pattern validArgName = Pattern.compile("--?[a-zA-Z0-9]+(-?[a-zA-Z0-9]*)*");

	private String[] names;
	private String description;
	private Consumer<E> action;
	private Predicate<E> validation;
	private boolean processed = false;
	private boolean required = false;
	private E defaultValue;
	private List<String> choices;
	private String destination;
	
	/**
	 * Default empty constructor
	 */
	public Argument() {
		
	}
	
	/**
	 * Check if this argument has already been parsed
	 */
	public boolean hasBeenProcessed() {
		return processed;
	}
	
	/**
	 * Check if this argument has still not been parsed
	 */
	public boolean hasNotBeenProcessed() {
		return !processed;
	}
	
	/**
	 * Check if this arguent has the given name
	 */
	public boolean hasName(String name) {
		if(name == null)
			return false;
		for(String n : names) {
			if(n.equals(name))
				return true;
		}
		return false;
	}
	
	/**
	 * Check if this argument has at least one of the given names
	 * @param names The names to look for
	 */
	public boolean hasOneOfNames(String ... names) {
		for(String n : names) {
			if(hasName(n))
				return true;
		}
		return false;
	}
	
	/**
	 * @return An array of this argument's names
	 */
	public String[] getNames() {
		return names;
	}
	
	/**
	 * Check if the argument name is valid
	 * @param name The name to verify
	 * @throws InvalidArgumentNameException if the argument name is invalid
	 */
	private void validateName(String name) throws InvalidArgumentNameException {
		if(!validArgName.matcher(name).matches())
			throw new InvalidArgumentNameException("Invalid argument name : " + name);
	}
	
	/**
	 * Set the names for this argument (don't forget dashes, eg : "-a" or "--args")
	 * @param firstName The main and mandatory name
	 * @param names Additional names
	 * @return this
	 * @throws InvalidArgumentNameException If the name is not valid
	 */
	public Argument<E> names(String firstName, String ... names) throws InvalidArgumentNameException {
		this.names = new String[names.length + 1];
		validateName(firstName);
		this.names[0] = firstName;
		for(int i = 0; i < names.length; i++) {
			validateName(names[i]);
			this.names[i+1] = names[i];
		}
		return this;
	}
	
	/**
	 * Set destination name for identifying argument in map containing results
	 * @param dest The destination name
	 * @return this
	 */
	public Argument<E> dest(String dest) {
		this.destination = dest;
		return this;
	}
	
	/**
	 * Get the destination name.
	 * @return Either the value set with {@link #dest(String)}, or will return the main argument name with dashes removed
	 */
	public String getDestination() {
		if(destination != null)
			return destination;
		else
			return names[0].replaceFirst("--?", "");
	}
	
	/**
	 * @return The description string set with {@link #help(String)}
	 */
	public String getHelp() {
		return description;
	}
	
	/**
	 * Set the description to be printed in help
	 * @param description The description
	 * @return this
	 */
	public Argument<E> help(String description) {
		this.description = description;
		return this;
	}
	
	/**
	 * Limit possible argument values to multiple choice
	 * @param choices The choices to limit to
	 * @return this
	 */
	public Argument<E> choices(String ... choices) {
		if(!this.requireValue())
			throw new RuntimeException("Argument " + this.names[0] + " can't have choices since no value is required");
		this.choices = Arrays.asList(choices);
		return this;
	}
	
	/**
	 * Get the default argument value
	 * @return The default value set with {@link #setDefault(Object)}
	 */
	public E getDefault() {
		return defaultValue;
	}
	
	/**
	 * Set default value for the argument
	 * @param defaultValue The default value
	 * @return this
	 */
	public Argument<E> setDefault(E defaultValue) {
		//TODO: Check against constraints if there are some
		this.defaultValue = defaultValue;
		return this;
	}
	
	/**
	 * Check either this argument has a default value or not
	 */
	public boolean hasDefault() {
		return defaultValue != null;
	}
	
	/**
	 * Set this argument as required or not
	 * @param required <code>true</code> if required, else <code>false</code>
	 * @return
	 */
	public Argument<E> required(boolean required) {
		this.required = required;
		return this;
	}
	
	/**
	 * Check if this argument is required or not. Set with {@link #required(boolean)}
	 */
	public boolean isRequired() {
		return required;
	}
	
	/**
	 * Add an assertion predicate which will be checked after argument value has been parsed
	 * @param predicate A predicate that checks the parsed value
	 * @return this
	 */
	public Argument<E> andAssert(Predicate<E> predicate) {
		if(validation == null)
			validation = predicate;
		else
			validation = validation.and(predicate);
		return this;
	}
	
	/**
	 * Add a consumer that will get the argument value after it has been parsed and checked
	 * @param action The consumer
	 * @return this
	 */
	public Argument<E> consume(Consumer<E> action) {
		if(this.action == null)
			this.action = action;
		else
			this.action = this.action.andThen(action);
		return this;
	}
	
	/**
	 * Add an action to run when the argument has been parsed
	 * @param action The action to run
	 * @return this
	 */
	public Argument<E> action(Runnable action) {
		consume((x) -> action.run());
		return this;
	}

	@Override
	public String helpStr() {
		StringBuilder help = new StringBuilder("    ");
		for(int i = 0; i < names.length; i++) {
			help.append(names[i]);
			if(i < names.length-1)
				help.append(", ");
		}
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

	/**
	 * Check assertions, then call consumers and actions, passing the given value
	 * in arguments
	 * @param value The value to call functions on
	 * @return The resulting value
	 * @throws InvalidValueException If the value does not pass checks
	 */
	private E callDirect(E value) throws InvalidValueException {
		if(value != null && validation != null && !validation.test(value))
			throw new InvalidValueException(names[0], value.toString());
		if(action != null)
			action.accept(value);
		processed = true;
		return value;
	}
	
	/**
	 * Process an empty value (for a flag argument)
	 * @return The resulting value
	 * @throws ArgParseException
	 */
	public E process() throws ArgParseException {
			return process(null);
	}
	
	/**
	 * Process a value : parse, check, and call functions
	 * @param value The string value to parse
	 * @return The parsed value
	 * @throws ArgParseException In case of parsing failed
	 */
	public E process(String value) throws ArgParseException {
		if(value == null && requireValue())
			throw new ValueRequiredException(this);
		if(choices != null && choices.size() > 0 && !choices.contains(value))
			throw new InvalidValueException(names[0], value, this.choices);
		return callDirect(parse(value));
	}
	
	/**
	 * Process with the default value
	 * @return The resulting default value
	 */
	public E processDefault() {
		try {
			return callDirect(defaultValue);
		} catch (InvalidValueException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Parse the value
	 * @param value The value to parse
	 * @return The parsed value
	 * @throws ArgParseException If parsing failed
	 */
	public abstract E parse(String value) throws ArgParseException;
	
	/**
	 * Check if this argument requires a value, or is a flag
	 */
	public abstract boolean requireValue();
	
	/**
	 * @return The value type description string, used in help messages
	 */
	public abstract String typeDesc();
	
}
