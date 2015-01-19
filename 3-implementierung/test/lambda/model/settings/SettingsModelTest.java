package lambda.model.settings;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests a SettingsModel
 * 
 * @author Kai Fieger
 */
public class SettingsModelTest implements SettingsModelObserver {

    private static final float EPSILON = 0.0001f;
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
     * Sets musicOn to true and false,
     * while making sure it works and the correct SettingsModelOberver-methods are called.
     */
    @Test
    public void testSetMusicOn() {
        settings.setMusicOn(true);
        if (!settings.isMusicOn()) {
            fail("musicOn not true");
        }
        if (!calledChangedMusicOn) {
            fail("setMusicOn didn't call changedMusicOn()");
        }
        calledChangedMusicOn = false;
        settings.setMusicOn(false);
        if (settings.isMusicOn()) {
            fail("musicOn not false");
        }
        if (!calledChangedMusicOn) {
            fail("setMusicOn didn't call changedMusicOn()");
        }
    }
    
    /**
     * Checks if the music volume can be set.
     * while making sure that changedMusicVolume() from the obervers is called. 
     */
    @Test
    public void testSetMusicVolume() {
        float testValue = 12.12f;
        settings.setMusicVolume(testValue);
        assertEquals(testValue, settings.getMusicVolume(), EPSILON);
        if (!calledChangedMusicVolume) {
            fail("setMusicVolume didn't call changedMusicVolume()");
        }
    }
    
    /**
     * Checks if the sound volume can be set.
     * while making sure that changedSoundVolume() from the obervers is called. 
     */
    @Test
    public void testSetSoundVolume() {
        float testValue = 12.12f;
        settings.setSoundVolume(testValue);
        assertEquals(testValue, settings.getSoundVolume(), EPSILON);
        if (!calledChangedSoundVolume) {
            fail("setSoundVolume didn't call changedSoundVolume()");
        }
    }
    
    /**
     * Tests if setMusicVolume and setSoundVolume set the volumes to 0 or 100
     * depending on negative values or values bigger than 100.
     */
    @Test
    public void testForInvalidValues() {
        settings.setMusicVolume(120.12f);
        assertEquals(100.0f, settings.getMusicVolume(), 0.0f);
        settings.setMusicVolume(-12.3f);
        assertEquals(0.0f, settings.getMusicVolume(), 0.0f);
        settings.setSoundVolume(120.12f);
        assertEquals(100.0f, settings.getSoundVolume(), 0.0f);
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
