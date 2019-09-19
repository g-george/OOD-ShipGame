import java.util.LinkedList;
import java.util.Random;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
//Garrett George, SE350 Final Project

public class SeaMonsterMaker implements Runnable{
	
	int scale;
	Boolean running = true;
	OceanExplorer instance;
	Random rand = new Random();
	OceanMap oceanMap = OceanMap.getInstance();
	LinkedList<SeaMonster> seaMonsterImages = new LinkedList<SeaMonster>();
	
	int krakens = 20; //variables to determine monster count
	int hydras = 20;
	
	//Method responsible for creating krakens and monsters on ocean map, class must be altered to add more
	//Multithreading method within this class to cause movement
	public SeaMonsterMaker(OceanExplorer instance, int scale) {
		
		SeaMonsterFactory hydraFactory = FactoryChooser.buildFactory("Hydra");
		SeaMonsterFactory krakenFactory = FactoryChooser.buildFactory("Kraken");
		
		this.instance = instance;
		scale = instance.scale();
		
		boolean hydraplaced;
		boolean krakenplaced;
	
		for (int i = 0; i < hydras; i++) {
			hydraplaced = false;
			while (!hydraplaced) {
				int x = rand.nextInt(oceanMap.dimensions);
				int y = rand.nextInt(oceanMap.dimensions);	
				if(oceanMap.islands[x][y] == 0) {
					seaMonsterImages.add(hydraFactory.buildMonster(x, y, scale));
					oceanMap.islands[x][y] = 5;
					hydraplaced = true;
				}	
			}
		}
		for (int i = 0; i < krakens; i++) {
			krakenplaced = false;
			while (!krakenplaced) {
				int x = rand.nextInt(oceanMap.dimensions);
				int y = rand.nextInt(oceanMap.dimensions);
				if(oceanMap.islands[x][y] == 0) {
					seaMonsterImages.add(krakenFactory.buildMonster(x, y, scale));
					oceanMap.islands[x][y] = 5;
					krakenplaced = true; 
				}
			}	
		}
	}
	
	public void add(ObservableList<Node> sceneGraph){
		for(SeaMonster seamonster: seaMonsterImages){
			ImageView image = seamonster.getImageView();
			sceneGraph.add(image);
		}
	}
	
	//For all monsters, calls monster move for their respective movement types, and checks if current location is equal to player location
	//if player does not move and monster moves to player, game ends
	@Override
	public void run() {		
		while (true) {
			try {
				Thread.sleep(900);
			} catch (InterruptedException e) {
			}
			for (SeaMonster seamonsters: seaMonsterImages){
				seamonsters.move();
			}
			if (!instance.gameOver) {
				Platform.runLater(new Runnable() {
					public void run() {
						instance.monsterBattle();
					}
				});
			}
		}
	}
}