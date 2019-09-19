import java.io.InputStream;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequencer;



public class GameMusic {
	//Game music, may throw warning, requires adding new Hkey in regedit, known java bug, does not affect program.
	Sequencer sequencer;
	
	public void playMusic(){
		try {
		   sequencer = MidiSystem.getSequencer();
		   sequencer.open();
		   InputStream inputStream = getClass().getResourceAsStream("KlausBadelt_He's_a_Pirate.mid");
		   sequencer.setSequence(inputStream);
		   sequencer.start(); 
		} catch (Exception e) {}
	}
	
	public void stopMusic(){
		    sequencer.stop();
		 }
}

