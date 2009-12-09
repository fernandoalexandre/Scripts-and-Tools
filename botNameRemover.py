#! /usr/bin/env python

import os
import sys

sep = '.'
initText = """XDCC Bot Name Remover    Copyright (C) 2009 Fernando Alexandre

This program comes with ABSOLUTELY NO WARRANTY;
This is free software, and you are welcome to redistribute it
under certain conditions; More information at the begining of source code.\n"""

# Applies the transformation bla.bla.avi -> bla.avi in all files in folder [arg].
def process(arg):
    for root, dirs, files in os.walk(arg):
        for f in files:
            i = f.find(sep) + 1
            os.rename(os.path.join(arg, f), os.path.join(arg, f[i:]))
            print "%s -> %s" % (f, f[i:])
        for d in dirs:
            process(os.path.join(arg, d))
    return

def main():
    if len(sys.argv) > 1 and os.path.isdir(sys.argv[1]):
        print initText
        for arg in sys.argv[1:]:
            print "Processing %s..." % (arg)
            process(arg)
    else:
        print "Usage: python %s <directory>" % sys.argv[0]
    return

if __name__ == "__main__":
    main()