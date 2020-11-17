import argparse
import os
import subprocess

#
# uses files containing the component representation of urls to create a chromium test file
#

parser = argparse.ArgumentParser()
parser.add_argument("-urlfile")

args = parser.parse_args()
urlfile = args.urlfile

f1=open("./url_parsing_prefix_NO_components.txt","r")
prefix=f1.read()
f=open("./url_parsing_suffix_NO_components.txt","r")
suffix=f.read()
i=2

urldata="\nstatic URLParseCase parse_cases[]={"

f=open(urlfile, "r")
filecontents=f.read()
filecontents=filecontents.replace("\\\\", "\\\\\\")
filecontents=filecontents.replace("\\\"", "\\\\\"")
urlcontents=filecontents.split("\n")

for url in urlcontents:
	urldata+=url+",\n" #TODO url format
	


f=open("url_parsing_unittest.cc","w")
f.write(prefix+urldata[:-2]+"};\n"+suffix)
f.close()


	





 
