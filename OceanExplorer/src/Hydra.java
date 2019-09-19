import java.awt.Point;
import java.util.Random;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
//Garrett George, SE350 Final Project

public class Hydra implements SeaMonster{

	int x,y,scale;
	OceanMap oceanMap;
	Random rand = new Random();
	Point seaMonsterLocation ;
	
	ImageView hydraImageView;
	
	//Hydra Constructor, scale is greater than other objects, because the hydra can destroy the player on 4 spots instead of 1
	public Hydra(int x, int y, int scale) {
		this.x = x;
		this.y = y;
		Image hydraImage = new Image("hydra.png",scale,scale,true,true);
		hydraImageView = new ImageView(hydraImage);
		setPositionX(x);
		setPositionY(y);
		seaMonsterLocation  = new Point(x,y);
		this.scale = scale;
		oceanMap = OceanMap.getInstance();
	}
	
	public ImageView getImageView() {
		return hydraImageView;
	}

	@Override
	public void setX(int x) {
		this.x = x;
		setPositionX(x);
	}

	@Override
	public void setY(int y) {
		this.y = y;
		setPositionY(y);
	}

	@Override
	public int getX() {
		return x;
	}

	@Override
	public int getY() {
		return y;
	}
	
	@Override
	public void setPositionX(int x) {
		hydraImageView.setX(x * scale);
	}
	
	@Override
	public void setPositionY(int y) {
		hydraImageView.setY(y * scale);
	}
	
	public Point seaMonsterLocation() {
		return seaMonsterLocation;
	}
	
	//monster move method for hydra, moves from top to bottom of map.  Resets when bottom border reached.
	@Override
	public void move() {
		int x = getX() + rand.nextInt(3)-1;
		int y = getY() + 1;
		
		if(oceanMap.isOcean(x, y)) {
			setX(x); setY(y);
			this.seaMonsterLocation = new Point(x,y);
		}
		if(y >= oceanMap.dimensions-1)
			setY(1);
	}
}

