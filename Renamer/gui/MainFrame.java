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

/**
 * Represents the Main windows of Renamer.
 * 
 * @author Fernando Alexandre
 */
public class MainFrame extends JFrame {

	/**
	 * Width of main window.
	 */
	protected static final int MAIN_WIDTH = 400;

	/**
	 * Height of main window.
	 */
	protected static final int MAIN_HEIGHT = 550;

	private static final long serialVersionUID = 1L;

	/**
	 * Project name field width.
	 */
	private static final int PROJECTFIELD_WIDTH = 10;

	/**
	 * Counter field width.
	 */
	private static final int COUNTERFIELD_WIDTH = 2;

	/**
	 * Default project name.
	 */
	private static final String DEFAULT_PROJECT_NAME = "project";

	/**
	 * Object with needed information for redo and undo of actions.
	 */
	protected static ActionStack actions;

	/**
	 * The directory currently being organized.
	 */
	public static String WORKING_DIR;

	/**
	 * Current project name.
	 */
	static String projectName;

	/**
	 * North panel of the main window.
	 */
	private static JPanel mainPanel;

	/**
	 * North panel of northPanel.
	 */
	private static JPanel northMainPanel;

	/**
	 * Main panel of northPanel.
	 */
	private static JPanel filePanel;

	/**
	 * Panel which shows selected thumbnail images.
	 */
	static ImagePanel previewPanel;

	/**
	 * Status bar object.
	 */
	private static StatusBar statusBar;

	/**
	 * Project name text field.
	 */
	static JTextField projectNameField;

	/**
	 * Counter text field.
	 */
	static JTextField counterField;

	/**
	 * Current counter value.
	 */
	protected static int counter;

	/**
	 * Tells if preview panel is showing or not.
	 */
	protected static boolean previewOpen;

	/* Presentations Messages */
	static String ERR_NAN(String number) {
		return String.format("%s is not a number!", number);
	}

	private static final String ERR_NOPIC = "No images in folder!";

	public static final String ERR_NOACTION = "No action to undo/redo.";

	public static final String ERR_RENAME = "Error renaming. Did not undo/redo.";

	public static String ERR_EXISTS(String name) {
		return String.format("%s already exists! Please Rename.", name);
	}

	private static final String ERR_PREVIEW_NOT_SUPPORTED = "Preview for bmp is not supported.";

	static String PREVIEWING(String filename) {
		return String.format("Previewing %s...", filename);
	}

	public static String SUCCESS_MSG(String orig, String mod) {
		return String.format("%s -> %s", orig, mod);
	}

	/* !Presentations Messages */

	/**
	 * Creates and shows the main window.
	 */
	public MainFrame() {
		actions = new ActionStack();
		Menu menu = new Menu();
		File workDirectory = getWorkDir();
		WORKING_DIR = workDirectory.getAbsolutePath();
		counter = 0;
		previewOpen = false;

		setPreferredSize(new Dimension(MAIN_WIDTH, MAIN_HEIGHT));
		setLayout(new BorderLayout());
		setJMenuBar(menu);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.setTitle("Rename - " + WORKING_DIR);
		mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		add(mainPanel, BorderLayout.NORTH);

		northMainPanel = new JPanel();
		northMainPanel.setPreferredSize(new Dimension(MAIN_WIDTH, 35));
		mainPanel.add(northMainPanel, BorderLayout.WEST);

		createProjectNameField();
		createCounterField();
		createFileList(workDirectory.list());
		createStatusBar();
		this.pack();
	}

	/**
	 * Creates projectNameField and adds it to mainPanel.
	 */
	private void createProjectNameField() {
		projectNameField = new JTextField(PROJECTFIELD_WIDTH);

		class projectNameListener implements KeyListener {

			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					projectName = projectNameField.getText();
					setStatusMessage("Project name changed to: " + projectName);
				}
			}

			public void keyReleased(KeyEvent e) { // Does Nothing
			}

