#! /usr/bin/env python
#   downloader -
#       A simple command line downloader. Supports HTTP/FTP. For FTP there is
#       a prompt asking for a username/password which can be left blank for 
#       anonymous login.
#
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
import httplib
import ftplib
from ftplib import FTP
import urlparse
import string

initText = """downloader    Copyright (C) 2009 Fernando Alexandre

This program comes with ABSOLUTELY NO WARRANTY;
This is free software, and you are welcome to redistribute it
under certain conditions; More information at the begining of source code.\n"""

# HTTP
def save_file(stream, dest, file):
    if os.path.isfile(os.path.join(dest, file)):
        print "File %s already exists. Overwriting..." % file
    file = open(os.path.join(dest, file), 'w')
    file.write(stream)
    return

def get_http_size(resp):
    if resp.getheader('Content-Length') == None:
        return '?'
    return resp.getheader('Content-Length')

def http_dl(p_url, dest):
    try:
        conn = httplib.HTTPConnection(p_url.netloc)
    except:
        print "Could not connect to %s." % p_url.netloc
        return

    conn.request("GET", p_url.path)
    resp = conn.getresponse()
    if resp.status == httplib.OK:
        print "Downloading %s to %s (%s bytes)..." % (p_url.path, dest, get_http_size(resp))
        split = url.split('/')
        save_file(resp.read(), dest,split[len(split) - 1])
        print "Finished!"
    else:
        print "Error: %s %s" % (resp.status, resp.reason)
    conn.close()
    return

# FTP
def get_path(path):
    split = path.replace('//', '/').split('/')
    result = ''
    for i in split[1:(len(split) - 1)]:
            result = result + '/%s' % i
    return (result, split[len(split) - 1])

def get_ftp_size(size):
    if size != None:
        return '?'
    return str(size)

def ftp_dl(p_url, dest):
    try:
        conn = FTP(p_url.netloc)
    except:
        print "Could not connect to %s." % p_url.netloc
        return

    print 'Please provide login (leave blank for anonymous)'
    user = raw_input('user: ')

    if user == '':
        print "user anonymous, passwd anonymous@"
        try:
            conn.login()
        else:
            print "No anonymous connections allowed."
            conn.close()
            return
    else:
        passwd = raw_input('password: ')
        try:
            conn.login(user, passwd)
        except:
            print 'Incorrect username or password.'
            conn.close()
            return

    path_tuple = get_path(p_url.path)
    try:
        conn.cwd(path_tuple[0])
    except:
        print 'Error: %s does not exist.' % path_tuple[0]
        conn.close()
        return

    file = open(os.path.join(dest, path_tuple[1]), 'w')
    try:
        resp = conn.size(path_tuple[1]) # Is not standardized
        print "Downloading %s to %s (%s bytes)..." % (path_tuple[1], dest, get_ftp_size(resp))
        conn.retrbinary('RETR ' + path_tuple[1], file.write)
    except:
        print 'Error: Could not download %s.' % path_tuple[1]
        conn.close()
        file.close()
        return

    conn.close()
    file.close()
    print "Finished!"
    return

def download(url, dest):
    p_url = urlparse.urlsplit(url)
    if p_url.scheme == 'http':
        http_dl(p_url, dest)
    elif p_url.scheme == 'ftp':
        ftp_dl(p_url, dest)
    else:
        print "Incorrect protocol for %s (%s)." % (url, p_url.scheme)
    return

def main():
    if len(sys.argv) > 2 and os.path.isdir(sys.argv[1]):
        print initText
        for arg in sys.argv[2:]:
            download(arg , sys.argv[1])
    else:
        print "Usage: python %s <destination> <file's>" % sys.argv[0]
    return

if __name__ == "__main__":
    main()