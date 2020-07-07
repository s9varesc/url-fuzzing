import os

urls=""
rel_dir='./urls/plain/'
for filename in os.listdir(rel_dir):
    if filename != "plainURLs":
        
        f=open(rel_dir+filename)
        s = f.read()
        urls+=s+"\n"

f=open('./urls/plainURLs', "w")
f.write(urls)
f.close()
