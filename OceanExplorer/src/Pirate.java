import java.awt.Point;
import java.util.Random;

import java.util.Observable;
import java.util.Observer;

//Garrett George, SE350 Final Project


//Observer design method implemented.
public class Pirate implements Observer, ShipInterface{
	
	OceanMap oceanMap = OceanMap.getInstance();
	Random rand = new Random();
	boolean moved = false;
	Point shipLocation;
	Point pirateLocation;
	
	ChaseStrategy chaseStrategy;

	//randomly place pirate ship on ocean tiles only, ships start with default smart chase
	public Pirate(OceanMap oceanMap) {
		setStrategy(new SmartChase());
		this.oceanMap = oceanMap;
		boolean placedShip = false;
		while(!placedShip) {
			int x = rand.nextInt(oceanMap.dimensions);
			int y = rand.nextInt(oceanMap.dimensions);
			if(oceanMap.islands[x][y] == 0) {
				pirateLocation = new Point(x,y);
				oceanMap.islands[x][y] = 2;
				placedShip = true;
				break;
			}
		} 
	}

	//set strategy for chase strategy
	public void setStrategy(ChaseStrategy strategy) {
		chaseStrategy = strategy;
	}
	
	//return pirate ship location
	public Point getPirateLocation(){
	       return this.pirateLocation; 
	} 
	
	//method used to determine player location
	public Point getShipLocation(){
	       return shipLocation;
	} 

	//when ship class performs action, pirate ship moves
	public void update(Observable o, Object arg) {
		if(o instanceof Ship){
			shipLocation = ((Ship)o).getShipLocation();
			chaseStrategy.movePirate(pirateLocation,shipLocation);
		}
	}
}