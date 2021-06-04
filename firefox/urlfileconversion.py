import argparse
import os
import subprocess
import json

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

prefix="var gIoService = Cc[\"@mozilla.org/network/io-service;1\"].getService(Ci.nsIIOService);\n"
#prefix="\"use strict\";\
#	\n var gIoService = Cc[\"@mozilla.org/network/io-service;1\"].getService(Ci.nsIIOService);\n"


def extractAllinputURLs(compstr):
	# extracts the original url for evaluation purposes
	tmp=""
	compstr=compstr.replace("spec", "\"spec\"").replace("relativeURI", "\"relativeURI\"")
	lines=compstr.split("\n")
	for line in lines:
		if "\"spec\":" in line or "\"relativeURI\":" in line:
			tmp+=line
	if not tmp[0]=="{":
		tmp="{"+tmp
	tmp=tmp+"}"
	tmp=tmp.replace("\",}", "\"}")
	unescaped=json.loads(tmp, strict=False)
	bas=unescaped["spec"]
	rel=""
	if "relativeURI" in unescaped:
		rel="<" +unescaped["relativeURI"]	
	return bas+rel
	


#collect url data
urlcontents=[]
for filename in os.listdir(dir):
	f=open(dir+"/"+filename, "r", encoding='utf-8')
	urlcontents+=[f.read()]

allinputs=""
#create test files with ~n test cases per file
n=100
testnames=[]
testchunks=[urlcontents[i*n:(i+1)*n] for i in range((len(urlcontents)+n-1)//n)]
testid=2
for chunk in testchunks:
	testid+=1
	urldata="var gTests = ["
	for url in chunk:
		urldata+="\n" + url + ","
		
		allinputs+=extractAllinputURLs(url)+"\n" 
				
	urldata=urldata[:-1]
	urldata+="];"
	testname="test_URIs_"+str(testid)+".js"
	testnames+=[testname]
	f=open("URLTestFiles/"+testname,"w", encoding='utf-8')
	f.write(prefix+"\n"+urldata+"\n"+suffix)
	f.close()

#register tests	
xpcshellinicontent="[DEFAULT]\nhead = head_channels.js head_cache.js head_cache2.js head_cookies.js\nretry = False\n"
for test in testnames:
	xpcshellinicontent+="["+test+"]\n"

f=open("URLTestFiles/xpcshell.ini","w")
f.write(xpcshellinicontent)
#print(xpcshellinicontent[:300])
f.close()

allinputs=allinputs[:-1]
f=open("allinputURLs", "w", encoding='utf-8')
f.write(allinputs)
f.close()

for filename in os.listdir(dir+"/.."):
	if "seed" in filename:
		f=open(dir+"/../"+filename, "r", encoding='utf-8')
		seed=f.read()
		print(seed)
		f.close()
		f=open("./../evaluation-tools/used_seed", "w")
		f.write(seed)
		f.close()


	





 
