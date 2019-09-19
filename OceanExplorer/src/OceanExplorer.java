import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import java.util.LinkedList;
import java.util.Random;

//Garrett George, SE350 Final Project
//Main class for Treasure Island Game, responsible for drawing most images, setting pirate ship strategy, adding monsters from 
//monster factory, game menu, player keyboard input handling, win/lose conditions, and screen movement

public class OceanExplorer extends Application {
	
	int[][] islandMap;
	Pane root, parentRoot;

	final int scale = 100;//all image sizes tied to this variable, can handle slight alterations down to 50 without too much notice on game play
	
	private final int windowSize = 9; //windowSize must be odd or else player ship will not appear in center of screen//
	private final int mapScale = scale*windowSize; 
	
	boolean gameOver = false;
	int moveCount, size; //counts number of keys pressed by player
	
	private GameMusic music = new GameMusic(); //music class instance
	
	Label moves,type;
	
	ImageView shipImageView, oceanImageView, islandImageView, borderImageView, 
	pirate1ImageView, pirate2ImageView, ghostImageView, fleetImageView, gameOverImageView, treasureImageView;
	
	LinkedList<ShipInterface> pirates = new LinkedList<ShipInterface>();
	
	protected static OceanMap oceanMap = OceanMap.getInstance();
	Scene scene;
	
	Ship ship;
	Pirate pirate1, pirate2, ghost;
	
	Random rand = new Random();
	
	boolean mode = true; //mode is the chase strategy set for pirate ships, 1 or 2

	SeaMonsterMaker seaMonsterMaker;
	Thread seaMonsterThread;
	
	
	
	
	//Start function for map loading, image loading, setting observers, new game button creation
	//event handling, and pane creation
	public void start(Stage oceanStage) throws Exception {
		 
		islandMap = oceanMap.getMap();
		this.size = oceanMap.getDimensions();
		
		parentRoot= new AnchorPane();
		root = new AnchorPane();
		parentRoot.getChildren().add(root);
		
		drawMap();
		bigIsland();
		
		//add ships/observers, as well as set strategy for special ghost ship.
		ship = new Ship(oceanMap);
		pirate1 = new Pirate(oceanMap);
		pirate2 = new Pirate(oceanMap);
		ghost = new Pirate(oceanMap);
		ship.addObserver(pirate1);
		ship.addObserver(pirate2);
		ship.addObserver(ghost);
		pirates.add(pirate1);
		pirates.add(pirate2);
		pirates.add(ghost);
		ghost.setStrategy(new GuardChase());
		
		loadImages(); //method to call a number of image related load functions
		
		//add in monsters from factory
		seaMonsterMaker = new SeaMonsterMaker(this, scale);
		seaMonsterMaker.add(root.getChildren());
		seaMonsterThread = new Thread(seaMonsterMaker);
		seaMonsterThread.start();
		
		//Button to reset game and start over from beginning, does not create new ocean map class due to single instance design
		Button newGame = new Button("New Game");
		newGame.setTranslateX(12);
		newGame.setTranslateY(mapScale+8);
		parentRoot.getChildren().add(newGame);
		
		newGame.setOnAction(new EventHandler<ActionEvent>() {
		    @Override 
		    public void handle(ActionEvent e) {
		        try { 
		        	gameOver = false;
		        	moveCount = 0;
		        	music.stopMusic();
		        	start(oceanStage);
				} catch (Exception E) {}	
		    }
		});
		
		scene = new Scene(parentRoot,mapScale,mapScale+scale);
		oceanStage.setTitle("The Search For Treasure Island");
		oceanStage.setScene(scene);
		oceanStage.show();
		oceanMap.removeStartSpots(); //method that switches all tiles back to 0 except for island and border tiles.
		music.playMusic();  //plays game music
		setSail();
		
	}

	
	
