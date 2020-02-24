from urllib.parse import urlparse


file=open("../exampleURLs/PlainURLs","r")

lines=file.readlines()
urls=[]
for line in lines:
    urls.append(line)
    print(line)

for url in urls:
    res=urlparse(url)

