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

testnames=[]
for filename in os.listdir(dir):
	i+=1
	urldata="var gTests = ["
	if filename[0]=="c": #open only component files #can be removed, now only component files in folder
		f=open(dir+"/"+filename, "r")
		url=f.read()
		urldata+=url+"];"
		prefix="\"use strict\";\
	\n var gIoService = Cc[\"@mozilla.org/network/io-service;1\"].getService(Ci.nsIIOService);\n"
		js_file=prefix+"\n"+urldata+"\n"+suffix
	
		f=open("URLTestFiles/test_URIs_"+str(i)+".js","w") #TODO collect multiple inputs in test file
		testnames+=["test_URIs_"+str(i)+".js"]
		f.write(js_file)
		f.close()
	
xpcshellinicontent="[DEFAULT]\nhead = head_channels.js head_cache.js head_cache2.js head_cookies.js\nretry = False\n"
for test in testnames:
	xpcshellinicontent+="["+test+"]\n"

f=open("URLTestFiles/xpcshell.ini","w")
f.write(xpcshellinicontent)
f.close()

	





 
