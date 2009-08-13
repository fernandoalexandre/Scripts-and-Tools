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
package interfacing;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class Menu extends JMenuBar {

	private static final long serialVersionUID = 1L;

	public Menu()
	{}

	public JMenuBar createMenuBar()
	{
		JMenuBar menu = new JMenuBar();

		menu.add( createFileMenu() );
		menu.add( createEditMenu() );
		menu.add( createHelpMenu() );
		return menu;
	}
	public JMenu createFileMenu()
	{
		JMenu fileMenu = new JMenu("File");
		//fileMenu.add(createOpenDir());
		fileMenu.add( createExit() );
		return fileMenu;
	}

	public JMenu createEditMenu()
	{
		JMenu editMenu = new JMenu("Edit");
		editMenu.add( createUndo() );
		editMenu.add( createRedo() );
		return editMenu;
	}

	public JMenu createHelpMenu()
	{
		JMenu helpMenu = new JMenu("Help");
		helpMenu.add( createAbout() );
		helpMenu.add( createHowTo() );
		return helpMenu;
	}

	//---------------------File---------------------------

	/*private JMenuItem createOpenDir()
	{
		JMenuItem openDir = new JMenuItem("Open Directory");

		class OpenDirListener implements ActionListener
		{
			public void actionPerformed(ActionEvent e) {
				MainFrame.WORKING_DIR = getWorkDir();
			}	
		}

		ActionListener listener = new OpenDirListener();
		openDir.addActionListener(listener);
		return openDir;
	}*/

	private JMenuItem createExit()
	{
		JMenuItem exit = new JMenuItem("Exit");

		class ExitListener implements ActionListener
		{
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}	
		}
		ActionListener listener = new ExitListener();
		exit.addActionListener(listener);

		return exit;
	}

	//-----------------------Edit------------------------

	private JMenuItem createUndo()
	{
		JMenuItem undo = new JMenuItem("Undo");

		class UndoListener implements ActionListener
		{
			public void actionPerformed(ActionEvent e) {
				MainFrame.actions.undo();
			}	
		}
		ActionListener listener = new UndoListener();
		undo.addActionListener(listener);

		return undo;
	}

	private JMenuItem createRedo()
	{
		JMenuItem redo = new JMenuItem("Redo");

		class RedoListener implements ActionListener
		{
			public void actionPerformed(ActionEvent e) {
				MainFrame.actions.redo();
			}	
		}
		ActionListener listener = new RedoListener();
		redo.addActionListener(listener);

		return redo;
	}
	
	//-----------------------Help------------------------
	
	private JMenuItem createHowTo()
	{
		JMenuItem howTo = new JMenuItem("How to");

		class HowToListener implements ActionListener
		{
			public void actionPerformed(ActionEvent e) {
				createNewHelpFrame();
			}	
		}
		ActionListener listener = new HowToListener();
		howTo.addActionListener(listener);

		return howTo;
	}
	
	private JMenuItem createAbout()
	{
		JMenuItem about = new JMenuItem("About");

		class aboutListener implements ActionListener
		{
			public void actionPerformed(ActionEvent e) {
				createNewAboutFrame();
			}	
		}
		ActionListener listener = new aboutListener();
		about.addActionListener(listener);

		return about;
	}
	
	private void createNewAboutFrame()
	{
		JFrame about = new JFrame();
		about.setPreferredSize( new Dimension(350, 130) );
		about.setLayout( new GridLayout(1,1) );
		about.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		final JEditorPane editor = new JEditorPane();
		about.setLocationRelativeTo(null);
	    editor.setEditable(false);               // we're browsing not editing
	    editor.setContentType("text/html");      // must specify HTML text
	    
	    String message = "<html><head></head><body style=\"background : #DDDDDD\"><center><p><b>About</b></p>" +
		"<p>Renamer v1.0</p>" +
		"<p>Made by Fernando \"kernl\" Alexandre</p></center></body></html>";
	    editor.setText(message);  // specify the text to display
	    
		about.add(editor);
		about.setVisible(true);
		about.validate();
		about.pack();
	}
	
	private void createNewHelpFrame()
	{
		JFrame help = new JFrame();
		help.setPreferredSize( new Dimension(400, 200) );
		help.setLayout( new GridLayout(1,1) );
		help.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		help.setLocationRelativeTo(null);
		
		final JEditorPane editor = new JEditorPane();
	    editor.setEditable(false);               // we're browsing not editing
	    editor.setContentType("text/html");      // must specify HTML text
	    
	    String message = "<html><head></head><body style=\"background : #DDDDDD\"><p><center><b>How-To</b></center></p>" +
		"<p>On startup use the 'Open' menu choose the folder where the pictures to-be-renamed are.</p>" +
		"<p>To preview: Right-click the name of the file to preview.</p>" + 
		"<p>To rename: Double-left-click the name of the file to rename to the format (project_name)_0000.(extension).</p></body></html>";
	    editor.setText(message);  // specify the text to display
		
	    help.add(editor);
		help.setVisible(true);
		help.validate();
		help.pack();
	}
}
