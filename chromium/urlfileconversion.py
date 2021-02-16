import argparse
import os
import subprocess

#
# uses files containing the component representation of urls to create a chromium test file
#

parser = argparse.ArgumentParser()
parser.add_argument("-dir")

args = parser.parse_args()
dir = args.dir

f1=open("./url_parsing_prefix.txt","r")
prefix=f1.read()
f=open("./url_parsing_suffix.txt","r")
suffix=f.read()
i=2

urldata="\nstatic URLParseCase parse_cases[]={"
allinputs=""
for filename in os.listdir(dir):
	i+=1
	f=open(dir+"/"+filename, "r")
	url=f.read()					
	#url.replace("\\\\", "\\\\\\")
	#url.replace("\\\"", "\\\\\"")

	inp=url[2:]
	cutindex=inp.find("\",\"")
	inp=inp[:cutindex]
	allinputs+=inp+"\n"


	urldata+=url+",\n"
	


f=open("url_parsing_unittest.cc","w")
f.write(prefix+urldata[:-2]+"};\n"+suffix)
f.close()


allinputs=allinputs[:-1]
f=open("allinputURLs", "w")
f.write(allinputs)
f.close()

	





 
