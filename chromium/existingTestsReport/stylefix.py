

import os

for filename in os.listdir('./'):
    #print filename
    f=open('./'+filename)
    s = f.read()
    if filename!="stylefix.py":
        s = s.replace('../../../../../../../style.css', './style.css')
    #print "replaced in " + filename
    f=open('./'+filename, "w")
    f.write(s)
    f.close()
