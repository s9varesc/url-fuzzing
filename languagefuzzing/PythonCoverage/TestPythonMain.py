from urllib.parse import urlparse
from urllib.parse import urljoin


file=open("../urls/plainURLs","r", encoding='utf-8')

lines=file.readlines()
urls=[]
exceptions=""
for line in lines:
    urls.append(line[:-1])
    #print(line)

for url in urls:
    try:
        (base, rel)=url.split("<")
    except Exception as e:
        rel=""
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
    

