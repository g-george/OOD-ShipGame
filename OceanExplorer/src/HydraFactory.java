
//Garrett George, SE350 Final Project

public class HydraFactory extends SeaMonsterFactory{

	public SeaMonster buildMonster(int x, int y, int scale) {
		return new Hydra(x,y,scale);
	}
}
