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

f=open("./test_URIs_suffix.txt","r")
suffix=f.read()
i=2
for filename in os.listdir(dir):
	i+=1
	urldata="var gTests = ["
	if filename[0]=="c": #open only component files
		f=open(dir+"/"+filename, "r")
		url=f.read()
		urldata+=url+"]\n"
	prefix="\"use strict\";\
	\n var gIoService = Cc[\"@mozilla.org/network/io-service;1\"].getService(Ci.nsIIOService);\n"
	js_file=prefix+"\n"+urldata+"\n"+suffix
	
	f=open("generatedTestFiles/test_URIs_"+str(i)+".js","w")
	f.write(js_file)
	f.close()


	





 
