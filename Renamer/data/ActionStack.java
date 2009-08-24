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

import gui.MainFrame;

import java.util.Stack;

/**
 * Represents the controler of undo/redo stacks.
 * 
 * @author Fernando Alexandre
 */
public class ActionStack {

	/**
	 * Stack with undo actions.
	 */
	private Stack<IAction> undo;

	/**
	 * Stack with redo actions.
	 */
	private Stack<IAction> redo;

	/**
	 * Creates an ActionStack.
	 */
	public ActionStack() {
		this.undo = new Stack<IAction>();
		this.redo = new Stack<IAction>();
	}

	/**
	 * Saves an action to the right stack.
	 * 
	 * @param action
	 *            IAction to be saved.
	 */
	public void saveAction(IAction action) {
		if (action.getClass().equals(RedoAction.class))
			this.redo.add(action);
		else
			this.undo.add(action);
	}

	/**
	 * Undo's an action.
	 */
	public void undo() {
		if (!this.undo.empty()) {
			IAction curr = this.undo.pop();
			if (!curr.apply()) {
				this.undo.push(curr);
				return;
			}
			this.redo.push(new RedoAction(curr));
		} else
			MainFrame.setStatusMessage(MainFrame.ERR_NOACTION);
	}

	/**
	 * Redo's an action.
	 */
	public void redo() {
		if (!this.redo.empty()) {
			IAction curr = this.redo.pop();
			if (!curr.apply()) {
				this.redo.push(curr);
				return;
			}
			this.undo.push(new UndoAction(curr));
		} else
			MainFrame.setStatusMessage(MainFrame.ERR_NOACTION);
	}
}
