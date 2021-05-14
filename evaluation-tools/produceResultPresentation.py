import argparse
import os
import subprocess
import json
import markdown
from bs4 import BeautifulSoup


# links to the coverage reports
covreps={}
covreps["chromium"]="\n#### Chromium\n\n[Overview](./chromium/report.html)\n\n[Source File Report](./chromium/url_parse.cc.html)\n\n"
covreps["firefox"]="\n#### Firefox\n\n[Overview](./firefox/index.html)\n\n[Source File Report](./firefox/nsURLParsers.cpp.gcov.html)\n\n" 
covreps["c"]="\n#### C\n\n[Overview](./C/index.html)\n\n[Source File Report](./C/src/UriParse.c.gcov.html)\n\n"
covreps["cpp"]="\n#### C\\+\\+\n\n[Overview](./Cpp/index.html)\n\n[Source File Report](./Cpp/src/URI.cpp.gcov.html)\n\n"
covreps["go"]="\n#### GO\n\n[Source File Report](./Go/index.html)\n\n"
covreps["java"]="\n#### Java\n\n[Overview](./Java/index.html)\n\n[Source File Report](./Java/java/net/URL.html)\n\n" 
covreps["javaScripturijs"]="\n#### JavaScript urijs\n\n[Overview](./JavaScript/urijs/index.html)\n\n[Source File Report](./JavaScript/urijs/URI.js.html)\n\n"
covreps["javaScriptwhatwg-url"]="\n#### JavaScript whatwg\\-url\n\n[Overview](./JavaScript/whatwg-url/index.html)\n\n[Source File Report](./JavaScript/whatwg-url/whatwg-url/dist/url-state-machine.js.html)\n\n"
covreps["php"]="\n#### PHP\n\n[Overview](./PHP/index.html)\n\n[Source File Report](./PHP/UriString.php.html)\n\n"
covreps["python"]="\n#### Python\n\n[Overview](./Python/index.html)\n\n[Source File Report](./Python/_usr_lib_python3_6_urllib_parse_py.html)\n\n"
covreps["ruby"]="\n#### Ruby\n\n[Overview](./Ruby/index.html#_AllFiles)\n\n"

source_reports={}
source_reports["chromium"]="chromium/report.html"
source_reports["firefox"]="firefox/nsURLParsers.cpp.gcov.html" 
source_reports["c"]="C/src/UriParse.c.gcov.html"
source_reports["cpp"]="Cpp/src/URI.cpp.gcov.html"
source_reports["go"]="Go/index.html"
source_reports["java"]="Java/java/net/URL.html" 
source_reports["javascripturijs"]="JavaScript/urijs/URI.js.html"
source_reports["javascriptwhatwg-url"]="JavaScript/whatwg-url/whatwg-url/dist/url-state-machine.js.html"
source_reports["php"]="PHP/index.html"
source_reports["python"]="Python/_usr_lib_python3_6_urllib_parse_py.html"
source_reports["ruby"]="Ruby/index.html"


max_inputs_prettify=10000	#if the results contain more inputs, html files will not be styled as much

def extractCoverage(parser, parsed_report):
	if parser=="firefox" or parser=="c" or parser=="cpp":
		return extractLCOV(parsed_report)
	elif parser=="chromium":
		tbody=parsed_report.find("tbody").find("tr", attrs={"class", "light-row"})
		td=tbody.find_all("td")
		percent=td[1].find("pre")
		cov=float((percent.text).split("%")[0])
		return cov
	elif parser=="go":
		op=parsed_report.find("option")
		cov=float(op.text.split("(")[1].split("%")[0])
		return cov
	elif parser=="java":
		tds=parsed_report.find_all("td", attrs={"class", "reportValue"})
		cov=float(tds[3].find("b").text)
		return cov
	elif parser=="javascripturijs" or parser=="javascriptwhatwg-url":
		return extractNYC(parsed_report)
	elif parser=="php":
		tbody=parsed_report.find("tbody")
		cov=float(tbody.find("div").find("span").text.split("%")[0])
		return cov
	elif parser=="python":
		cov=float(parsed_report.find("title").text.split(":")[1][:-1])
		return cov
	elif parser=="ruby":
		h=parsed_report.find("h2").find("span", attrs={"class", "covered_percent"})
		span=h.find("span").text.split("%")[0]
		cov=float(span)
		return cov
	else:
		return 0

