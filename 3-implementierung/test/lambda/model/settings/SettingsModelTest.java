package lambda.model.settings;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests a SettingsModel
 * 
 * @author Kai Fieger
 */
public class SettingsModelTest implements SettingsModelObserver {

    private static final float EPSILON = 0.001f;
    private boolean calledChangedMusicOn;
    private boolean calledChangedMusicVolume;
    private boolean calledChangedSoundVolume;
    private SettingsModel settings;

    @Before
    public void setUp() throws Exception {
        calledChangedMusicOn = false;
        calledChangedMusicVolume = false;
        calledChangedSoundVolume = false;
        settings = new SettingsModel();
        settings.addObserver(this);
    }

    @After
    public void tearDown() throws Exception {
        settings.removeObserver(this);
    }

    /**
     * Sets musicOn to true and false, while making sure it works and the
     * correct SettingsModelOberver-methods are called.
     */
    @Test
    public void testSetMusicOn() {
        settings.setMusicOn(true);
        assertTrue(settings.isMusicOn());
        assertTrue(calledChangedMusicOn);
        calledChangedMusicOn = false;
        settings.setMusicOn(false);
        assertFalse(settings.isMusicOn());
        assertTrue(calledChangedMusicOn);
    }

    /**
     * Checks if the music volume can be set. while making sure that
     * changedMusicVolume() from the obervers is called.
     */
    @Test
    public void testSetMusicVolume() {
        float testValue = 0.12f;
        settings.setMusicVolume(testValue);
        assertEquals(testValue, settings.getMusicVolume(), EPSILON);
        assertTrue(calledChangedMusicVolume);
    }

    /**
     * Checks if the sound volume can be set. while making sure that
     * changedSoundVolume() from the obervers is called.
     */
    @Test
    public void testSetSoundVolume() {
        float testValue = 0.12f;
        settings.setSoundVolume(testValue);
        assertEquals(testValue, settings.getSoundVolume(), EPSILON);
        assertTrue(calledChangedSoundVolume);
    }

    /**
     * Tests if setMusicVolume and setSoundVolume set the volumes to 0 or 1
     * depending on negative values or values bigger than 1.
     */
    @Test
    public void testForInvalidValues() {
        settings.setMusicVolume(120.12f);
        assertEquals(1.0f, settings.getMusicVolume(), 0.0f);
        settings.setMusicVolume(-12.3f);
        assertEquals(0.0f, settings.getMusicVolume(), 0.0f);
        settings.setSoundVolume(120.12f);
        assertEquals(1.0f, settings.getSoundVolume(), 0.0f);
        settings.setSoundVolume(-12.3f);
        assertEquals(0.0f, settings.getSoundVolume(), 0.0f);
    }

    @Override
    public void changedMusicOn() {
        calledChangedMusicOn = true;
    }

    @Override
    public void changedMusicVolume() {
        calledChangedMusicVolume = true;
    }

    @Override
    public void changedSoundVolume() {
        calledChangedSoundVolume = true;
    }

}
