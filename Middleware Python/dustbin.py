'''
Represents nodes in the problem graph or network.
Locatin coordinates can be passed while creating the object or they
will be assigned random values.
'''
from globals import *
from connection import *
class Dustbin:
	# Good old constructor
	def __init__ (self,x,y,locnam):
			self.x = x
			self.y = y
			self.locnam=locnam

	def getX (self):
		return float(self.x)

	def getY (self):
		return float(self.y)
	def getLocnam (self):
		return self.locnam
	# Returns distance to the dustbin passed as argument
	def distanceTo (self, db):
		if self.getX()==-1 or db.getX()==-1 :
			xDis = abs(self.getX() - db.getX())
			yDis = abs(self.getY() - db.getY())
			dis = math.sqrt((xDis*xDis) + (yDis*yDis))
			return dis
		else : 
			dis=getDist(self,db)
			return dis

	# Gives string representation of the Object with coordinates
	def toString (self):
		s =  str(self.getX())+","+str(self.getY())
		return s

	# Check if cordinates have been assigned or not
	# Dusbins with (-1, -1) as coordinates are created during creation on chromosome objects
	def checkNull(self):
		if self.x == -1:
			return True
		else:
			return False
