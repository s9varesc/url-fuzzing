import argparse
import os
import subprocess
import json

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
nr=0

def extractAllinputURLs(compstr):
	tmp0="{\"base\":"
	tmp1=" , \"relative\":"
	tmp2=" }"
	
	inp=compstr[1:]

	cutindex=inp.find("\",\"") 

	bas=inp[:cutindex+1] #= "base"

	nextcut=inp.find("\",\"",cutindex+3 )

	rel=inp[cutindex+2:nextcut+1] #= "relative"
	tmp=tmp0+bas+tmp1+rel+tmp2

	unescaped=json.loads(tmp, strict=False)
	

	base=unescaped["base"]
	rela=unescaped["relative"]
	if len(rela)>0:
		rela="<" +rela	
	return base+rela
	


for filename in os.listdir(dir):
	i+=1
	f=open(dir+"/"+filename, "r", encoding='utf-8')
	url=f.read()					
	#url.replace("\\\\", "\\\\\\")
	#url.replace("\\\"", "\\\\\"")

	allinputs+=extractAllinputURLs(url)+"\n"

	nr+=1
	urldata+=url+",\n"
	


f=open("url_parsing_unittest.cc","w", encoding='utf-8')
f.write(prefix+urldata[:-2]+"};\n"+suffix)
f.close()


allinputs=allinputs[:-1]
f=open("allinputURLs", "w", encoding='utf-8') 
f.write(allinputs)
f.close()

	





 
