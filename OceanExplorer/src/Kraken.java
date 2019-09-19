import java.awt.Point;
import java.util.Random;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
//Garrett George, SE350 Final Project

public class Kraken implements SeaMonster{

	int x, y, scale;
	OceanMap oceanMap;
	Random rand = new Random();
	Point seaMonsterLocation ;
	ImageView krakenImageView;

	//Kraken Constructor
	public Kraken(int x, int y, int scale) {
		this.x = x;
		this.y = y;
		Image krakenImage = new Image("kraken.png",scale*1.10,scale,true,true);
		krakenImageView = new ImageView(krakenImage);
		setPositionX(x);
		setPositionY(y);
		seaMonsterLocation = new Point(x,y);
		this.scale = scale;
		oceanMap = OceanMap.getInstance();
	}
	
	public ImageView getImageView() {
		return krakenImageView;
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
		krakenImageView.setX(x * scale);
	}
	
	@Override
	public void setPositionY(int y) {
		krakenImageView.setY(y * scale);
	}
	
	public Point seaMonsterLocation() {
		return seaMonsterLocation;
	}
	
	//monster move method for kraken, moves from left to right on map.  Resets when right border reached.
	@Override
	public void move() {
		int x = getX() + 1;
		int y = getY() + rand.nextInt(3)-1;
		
		if(oceanMap.isOcean(x, y)) {
			setX(x); setY(y);
			this.seaMonsterLocation = new Point(x,y);
		}
		if(x >= oceanMap.dimensions-1)
			setX(1);
	}
}
