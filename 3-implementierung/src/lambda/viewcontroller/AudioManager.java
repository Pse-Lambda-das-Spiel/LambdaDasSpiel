package lambda.viewcontroller;

import java.io.BufferedReader;
import java.io.IOException;

import lambda.model.profiles.ProfileManager;
import lambda.model.profiles.ProfileManagerObserver;
import lambda.model.settings.SettingsModel;
import lambda.model.settings.SettingsModelObserver;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class AudioManager implements ProfileManagerObserver, SettingsModelObserver {
    private static final String soundFolder = "data/sounds/";
    private static final String defaultMusic = "data/defaultMusic.mp3";
    private static final AudioManager manager = new AudioManager();
    private AssetManager assets;
    private SettingsModel settings;
    private Music music;
    private boolean loggedIn;
    
    /*
    Currently no music when logged out and standard volume for sounds.
    Better ideas?
     */
    
    private AudioManager() {
        loggedIn = false;
        ProfileManager.getManager().addObserver(this);
        settings = new SettingsModel();
        settings.addObserver(this);
    }
    
    public static void queueAssets(AssetManager assets) {
        manager.assets = assets;
        assets.load(defaultMusic, Music.class);
        try {
            BufferedReader reader = Gdx.files.internal(soundFolder + "names.txt").reader(4096);
            String name = null;
            while ((name = reader.readLine()) != null) {
                assets.load(soundFolder + name + ".mp3", Sound.class);
            }
            reader.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public static void init() {
        manager.music = manager.assets.get(defaultMusic, Music.class);
        playDefaultMusic();
    }

    public static void setLoggedIn(boolean loggedIn) {
        manager.loggedIn = loggedIn;
        manager.changedMusicOn();
    }
    
    public static void playSound(String name) {
        playSound(manager.assets.get(soundFolder + name + ".mp3", Sound.class));
    }
    
    public static void playSound(Sound sound) {
        sound.play((manager.loggedIn ? manager.settings.getSoundVolume() : 0.2f));
    }
    
    public static void playDefaultMusic() {
        playMusic(manager.assets.get(defaultMusic, Music.class));
    }
    
    public static void playMusic(Music music) {
        manager.music.stop();
        manager.music = music;
        manager.changedMusicOn();
        manager.music.setLooping(true);
        manager.music.play();
    }
    
    @Override
    public void changedProfile() {
        settings.removeObserver(this);
        settings = ProfileManager.getManager().getCurrentProfile().getSettings();
        settings.addObserver(this);
        changedMusicOn();
    }
    
    @Override
    public void changedMusicOn() {
        if (settings.isMusicOn() && loggedIn) {
            music.setVolume(settings.getMusicVolume());
        } else {
            music.setVolume(0);
        }
    }

    @Override
    public void changedMusicVolume() {
        changedMusicOn();
    }

	@Override
	public void changedSoundVolume() {
	}

	@Override
	public void changedProfileList() {		
	}

}
