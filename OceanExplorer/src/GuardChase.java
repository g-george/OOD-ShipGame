import java.awt.Point;
import java.util.Random;
//Garrett George, SE350 Final Project

public class GuardChase implements ChaseStrategy {
	
	//Guard Chase strategy - When this strategy is set, pirate ships move towards treasure island and randomly patrol around it.
	//While not near the island, movePirate calls guardTreasureIsland until distance threshold met, then switches to patrolIsland.
	//If ship does not move, assumed island is in path, and calls moveAroundIsland
	
	Random rand = new Random();
	OceanMap oceanMap = OceanMap.getInstance();
	Point treasureIsland = oceanMap.getSecret();
	boolean moved;
	
	//calls 3 move functions until ship is moved
	public void movePirate(Point pirateLocation, Point shipLocation) {
		moved = false;
		if (pirateLocation.x >= treasureIsland.x+3 || pirateLocation.y >= treasureIsland.y+3 || 
			pirateLocation.x <= treasureIsland.x-3 || pirateLocation.y <= treasureIsland.y-3) {
			moved = guardTreasureIsland(pirateLocation, treasureIsland);
		}
		else {
			moved = patrolIsland (pirateLocation, shipLocation);
		}
		if (moved == false) { 
			moveAroundIsland(pirateLocation); 
		}
	}
			
	//uses random number generator to move a random direction.  Attempts this 8 times, if it does not select a viable move
	//it is assumed islands are near the treasure island and moveAroundIsland gets called to assist movement.
	public boolean patrolIsland (Point pirateLocation, Point shipLocation)  {
		int count = 0;
		moved = false;
		while (count < 8) {
			int z = rand.nextInt(4);
			if (z==0 && oceanMap.isOcean(pirateLocation.x+1, pirateLocation.y)) {
					pirateLocation.x++;
					moved = true; count = 7;
				}
			if (z==1 && oceanMap.isOcean(pirateLocation.x-1, pirateLocation.y)) {
					pirateLocation.x--;
					moved = true; count = 7;
				}
			if (z==2 && oceanMap.isOcean(pirateLocation.x, pirateLocation.y+1)) {
					pirateLocation.y++;
					moved = true; count = 7;
				}
			if (z==3 && oceanMap.isOcean(pirateLocation.x, pirateLocation.y-1)) {
					pirateLocation.y--;
					moved = true; count = 7;
				}
			count++;
			}
		return moved;
	}
	
	//works much like smart chase, moves to treasure island until within range to switch to patrolIsland
	//uses random number generator to give ship a random zigzag movement to destination
	public boolean guardTreasureIsland(Point pirateLocation, Point treasureIsland) {
		moved = false;
		int z = rand.nextInt(2);
		if (z==0) {
			if (pirateLocation.y < treasureIsland.y && oceanMap.isOcean(pirateLocation.x, pirateLocation.y+1)) {
				pirateLocation.y++; moved = true;
			}
			else if (pirateLocation.x > treasureIsland.x && oceanMap.isOcean(pirateLocation.x-1, pirateLocation.y)) {
				pirateLocation.x--; moved = true;
			}
			else if (pirateLocation.y > treasureIsland.y && oceanMap.isOcean(pirateLocation.x, pirateLocation.y-1)) {
				pirateLocation.y--; moved = true;
			}
			else if (pirateLocation.x < treasureIsland.x && oceanMap.isOcean(pirateLocation.x+1, pirateLocation.y)) {
				pirateLocation.x++; moved = true;
			}
		}
		if (z==1) {
			if (pirateLocation.x < treasureIsland.x && oceanMap.isOcean(pirateLocation.x+1, pirateLocation.y)) {
				pirateLocation.x++; moved = true;
			}
			else if (pirateLocation.y > treasureIsland.y && oceanMap.isOcean(pirateLocation.x, pirateLocation.y-1)) {
				pirateLocation.y--; moved = true;
			}
			else if (pirateLocation.x > treasureIsland.x && oceanMap.isOcean(pirateLocation.x-1, pirateLocation.y)) {
				pirateLocation.x--; moved = true;
			}
			else if (pirateLocation.y < treasureIsland.y && oceanMap.isOcean(pirateLocation.x, pirateLocation.y+1)) {
				pirateLocation.y++; moved = true;
			}
		}
		return moved;
	}
	
	//function that assists when ship does not move from other functions, attempts to move around obstruction in two random directions
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
