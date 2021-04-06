import argparse
import os
import subprocess

parser = argparse.ArgumentParser()
parser.add_argument("-dir")
args = parser.parse_args()
dir = args.dir

for filename in os.listdir(dir):
    #print filename
    if filename.endswith(".html"):
    	f=open(dir+"/"+filename,  "r", encoding='utf-8')
    	s = f.read()
    	s = s.replace('../../../../../../../style.css', './style.css')
    	#print "replaced in " + filename
    	f=open(dir+"/"+filename, "w", encoding='utf-8')
    	f.write(s)
    	f.close()
