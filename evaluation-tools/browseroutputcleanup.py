import argparse
import os
import subprocess
import json


# splits the output log created by a Browser into [Browser]Exceptions.txt and [Browser]Errors.txt for further evaluation

# open the given directory and read the available log files
parser = argparse.ArgumentParser()
parser.add_argument("-dir")
args = parser.parse_args()
dir = args.dir

for file in os.listdir(dir):
	if file.endswith('.log'):
		with open(dir +"/"+ file) as f:
			logfile=f.read()  
			browsername=file[:-10] #remove "output.log"

		exceptionfiledata="\n" #match other exception files
		errorfiledata=""
		splitlogfile=logfile.split("\n")
		for line in splitlogfile:
			if "{url" in line:
				if "error:{component:" in line:
					#failed component match
					errorfiledata+=line+"\n"
				else:
					#exception during parsing
					exceptionfiledata+=line+",\n" #matches format of languages
	
		#write resulting files
		exfile=open(dir +"/"+ browsername+"Exceptions.txt", "w")
		exfile.write(exceptionfiledata)
		exfile.close()

		erfile=open(dir +"/"+ browsername+"Errors.txt", "w")
		erfile.write(errorfiledata)
		erfile.close()	
