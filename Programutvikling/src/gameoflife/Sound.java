package gameoflife;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 * @author Mathias Haslund
 * @author Josef Krivan
 * @version 1.0
 */

/**
 * The Sound class provides support for sound in the game.
 * 
 * Sound provides a constructor for creating sound objects used for playing music and sound effects ({@link gameoflife.Sound#Sound(SoundTypes soundName) Sound}), play/pause the sound ({@link gameoflife.Sound#playSound() playSound}, {@link gameoflife.Sound#pauseSound() pauseSound}) and stop the sound ({@link gameoflife.Sound#stopSound() stopSound}). 
 */
public class Sound {    

    /**
    * Enumeration that holds the avilable sound types. This includes background music/sound, as well as sounds for clicking the different buttons.
    */
    public enum SoundTypes {
        BACKGROUND, START, STOP, CLICK;
    }
    
    /**
     * Maps the SoundTypes enumerations to the file name of the audio files.
     */
    private Map<String, String> soundList = new HashMap();
    
    /**
    * Media player object the play/pause/stop methods uses.
    */ 
    private MediaPlayer player;
    
    /**
    * Creates a sound object using a specific SoundType.
    * @param soundType Used for selecting the correct audio file.
    */  
    public Sound(SoundTypes soundType){
        soundList.put(SoundTypes.BACKGROUND.toString(), "sound/sneaky.wav");
        soundList.put(SoundTypes.START.toString(), "sound/start.wav");
        soundList.put(SoundTypes.STOP.toString(), "sound/stop.wav");
        soundList.put(SoundTypes.CLICK.toString(), "sound/click.wav");
        String soundString = soundList.get(soundType.toString());
        Media currentSound = new Media(new File(soundString).toURI().toString());
        player = new MediaPlayer(currentSound);
    }    
    /**
    * Plays sound from player.
    * The audio is chosen when the Sound object is initialized.
    */
    protected void playSound(){
        player.play();
    }
    
    /**
    * Pauses sound from player.
    * The audio is chosen when the Sound object is initialized.
    */    
    protected void pauseSound(){
        player.pause();
    }
    
    /**
    * Stops sound from player.
    * The audio is chosen when the Sound object is initialized.
    */    
   /* protected void stopSound(){
        player.stop();
    }*/
}