def extractNYC(parsed_report):
	maindiv=parsed_report.find("div", attrs={"class", "pad1"})
	divs=maindiv.find_all("div")

	for div in divs:
		qspan=div.find("span",attrs={"class", "quiet"})
		if qspan.text == "Lines":
			cov=div.find("span",attrs={"class", "strong"})
			return float(cov.text[:-2])

def extractLCOV(parsed_report):
	table=parsed_report.find("table")
	table_data=table.find_all("tr")

	for row in table_data:
	#print(row)
		if row.find("tr") is not None:
		#print(row.find_all("tr"))
			pass
		else:
			headers=row.find_all("td", attrs={"class", "headerItem"})
			thisrow=False
			for header in headers:
				if header.text=="Lines:":
					thisrow=True
			if thisrow:
				coverage=0.0
				attr=["headerCovTableEntryHi", "headerCovTableEntryMed", "headerCovTableEntryLo"]
				for att in attr:
					content=row.find("td", attrs={"class",att })
					if content is not None:
						coverage+=float(content.text[:-1])
				return coverage
			



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
	res=res.replace("<", " < ")
	return " "+escbt+" "+res+" "+escbt+" "


parser = argparse.ArgumentParser()
parser.add_argument("-data")
args = parser.parse_args()
datadir = args.data
if datadir[-1:]!="/":datadir+="/"

pranking={}
uranking={}
eranking={} 
# read json file
for file in os.listdir(datadir):
	if "parser" in file:
		with open(datadir+"/"+file, encoding='utf-8') as f:
			pranking=f.read()
	if "url" in file:
		with open(datadir+"/"+file, encoding='utf-8') as f:
			uranking=f.read()
	if "error" in file: 
		with open(datadir+"/"+file, encoding='utf-8') as f:
			eranking=f.read()


# multiple rankings: focus on parsers: {parsername: { errorcount: , nrerrtypes: ,
#						 							errtypes: {errtype:[url, url,...]} }}
#					focus on urls: {url: [parsers]} 
#					browsers including exceptions and errors   


# prepare data structures
ptuples=[]
ptables=[] #list of md tables with heading
ptableheader=" Exception Type | URLs \n --- | --- "

parsertable=" Parsername | Number of Exceptions | Number of Different Exceptions | Code Coverage \n --- | --- | --- | ---\n"

parserdata=json.loads(pranking, strict=False)


# extract coverages to put in table
result_dir=datadir+"../"
coverages={}
for parsername in parserdata:
	html=""
	with open(result_dir+source_reports[parsername.lower()], encoding='utf-8') as f:
			html=f.read()

	parsed_report=BeautifulSoup(html, "lxml")
	
	cov=extractCoverage(parsername.lower(), parsed_report)
	coverages[parsername]=cov


# create table representation of the parser results
for pname in parserdata:
	edata=parserdata[pname]
	ptuple=(pname, edata["errorcount"], edata["nrerrtypes"], coverages[pname]) 
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
for (pn, en, den, cov) in sortedptuples:
	parsertable+=pn +" | "+ str(en) +" | "+ str(den) + " | " +str(cov)+"% \n"
	pnames+=[pn]


# create table representation of URLs
urldata=json.loads(uranking, strict=False)


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
errdata=json.loads(eranking, strict=False)

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

result +="## Browsers\n\n" 
result+=otable+"\n"

result += "[full browser comparison](./browseroverview.html)\n\n"
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

