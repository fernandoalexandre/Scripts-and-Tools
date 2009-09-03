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
package gui;

import java.awt.Dimension;

import javax.swing.JLabel;

/**
 * Represents a StatusBar which can show one message.
 * 
 * @author Fernando Alexandre
 */
public class StatusBar extends JLabel {

	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new instance of StatusBar
	 */
	public StatusBar() {
		super();
		super.setPreferredSize(new Dimension(100, 16));
		setMessage("Ready");
	}

	/**
	 * Sets a message to self.
	 * 
	 * @param message
	 *            String message to be shown.
	 */
	public void setMessage(String message) {
		super.setText(String.format(" %s", message));
	}
}