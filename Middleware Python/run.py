from dustbin import *
from connection import *


num=getNumNodes()

def getAdjecencyMatrix():
    dustlist=[]
    for i in range (num):
        v=Coordinates(i)
        db=Dustbin(v[1],v[2],v[0])
        dustlist.append(db)
        distance=[]
        for db in dustlist :
            dist=[]
            for db2 in dustlist :
                if(db.getLocnam()==db2.getLocnam()):
                    dist.append(0)
                else:
                    dist.append(db.distanceTo(db2))		
            distance.append(dist)
    
    for items in distance:
        print(items)
        
getAdjecencyMatrix()

from main import *
startWork()
