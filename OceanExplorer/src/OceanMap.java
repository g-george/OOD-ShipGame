import java.awt.Point;
import java.util.Random;

//Garrett George, SE350 Final Project

//singleton design method to create only one instance of ocean map
public class OceanMap {
	
	static OceanMap oceanMap;
	
	int[][] islands; //0 is water, 1 are islands, 2 are ships, 3 are borders, 4 is treasure island, 5 are sea monsters.
	int treasureX, treasureY, dimensions, islandCount;
	
	Random rand = new Random();
	
	   //Constructor, dimension is only variable that should be changed to alter grid size
	   private OceanMap() {
		   this.dimensions =  40;
		   this.islandCount = dimensions*4;
		   createGrid();
		   addBorder();
		   placeIslands();
	   }
	   
	   //Set values of grid zones to 0, ocean = 0.
	   private void createGrid() {
		   islands = new int[dimensions][dimensions];
		   for(int x = 0; x < dimensions; x++) 
			   for (int y = 0; y < dimensions; y++) 
				   islands[x][y] = 0;
	   }
	   
	   //Creates border on map edges, borders = 3.
	   private void addBorder() {
		   for(int i = 0; i < dimensions; i++) {
				   islands[0][i] = 3;
				   islands[i][0] = 3;
				   islands[dimensions-1][i] = 3;
				   islands[i][dimensions-1] = 3;
			   }
	   }
	   
	   //inserts # of islands declared in this instance
	   private void placeIslands() {
		   int size = dimensions*dimensions;
		   int islandsToPlace = islandCount;
		   int treasureIsland = islandCount;
		   
		   //If there's more islands than zones, creates maximum number of islands 
		   //and enough ocean zones for ship/sea monster placement.
		   if (islandsToPlace >= size - 10) 
			   islandsToPlace = size - 10;
		   
		   //Random island placement, first placement is treasure island.
		   while(islandsToPlace > 0) {
			   int x = rand.nextInt(dimensions);
			   int y = rand.nextInt(dimensions);
			   if(islands[x][y] == 0) {
				   islands[x][y] = 1;
				   if (islandsToPlace == treasureIsland) {
					   islands[x][y] = 4;
					   this.treasureX = x;
					   this.treasureY = y;
				   }
				   islandsToPlace--;
			   }
		   }
	   }
	   
		public int getDimensions() {
			return dimensions;
		}
		
		public int[][] getMap() {
			return islands;
		}
		
		//determines which island is treasure island, known by pirate guardChase strategy
		public Point getSecret() {
			Point location = new Point(treasureX,treasureY);
			return location;
		
		}
		
		//Check if tile is treasure island. Player can move onto this zone, but enemies cannot.
		public boolean isTreasure(int x, int y) {
			if(islands[x][y] == 4)
				return true;
			else {
				return false;
			}
		}
		
		//Check if tile is an ocean zone, used for player, pirate and sea monster movement.
		public boolean isOcean(int x, int y) {
			if(islands[x][y] == 0)
				return true;
			else {
				return false;
			}
		}
		
		//function that removes all ship/monster starting locations and converts them back into default ocean tiles
		public void removeStartSpots() {
			for(int x = 0; x < dimensions; x++) 
				for (int y = 0; y < dimensions; y++) 
					if (islands[x][y] == 2 || islands[x][y] == 5)
						islands[x][y] = 0;
		}

		//check if tile is an island zone, used for pirate movement
		public boolean isIsland(int x, int y) {
			if(islands[x][y] == 1)
				return true;
			else {
				return false;
			}
		}
		
		//singleton instance
		public static OceanMap getInstance() {
			if(oceanMap == null) {
				oceanMap = new OceanMap();
			}
			return oceanMap;
	}
}
		
		