import os
import argparse


parser = argparse.ArgumentParser()
parser.add_argument("-dir")

args = parser.parse_args()
dir = args.dir


urls=""
rel_dir=dir+"/"
for filename in os.listdir(rel_dir):
    if filename != "plainURLs":
        
        f=open(rel_dir+filename)
        s = f.read()
        urls+=s+"\n"

f=open('./urls/plainURLs', "w")
f.write(urls)
f.close()
