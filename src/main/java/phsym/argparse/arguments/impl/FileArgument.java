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

import java.io.File;
import java.io.IOException;

import phsym.argparse.arguments.Argument;
import phsym.argparse.exceptions.ArgParseException;
import phsym.argparse.exceptions.InvalidValueException;

/**
 * Represent a file argument
 * @author phsym
 *
 */
public class FileArgument extends Argument<File> {
	
	private boolean exists = false;
	private boolean directory = false;
	private boolean create = false;
	private boolean writable = false;
	private boolean readable = false;
	private boolean executable = false;
	
	/**
	 * Default constructor
	 */
	public FileArgument(){
		super();
	}
	
	/**
	 * Add a constraint on file existency
	 * @param value If <code>true</code>, file must exist
	 * @return this
	 */
	public FileArgument exists(boolean value) {
		this.exists = value;
		return this;
	}
	
	/**
	 * Add a contraint that provided path must be a directory
	 * @param value If <code>true</code>, file must be a directory
	 * @return this
	 */
	public FileArgument directory(boolean value) {
		this.directory = value;
		return this;
	}
	
	/**
	 * Create the file, with given rights
	 * @param executable Execution right
	 * @param readable Read right
	 * @param writable write right
	 * @return this
	 */
	public FileArgument create(boolean executable, boolean readable, boolean writable) {
		this.create = true;
		this.executable = executable;
		this.readable = readable;
		this.writable = writable;
		return this;
	}

	@Override
	public File parse(String value) throws ArgParseException {
		File file = new File(value);
		if(!file.exists() && create) {
			if(directory) {
				if(!file.mkdirs())
					throw new ArgParseException("Could not create dirs " + file.getAbsolutePath());
			} else
				try {
					if(file.getParentFile()!= null && !file.getParentFile().mkdirs())
						throw new ArgParseException("Could not create parent dirs " + file.getAbsolutePath());
					System.out.println("create new file");
					file.createNewFile();
					file.setExecutable(executable);
					file.setReadable(readable);
					file.setWritable(writable);
				} catch (IOException e) {
					throw new ArgParseException("Could not create file " + file.getAbsolutePath(), e);
				}
		}
		if(exists && !file.exists())
			throw new InvalidValueException(this.getNames()[0], file.getAbsolutePath(), " must exists");
		if(directory && file.exists() && !file.isDirectory())
			throw new InvalidValueException(this.getNames()[0], file.getAbsolutePath(), " must be a directory");
		else if(!directory && file.exists() && !file.isFile())
			throw new InvalidValueException(this.getNames()[0], file.getAbsolutePath(), " must be a file");
		
		return file;
	}

	@Override
	public boolean requireValue() {
		return true;
	}

	@Override
	public String typeDesc() {
		if(directory)
			return "dir";
		return "file";
	}
}
