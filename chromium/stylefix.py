import argparse
import os
import subprocess

parser = argparse.ArgumentParser()
parser.add_argument("-dir")
args = parser.parse_args()
dir = args.dir

for filename in os.listdir(dir):
    #print filename
    f=open(dir+"/"+filename)
    s = f.read()
    if filename.endswith(".html"):
        s = s.replace('../../../../../../../style.css', './style.css')
    #print "replaced in " + filename
    f=open(dir+"/"+filename, "w")
    f.write(s)
    f.close()