			public void keyTyped(KeyEvent e) { // Does Nothing
			}
		}
		KeyListener listener = new projectNameListener();
		projectNameField.addKeyListener(listener);

		projectNameField.setText(DEFAULT_PROJECT_NAME);
		projectName = DEFAULT_PROJECT_NAME;
		northMainPanel.add(new JLabel("Project name: "), BorderLayout.NORTH);
		northMainPanel.add(projectNameField, BorderLayout.NORTH);
	}

	/**
	 * Increments counter and updates the field's value.
	 */
	public static void incCounter() {
		counter++;
		counterField.setText(String.valueOf(counter));
	}

	/**
	 * Decrements counter and updates the field's value.
	 */
	public static void decCounter() {
		counter--;
		counterField.setText(String.valueOf(counter));
	}

	/**
	 * Creates counterField and adds it to northMainPanel.
	 */
	private void createCounterField() {
		counterField = new JTextField(COUNTERFIELD_WIDTH);

		class counterListener implements KeyListener {

			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					try {
						counter = Integer.parseInt(counterField.getText());
						setStatusMessage("Counter changed to: " + counter);
					} catch (NumberFormatException ex) {
						setStatusMessage(ERR_NAN(counterField.getText()));
					}
				}
			}

			public void keyReleased(KeyEvent e) { // Does Nothing
			}

			public void keyTyped(KeyEvent e) { // Does Nothing
			}
		}
		KeyListener listener = new counterListener();
		counterField.addKeyListener(listener);

		counterField.setText(String.valueOf(counter));
		northMainPanel.add(counterField, BorderLayout.CENTER);
	}

	/**
	 * Creates and adds statusBar to south panel of self.
	 */
	private void createStatusBar() {
		statusBar = new StatusBar();
		add(statusBar, BorderLayout.SOUTH);
	}

	/**
	 * Creates, populates the file list and add's to center panel of self.
	 * 
	 * @param fileList
	 *            A String array with all the file names in WORKING_DIR.
	 */
	private void createFileList(String[] fileList) {
		filePanel = new JPanel();
		filePanel.setLayout(new BoxLayout(filePanel, BoxLayout.Y_AXIS));
		JScrollPane files = new JScrollPane(filePanel);
		boolean added = false; // If there was any image in the folder
		for (String e : fileList) {
			FileLabel current = new FileLabel(e);
			if (current.isImage()) {
				current.setAlignmentX(Component.CENTER_ALIGNMENT);

				MouseListener listener = new currentFileListener();
				current.addMouseListener(listener);

				filePanel.add(current);
				added = true;
			}
		}
		if (!added)
			filePanel.add(new JLabel(ERR_NOPIC));
		add(files, BorderLayout.CENTER);
	}

	/**
	 * Gets the desired file name according to the counter and projectName.
	 * 
	 * @param extension
	 *            Extension of the file.
	 * @return The desired file name.
	 */
	public static String getFileName(String extension) {
		if (counter < 10) {
			return String
			.format("%s_000%d.%s", projectName, counter, extension);
		} else if (counter < 100) {
			return String.format("%s_00%d.%s", projectName, counter, extension);
		} else if (counter < 1000) {
			return String.format("%s_0%d.%s", projectName, counter, extension);
		}
		return String.format("%s_%d.%s", projectName, counter, extension);
	}

	/**
	 * Sets a message in statusBar
	 * 
	 * @param message
	 *            Message to be shown.
	 */
	public static void setStatusMessage(String message) {
		statusBar.setMessage(message);
	}

	/**
	 * Gets the work directory from a file chooser.
	 * 
	 * @return The work directory path.
	 */
	protected File getWorkDir() {
		JFileChooser loadFile = new JFileChooser();
		loadFile.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		int result = loadFile.showOpenDialog(this);

		if (result == JFileChooser.APPROVE_OPTION) {
			File file = loadFile.getSelectedFile();
			return file;
		} else if (result == JFileChooser.CANCEL_OPTION)
			this.dispose();
		System.exit(0);
		return null;
	}

	/**
	 * Determines the system path symbol.
	 * 
	 * @return The system path symbol.
	 */
	public static char getSystemPathSymbol() {
		String osName = System.getProperty("os.name");
		return osName.toUpperCase().indexOf("WINDOWS") == 0 ? '\\' : '/';
	}

	class currentFileListener implements MouseListener {
		public void mouseClicked(MouseEvent e) { // Does Nothing
		}

		public void mouseEntered(MouseEvent e) { // Does Nothing
		}

		public void mouseExited(MouseEvent e) { // Does Nothing
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
		 */
		public void mousePressed(MouseEvent e) {
			if ((e.getButton() == MouseEvent.BUTTON1)
					&& (e.getClickCount() == 2)) {
				FileLabel current = (FileLabel) e.getComponent();
				String newFileName = getFileName(current.getExtension());

				String filePath = WORKING_DIR + getSystemPathSymbol();
				// File with old name
				File file = new File(filePath + current.getFileName());
				// File with new name
				File file2 = new File(filePath + newFileName);

				// Rename file
				if (!file2.exists()) {
					boolean success = file.renameTo(file2);
					if (!success)
						setStatusMessage(ERR_RENAME);
					else {
						setStatusMessage(SUCCESS_MSG(current.getFileName(),
								newFileName));
						actions.saveAction(new UndoAction(counter, current
								.getFileName(), newFileName, current));
						current.setFileName(newFileName);
						counter++;
						counterField.setText(String.valueOf(counter));
					}
				} else
					setStatusMessage(ERR_EXISTS(newFileName));
			}
			if (e.getButton() == MouseEvent.BUTTON3) {
				FileLabel current = (FileLabel) e.getComponent();
				if (!current.getExtension().toLowerCase().equals("bmp")) {
					setStatusMessage(PREVIEWING(current.getFileName()));
					if (!previewOpen) {
						previewWindow(current);
						previewOpen = true;
					} else
						updatePreviewWindow(current);
				} else {
					setStatusMessage(ERR_PREVIEW_NOT_SUPPORTED);
					if (previewOpen)
						normalWindow();
				}
			}
		}

		/**
		 * Updated the preview window.
		 * 
		 * @param current
		 *            The image to be shown.
		 */
		private void updatePreviewWindow(FileLabel current) {
			previewPanel.setImage(new ImageIcon(current.getImagePath()));
			MainFrame.this.setPreferredSize(new Dimension(MAIN_WIDTH
					+ previewPanel.getWidth(), MAIN_HEIGHT));
			MainFrame.this.pack();
		}

		/**
		 * Turns the preview panel on.
		 * 
		 * @param current
		 *            Image to be shown.
		 */
		private void previewWindow(FileLabel current) {
			previewPanel = new ImagePanel(current.getImagePath());

			class previewListener implements MouseListener {
				public void mouseClicked(MouseEvent e) { // Does Nothing
				}

				public void mouseEntered(MouseEvent e) { // Does Nothing
				}

				public void mouseExited(MouseEvent e) { // Does Nothing
				}

				public void mousePressed(MouseEvent e) {
					normalWindow();
				}

				public void mouseReleased(MouseEvent e) { // Does Nothing
				}

			}
			MouseListener listener = new previewListener();
			previewPanel.addMouseListener(listener);

			MainFrame.this.setPreferredSize(new Dimension(MAIN_WIDTH
					+ previewPanel.getWidth(), MAIN_HEIGHT));
			MainFrame.this.add(previewPanel, BorderLayout.EAST);
			MainFrame.this.pack();
		}

		/**
		 * Closes the preview panel.
		 */
		void normalWindow() {
			MainFrame.this.setPreferredSize(new Dimension(MAIN_WIDTH,
					MAIN_HEIGHT));
			MainFrame.this.remove(previewPanel);
			MainFrame.this.validate();
			previewOpen = false;
			MainFrame.this.pack();
		}

		public void mouseReleased(MouseEvent e) { // Does Nothing
		}
	}
}
