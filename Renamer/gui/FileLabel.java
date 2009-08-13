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

import javax.swing.JLabel;


public class FileLabel extends JLabel {

	private static final long serialVersionUID = 1L;

	private static final String[] IMAGE_EXTENSIONS = 
	{"jpg", "jpeg", "gif", "png", "raw", "tiff", "bmp"};

	public FileLabel(String fileName)
	{
		super(fileName);
	}

	public String getFileName()
	{
		return super.getText();
	}

	public void setFileName(String fileName)
	{
		super.setText(fileName);
	}

	public String getExtension()
	{
		String[] result = getFileName().split("\\.");
		if (result.length > 1) 
			return result[result.length - 1];
		else
			return null;
	}

	public String getImagePath()
	{
		return MainFrame.WORKING_DIR + MainFrame.getSystemPathSymbol() + getFileName();
	}

	public boolean isImage()
	{
		String fileExtension = getExtension();
		if(fileExtension != null)
			for(String e : IMAGE_EXTENSIONS)
				if(fileExtension.toLowerCase().compareTo(e) == 0)
					return true;
		return false;
	}

}
