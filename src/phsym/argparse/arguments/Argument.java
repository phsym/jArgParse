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
import java.util.function.Consumer;

import phsym.argparse.exceptions.ValueRequiredException;

public abstract class Argument<E> {

	private String shortName;
	private String description;
	private Consumer<E> action;
	private boolean processed = false;
	private boolean required = false;
	
	public Argument() {
		
	}
	
	public boolean hasBeenProcessed() {
		return processed;
	}
	
	public String getShortName() {
		return shortName;
	}
	
	public Argument<E> setShortName(String shortName) {
		this.shortName = shortName;
		return this;
	}
	
	public String getDescription() {
		return description;
	}
	
	public Argument<E> setDescription(String description) {
		this.description = description;
		return this;
	}
	
	public Argument<E> setRequired(boolean required) {
		this.required = required;
		return this;
	}
	
	public boolean isRequired() {
		return required;
	}
	
	public Argument<E> setAction(Consumer<E> action) {
		this.action = action;
		return this;
	}

	public String helpStr() {
		StringBuilder help = new StringBuilder(" ");
		help.append(shortName);
		if(requireValue())
			help.append(" <").append(typeDesc()).append(">");
		else
			help.append("\t");
		help.append("\t").append(description);
		return help.toString();
	}

	public E call() throws ValueRequiredException {
		return call(null);
	}
	
	public E call(String value) throws ValueRequiredException {
		if(value == null && requireValue())
			throw new ValueRequiredException(shortName);
		E parsedVal = parse(value);
		if(action != null)
			action.accept(parsedVal);
		processed = true;
		return parsedVal;
	}
	
	public abstract E parse(String value);
	public abstract boolean requireValue();
	public abstract String typeDesc();
	
}
