import argparse
import os
import subprocess
import json
import markdown


# links to the coverage reports
covreps={}
covreps["chromium"]="\n#### Chromium\n\n[Overview](./chromium/report.html)\n\n[Source File Report](./chromium/url_parse.cc.html)\n\n"
covreps["firefox"]="\n#### Firefox\n\n[Overview](./firefox/index.html)\n\n[Source File Report](./firefox/netwerk/base/nsURLParsers.cpp.gcov.html)\n\n"
covreps["C"]="\n#### C\n\n[Overview](./C/index.html)\n\n[Source File Report](./C/src/UriParse.c.gcov.html)\n\n"
covreps["Cpp"]="\n#### C\\+\\+\n\n[Overview](./Cpp/index.html)\n\n[Source File Report](./Cpp/src/URI.cpp.gcov.html)\n\n"
covreps["Go"]="\n#### GO\n\n[Source File Report](./Go/index.html)\n\n"
covreps["Java"]="\n#### Java\n\n[Overview](./Java/index.html)\n\n[Source File Report](./Java/java/net/URL.html)\n\n" 
covreps["JavaScripturijs"]="\n#### JavaScript urijs\n\n[Overview](./JavaScript/urijs/index.html)\n\n[Source File Report](./JavaScript/urijs/URI.js.html)\n\n"
covreps["JavaScriptwhatwg-url"]="\n#### JavaScript whatwg\\-url\n\n[Overview](./JavaScript/whatwg-url/index.html)\n\n[Source File Report](./JavaScript/whatwg-url/whatwg-url/dist/url-state-machine.js.html)\n\n"
covreps["PHP"]="\n#### PHP\n\n[Overview](./PHP/index.html)\n\n[Source File Report](./PHP/UriString.php.html)\n\n"
covreps["Python"]="\n#### Python\n\n[Overview](./Python/index.html)\n\n[Source File Report](./Python/_usr_lib_python3_6_urllib_parse_py.html)\n\n"
covreps["Ruby"]="\n#### Ruby\n\n[Overview](./Ruby/index.html#_AllFiles)\n\n"


#TODO explicitly state browsers -> could include others later on


def escape_md(data):
	#escaping for exception messages
	res=data
	res=res.replace("|", "&#124;")
	#res=res.replace("`", "\\`")
	return res

def url_escape_md(data):
	#escaping for urls(=displayed as code blocks)
	res=data
	#make sure to use enough escaping backticks
	currentnrbt=0
	maxnrbt=0
	lastchar=None
	for c in res:
		if c == lastchar:
			if c =="`":
				currentnrbt+=1
				maxnrbt=max(maxnrbt, currentnrbt)
		else:
			currentnrbt=0
		lastchar=c

	escbt="```"
	for i in range(1, maxnrbt):
		escbt+="`"
	#res=res.replace("|", "\|")
	return " "+escbt+" "+res+" "+escbt+" "


parser = argparse.ArgumentParser()
parser.add_argument("-data")
args = parser.parse_args()
datadir = args.data
if datadir[-1:]!="/":datadir+="/"

pranking=""
uranking=""
eranking="" 
# read json file
for file in os.listdir(datadir):
	if "parser" in file:
		with open(datadir+"/"+file) as f:
			pranking=f.read()
	if "url" in file:
		with open(datadir+"/"+file) as f:
			uranking=f.read()
	if "error" in file: 
		with open(datadir+"/"+file) as f:
			eranking=f.read()


# multiple rankings: focus on parsers: {parsername: { errorcount: , nrerrtypes: ,
#						 							errtypes: {errtype:[url, url,...]} }}
#					focus on urls: {url: [parsers]} 
#					browsers including exceptions and errors   


# prepare data structures
ptuples=[]
ptables=[] #list of md tables with heading
ptableheader=" Exception Type | URLs \n --- | --- "

parsertable=" Parsername | Number of Exceptions | Number of Different Exceptions \n --- | --- | --- \n"

# create table representation of the parser results
parserdata=json.loads(pranking)

