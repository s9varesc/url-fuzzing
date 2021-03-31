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

f1=open("./url_parsing_prefix_NO_components.txt","r", encoding='utf-8')
prefix=f1.read()
f=open("./url_parsing_suffix_NO_components.txt","r", encoding='utf-8')
suffix=f.read()
i=2

urldata="\nstatic URLParseCase parse_cases[]={"
allinputs=""
for filename in os.listdir(dir):
	i+=1
	f=open(dir+"/"+filename, "r")
	urlcontents=f.read()  				
	allinputs+=urlcontents+"\n"

	urlcontents=urlcontents.replace("\\\\", "\\\\\\")	#TODO: check this again
	urlcontents=urlcontents.replace("\\\"", "\\\\\"")
	url="{\""+ urlcontents +"\"}"
	
	urldata += url+",\n"

f=open("url_parsing_unittest.cc","w", encoding='utf-8')
f.write(prefix+urldata[:-2]+"};\n"+suffix)
f.close()

allinputs=allinputs[:-1]
f=open("allinputURLs", "w", encoding='utf-8')
f.write(allinputs)
f.close()

	





 