if nrurls < max_inputs_prettify:
	resfile=open( datadir+"../resultoverview.md", "w", encoding='utf-8')
	resfile.write(result)
	resfile.close()
else:
	print("skip writing md file")


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
if nrurls < max_inputs_prettify:
	htmlresult=htmlresult.replace("<table>", "<table class=\"simpletable\">")
	htmlresult=htmlresult.replace("<br-->", "<br>")
	htmlresult=htmlresult.replace("<--br>", "<br>")
else:
	print("skip html prettify due to large nr of inputs ("+str(nrurls)+")")

print("writing resultoverview")
htmlfile=open( datadir+"../resultoverview.html", "w", encoding='utf-8')
htmlfile.write(htmlhead + htmlresult +htmltail)
htmlfile.close()



# produce colorful browser table with all urls

#url | firefox | chromium

# green for pass, yellow for component, red for parsing, purple for whatwg fail

# create mappings of url : result for each browser



bres=[]
for bname in errdata:
	elist=errdata[bname]
	blist={}
	blist["name"]=bname
	for erdata in elist:   
		u=erdata["url"]
		c=escape_md(erdata["error"]["component"])
		exp=url_escape_md(erdata["error"]["expected"])
		a=url_escape_md(erdata["error"]["actual"])
		message="STYLEC "+c+ " is \""+a[1:-1]+ "\" != \""+ exp[1:-1]+"\""

		blist[u]=message
	bres+=[blist]

if len(bres)>=1:
	for url in urldata: 
		parsers=urldata[url]	
		for b in bres:
			name=b["name"]
			if parsers:
				if name in parsers:
					#place parsing failure in results
					m="STYLEF"
					message=" "
					#find error message in parserdata
					pd=parserdata[name]["errtypes"]
					for et in pd:
						if url in pd[et]:
							message+=et
					b[url]=m+message



			#place parsing success in results if there is no entry for a failure
			try:
				comp=b[url]
			except KeyError:
				comp=""
			if comp=="":
				b[url]="STYLEP PASS"



bsize=len(bres)
eqsucc=0
eqfail=0
allcomp=0
whatfail=0
whatnr=0


bcomptable="URLs "
bcomphelp="--- "

bcount=[]

for i in range(0, bsize):
	bcount+=[[]]
	bcomptable+=" | "+ bres[i]["name"]
	bcomphelp+="| ---"
	bcount[i]=[0,0,0,0]


bcomptable+=" \n"+bcomphelp+"\n"


styleinfo=True
if nrurls > max_inputs_prettify:
	styleinfo=False

for url in urldata:
	if bsize <1:
		break
	nrsucc=0
	nrfail=0
	nrcomp=0

	parsers=urldata[url]
	uline=""
	if "JavaScriptwhatwg-url" in parsers:
		uline="STYLEW "
		whatnr+=1
	uline += url_escape_md(url)
	for i in range(0, bsize): # calculate more statistics here
		br=bres[i]
		if styleinfo:
			uline+= " | "+ br[url]
		else:
			uline+= " | "+ br[url][6:]
		# count results for statistics
		mess=br[url]
		if mess[:6]=="STYLEP":
			nrsucc+=1
			bcount[i][0]+=1
		if mess[:6]=="STYLEF":
			nrfail+=1
			bcount[i][1]+=1
			if uline[:6]=="STYLEW":
				bcount[i][2]+=1
		if mess[:6]=="STYLEC":
			nrcomp+=1
			bcount[i][3]+=1

	#save counted results
	
	if nrsucc==bsize:
		eqsucc+=1
	if nrfail==bsize:
		eqfail+=1
		if uline[:6]=="STYLEW":
			whatfail+=1
	if nrcomp==bsize:
		allcomp+=1


	if not styleinfo:
		if uline[:6]=="STYLEW":
			uline=uline[6:]
	uline+="\n"
	bcomptable+=uline




