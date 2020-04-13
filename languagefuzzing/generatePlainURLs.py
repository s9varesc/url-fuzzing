import os

urls=""
rel_dir='./urls/'
for filename in os.listdir(rel_dir):
    if filename != "plainURLs":
        print "opening" + filename
        f=open(rel_dir+filename)
        s = f.read()
        start=s.find("spec: \"")+7
        end=s.find("\",\n", start)
        url=s[start:end]
        urls+=url+"\n"

f=open(rel_dir+'plainURLs', "w")
f.write(urls)
f.close()
