import os

for filename in os.listdir('./testuri/uri'):
    #print filename
    f=open('./testuri/uri/'+filename)
    s = f.read()
    s = s.replace('require \'uri/', 'require_relative \'./')
    s = s.replace('require \"uri/', 'require_relative \"./')
    #print "replaced in " + filename
    f=open('./testuri/uri/'+filename, "w")
    f.write(s)
    f.close()

filename='uri.rb'
#print filename
f=open('./testuri/'+filename)
s = f.read()
s = s.replace('require \'uri', 'require_relative \'./uri')
#print "replaced in " + filename
f=open('./testuri/'+filename, "w")
f.write(s)
f.close()
