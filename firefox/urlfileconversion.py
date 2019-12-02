import argparse
import os
import subprocess

#
# uses files containing the component representation of urls to create a firefox test file
# similar to test_URIs.js
#

parser = argparse.ArgumentParser()
parser.add_argument("-dir")

args = parser.parse_args()
dir = args.dir

urldata="var gTests = ["


for filename in os.listdir(dir):
	if filename[0]=="c": #open only component files
		f=open(dir+"/"+filename, "r")
		url=f.read()
		urldata+=url+",\n"
urldata=urldata[:-2]
urldata+="];\n"

prefix="\"use strict\";\
\n var gIoService = Cc[\"@mozilla.org/network/io-service;1\"].getService(Ci.nsIIOService);\n"

f=open("C:/mozilla-source/mozilla-central/netwerk/test/unit/uri/test_URIs_suffix.txt","r")
suffix=f.read()
js_file=prefix+"\n"+urldata+"\n"+suffix

f=open("C:/mozilla-source/mozilla-central/netwerk/test/unit/test_URIs3.js","w")
f.write(js_file)
f.close()


	





 
