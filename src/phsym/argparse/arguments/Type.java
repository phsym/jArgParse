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

import java.lang.reflect.InvocationTargetException;
import java.util.function.Consumer;

import phsym.argparse.arguments.impl.BoolArgument;
import phsym.argparse.arguments.impl.IntArgument;
import phsym.argparse.arguments.impl.StringArgument;
import phsym.argparse.arguments.impl.StringArrayArgument;
import phsym.argparse.arguments.impl.StringMapArgument;

public enum Type {
	BOOL(BoolArgument.class),
	INT(IntArgument.class),
	STRING(StringArgument.class),
	STRING_ARRAY(StringArrayArgument.class),
	STRING_MAP(StringMapArgument.class);
	
	private Class<? extends Argument<?>> clazz;
	
	Type(Class<? extends Argument<?>> clazz) {
		this.clazz = clazz;
	}
	
	
	public Argument<?> newInstance(String name, String help, Consumer<?> action) {
		try {
			return clazz.getConstructor(String.class, String.class, Consumer.class).newInstance(name, help, action);
		} catch (InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			throw new RuntimeException("FATAL unexpected error", e);
		}
	}
	
	public Argument<?> newInstance(String name, String help) {
//		return this.newInstance(name, help, null);
		try {
			return clazz.getConstructor(String.class, String.class).newInstance(name, help);
		} catch (InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			throw new RuntimeException("FATAL unexpected error", e);
		}
	}
}
