import java.awt.Point;
//Garrett George, SE350 Final Project


import javafx.scene.image.ImageView;
//Interface for SeaMonsters - Hydra and Kraken classes.
public interface SeaMonster {	
	
	public ImageView getImageView();
	
	public void setX(int x);
	
	void setY(int y);
	
	int getX();
	
	int getY();
	
	public void setPositionX(int x);
	
	public void setPositionY(int y);
	
	public Point seaMonsterLocation();
	
	public void move();
}
