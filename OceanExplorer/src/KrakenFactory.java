//Garrett George, SE350 Final Project


public class KrakenFactory extends SeaMonsterFactory {
	
	public SeaMonster buildMonster(int x, int y, int scale) {
		return new Kraken(x,y,scale);
	}
}
