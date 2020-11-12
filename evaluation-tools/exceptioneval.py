import argparse
import os
import subprocess
import json

# correct escaping 
def fixdatapoint(datapoint):
	print(datapoint)
	if len(datapoint)<=2: return datapoint
	expectedquotationmarks=8
	res=datapoint
	#if datapoint.count("\"") > expectedquotationmarks:
	[dp1, dp2]=datapoint.split("\n")
	pre1=dp1[:9] #{ "url":"
	p1=dp1[9:-2] #url contents
	post1=dp1[-2:] # ",

	pre2=dp2[:14]#"exception":"
	p2=dp2[14:-2] #exception contents
	post2=dp2[-2:] # "}

	p1=p1.replace("\\", "\\\\")
	p2=p2.replace("\\", "\\\\")

	p1=p1.replace("\"", "\\\"")
	p2=p2.replace("\"", "\\\"")
		
	if p1[-1:]=="\\":
		p1=p1+" "

	if p2[-1:]=="\\":
		p2=p2+" "

		
		
	res=pre1+p1+post1+pre2+p2+post2
	return res


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
	if file.endswith('URLs'):
		with open(dir +"/"+ file) as f:
			plainURLs=f.read()   
		

# all urls that were tested

urls=plainURLs.split("\n")

parsers={}
# read the exceptions into dictionaries
for exfile in os.listdir(dir):
	if 'Exceptions' in exfile: 
		parsername=exfile.replace("Exceptions","").replace(".txt","")
		with open(dir +"/"+ exfile) as f:
			data=f.read()
		
		data = data.replace("{ url:", "{ \"url\":") 
		data = data.replace("exception:", "\"exception\":")
		
		data = data[1:-1]
		

		sep="},\n" 
		splitdata=[d+"}" for d in data.split(sep) if d] #one entry per {url:...,exception:...},
		#print(splitdata[2])
		#produce dictionary with {url1 : exception1, url2:...}
		datadict={}
		print(parsername)
		for datapoint in splitdata:
			#datapoint=datapoint[1:]
			#print(datapoint)
			datapoint=fixdatapoint(datapoint)
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
		temp = temp.replace(errdata, "")
		if temp in errtypes: 
			errtypes[temp]+=[errdata]
		else:
			errtypes[temp]=[errdata]

	# place results
	countresults["nrerrtypes"]=len(errtypes)
	countresults["errtypes"]=errtypes
	parserranking[parser]=countresults

#url ranking
for url in urls:
	#urlranking[url]=[]
	for parser in parsers:
		if url in parsers[parser]:
			#print(url)
			if url not in urlranking:
				urlranking[url]=[]
			urlranking[url]+=[parser]

print("Results: \n")
print("number of urls tested: "+ str(len(urls)))
print("number of parsers: "+str(len(parsers)))
print("ranking of parsers (unsorted)")
print(parserranking)
print("ranking of urls (unsorted)")
print(urlranking)










