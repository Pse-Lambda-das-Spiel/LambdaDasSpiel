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
        musicVolume = 0.1f;
        soundVolume = 0.1f;
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
     * Return the volume of the music.
     * 
     * @return musicVolume lies in the interval [0,1].
     */
    public float getMusicVolume() {
        return musicVolume;
    }

    /**
     * Sets the volume of the music. Afterwards it notifies its
     * observers by calling their
     * {@link SettingsModelObserver#changedMusicVolume() changedMusicVolume()}
     * method.
     * 
     * @param musicVolume
     *            should lie in the interval [0,1]. If not it gets set to 0 or
     *            1 (depending on which is closer).
     */
    public void setMusicVolume(float musicVolume) {
        if (musicVolume < 0) {
            this.musicVolume = 0;
        } else if (musicVolume > 1) {
            this.musicVolume = 1;
        } else {
            this.musicVolume = musicVolume;
        }
        notify(o -> o.changedMusicVolume());
    }

    /**
     * Return the volume of the sound.
     * 
     * @return soundVolume lies in the interval [0,1].
     */
    public float getSoundVolume() {
        return soundVolume;
    }

    /**
     * Sets the volume of the sound. Afterwards it notifies its
     * Observers by calling their
     * {@link SettingsModelObserver#changedSoundVolume() changedSoundVolume()}
     * method.
     * 
     * @param soundVolume
     *            should lie in the interval [0,1]. If not it gets set to 0 or
     *            1 (depending on which is closer).
     */
    public void setSoundVolume(float soundVolume) {
        if (soundVolume < 0) {
            this.soundVolume = 0;
        } else if (soundVolume > 1) {
            this.soundVolume = 1;
        } else {
            this.soundVolume = soundVolume;
        }
        notify(o -> o.changedSoundVolume());
    }

}
