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
 * Java file for "Sound" methods.
 */
public class Sound {    
/**
 * Controller method for playing sounds on "Background","Play" and "Step" buttons.
 */
    public enum SoundTypes {
        BACKGROUND, PLAY, STEP;
    }
    Map<String, String> soundList = new HashMap();
/**
 * Class for "Background" sound-type.
 */    
    public Sound(){
        soundList.put(SoundTypes.BACKGROUND.toString(), "sneaky.mp3");
    }    
/**
 * Class for the playing sounds.
 */
    MediaPlayer mediaPlayer;
/**
 * Method for playing a specific sound file.
 * @param soundName 
 */
    protected void playSound(SoundTypes soundName){
        
        String soundString = soundList.get(soundName.toString());
        Media sound = new Media(new File(soundString).toURI().toString());
/**
 * Initializing a mediaPlayer object for playing sound files.
 * @see GameBoardController.java (Initialize).
 */
        mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.play();
    }
}
