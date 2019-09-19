import java.awt.Point;
//Garrett George, SE350 Final Project

//Chase strategy interface for GuardChase, SmartChase
public interface ChaseStrategy {
	public void movePirate(Point pirateLoc, Point playerLoc);
	public void moveAroundIsland(Point pirateLocation);
}
