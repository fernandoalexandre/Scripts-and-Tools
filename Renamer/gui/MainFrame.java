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

import data.*;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.File;
import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

public class MainFrame extends JFrame {

	protected static final int MAIN_WIDTH = 350;

	protected static final int MAIN_HEIGHT = 550;

	private static final long serialVersionUID = 1L;

	private static final int PROJECTFIELD_WIDTH = 10;

	private static final int COUNTERFIELD_WIDTH = 2;

	private static final String DEFAULT_PROJECT_NAME = "project";
	
	protected static ActionStack actions; 

	public static String WORKING_DIR;

	private static String projectName;

	private static JPanel northPanel;

	private static JPanel centerPanel;

	private static JPanel northMainPanel;

	private static JPanel filePanel;

	private static ImagePanel previewPanel;

	private static StatusBar statusBar;

	private static JTextField projectNameField;

	private static JTextField counterField;

	protected static int counter;

	protected final JFrame MainWindow = this;

	protected static boolean previewOpen;
	
	/*Presentations Messages*/
	private static String ERR_NAN(String number)
	{	return String.format("%s is not a number!", number);	}
	
	private static final String ERR_NOPIC = "No images in folder!";
	
	public static final String ERR_NOACTION = "No action to undo/redo.";

	public static final String ERR_RENAME = "Error renaming. Did not undo/redo.";

	public static String ERR_EXISTS (String name) 
	{	return String.format("%s already exists! Please Rename.", name);	}
	
	public static String SUCCESS_MSG(String orig, String mod)
	{	return String.format("%s -> %s", orig, mod);	}
	/*!Presentations Messages*/
	
	public MainFrame()	
	{
		actions = new ActionStack();
		Menu menu = new Menu();
		File workDirectory = getWorkDir();
		WORKING_DIR = workDirectory.getAbsolutePath();
		counter = 0;
		previewOpen = false;
		
		setPreferredSize(new Dimension(MAIN_WIDTH, MAIN_HEIGHT));
		setLayout( new BorderLayout() );
		setJMenuBar( menu.createMenuBar() );
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.setTitle("Rename - " + WORKING_DIR);
		northPanel = new JPanel();
		northPanel.setLayout( new GridLayout(1,2) );
		add(northPanel, BorderLayout.NORTH);

		centerPanel = new JPanel();
		add(centerPanel, BorderLayout.CENTER);

		createProjectNameField();
		createCounterField();
		createFileList( workDirectory.list() );
		createStatusBar();
		this.pack();
	}

	private void createProjectNameField()
	{
		northMainPanel = new JPanel();
		projectNameField = new JTextField(PROJECTFIELD_WIDTH);
		northMainPanel.setPreferredSize( new Dimension(MAIN_WIDTH, 35) );
		northMainPanel.setMaximumSize( new Dimension(MAIN_WIDTH, 35) );
		northMainPanel.setMinimumSize( new Dimension(MAIN_WIDTH, 35) );
		northPanel.add(northMainPanel);

		class projectNameListener implements KeyListener
		{

			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER)
				{
					projectName = projectNameField.getText();
					setStatusMessage("Project name changed to: " + projectName);
				}
			}

			public void keyReleased(KeyEvent e) {}

