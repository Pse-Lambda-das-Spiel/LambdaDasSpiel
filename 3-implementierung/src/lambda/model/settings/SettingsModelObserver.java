package lambda.model.settings;

/**
 * An interface for an observer of an SettingsModel-object. An observed
 * SettingsModel-object calls the containing methods of its observers. Implement
 * this interface to keep a class informed on changed settings.
 * 
 * @author Kai Fieger
 */
public interface SettingsModelObserver {

    /**
     * Gets called by an observed SettingsModel-object as the music is turned on
     * or off.
     */
    public void changedMusicOn();

    /**
     * Gets called by an observed SettingsModel-object as the music volume is
     * changed.
     */
    public void changedMusicVolume();

    /**
     * Gets called by an observed SettingsModel-object as the sound volume is
     * changed.
     */
    public void changedSoundVolume();

}
