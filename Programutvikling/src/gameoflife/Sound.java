/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gameoflife;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 *
 * @author Espen
 */
public class Sound {    
    public enum SoundTypes {
        BACKGROUND, PLAY, STEP;
    }
    Map<String, String> soundList = new HashMap();
    
    public Sound(){
        soundList.put(SoundTypes.BACKGROUND.toString(), "sneaky.mp3");
    }    

    MediaPlayer mediaPlayer;
    protected void playSound(SoundTypes soundName){
        
        String soundString = soundList.get(soundName.toString());
        Media sound = new Media(new File(soundString).toURI().toString());
        mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.play();
    }
}
