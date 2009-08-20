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

unsortedFolder = 'unsorted'
destLoc = os.environ['HOME'] + '/Pictures/Wallpapers/'
sep = 'x'
dest = []
li = []
initText = """Wallpaper Organize    Copyright (C) 2009 Fernando Alexandre

This program comes with ABSOLUTELY NO WARRANTY;
This is free software, and you are welcome to redistribute it
under certain conditions; More information at the begining of source code.\n"""

def process(targetLoc):
    print "%s -> %s\n" % (targetLoc, destLoc)

    for root, dirs, files in os.walk(targetLoc):
        for f in files:
            try:
                if f !=".DS_Store":
                    im = Image.open(os.path.join(targetLoc, f))
                    key = "%d%s%d" % (im.size[0], sep, im.size[1])
                    if key in dest:
                        shutil.copy(os.path.join(targetLoc, f),
                                    os.path.join(os.path.join(destLoc, key), f))
                        print "%s -> %s" % (f, key)
                    else:
                        shutil.copy(os.path.join(targetLoc, f),
                                     os.path.join(os.path.join(destLoc, unsortedFolder), f))
                        print "%s -> %s" % (f, unsortedFolder)

            except IOError, e:
                print "File IO Error: %s" % (f)
        for d in dirs:
            process(os.path.join(targetLoc, d))
    return

def populate_dest():
    for root, dirs, files in os.walk(destLoc):
        for d in dirs:
            dest.insert(0, d)
    try:
        dest.index(unsortedFolder)
    except ValueError:
        os.makedirs(os.path.join(destLoc, unsortedFolder))
    return

def process_dupes(loc):
    print "%s\n" % (loc)
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
        process_dupes(destLoc)
        print "Finished."
    else:
        print "Usage: python WallpaperOrganize.py <destination folder(s)>"
    return

if __name__ == "__main__":
    main()