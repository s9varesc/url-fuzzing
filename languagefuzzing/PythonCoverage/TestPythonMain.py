from urllib.parse import urlparse
from urllib.parse import urljoin


file=open("../urls/plainURLs","r")

lines=file.readlines()
urls=[]
exceptions=""
for line in lines:
    urls.append(line[:-1])
    #print(line)

for url in urls:
	(base, rel)=url.split("<")
    try:
    	if (rel != ""):
    		urljoin(base, rel)
    	else:
			urlparse(url)
    except Exception as e:
        exceptions+="\n{\"url\":\""+url+"\", \"exception\":\""+str(e)+"\"}"

f=open('PythonExceptions.txt', 'w')
f.write(exceptions)
f.close()
	