			public void keyTyped(KeyEvent e) {}
		}
		KeyListener listener = new projectNameListener();
		projectNameField.addKeyListener(listener);

		projectNameField.setText(DEFAULT_PROJECT_NAME);
		projectName = DEFAULT_PROJECT_NAME;
		northMainPanel.add(new JLabel("Project name: "), BorderLayout.WEST);
		northMainPanel.add(projectNameField, BorderLayout.WEST);
	}
	
	public static void incCounter()
	{
		counter++;
		counterField.setText( String.valueOf(counter) );
	}
	
	public static void decCounter()
	{
		counter--;
		counterField.setText( String.valueOf(counter) );
	}

	private void createCounterField()
	{
		counterField = new JTextField(COUNTERFIELD_WIDTH);

		class counterListener implements KeyListener
		{

			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER)
				{
					try
					{
						counter = Integer.parseInt( counterField.getText() );
						setStatusMessage("Counter changed to: " + counter);
					} catch(NumberFormatException ex)
					{
						setStatusMessage( ERR_NAN( counterField.getText() ) );
					}
				}
			}

			public void keyReleased(KeyEvent e) {}

			public void keyTyped(KeyEvent e) {}
		}
		KeyListener listener = new counterListener();
		counterField.addKeyListener(listener);

		counterField.setText( String.valueOf(counter) );
		northMainPanel.add(counterField, BorderLayout.CENTER);
	}

	private void createStatusBar()
	{
		statusBar = new StatusBar();
		add(statusBar, BorderLayout.SOUTH);
	}

	private void createFileList(String[] fileList)
	{
		filePanel = new JPanel();
		filePanel.setLayout(new BoxLayout(filePanel, BoxLayout.Y_AXIS));
		JScrollPane files = new JScrollPane(filePanel);
		boolean added = false; // If there was any image in the folder
		for(String e : fileList)
		{
			FileLabel current = new FileLabel(e);
			if (current.isImage())
			{
				current.setAlignmentX(Component.CENTER_ALIGNMENT);

				MouseListener listener = new currentFileListener();
				current.addMouseListener(listener);

				filePanel.add(current);
				added = true;
			}
		}
		if(!added)
			filePanel.add(new JLabel(ERR_NOPIC));
		add(files, BorderLayout.CENTER);
	}

	public static String getFileName(String extension)
	{
		if(counter < 10)
		{
			return String.format("%s_000%d.%s", projectName, counter, extension); 
		}
		else if(counter < 100)
		{
			return String.format("%s_00%d.%s", projectName, counter, extension); 
		}
		else if(counter < 1000)
		{
			return String.format("%s_0%d.%s", projectName, counter, extension); 
		}
		return String.format("%s_%d.%s", projectName, counter, extension);
	}
	
	public static void setStatusMessage(String message)
	{
		statusBar.setMessage(message);
	}

	protected File getWorkDir()
	{
		JFileChooser loadFile = new JFileChooser();
		loadFile.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		int result = loadFile.showOpenDialog(this);

		if (result == JFileChooser.APPROVE_OPTION)
		{
			File file = loadFile.getSelectedFile();
			return file;
		}
		else if(result == JFileChooser.CANCEL_OPTION)
			this.dispose();
		System.exit(0);
		return null;
	}

	public static char getSystemPathSymbol()
	{
		String osName = System.getProperty("os.name");
		return osName.toUpperCase().indexOf("WINDOWS") == 0 ? '\\' : '/';
	}

	class currentFileListener implements MouseListener
	{
		public void mouseClicked(MouseEvent e) {}

		public void mouseEntered(MouseEvent e) {}

		public void mouseExited(MouseEvent e) {}

		public void mousePressed(MouseEvent e) 
		{
			if( (e.getButton() == MouseEvent.BUTTON1) && (e.getClickCount() == 2) )
			{
				FileLabel current = (FileLabel) e.getComponent();
				String newFileName = getFileName( current.getExtension() );

				String filePath = WORKING_DIR + getSystemPathSymbol();
				// File with old name
				File file = new File(filePath + current.getFileName());
				// File with new name
				File file2 = new File(filePath + newFileName);

				// Rename file
				if(!file2.exists())
				{
					boolean success = file.renameTo(file2);
					if (!success) {
						setStatusMessage(ERR_RENAME);
					}
					else {
						setStatusMessage( SUCCESS_MSG(current.getFileName(), newFileName) );
						actions.saveAction(
								new UndoAction(counter, current.getFileName(), newFileName, current));
						current.setFileName(newFileName);
						counter++;
						counterField.setText( String.valueOf(counter) );
					}
				} else {
					setStatusMessage( ERR_EXISTS(newFileName) );
				}
			}
			if(e.getButton() == MouseEvent.BUTTON3)
			{
				FileLabel current = (FileLabel) e.getComponent();
				if(!previewOpen)
				{
					previewWindow(current);
					previewOpen = true;
				}
				else
				{
					updatePreviewWindow(current);
				}
			}
		}
		
		private void updatePreviewWindow(FileLabel current)
		{
			previewPanel.setImage(new ImageIcon( current.getImagePath() ));
			MainWindow.setPreferredSize(new Dimension(
					MAIN_WIDTH + previewPanel.getWidth(), MAIN_HEIGHT));
			MainWindow.pack();
		}
		
		private void previewWindow(FileLabel current)
		{
			previewPanel = new ImagePanel(current.getImagePath());

			class previewListener implements MouseListener
			{
				public void mouseClicked(MouseEvent e) {}

				public void mouseEntered(MouseEvent e) {}

				public void mouseExited(MouseEvent e) {}

				public void mousePressed(MouseEvent e) 
					{ normalWindow(); }

				public void mouseReleased(MouseEvent e) {}

			}
			MouseListener listener = new previewListener();
			previewPanel.addMouseListener(listener);

			MainWindow.setPreferredSize(new Dimension(
					MAIN_WIDTH + previewPanel.getWidth(), MAIN_HEIGHT));
			MainWindow.add(previewPanel, BorderLayout.EAST);
			MainWindow.pack();
		}
		
		private void normalWindow()
		{
			MainWindow.setPreferredSize(new Dimension(MAIN_WIDTH, MAIN_HEIGHT));
			MainWindow.remove(previewPanel);
			MainWindow.validate();
			previewOpen = false;
			MainWindow.pack();
		}

		public void mouseReleased(MouseEvent e) {}
	}
}
