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

abstract class Action implements IAction{

	private int counter;
	private String modifiedFile;
	private String originalFile;
	private FileLabel label;

	protected Action(int currentCounter, String originalFile, String modifiedFile, FileLabel label)
	{
		this.counter = currentCounter;
		this.modifiedFile = modifiedFile;
		this.originalFile = originalFile;
		this.label = label;
	}

	public int getCounter()
	{
		return counter;
	}

	public String getOriginalFile()
	{
		return originalFile;
	}

	public String getModifiedFile()
	{
		return modifiedFile;
	}

	public FileLabel getLabel()
	{
		return label;
	}
	
	// If this is executed, it is counted as an Error.
	public boolean apply()
	{
		return false;
	}
	//Debug
	public String toString()
	{
		return String.format("%s :: %s :: %s", counter, originalFile, modifiedFile);
	}
}
