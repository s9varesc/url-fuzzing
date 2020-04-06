from urlparse import urlparse


file=open("../urls/plainURLs","r")

lines=file.readlines()
urls=[]
for line in lines:
    urls.append(line)
    print(line)

for url in urls:
    urlparse(url)

