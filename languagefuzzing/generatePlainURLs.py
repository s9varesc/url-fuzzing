import os

urls=""
rel_dir='./urls/'
for filename in os.listdir(rel_dir):
    if filename != "plainURLs":
        
        f=open(rel_dir+filename)
        s = f.read()
        start=s.find("spec:\"")+6
        end1=s.find("\",\n", start)
        end2=s.find("\"}")
        end=end1 if end1>=0 else end2
        url=s[start:end]
        urls+=url+"\n"

f=open(rel_dir+'plainURLs', "w")
f.write(urls)
f.close()