for pname in parserdata:
	edata=parserdata[pname]
	ptuple=(pname, edata["errorcount"], edata["nrerrtypes"])
	ptuples+=[ptuple]
	ptable="### "+pname+"\n\n"+ptableheader+"\n"
	
	for et in edata["errtypes"]:
		eurls=edata["errtypes"][et]
		etline=""
		etline+=url_escape_md(et)+ " | "
		for eurl in eurls:
			etline += url_escape_md(eurl) +" <br>"
		ptable+=etline+" \n"

	ptables+=[ptable]


# sort ptubles and create overview table

sortedptuples=sorted(ptuples, key=lambda ptuple: ptuple[1])
pnames=[]
for (pn, en, den) in sortedptuples:
	parsertable+=pn +" | "+ str(en) +" | "+ str(den) +"\n"
	pnames+=[pn]

# create table representation of URLs
urldata=json.loads(uranking)

utable=" URL | Parsers \n --- | --- \n"

# keep track of urls that didnt cause parsing exceptions
succURLs=[]
nrurls=0
for url in urldata: 
	parsers=urldata[url]	
	nrurls+=1
	if not parsers:
		succURLs+=[escape_md(url)]
	else:
		uline=url_escape_md(url)+" | "
		for p in parsers:
			uline += p + " <br>"
		utable+=uline+"\n"

# Browsers
errdata=json.loads(eranking)
## create overview table: browser | nr fails | nr exceptions | nr errors
otable=" Browser | Overall Failures | Parsing Exceptions | Verification Errors \n --- | --- | --- | --- \n"
for bname in errdata:
	nrex=parserdata[bname]["errorcount"]
	nrer=len(errdata[bname])
	allf=nrex+nrer
	otable+=bname+" | "+ str(allf)+" | "+ str(nrex)+ " | "+str(nrer)+"\n"

## create specialized tables per browser: url | component | expected | actual
vtables=[]
for bname in errdata:
	vtable="### "+bname+"\n\n" 
	vtable+=" URL | Component | Expected Value | Actual Value \n --- | --- | --- | --- \n"
	elist=errdata[bname]
	for erdata in elist:   
		u=url_escape_md(erdata["url"])
		c=escape_md(erdata["error"]["component"])
		exp=url_escape_md(erdata["error"]["expected"])
		a=url_escape_md(erdata["error"]["actual"])
		vtable+=u+ " | "+c+" | "+exp+" | "+ a+"\n"
	vtables+=[vtable]

## maybe create url comparison with parsing/verification passes



# Build the Document

result="# Results \n\n"
result+="Total number of URLs: "+str(nrurls)+"\n\n"
result+="Total number of Parsers: "+str(len(pnames))+"\n\n" 


result +="## Parser Comparison \n\n"
result +=parsertable+"\n\n"

result += "*note:*  base and relative URLs are represented as \"base<relative\" in this document for readabilty, the actually parsed inputs do not contain \"<\" \n\n"

for ptable in ptables:
	result+=ptable+"\n\n"
	
	

result +="## URL Comparison \n\n"
result += utable+"\n"

result +="## Browsers\n\n" # TODO include only if browsers present
result+=otable+"\n"
for vtable in vtables:
	result+=vtable+"\n"

result +="## Coverage Reports \n\n"

brep=""
prep=""
for name in covreps:
	if name in pnames:
		if name in ["firefox", "chromium"]:
			brep+=covreps[name]
		else:
			prep+=covreps[name]

if brep != "":
	result+="### Browsers\n\n"+brep
if prep != "":
	result+="### Stand-Alone Parsers\n\n"+prep


resfile=open( datadir+"../resultoverview.md", "w")
resfile.write(result)
resfile.close()

htmlresult=markdown.markdown(result, extensions=['extra'])

htmlhead="<!DOCTYPE html>\
<html lang=\"en\">\
\
<head>\
<meta charset=\"utf-8\">\
<link rel=\"stylesheet\" type=\"text/css\" href=\"style.css\">\
<title>URL-Fuzzing</title>\
</head>\
\
<body>\n"
htmltail="</body>\
</html>"
htmlresult=htmlresult.replace("<table>", "<table class=\"simpletable\">")
htmlresult=htmlresult.replace("<br-->", "<br>")
htmlresult=htmlresult.replace("<--br>", "<br>")
	

htmlfile=open( datadir+"../resultoverview.html", "w")
htmlfile.write(htmlhead + htmlresult +htmltail)
htmlfile.close()







