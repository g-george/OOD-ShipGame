import java.awt.Point;
import java.util.Random;
//Garrett George, SE350 Final Project
public class SmartChase implements ChaseStrategy {
	
	//default chase strategy set for pirate ships
	//moves pirate towards players ships, if island in way, calls moveAroundIsland function to assist
	//both move methods use random numbers to create variance for all pirate ship movements
	//as well as occasional not allow pirate ship movements so pursuits are not so relentless for the player.
	
	Random rand = new Random();
	OceanMap oceanMap = OceanMap.getInstance();
	
	boolean moved;
	
	//random number generator to sometimes prefer x over y and vice versa for determined direction towards player ship
	public void movePirate(Point pirateLocation, Point shipLocation) {
		moved = false;
		int z = rand.nextInt(2);
			if (z==0) {
				if (pirateLocation.x < shipLocation.x && oceanMap.isOcean(pirateLocation.x+1, pirateLocation.y)) {
					pirateLocation.x++;
					moved = true;
				}
				if (pirateLocation.x > shipLocation.x && oceanMap.isOcean(pirateLocation.x-1, pirateLocation.y)) {
					pirateLocation.x--;
					moved = true;
				}
			}
			else  {
				if (pirateLocation.y < shipLocation.y && oceanMap.isOcean(pirateLocation.x, pirateLocation.y+1)) {
					pirateLocation.y++; 
					moved = true;
				}
				if (pirateLocation.y > shipLocation.y && oceanMap.isOcean(pirateLocation.x, pirateLocation.y-1)) {
					pirateLocation.y--;
					moved = true;
				}
			}
			if (moved == false) { moveAroundIsland(pirateLocation); }
		}
	
	//function use to move ships around islands, uses random binary number to prevent ship from looping between two locations
	//will move pirate ships around island if player ship doesn't move (such as attempts to move towards island but already next to)
	public void moveAroundIsland(Point pirateLocation) {
		int w = rand.nextInt(2);
		//if statement to check if new pirateLocation is within grid
		if (pirateLocation.x+1 < oceanMap.dimensions && pirateLocation.y+1 < oceanMap.dimensions && pirateLocation.x-1 > 0 && pirateLocation.y-1 > 0) {
			if (w == 0) {
				if( oceanMap.isIsland(pirateLocation.x+1, pirateLocation.y) && oceanMap.isOcean(pirateLocation.x, pirateLocation.y+1)) { 
					pirateLocation.y++; }
				else if( oceanMap.isIsland(pirateLocation.x-1, pirateLocation.y) && oceanMap.isOcean(pirateLocation.x, pirateLocation.y-1)) {
					pirateLocation.y--; }
				else if( oceanMap.isIsland(pirateLocation.x, pirateLocation.y+1) && oceanMap.isOcean(pirateLocation.x+1, pirateLocation.y)) {
					pirateLocation.x++; }
				else if(oceanMap.isIsland(pirateLocation.x, pirateLocation.y-1) && oceanMap.isOcean(pirateLocation.x-1, pirateLocation.y)) {
					pirateLocation.x--; } 
			}
			if (w == 1) {
				if(oceanMap.isIsland(pirateLocation.x+1, pirateLocation.y) && oceanMap.isOcean(pirateLocation.x, pirateLocation.y-1)) { 
					pirateLocation.y--; }
				else if(oceanMap.isIsland(pirateLocation.x-1, pirateLocation.y) && oceanMap.isOcean(pirateLocation.x, pirateLocation.y+1)) {
					pirateLocation.y++; }
				else if(oceanMap.isIsland(pirateLocation.x, pirateLocation.y+1) && oceanMap.isOcean(pirateLocation.x-1, pirateLocation.y)) {
					pirateLocation.x--; }
				else if(oceanMap.isIsland(pirateLocation.x, pirateLocation.y-1) && oceanMap.isOcean(pirateLocation.x+1, pirateLocation.y)) {
					pirateLocation.x++; }
			} 
		}
	}
}
