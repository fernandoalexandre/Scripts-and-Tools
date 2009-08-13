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

class ImagePanel extends JPanel {

	private static final long serialVersionUID = 1L;
	protected static final int MAX_WIDTH = 500;
	public static final int VERTICAL = 0;
	public static final int HORIZONTAL = 1;
	private ImageIcon image;
	private ImageIcon thumb;
	private Dimension size;

	public ImagePanel(String img) {
		this(new ImageIcon(img));
	}

	public ImagePanel(ImageIcon img) {
		setImage(img);
	}

	public void setImage(ImageIcon img)
	{
		this.image = img;
		size = getThumbSize( image.getImage() );
		setPreferredSize(size);
		setMinimumSize(size);
		setMaximumSize(size);
		setSize(size);
		setLayout(null);
		this.validate();
	}

	public void paintComponent(Graphics g) {
		int x = 0, y = 0;
		
		Image img = thumb.getImage();
		if(img.getHeight(null) < MainFrame.MAIN_HEIGHT)
		{
			y = (int) (MainFrame.MAIN_HEIGHT - (img.getHeight(null) * 1.2)) / 2;
			size = new Dimension(size.width, size.height + img.getHeight(null) / 2);
			setPreferredSize(size);
			setMinimumSize(size);
			setMaximumSize(size);
			setSize(size);
			setLayout(null);
			this.validate();
		}
		g.drawImage(img, x, y, null);
	}

	private Dimension getThumbSize(Image img)
	{
		float height_ratio = img.getHeight(null) / MainFrame.MAIN_HEIGHT;
		float width_ratio = img.getWidth(null) / MAX_WIDTH;
		float ratio = Math.max(height_ratio, width_ratio);

		int width = (int) (img.getWidth(null) / ratio);
		int height = (int) (img.getHeight(null) / ratio);

		if(ratio == height_ratio)
			getThumbnail(height, VERTICAL);
		else
			getThumbnail(width, HORIZONTAL);

		return new Dimension(width, height);
	}

	private Image getThumbnail(int size, int dir)
	{
		if (dir == HORIZONTAL)
		{
			thumb = new ImageIcon(
					image.getImage().getScaledInstance(
							size, -1, Image.SCALE_SMOOTH)
			);
		}
		else
		{
			thumb = new ImageIcon(
					image.getImage().getScaledInstance(
							-1, size, Image.SCALE_SMOOTH)
			);
		}
		return thumb.getImage();
	}

}
