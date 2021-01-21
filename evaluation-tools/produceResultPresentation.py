import argparse
import os
import subprocess
import json
import markdown

# links to the coverage reports
covreps={}
covreps["chromium"]="\n#### Chromium\n\n[Overview](./chromium/report.html)\n\n[Source File Report](./chromium/url_parse.cc.html)\n"
covreps["firefox"]="\n#### Firefox\n\n[Overview](./firefox/index.html)\n[Source File Report](./firefox/netwerk/base/nsURLParser.cpp.gcov.html)\n"
covreps["C"]="\n#### C\n\n[Overview](./C/index.html)\n\n[Source File Report](./C/src/UriParse.c.gcov.html)\n"
covreps["Cpp"]="\n#### C\\+\\+\n\n[Overview](./Cpp/index.html)\n\n[Source File Report](./Cpp/src/URI.cpp.gcov.html)\n"
covreps["Go"]="\n#### GO\n\n[Source File Report](./Go/index.html)\n"
covreps["Java"]="\n #### JAVA REPORT HERE" #TODO
covreps["urijs"]="\n#### JavaScript urijs\n\n[Overview](./JavaScript/urijs/index.html)\n\n[Source File Report](./JavaScript/urijs/src/URI.js.html)\n"
covreps["whatwg"]="\n#### JavaScript whatwg\\-url\n\n[Overview](./JavaScript/whatwg-url/index.html)\n\n[Source File Report](./JavaScript/whatwg-url/lib/url-state-machine.js.html)\n"
covreps["PHP"]="\n#### PHP\n\n[Overview](./PHP/index.html)\n\n[Source File Report](./PHP/UriString.php.html)\n"
covreps["Python"]="\n#### Python\n\n[Overview](./Python/index.html)\n\n[Source File Report](./Python/_usr_lib_python3_6_urllib_parse_py.html)\n"


#TODO explicitly state browsers -> could include others later on




parser = argparse.ArgumentParser()
parser.add_argument("-data")
args = parser.parse_args()
datadir = args.data

pranking=""
uranking=""
eranking="" 
# read json file
for file in os.listdir(datadir):
	if file.contains("parser"):
		with open(datadir+"/"+file) as f:
			pranking=f.read()
	if file.contains("url"):
		with open(datadir+"/"+file) as f:
			uranking=f.read()
	if file.contains("error"): 
		with open(datadir+"/"+file) as f:
			eranking=f.read()


# multiple rankings: focus on parsers: {parsername: { errorcount: , nrerrtypes: ,
#						 							errtypes: {errtype:[url, url,...]} }}
#					focus on urls: {url: [parsers]} 
#					browsers including exceptions and errors    TODO!!!!!!!!


# prepare data structures
ptuples=[]
ptables=[] #list of md tables with heading
ptableheader=" Exception Type | URLs \n --- | --- \n"

parsertable=" Parsername | Number of Exceptions | Number of Different Exceptions \n --- | --- | --- \n"

# create table representation of the parser results
parserdata=json.loads(pranking)

for (pname, edata) in parserdata:
	ptuple=(pname, edata["errorcount"], edata["nrerrtypes"])
	ptuples+=[ptuple]
	ptable="###"+pname+"\n"+ptableheader+"\n"
	
	for (et, eurls) in edata["errtypes"]:
		etline=""
		etline+=et+ " | "
		for eurl in eurls:
			etline += eurl +"<br>"
		ptable+=etline[:-4]+"\n"

	ptables+=[ptable]

# sort ptubles and create overview table

sortedptuples=sorted(ptuples, key=lambda ptuple: ptuple[1])
pnames=[]
for (pn, en, den) in sortedptuples:
	parsertable+=pn +" | "+ en +" | "+ den +"\n"
	pnames+=[pn]

# create table representation of URLs
urldata=json.loads(uranking)

utable=" URL | Parsers \n --- | --- \n"

# keep track of urls that didnt cause parsing exceptions
succURLs=[]
nrurls=0
for (url, parsers) in urldata: #TODO sorted would be nicer
	nrurls+=1
	if not parsers:
		succURLs+=[url]
	else:
		uline=url+" | "
		for p in parsers:
			uline += p + "<br>"
		utable+=uline[:-4]+"\n"

# Browsers

## create overview table: browser | nr fails | nr exceptions | nr errors
otable=" Browser | Overall Failures | Parsing Exceptions | Verification Errors \n --- | --- | --- | --- \n"
for bname in eranking:
	nrex=pranking[bname]["errorcount"]
	nrer=len(eranking[bname])
	allf=nrex+nrer
	otable+=bname+" | "+ allf+" | "+ nrex+ " | "+nrer+"\n"

## create specialized tables per browser: url | component | expected | actual
vtables=[]
for bname in eranking:
	vtable="### "+bname+"\n\n"
	vtable+=" URL | Component | Expected Value | Actual Value \n --- | --- | --- | --- \n"
	elist=eranking[bname]
	for erdata in elist:   #TODO make sure this is accessed correctly
		u=erdata["url"]
		c=erdata["error"]["component"]
		exp=erdata["error"]["expected"]
		a=erdata["error"]["actual"]
		vtable+=u+ " | "+c+" | "+exp+" | "+ a+"\n"
	vtables+=vtable
## maybe create url comparison with parsing/verification passes



# Build the Document

result="# Results \n\n"
result+="Total number of URLs: "+nrurls+"\n\n"
result+="Total number of Parsers: "+len(pnames)+"\n\n" 


result +="## Parser Comparison \n\n"
result +=parsertable+"\n"

for ptable in ptables:
	result+=ptable+"\n"

result +="## URL Comparison \n\n"
result += utable+"\n"

result +="## Browsers\n\n" # TODO include only if browsers present
result+=otable+"\n"
for vtable in vtables:
	result+=vtable+"\n"

result +="## Coverage Reports \n\n"
#TODO only include actually present reports: dict of links ie parser : link TODO
brep=""
prep=""
for name in covreps:
	if name in pnames:
		if name in ["firefox", "chromium"]:
			brep+=covreps[name]
		else:
			prep+=covreps[name]

if brep != "":
	result+="### Browsers\n"+brep
if prep != "":
	result+="### Stand-Alone Parsers\n"+prep


resfile=open( datadir+"../resultoverview.md", "w")
resfile.write(result)
resfile.close()

htmlresult=markdown.markdown(result)
htmlfile=open( datadir+"../resultoverview.html", "w")
htmlfile.write(htmlresult)
htmlfile.close()







