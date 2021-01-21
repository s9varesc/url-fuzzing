import argparse
import os
import subprocess
import json

# correct escaping 
def escaping(data):
	tmp=data.replace("\\", "\\\\")
	tmp=tmp.replace("\"", "\\\"" )
	tmp=tmp.replace("\'", "\\\'")
	return tmp
def fixdatapoint(datapoint): #TODO use escapebetween
	
	if len(datapoint)<=2: return datapoint
	res=datapoint
	
	[dp1, dp2]=datapoint.split("\", \"exception\":\"")
	dp1=dp1+"\","
	
	pre1=dp1[:8] #{ "url":"
	p1=dp1[8:-2] #url contents
	post1=dp1[-2:] + " \"exception\":\""

	pre2=""# 
	p2=dp2[:-2] #exception contents
	post2=dp2[-2:] # "}

	p1=escaping(p1)
	p2=escaping(p2)

	if p1[-1:]=="\\":
		p1=p1+" "

	if p2[-1:]=="\\":
		p2=p2+" "

	res=pre1+p1+post1+pre2+p2+post2
	return res

def fixerrordatapoint(datapoint):
	# specialized on browser component errors
	wdata=datapoint

	#{"url":"
	# ", "error":{"component":"
	# ", "expected":"
	# ", "actual":"
	# "}}
	
	s1="{\"url\":\""
	s2="\", \"error\":{\"component\":\""
	s3="\", \"expected\":\""
	s4="\", \"actual\":\""
	s5="\"}}"

	res=datapoint
	res=escapebetween(s1, s2, res)
	res=escapebetween(s2, s3, res)
	res=escapebetween(s3, s4, res)
	res=escapebetween(s4, s5, res)
	
	return res

def escapebetween(s1, s2, data):
	wd=data
	i1=wd.find(s1)
	i2=wd.find(s2)

	content=wd[i1+len(s1):s2]
	escapedcontent=escaping(content)
	return wd[:i1+len(s1)]+escapedcontent+wd[s2:]

#
# evaluates the collected exceptions and errors
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
		
		#data = data.replace("{ url:", "{ \"url\":") 
		#data = data.replace("exception:", "\"exception\":")
		
		data = data[1:-1]
		

		sep="}\n" 
		splitdata=[d+"}" for d in data.split(sep) if d] #one entry per {url:...,exception:...}
		#print(splitdata[2])
		#produce dictionary with {url1 : exception1, url2:...}
		datadict={}
		
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


pfile=open( dir+"../evaldata/"+"parsercomp.json", "w") 
pfile.write(parserranking)
pfile.close()

ufile=open( dir+"../evaldata/"+"urlcomp.json", "w")
ufile.write(urlranking)
ufile.close()




# create error dictionaries:
# { browser : [errordata, ... ]}
# with errordata looking like {"url":"http://...", "error":{"component":"port", "expected":"20", "actual":"xxx"}}

errorranking={}
for file in os.listdir(dir):
	if file.contains('Errors'):
		with open(dir +"/"+ file) as f:
			errdata=f.read()   
			name=file.replace("Errors.txt", "")
			esplit=errdata.split("\n")
			fixederrdata={}
			for errd in esplit:
				fixederrdata+=[fixerrordatapoint(errd)]
			tmp=json.loads(fixederrdata)
			errorranking[name]=tmp #TODO make sure its a list




efile=open( dir+"../evaldata/"+"errorcomp.json", "w")
efile.write(errorranking)
efile.close()







