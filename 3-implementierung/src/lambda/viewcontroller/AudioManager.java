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

/**
 * Manages the audio output of the game. Provides methods that allow playing
 * ingame sounds and music matching the game's current sound and music settings.
 * 
 * @author Kai Fieger
 */
public class AudioManager implements ProfileManagerObserver,
        SettingsModelObserver {
    private static final String SOUND_FOLDER = "data/sounds/";
    private static final String DEFAULT_MUSIC = "data/defaultMusic.mp3";
    private static final AudioManager MANAGER = new AudioManager();
    private AssetManager assets;
    private SettingsModel settings;
    private Music music;
    private boolean loggedIn;

    /*
     * Currently no music when logged out and standard volume for sounds. Better
     * ideas?
     */

    private AudioManager() {
        loggedIn = false;
        ProfileManager.getManager().addObserver(this);
        settings = new SettingsModel();
        settings.addObserver(this);
    }

    /**
     * Queues all sound-assets in the game to be loaded by the given asset
     * manager. Also loads the standard music of the game menus.
     * 
     * @param assets
     *            the asset manager
     */
    public static void queueAssets(AssetManager assets) {
        MANAGER.assets = assets;
        assets.load(DEFAULT_MUSIC, Music.class);
        try {
            BufferedReader reader = Gdx.files.internal(
                    SOUND_FOLDER + "names.txt").reader(4096);
            String name = null;
            while ((name = reader.readLine()) != null) {
                assets.load(SOUND_FOLDER + name + ".mp3", Sound.class);
            }
            reader.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * Initializes the AudioManager. Has to be called after all assets queued
     * through queueAssets are loaded.
     */
    public static void init() {
        MANAGER.music = MANAGER.assets.get(DEFAULT_MUSIC, Music.class);
        playDefaultMusic();
    }

    /**
     * Sets the state of the AudioManager to a loggedIn- or loggedOut-state.
     * (audio output is different in each state)
     * 
     * @param loggedIn
     *            if player is currently logged in or not
     */
    public static void setLoggedIn(boolean loggedIn) {
        MANAGER.loggedIn = loggedIn;
        MANAGER.changedMusicOn();
    }

    /**
     * Loads the sound with the given name from the sound folder and plays it
     * matching the game's current sound settings.
     * 
     * @param name
     *            Name of the sound.
     */
    public static void playSound(String name) {
        if (MANAGER.assets.isLoaded(SOUND_FOLDER + name + ".mp3", Sound.class)) {
            playSound(MANAGER.assets.get(SOUND_FOLDER + name + ".mp3",
                    Sound.class));
        }
    }

    /**
     * Plays the given sound matching the game's current sound settings.
     * 
     * @param sound
     *            Sound that should be played.
     */
    public static void playSound(Sound sound) {
        if (sound == null) {
            throw new IllegalArgumentException("sound cannot be null");
        }
        sound.play((MANAGER.loggedIn ? MANAGER.settings.getSoundVolume() : 0.2f));
    }

    /**
     * Plays to game's default music on a loop matching the game's current music
     * setting, after stopping any other music that might be playing through the
     * AudioManager.
     */
    public static void playDefaultMusic() {
        playMusic(MANAGER.assets.get(DEFAULT_MUSIC, Music.class));
    }

    /**
     * Plays the given music on a loop matching the game's current music
     * setting, after stopping any other music that might be playing through the
     * AudioManager.
     *
     * @param music
     *            Music that should be played.
     */
    public static void playMusic(Music music) {
        if (music == null) {
            throw new IllegalArgumentException("music cannot be null");
        }
        if (music != MANAGER.music) {
            MANAGER.music.stop();
            MANAGER.music = music;
        }
        MANAGER.changedMusicOn();
        MANAGER.music.setLooping(true);
        MANAGER.music.play();
    }

    @Override
    public void changedProfile() {
        settings.removeObserver(this);
        settings = ProfileManager.getManager().getCurrentProfile()
                .getSettings();
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
