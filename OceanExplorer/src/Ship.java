import java.awt.Point;
import java.util.Random;
import java.util.Observable;
//Garrett George, SE350 Final Project

//Observer design pattern implemented
public class Ship extends Observable implements ShipInterface {
	
	OceanMap oceanMap = OceanMap.getInstance();
	Point currentLocation;
	Random rand = new Random();
	
	//randomly place player ship on ocean, declares that spot a starting Location for pirate class to follow
	public Ship(OceanMap oceanMap) {
		this.oceanMap = oceanMap;
		boolean placedShip = false;
		while(!placedShip) {
			int x = rand.nextInt(oceanMap.dimensions);
			int y = rand.nextInt(oceanMap.dimensions);
			if(oceanMap.islands[x][y] == 0) {
				placedShip = true;
				currentLocation = new Point(x,y);
				oceanMap.islands[x][y] = 2;
				break;
			}
		} 
	}
	//return current location for player ship
	public Point getShipLocation(){
	       return currentLocation; 
	} 
	
	//move functions, checks direction of movement, and if ocean, allow player ship to move to that location, and notifies pirate ships.
	public void goEast() {
		if(currentLocation.x < oceanMap.getDimensions()-1 && (oceanMap.isOcean(currentLocation.x+1, currentLocation.y)
				|| oceanMap.isTreasure(currentLocation.x+1, currentLocation.y))) {
			currentLocation.x++;
		}	
		setChanged();
		notifyObservers();
	}
	
	public void goWest() {
		if(currentLocation.x > 0 && (oceanMap.isOcean(currentLocation.x-1, currentLocation.y)
				|| oceanMap.isTreasure(currentLocation.x-1, currentLocation.y))) {
			currentLocation.x--;
		}	
		setChanged();
		notifyObservers();
	}
	
	public void goNorth() {
		if(currentLocation.y > 0 && (oceanMap.isOcean(currentLocation.x, currentLocation.y-1)
				|| oceanMap.isTreasure(currentLocation.x, currentLocation.y-1))) {
			currentLocation.y--;
		}
		setChanged();
		notifyObservers();
	}
	
	public void goSouth() {
		if(currentLocation.y < oceanMap.getDimensions()-1 && (oceanMap.isOcean(currentLocation.x, currentLocation.y+1)
				|| oceanMap.isTreasure(currentLocation.x, currentLocation.y+1))) {
			currentLocation.y++;
		}			
		setChanged();
		notifyObservers();
	}
}
