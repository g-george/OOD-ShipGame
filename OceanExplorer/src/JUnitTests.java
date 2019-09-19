import org.junit.Test;
import static org.junit.Assert.*;

//Garrett George, SE350 Final Project

public class JUnitTests {

    @Test  //Checks to see if islands created are what appears on the ocean map
    public void t0() {
        int islandCount = 0;
        for(int i = 0; i < OceanMap.getInstance().getDimensions(); i++){
            for (int j = 0; j < OceanMap.getInstance().getDimensions(); j++){
                if(OceanMap.getInstance().getMap()[i][j] == 1 || OceanMap.getInstance().getMap()[i][j] == 4) {
                    islandCount++;
                }
            }
        }
        assertTrue(islandCount == OceanMap.getInstance().islandCount);
    }
    @Test //Checks to see if map has the correct amount of borders
    public void t1() {
    	int borderCount = 0;
        for(int i = 0; i < OceanMap.getInstance().getDimensions(); i++){
            for (int j = 0; j < OceanMap.getInstance().getDimensions(); j++){
            	 if(OceanMap.getInstance().getMap()[i][j] == 3) {
                     borderCount++;
                 }
            }
        }
    	assertTrue(borderCount == OceanMap.getInstance().getDimensions()*4-4); 
    }
    
 
    @Test //Checks to see if only one instance of ocean map
    public void t2() {  
        assertTrue(OceanMap.getInstance() == OceanExplorer.oceanMap);
    }
}
