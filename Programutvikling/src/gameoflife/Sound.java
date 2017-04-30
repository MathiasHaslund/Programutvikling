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
        BACKGROUND, START, STOP, CLICK;
    }
    private Map<String, String> soundList = new HashMap();
    
    private MediaPlayer player;

    public Sound(SoundTypes soundName){
        soundList.put(SoundTypes.BACKGROUND.toString(), "sound/sneaky.wav");
        soundList.put(SoundTypes.START.toString(), "sound/start.wav");
        soundList.put(SoundTypes.STOP.toString(), "sound/stop.wav");
        soundList.put(SoundTypes.CLICK.toString(), "sound/click.wav");
        String soundString = soundList.get(soundName.toString());
        Media currentSound = new Media(new File(soundString).toURI().toString());
        player = new MediaPlayer(currentSound);
    }    

    protected void playSound(){
        player.play();
    }
    
    protected void pauseSound(){
        player.pause();
    }
    
    protected void stopSound(){
        player.stop();
    }
}
