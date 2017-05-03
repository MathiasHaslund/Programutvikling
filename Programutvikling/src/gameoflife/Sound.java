package gameoflife;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 * @author Mathias Haslund
 * @author Josef Krivan
 * @version 0.9
 * @since 0.9 (29/04/2017)
 */

/**
 * The main class for sound.java
 */
public class Sound {    
/**
 * Controller method for playing sounds on "Background","START","STOP" and "CLICK" buttons.
 * Contains SoundTypes enum for the different sounds based on the corresponding buttons.
 * @see GameBoard.fxml (For the different buttons and the actions associated with SoundTypes)
 */
    public enum SoundTypes {
        BACKGROUND, START, STOP, CLICK;
    }
    private Map<String, String> soundList = new HashMap();
/**
 * MediaPlayer class is used for creating an object that can play sound files.
 */ 
    private MediaPlayer player;
/**
 * Class for any SoundType that exists in the SoundTypes enum.
 * BACKGROUND is the current sound type implemented, but "PLAY" and "STEP" can also be used.
 * @see initialize (GameBoardController.java)
 * @see mediaPlayer
 * @since 0.8
 */  
    public Sound(SoundTypes soundName){
        soundList.put(SoundTypes.BACKGROUND.toString(), "sound/sneaky.wav");
        soundList.put(SoundTypes.START.toString(), "sound/start.wav");
        soundList.put(SoundTypes.STOP.toString(), "sound/stop.wav");
        soundList.put(SoundTypes.CLICK.toString(), "sound/click.wav");
        String soundString = soundList.get(soundName.toString());
        Media currentSound = new Media(new File(soundString).toURI().toString());
        player = new MediaPlayer(currentSound);
    }    
/**
 * Method for playing a specific sound file. 
 * SoundTypes from public sound.
 * @see Sound to see which elements of the enum have .mp3 files attached to them.
 */
    protected void playSound(){
        player.play();
    }
/**
 * Method that pauses the current sound file that is playing. 
 */    
    protected void pauseSound(){
        player.pause();
    }
/**
 * Method that stops the current sound file that is playing.
 */    
    protected void stopSound(){
        player.stop();
    }
}
