#! /usr/bin/env python

import os
import sys
import httplib
import urlparse
import string

def save_file(stream, dest, file):
    if os.path.isfile(os.path.join(dest, file)):
        print "File %s already exists. Overwriting..." % file
    file = open(os.path.join(dest, file), 'w')
    file.write(stream)
    return

def get_http_size(resp):
    if resp.getheader('Content-Length') == None:
        return '?'
    else:
        return resp.getheader('Content-Length')

def http_dl(url, p_url, dest):
    conn = httplib.HTTPConnection(p_url.netloc)
    conn.request("GET", p_url.path)
    resp = conn.getresponse()
    print "Downloading %s to %s (%s bytes)..." % (url, dest, get_http_size(resp))
    if resp.status == 200:
        split = url.split('/')
        save_file(resp.read(), dest,split[len(split) - 1])
        print "Finished!"
    else:
        print "Error: %s %s" % (resp.status, resp.reason)
    return

def ftp_dl(url, p_url, dest):
    return

def download(url, dest):
    p_url = urlparse.urlsplit(url)
    if p_url.scheme == 'http':
        http_dl(url, p_url, dest)
    elif p_url.scheme == 'ftp':
        ftp_dl(url, p_url, dest)
    else:
        print "Incorrect protocol for %s (%s)." % (url, p_url.scheme)
    return

def main():
    if len(sys.argv) > 2:
        for arg in sys.argv[2:]:
            download(arg , sys.argv[1])
    else:
        print "Usage: python %s <destination> <file's>" % sys.argv[0]
    return

if __name__ == "__main__":
    main()