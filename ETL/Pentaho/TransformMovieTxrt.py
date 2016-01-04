#!/usr/bin/env python3
# -*- coding: utf-8 -*-

import gzip
#import simplejson

def parse(filename):
  f = open(filename, 'r')
  entry = {}
  for l in f:
    l = l.strip()
    colonPos = l.find(':')
    if colonPos == -1:
      yield entry
      entry = {}
      continue
    eName = l[:colonPos]
    rest = l[colonPos+2:]
    entry[eName] = rest
  yield entry

f = open("movies.txt",'wb')
for e in parse("/Volumes/Untitled/tmpmovie.txt"):
	#f.write(e)
  f.writelines(["%s|" % item for item in e.values()])
  f.write('\n')
  
f.close()