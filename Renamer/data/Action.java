/*	Copyright (C) 2009	Fernando Alexandre

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package data;

import gui.FileLabel;

/**
 * Represents an Action.
 * 
 * @author Fernando Alexandre
 */
abstract class Action implements IAction {

	/**
	 * Counter's value.
	 */
	private int counter;

	/**
	 * Modified file name.
	 */
	private String modifiedFile;

	/**
	 * Original file name.
	 */
	private String originalFile;

	/**
	 * Label in file list.
	 */
	private FileLabel label;

	/**
	 * Creates an Action.
	 * 
	 * @param currentCounter
	 *            Counter value when action was created.
	 * @param originalFile
	 *            Original file name.
	 * @param modifiedFile
	 *            Modified file name.
	 * @param label
	 *            Point to the label in the file list.
	 */
	protected Action(int currentCounter, String originalFile,
			String modifiedFile, FileLabel label) {
		this.counter = currentCounter;
		this.modifiedFile = modifiedFile;
		this.originalFile = originalFile;
		this.label = label;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see data.IAction#getCounter()
	 */
	public int getCounter() {
		return this.counter;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see data.IAction#getOriginalFile()
	 */
	public String getOriginalFile() {
		return this.originalFile;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see data.IAction#getModifiedFile()
	 */
	public String getModifiedFile() {
		return this.modifiedFile;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see data.IAction#getLabel()
	 */
	public FileLabel getLabel() {
		return this.label;
	}

	// If this is executed, it is counted as an Error.
	/*
	 * (non-Javadoc)
	 * 
	 * @see data.IAction#apply()
	 */
	public boolean apply() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return String.format("%s :: %s :: %s", this.counter, this.originalFile,
				this.modifiedFile);
	}
}
