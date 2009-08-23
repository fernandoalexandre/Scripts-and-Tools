#! /usr/bin/env python
#   Image Organize - Organizes Wallpapers by resolution to a target location.
#       Needs for the folders "WidthxHeight" folders already created at destLoc
#       aswell as the "unsorted" folder.
#.
#   Copyright (C) 2009  Fernando Alexandre
#
#   This program is free software: you can redistribute it and/or modify
#   it under the terms of the GNU General Public License as published by
#   the Free Software Foundation, either version 3 of the License, or
#   (at your option) any later version.
#
#   This program is distributed in the hope that it will be useful,
#   but WITHOUT ANY WARRANTY; without even the implied warranty of
#   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#   GNU General Public License for more details.
#
#   You should have received a copy of the GNU General Public License
#   along with this program.  If not, see <http://www.gnu.org/licenses/>.

import os
import sys
import Image
import shutil
import hashlib

unsortedDir = 'unsorted'
destDir = os.environ['HOME'] + '/Pictures/Wallpapers/'
sep = 'x'
dest = []
li = []
initText = """Wallpaper Organize    Copyright (C) 2009 Fernando Alexandre

This program comes with ABSOLUTELY NO WARRANTY;
This is free software, and you are welcome to redistribute it
under certain conditions; More information at the begining of source code.\n"""

# Organizes the images by resolution from <targetDir> to <destDir>.
def process(targetDir):
    print "%s -> %s\n" % (targetDir, destDir)

    for root, dirs, files in os.walk(targetDir):
        for f in files:
            try:
                if f !=".DS_Store":
                    im = Image.open(os.path.join(targetDir, f))
                    key = "%d%s%d" % (im.size[0], sep, im.size[1])
                    if key in dest:
                        shutil.copy(os.path.join(targetDir, f),
                                    os.path.join(os.path.join(destDir, key), f))
                        print "%s -> %s" % (f, key)
                    else:
                        shutil.copy(os.path.join(targetDir, f),
                                     os.path.join(os.path.join(destDir, unsortedDir), f))
                        print "%s -> %s" % (f, unsortedDir)

            except IOError, e:
                print "File IO Error: %s" % (f)
        for d in dirs:
            process(os.path.join(targetDir, d))
    return

# Populates <dest> list with all the directories in the <destDir>. Only the
# appropriate [width][sep][height] directories will be used. Creates "unsorted"
# directory if it does not exist.
def populate_dest():
    for root, dirs, files in os.walk(destDir):
        for d in dirs:
            dest.insert(0, d)
    try:
        dest.index(unsortedDir)
    except ValueError:
        os.makedirs(os.path.join(destDir, unsortedDir))
    return

# Detects and deletes all the duplicates in the <destLoc> and all the directories
# within it.
def process_dupes(loc):
    print "%s" % (loc)
    for root, dirs, files in os.walk(loc):
        for f in files:
            try:
                file = open(os.path.join(loc, f), "r")
                fileMd5 = hashlib.md5(file.read()).hexdigest()
                try:
                    li.index(fileMd5)
                    print "%s deleted." % (os.path.join(loc, f))
                    os.remove(os.path.join(loc, f))
                except ValueError:
                    li.insert(0, fileMd5)
            except IOError:
                pass
        for d in dirs:
            process_dupes(os.path.join(loc, d))
    return

def main():
    print initText

    if len(sys.argv) > 1:
        populate_dest()
        for arg in sys.argv[1:]:
            print "Organizing %s..." % (arg)
            process(arg)
            print "Finished %s." % (arg)
        print "Starting to remove duplicates..."
        process_dupes(destDir)
        print "Finished."
    else:
        print "Usage: python %s <destination folder(s)>" % sys.argv[0]
    return

if __name__ == "__main__":
    main()