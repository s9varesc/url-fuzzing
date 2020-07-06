from urlparse import urlparse


file=open("../urls/plainURLs","r")

lines=file.readlines()
urls=[]
exceptions=""
for line in lines:
    urls.append(line[:-1])
    #print(line)

for url in urls:
    try:
       urlparse(url)
    except Exception as e:
        exceptions+="\n{ url:\""+url+"\",\n exception:\""+str(e)+"\"},"

f=open('PythonExceptions.txt', 'w')
f.write(exceptions[:-1])
f.close()
	

