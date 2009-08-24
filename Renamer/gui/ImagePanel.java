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
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 * Represents an image panel.
 * 
 * @author Fernando Alexandre
 */
public class ImagePanel extends JPanel {

	private static final long serialVersionUID = 1L;

	/**
	 * Maximum width of an image.
	 */
	protected static final int MAX_WIDTH = 550;

	/**
	 * Image that is shown.
	 */
	private ImageIcon image;

	/**
	 * Thumb of the image.
	 */
	private ImageIcon thumb;

	/**
	 * Size of the panel
	 */
	private Dimension size;

	/**
	 * Creates an Image panel.
	 * 
	 * @param img
	 *            Path to the image to be shown.
	 */
	public ImagePanel(String img) {
		this(new ImageIcon(img));
	}

	/**
	 * Creates an Image Panel
	 * 
	 * @param img
	 *            ImageIcon object to be shown.
	 */
	public ImagePanel(ImageIcon img) {
		setImage(img);
	}

	/**
	 * Sets the image to be shown.
	 * 
	 * @param img
	 *            Image to be shown.
	 */
	public void setImage(ImageIcon img) {
		this.image = img;
		size = getThumbSize(image.getImage());
		setPreferredSize(size);
		setMinimumSize(size);
		setMaximumSize(size);
		setSize(size);
		setLayout(null);
		this.validate();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	public void paintComponent(Graphics g) {
		int x = 0, y = 0;

		Image img = thumb.getImage();
		if (img.getHeight(null) < MainFrame.MAIN_HEIGHT) {
			y = (int) (MainFrame.MAIN_HEIGHT - (img.getHeight(null) * 1.2)) / 2;
			size = new Dimension(size.width, size.height + img.getHeight(null)
					/ 2);
			setPreferredSize(size);
			setMinimumSize(size);
			setMaximumSize(size);
			setSize(size);
			setLayout(null);
			this.validate();
		}
		g.drawImage(img, x, y, null);
	}

	/**
	 * Determines the thumbnail size.
	 * 
	 * @param img
	 *            Desired image.
	 * @return Dimension with the image.
	 */
	private Dimension getThumbSize(Image img) {
		double ratio;
		double height_ratio = img.getHeight(null)
				/ (double) MainFrame.MAIN_HEIGHT * 1.2;
		double width_ratio = img.getWidth(null) / (double) MAX_WIDTH;

		if (img.getHeight(null) == img.getWidth(null))
			ratio = height_ratio;
		else
			ratio = Math.max(height_ratio, width_ratio);

		int width = (int) (img.getWidth(null) / ratio);
		int height = (int) (img.getHeight(null) / ratio);

		if (img.getHeight(null) == img.getWidth(null))
			getThumbnail(height, width);
		else if (ratio == height_ratio)
			getThumbnail(-1, height);
		else
			getThumbnail(width, -1);

		return new Dimension(width, height);
	}

	/**
	 * Creates a thumbnail.
	 * 
	 * @param width
	 *            Width of the thumbnail.
	 * @param height
	 *            Height of the thumbnail.
	 */
	private void getThumbnail(int width, int height) {
		thumb = new ImageIcon(image.getImage().getScaledInstance(width, height,
				Image.SCALE_SMOOTH));
	}

}
