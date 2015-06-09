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
package phsym.argparse.exceptions;

import java.util.List;

/**
 * An exception thrown when an invalid argument value is parsed
 * @author pierresy
 *
 */
public class InvalidValueException extends ArgParseException {

	private static final long serialVersionUID = -2472708344900858077L;

	/**
	 * Constructor
	 * @param argName Argument name
	 * @param value Invalid value
	 * @param e
	 */
	public InvalidValueException(String argName, String value, Throwable e) {
		super("Invalid argument value for " + argName + " : " + value, e);
	}
	
	/**
	 * Constructor
	 * @param argName argument name
	 * @param value Invalid value
	 * @param choices Possible values when applicable
	 * @param e
	 */
	public InvalidValueException(String argName, String value, List<String> choices, Throwable e) {
		super("Invalid argument value for " + argName + " : " + value + ". Possible choices are " + choices.toString(), e);
	}
	
	/**
	 * Constructor
	 * @param argName Argument name
	 * @param value Invalid value
	 */
	public InvalidValueException(String argName, String value) {
		super("Invalid argument value for " + argName + " : " + value, null);
	}
	
	/**
	 * Constructor
	 * @param argName argument name
	 * @param value Invalid value
	 * @param choices Possible values when applicable
	 */
	public InvalidValueException(String argName, String value, List<String> choices) {
		super("Invalid argument value for " + argName + " : " + value + ". Possible choices are " + choices.toString(), null);
	}
	
	/**
	 * Constructor
	 * @param argName argument name
	 * @param value Invalid value
	 * @param msg Additional message
	 */
	public InvalidValueException(String argName, String value, String msg) {
		super("Invalid argument value for " + argName + " : " + value + " " + msg, null);
	}
}
