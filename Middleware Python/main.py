from galogic import *
import matplotlib.pyplot as plt
import progressbar
from connection import *
pbar = progressbar.ProgressBar()

def getRoutes(routes):
	crt=1
	l=len(routes)
	tempindex=0
	for i in range (l):
		if (routes[i]=='\n'):
			print ('Route '+str(crt)+': '+routes[tempindex:i])
			upload(routes[tempindex:i])
			tempindex=i+1
			crt+=1
			print('\n')

def startWork():
	# Add Dustbins
    for i in range(getNumNodes()):
    	val=Coordinates(i)
    	RouteManager.addDustbin(Dustbin(val[1],val[2],val[0]))
    
    yaxis=[]
    xaxis=[]
    pop = Population(populationSize, True)
    globalRoute = pop.getFittest()
    print ('Initial minimum distance: ' + str(globalRoute.getDistance()))
    
    #this is spartan
    for i in pbar(range(numGenerations)):
        pop = GA.evolvePopulation(pop)
        localRoute = pop.getFittest()
        yaxis.append(localRoute.getDistance())
        xaxis.append(i)
        if globalRoute.getDistance() > localRoute.getDistance():
            globalRoute = localRoute
        yaxis.append(localRoute.getDistance())
        xaxis.append(i)
    
    print ('Global minimum distance: ' + str(globalRoute.getDistance()))
    #print ('Final Route: ' + globalRoute.toString())
    print ('\nGetting individual routes')
    getRoutes(globalRoute.toString())
    fig = plt.figure()
    
    plt.plot(xaxis, yaxis, 'r-')
    plt.show()