crbsucc="|"
crbfail="|"
crbwf="|"
crbcf="|"
crbhead="|   | equal results |"
crbhhelp="\n|---|---|"
crbcov=""
for i in range(0, bsize):
	crbhead+= bres[i]["name"]+ " | "
	crbhhelp+="---|"
	crbsucc+=" ("+str(bcount[i][0])+"/"+str(nrurls)+")|"
	crbfail+=" ("+str(bcount[i][1])+"/"+str(nrurls)+")|"
	crbwf+=" ("+str(bcount[i][2])+"/"+str(whatnr)+")|"
	crbcf+=" ("+str(bcount[i][3])+"/"+str(nrurls)+")|"
	crbcov+=" "+str(coverages[bres[i]["name"]])+"% |"	#TODO make sure names match		
#use counted results
crtable=crbhead+crbhhelp+"\n"
crtable+="| parsed successfully | ("+str(eqsucc)+"/"+str(nrurls)+")"+crbsucc+"\n"
crtable+="| rejected | ("+str(eqfail)+"/"+str(nrurls)+")"+crbfail+"\n"
crtable+="| also rejected by whatwg | ("+str(whatfail)+"/"+str(whatnr)+")"+crbwf+"\n"
crtable+="| component errors | ("+str(allcomp)+"/"+str(nrurls)+")"+crbcf+"\n"
crtable+="| code coverage |   | "+crbcov+"\n"
eqres=allcomp+ eqsucc + eqfail
crtable+="| overall equal results | ("+str(eqres)+"/"+str(nrurls)+")|\n"
crtable+="| overall unequal results | ("+str(nrurls-eqres)+"/"+str(nrurls)+")|\n\n"


bov="# Combined Results\n\n"+crtable
bov+= "*note:*  due to different component names the table above does not consider which component caused the error, but the full comparison table below contains this information\n\n"





htmlresult1=markdown.markdown(bov, extensions=['extra'])
htmlresult1=htmlresult1.replace("<table>", "<table class=\"simpletable\">")
htmlresult2=markdown.markdown(bcomptable, extensions=['extra'])


htmlhead="<!DOCTYPE html>\
<html lang=\"en\">\
\
<head>\
<meta charset=\"utf-8\">\
<link rel=\"stylesheet\" type=\"text/css\" href=\"style.css\">\
<title>Browser Results</title>\
</head>\
\
<body>\n"
htmltail="</body>\
</html>"

pagestart="<h1>Full Browser Comparison</h1>\n"
pagestart+="<table class=\"legendtable\"><thead><tr></tr></thead><tbody>"
pagestart+="<tr><td> Legend:</td><td class=\"wgfail\">Parsing error in whatwg-url parser</td>"
pagestart+="<td class=\"pfail\">Parsing error</td>"
pagestart+="<td class=\"cfail\">Component is \"content\" != \"expected\"</td>"
pagestart+="<td class=\"psucc\">Success</td></tr></tbody></table>\n"

htmlresult=htmlresult1 + pagestart + htmlresult2

if nrurls < max_inputs_prettify:
	htmlresult=htmlresult.replace("<table>", "<table class=\"simpletable\">")
	htmlresult=htmlresult.replace("<br-->", "<br>")
	htmlresult=htmlresult.replace("<--br>", "<br>")

	htmlresult=htmlresult.replace("<td>STYLEP", "<td class=\"psucc\">")
	htmlresult=htmlresult.replace("<td>STYLEF", "<td class=\"pfail\">")
	htmlresult=htmlresult.replace("<td>STYLEC", "<td class=\"cfail\">")
	htmlresult=htmlresult.replace("<td>STYLEW", "<td class=\"wgfail\">")
else :
	print("skip prettify detailed results due to large nr of inputs("+str(nrurls)+")")

	

print("writing browseroverview")
htmlfile=open( datadir+"../browseroverview.html", "w", encoding='utf-8')
htmlfile.write(htmlhead + htmlresult +htmltail)
htmlfile.close()






				
			

		