	//bottom menu, locked in place on pane, contains new game button, move count, and strategy change button
	private void loadMenu() {
		
		//Menu background
		Rectangle rect = new Rectangle(0,mapScale,mapScale,scale);
		rect.setFill(new ImagePattern(new Image("menu.png"), 0, 0, 1, 1, true ));
		parentRoot.getChildren().add(rect);
		
		//move counter
		moves = new Label("Total Moves: " + moveCount);
		moves.setTranslateX(320 );
		moves.setTranslateY(mapScale+12);
		parentRoot.getChildren().add(moves);
		
		//Switches all ships to guardMode(mode 2), or switch non-ghost ships back to smartChase (mode 1)
		Button mode = new Button("Mode 1");
		mode.setTranslateX(235);
		mode.setTranslateY(mapScale+8);
		parentRoot.getChildren().add(mode);
		
		type = new Label("Ship Chase Type:");
		type.setTextFill(Color.BLACK);
		type.setTranslateX(115);
		type.setTranslateY(mapScale+12);
		parentRoot.getChildren().add(type);
		
		mode.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent d) {
				try {
				changeMode(mode);
				} catch (Exception D) {}
			}
		});
	}
	
	//counts player ship moves and resets after each game and stops counting if player ship touches pirate ship or monster
	public void moveCount() {
		if (gameOver == false) { moveCount++; }
		moves.setText("Total Moves: " + moveCount);
	}
	
	//button that switches chase strategies for pirates mid game
	public void changeMode(Button button) {
		mode = !mode;
		for(ShipInterface ships : pirates) {
			((Pirate) ships).setStrategy(mode ? new SmartChase() : new GuardChase());
		}
		button.setText(mode ? "Mode 1" : "Mode 2");
	}
	

	
	//Load Player Ship
	private void loadShipImage() {
		Image shipImage = new Image("ship.png",scale,scale,true,true);
		shipImageView = new ImageView(shipImage);
		shipImageView.setX(ship.getShipLocation().x * scale); 
		shipImageView.setY(ship.getShipLocation().y * scale);
		root.getChildren().add(shipImageView);
		centerScreen(ship.getShipLocation().x,ship.getShipLocation().y);
	}
	//Load Pirate Ship 1
	private void loadPirate1Image(){
		Image shipImage = new Image("pirateShip1.png",scale,scale,true,true); 
		pirate1ImageView = new ImageView(shipImage);
		pirate1ImageView.setX(pirate1.getPirateLocation().x * scale);
		pirate1ImageView.setY(pirate1.getPirateLocation().y * scale);
		root.getChildren().add(pirate1ImageView);
	}
	//Load Pirate Ship 2
	private void loadPirate2Image(){
		Image shipImage = new Image("pirateShip2.png",scale,scale,true,true); 
		pirate2ImageView = new ImageView(shipImage);
		pirate2ImageView.setX(pirate2.getPirateLocation().x * scale);
		pirate2ImageView.setY(pirate2.getPirateLocation().y * scale);
		root.getChildren().add(pirate2ImageView);
	}
	//Load ghost ship image
	private void loadGhostShipImage(){
		Image shipImage = new Image("ghostShip.png",scale,scale,true,true); 
		ghostImageView = new ImageView(shipImage);
		ghostImageView.setX(ghost.getPirateLocation().x * scale);
		ghostImageView.setY(ghost.getPirateLocation().y * scale);
		root.getChildren().add(ghostImageView);
	}
	//Load Combined Pirate Ship Image
	private void loadFleetImage(){
		Image fleetImage = new Image("pirateFleet.png",scale,scale,true,true); 
		fleetImageView = new ImageView(fleetImage);
	}
	//Load destroyed player ship image
	private void loadGameOverImage(){
		Image gameOverImage = new Image("dead.png",scale,scale,true,true); 
		gameOverImageView = new ImageView(gameOverImage);
	}
	

	
	
	
	
	
	//key event handler, also calls other functions that check if images are stacked, monster on player, player on treasure island
	private void setSail() {
		scene.setOnKeyPressed(new EventHandler<KeyEvent>(){

		@Override
		public void handle(KeyEvent ke) {
			switch(ke.getCode()) {
				case RIGHT:
					ship.goEast();
					break;
				case LEFT:
					ship.goWest();
					break;
				case UP:
					ship.goNorth();
					break;
				case DOWN:
					ship.goSouth();
					break;
				default:
					break;
			}
			
			shipImageView.setX(ship.getShipLocation().x * scale);
			shipImageView.setY(ship.getShipLocation().y * scale);
			gameEffects(); //function that calls on a number of functions that check player location
			centerScreen(ship.getShipLocation().x,ship.getShipLocation().y); //moves pane with player at center
			}
		});
	}
	
	
	//move screen with player ship at center, if player moves to border, do not center anymore
		public void centerScreen (int x, int y){
			root.setLayoutX(x*-scale + ((mapScale-scale)/2));
			root.setLayoutY(y*-scale + ((mapScale-scale)/2));
			
			if (root.getLayoutY() < (size*-scale)+mapScale)
				root.setLayoutY((size*-scale)+mapScale);
			else if (root.getLayoutY() > 0)
				root.setLayoutY(0);
			
			if (root.getLayoutX() < (size*-scale)+mapScale)
				root.setLayoutX((size*-scale)+mapScale);
			else if (root.getLayoutX() > 0)
				root.setLayoutX(0);
		}
		
	
	
	//container function for various functions that depend on player position after input
	public void gameEffects() {
		checkShips();
		shipBattle();
		monsterBattle();
		moveCount();
		treasureFound();
		hauntedShip();
		redHunter();
	}
	//group image load function
	public void loadImages() {
		loadShipImage();
		loadPirate1Image();
		loadPirate2Image();
		loadGhostShipImage();
		loadFleetImage();
		loadGameOverImage();
		loadMenu();
	}
	
	
	
	//moves ships and if they are stacked, creates a new images and removes old, ghost ship doesn't stack though.
	//function left in from first version of game, but serves no design purposes
	public void checkShips() {
		if (pirate1.getPirateLocation().equals(pirate2.getPirateLocation())) {
			fleetImageView.setX(pirate1.getPirateLocation().x*scale);
			fleetImageView.setY(pirate1.getPirateLocation().y*scale);
			if (!root.getChildren().contains(fleetImageView)) {
				root.getChildren().remove(pirate1ImageView);
				root.getChildren().remove(pirate2ImageView);
				root.getChildren().add(fleetImageView);
			}
		} else {
			if (root.getChildren().contains(fleetImageView)) {
				root.getChildren().add(pirate1ImageView);
				root.getChildren().add(pirate2ImageView);
				root.getChildren().remove(fleetImageView);
			}
			pirate1ImageView.setX(pirate1.getPirateLocation().x*scale);
			pirate1ImageView.setY(pirate1.getPirateLocation().y*scale);
			pirate2ImageView.setX(pirate2.getPirateLocation().x*scale);
			pirate2ImageView.setY(pirate2.getPirateLocation().y*scale);
			ghostImageView.setX(ghost.getPirateLocation().x*scale);
			ghostImageView.setY(ghost.getPirateLocation().y*scale);
		}
	}
	
	
	
	
	/***********   End Game Functions ***********/
	
	
	//if player moves onto treasure island, spawns treasure chest, removes player ship, and displays win banner
	private void treasureFound() {
		if (gameOver == false) {
			if (ship.getShipLocation().equals(oceanMap.getSecret())) {
				root.getChildren().remove(shipImageView);
				Image treasureImage = new Image("treasure.png",scale,scale,true,true); 
				treasureImageView = new ImageView(treasureImage);
				root.getChildren().add(treasureImageView);
				treasureImageView.setX(oceanMap.getSecret().x*scale);
				treasureImageView.setY(oceanMap.getSecret().y*scale);
				gameOver = true;
				music.stopMusic();
				winBanner();
			}
		}
	}
	//checks if ship occupies same location as pirate ship and loads gameOver image
	//sets gameOver to true, removes player ship. 
	public void shipBattle() {
		if (gameOver == false) {
			if (pirate1.getPirateLocation().equals(ship.getShipLocation()) || 
				pirate2.getPirateLocation().equals(ship.getShipLocation()) ||
				ghost.getPirateLocation().equals(ship.getShipLocation())) {
				root.getChildren().remove(shipImageView);
				root.getChildren().add(gameOverImageView);
				if (root.getChildren().contains(fleetImageView)) {
					root.getChildren().remove(fleetImageView);
				}
				gameOver = true; 
				music.stopMusic(); //stops game music
				loseBanner();//displays pirate game over image
			
			}
			gameOverImageView.setX(ship.getShipLocation().x*scale);
			gameOverImageView.setY(ship.getShipLocation().y*scale);
		}
	}
	//checks if ship occupies same location as pirate ship and loads seaMonster game over image
    //sets gameOver to true, removes player ship.
	public void monsterBattle() {
		if (gameOver == false) {
			for(SeaMonster seamonsters : seaMonsterMaker.seaMonsterImages) {
				 if(ship.getShipLocation().equals(seamonsters.seaMonsterLocation())) {
					 if (root.getChildren().contains(shipImageView)) {
						 root.getChildren().remove(shipImageView);
						 root.getChildren().add(gameOverImageView);
					 }
					 gameOver = true;
					 music.stopMusic(); //stops game music
					 monsterBanner(); //displays monster game over image 
				 }
			}
			gameOverImageView.setX(ship.getShipLocation().x*scale);
			gameOverImageView.setY(ship.getShipLocation().y*scale);
		}
		
	}
	
	
	
	/***********  End Game Screen Load Functions  ***********/
	
	
	//image for when player finds treasure island
	public void winBanner() {
		Image winImage = new Image("winBanner.png",mapScale*1.5,mapScale*1.5,true,true); 
		ImageView winImageView = new ImageView(winImage);
		winImageView.setX(scale*-3);
		winImageView.setY(scale);
		parentRoot.getChildren().add(winImageView);
	}
	
	//image if player is captured by pirates
	public void loseBanner() {
		Image loseImage = new Image("loseBanner.png",mapScale*1.5,mapScale*1.5,true,true); 
		ImageView loseImageView = new ImageView(loseImage);
		loseImageView.setX(scale*-1.7);
		loseImageView.setY(0);
		parentRoot.getChildren().add(loseImageView);
	}
	
	//image if player is sunk by monsters
	public void monsterBanner() {
		Image loseMonsterImage = new Image("monsterBanner.png",mapScale*1,mapScale*1,true,true); 
		ImageView loseMonsterImageView = new ImageView(loseMonsterImage);
		loseMonsterImageView.setX(scale*-0.2);
		loseMonsterImageView.setY(scale);
		parentRoot.getChildren().add(loseMonsterImageView);
	}
	
	
	
	/***********   Map Draw Functions  ***********/

	//Load island, border ocean images for GUI, random generation for two islands types (rocks or palm trees)
	//Creates border images around parameter of ocean map, gives treasure island a regular island tile and not rock tile
	public void drawMap() {
		 //default image load for islands/borders
		 Image oceanImage = new Image("ocean.jpg",scale-2,scale-2,true,true);
		 Image islandImage = new Image("island.jpg",scale-1,scale-2,true,true);
		 Image borderImage = new Image("border.jpg",scale-2,scale-2,true,true);
		for(int x = 0; x < oceanMap.dimensions; x++){
			   for(int y = 0; y < oceanMap.dimensions; y++){
			       Rectangle rect = new Rectangle(x*scale,y*scale,scale,scale);
			       rect.setStroke(Color.BLACK); 
			       root.getChildren().add(rect); 
			       if (islandMap[x][y] == 1) {
			    	   int z = rand.nextInt(2);
			    	   if (z==0) {
			    		   islandImage = new Image("island.jpg",scale-1,scale-2,true,true);
			    		   islandImageView = new ImageView(islandImage);
			    	   } else { //if z == 1 then make rock island instead
			    		   islandImage = new Image("rock.jpg",scale-1,scale-2,true,true);
			    		   islandImageView = new ImageView(islandImage);
			    	   }
					   islandImageView.setX(x*scale); 
					   islandImageView.setY(y*scale);
			    	   root.getChildren().add(islandImageView);
			       }
			       if (islandMap[x][y] == 0) {
					   oceanImageView = new ImageView(oceanImage);
			    	   oceanImageView.setX(x*scale); 
			       	   oceanImageView.setY(y*scale);
			    	   root.getChildren().add(oceanImageView);
			       }
			       if (islandMap[x][y] == 3) {
					   borderImageView = new ImageView(borderImage);
			    	   borderImageView.setX(x*scale); 
			       	   borderImageView.setY(y*scale);
			    	   root.getChildren().add(borderImageView);
			       }
			       if (islandMap[x][y] == 4) { //should only create 1 treasure island
			    	   islandImage = new Image("island.jpg",scale-1,scale-2,true,true);
			    	   islandImageView = new ImageView(islandImage);
			    	   islandImageView.setX(x*scale); 
					   islandImageView.setY(y*scale);
					   root.getChildren().add(islandImageView);
			       }
			} 
		}
	}
	//Function for Creating 2x1 sized Islands, works if two islands are adjacent, replaces them with a larger tile
	public void bigIsland() {
		for (int x = 0; x<islandMap.length-2; x++){
			for (int y = 0; y<islandMap[x].length; y++){
				if (islandMap[x][y] == 1 && islandMap[x+1][y] == 1 && islandMap[x+2][y] == 0) {
					Image islandImage = new Image("bigisland.png",scale*2-2,scale-2,true,true);
					islandImageView = new ImageView(islandImage);
					islandImageView.setX(x*scale); 
					islandImageView.setY(y*scale);
					root.getChildren().add(islandImageView);
				}
			}
		}
	}
	
	
	/***********   Strategy Switch Functions   ***********/

	//function that switches ghost ship strategy if player gets to close, will chase until mode is set to 2 in game
	public void hauntedShip() {
		double distance = Math.hypot(ghost.pirateLocation.x-ship.currentLocation.x, ghost.pirateLocation.y-ship.currentLocation.y);
		if (distance <= 1.5) {
			ghost.setStrategy(new SmartChase());
		}
	}
	//if mode 2 selected in game, this function switches the red pirate ship back to chase mode if within range of player
	public void redHunter() {
		double distance = Math.hypot(pirate2.pirateLocation.x-ship.currentLocation.x, pirate2.pirateLocation.y-ship.currentLocation.y);
		if (distance <= 2.25) {
			pirate2.setStrategy(new SmartChase());
		}
	}
	

	
	
	
	
	
	//return scale of this class for other classes to use
	public int scale() {
		return scale;
	}

	
	//Main Launch method
	public static void main(String args[]) {
		launch(args);
	}
}
