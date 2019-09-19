
//Garrett George, SE350 Final Project

public class FactoryChooser {
	//Factory chooser, string passed to this class to determine monster Factory type
	public static SeaMonsterFactory buildFactory(String seamonster) {
		
		SeaMonsterFactory seaMonster = null;
		
		if(seamonster.equals("Hydra")) {
			seaMonster = new HydraFactory();
		}
		if(seamonster.equals("Kraken")) {
			seaMonster = new KrakenFactory();
		}
		return seaMonster;
	}
}
