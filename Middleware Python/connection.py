import requests
import json

stationName="chennai"
url="http://YOUR_DOMAIN_NAME/TSP%20APP/pythonapis.php"

locations=requests.post(url, data = {'station':stationName,'code':'location'}).json()
distances=requests.post(url, data = {'station':stationName,'code':'distances'}).json()
attendance=requests.post(url, data = {'station':stationName,'code':'attendance'}).json()

def convert(data):
    final_data=[]
    for item in data:
        final_data.append(list(item.values()))
    return final_data

locations=convert(locations)
distances=convert(distances)
attendance=convert(attendance)

def getNumNodes() :
	return len(locations)

def Coordinates(num) :
	length=len(locations)
	i=0
	for item in locations:
		if i==num :
			return item
		i+=1	

def getDist(db1, db2):
	n1=db1.getLocnam()
	n2= db2.getLocnam()
	for it in distances:
		if(it[0]==n1 and it[1]==n2):
			return float(it[2])

def upload(route):
    routes=convert(requests.post(url, data = {'station':stationName,'code':'route'}).json())
    
    Route="Route "+str(len(routes)+1)
    l_id=attendance[len(routes)][0]
    
    send="http://proapplication.000webhostapp.com/TSP%20APP/AddRoute.php"
    data={
        'station':stationName,
        'name': Route,
        'route': route,
        'l_id':l_id}
    msg=requests.post(send, json= data).json()
    print(msg['Msg'])

def getLaborNumber() :
	return len(attendance)

