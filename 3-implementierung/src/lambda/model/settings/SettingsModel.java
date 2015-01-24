package lambda.model.settings;

import lambda.Observable;

/**
 * Represents the settings of a profile.
 * 
 * @author Kai Fieger
 */
public class SettingsModel extends Observable<SettingsModelObserver> {

    private boolean musicOn;
    private float musicVolume;
    private float soundVolume;

    /**
     * Creates a new settings object and initializes it with standard values.
     */
    public SettingsModel() {
        musicOn = true;
        musicVolume = 100;
        soundVolume = 100;
    }

    /**
     * Return if the game should play music or not.
     * 
     * @return If the game should play music or not.
     */
    public boolean isMusicOn() {
        return musicOn;
    }

    /**
     * Sets musicOn, which represents the status whether the game should play
     * music or not. Afterwards it notifies its observers by calling their
     * {@link SettingsModelObserver#changedMusicOn() changedMusicOn()} method.
     * 
     * @param musicOn
     */
    public void setMusicOn(boolean musicOn) {
        this.musicOn = musicOn;
        notify(o -> o.changedMusicOn());
    }

    /**
     * Return the volume of the music in percent.
     * 
     * @return musicVolume lies in the interval [0,100].
     */
    public float getMusicVolume() {
        return musicVolume;
    }

    /**
     * Sets the volume of the music in percent. Afterwards it notifies its
     * observers by calling their
     * {@link SettingsModelObserver#changedMusicVolume() changedMusicVolume()}
     * method.
     * 
     * @param musicVolume
     *            should lie in the interval [0,100]. If not it gets set to 0 or
     *            100 (depending on which is closer).
     */
    public void setMusicVolume(float musicVolume) {
        if (musicVolume < 0) {
            this.musicVolume = 0;
        } else if (musicVolume > 100) {
            this.musicVolume = 100;
        } else {
            this.musicVolume = musicVolume;
        }
        notify(o -> o.changedMusicVolume());
    }

    /**
     * Return the volume of the sound in percent.
     * 
     * @return soundVolume lies in the interval [0,100].
     */
    public float getSoundVolume() {
        return soundVolume;
    }

    /**
     * Sets the volume of the sound in percent. Afterwards it notifies its
     * Observers by calling their
     * {@link SettingsModelObserver#changedSoundVolume() changedSoundVolume()}
     * method.
     * 
     * @param soundVolume
     *            should lie in the interval [0,100]. If not it gets set to 0 or
     *            100 (depending on which is closer).
     */
    public void setSoundVolume(float soundVolume) {
        if (soundVolume < 0) {
            this.soundVolume = 0;
        } else if (soundVolume > 100) {
            this.soundVolume = 100;
        } else {
            this.soundVolume = soundVolume;
        }
        notify(o -> o.changedSoundVolume());
    }

}
