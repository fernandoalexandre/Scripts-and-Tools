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
 * Interface of an Action.
 * 
 * @author Fernando Alexandre
 */
public interface IAction {

	/**
	 * Gets the counts
	 * 
	 * @return Counter value.
	 */
	public int getCounter();

	/**
	 * Gets the original file name.
	 * 
	 * @return String with original file name.
	 */
	public String getOriginalFile();

	/**
	 * Gets the modified file name.
	 * 
	 * @return String with modified file name.
	 */
	public String getModifiedFile();

	/**
	 * Gets the label object.
	 * 
	 * @return FileLabel object.
	 */
	public FileLabel getLabel();

	/**
	 * Debug string representing the Action.
	 * 
	 * @return Debug string.
	 */
	public String toString();

	/**
	 * Applies the action.
	 * 
	 * @return True if the action was applied, false otherwise.
	 */
	boolean apply();
}
