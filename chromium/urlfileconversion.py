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
for filename in os.listdir(dir):
	i+=1
	f=open(dir+"/"+filename, "r")
	url=f.read()
	urldata+=url+",\n"
	


f=open("url_parsing_unittest.cc","w")
f.write(prefix+urldata[:-2]+"};\n"+suffix)
f.close()


	





 