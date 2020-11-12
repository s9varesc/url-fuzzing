import argparse
import os
import subprocess
import json

#
# evaluates the collected exceptions
#

# the diretory containing the collected exceptions as *Exceptions.txt 
# as well as the generated URLs as plainURLs.txt
parser = argparse.ArgumentParser()
parser.add_argument("-dir")
args = parser.parse_args()
dir = args.dir

for file in os.listdir(dir):
	if file.endswith('URLs.txt'):   
		plainURLs=file.read()  

# all urls that were tested

urls=plainURLs.split("\n")

parsers={}
# read the exceptions into dictionaries
for exfile in os.listdir(dir):
	if exfile.endswith('Exceptions.txt'): 
		parsername=exfile[:-len("Exceptions.txt")]
		with open(exfile) as f:
			data=f.read()
		#TODO
		data.replace("{ url:", "{ \"url\":") #check if replacing really necessary
		data.replace("exception:", "\"exception\":")

		sep="},"
		splitdata=[d+sep for d in data.split(data) if d] #one entry per {url:...,exception:...},

		#produce dictionary with {url1 : exception1, url2:...}
		datadict={}
		for datapoint in splitdata:
			tempdict=json.loads(datapoint)
			datadict[tempdict["url"]]=tempdict["exception"]

		# {parsername1 : {url1:exception1, url2:...},
		#  parsername2 : {...}}
		parsers[parsername]=datadict 


#evaluate
parserranking={} # {parsername: { errorcount: , nrerrtypes: ,
#						 errtypes: {errtype:[url, url,...]} ]

urlranking={} # {url: , nrerrors: , parsers: []}
# basic counting & parser ranking
for parser in parsers:
	countresults={}
	# overall nr of errors
	countresults["errorcount"]=len(parsers[parser])

	# group errors by their messages
	errtypes={}
	for errdata in parsers[parser]:
		temp=parsers[parser][errdata]
		temp.replace(errdata, "")
		if temp in errtypes: 
			errtypes[temp]+=[errdata]
		else:
			errtypes[temp]=[errdata]

	# place results
	countresults["nrerrtypes"]=len(errtypes)
	countresults["errtypes"]=errtypes
	parserranking[parsername]=countresults

#url ranking
for url in urls:
	urlranking[url]=[]
	for parser in parsers:
		if url in parsers[parser]:
			urlranking[url]+=[parser]

print("Results: \n")
print("number of urls tested: "+ len(urls))
print("number of parsers: "+len(parsers))
print("ranking of parsers (unsorted)")
print(parserranking)
print("ranking of urls (unsorted)")







