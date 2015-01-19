package lambda.model.profiles;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests ProfileModels
 * 
 * @author Kai Fieger
 */
public class ProfileModelTest implements ProfileModelObserver {

    private boolean calledChangedLevelIndex;
    private boolean calledChangedCoins;
    private ProfileModel profile;
    
    @Before
    public void setUp() throws Exception {
        calledChangedLevelIndex = false;
        calledChangedCoins = false;
        profile = new ProfileModel("testName");
        profile.addObserver(this);
    }

    @After
    public void tearDown() throws Exception {
        profile.removeObserver(this);
    }

    /**
     * Tests a new profile for correct standard values.
     */
    @Test
    public void testNewProfile() {
        assertEquals("testName", profile.getName());
        assertEquals("a0", profile.getAvatar());
        assertEquals("de", profile.getLanguage());
        assertEquals(1, profile.getLevelIndex());
        assertEquals(0, profile.getCoins());
        assertNotNull(profile.getSettings());
        assertNotNull(profile.getShop());
        assertNotNull(profile.getStatistics());
    }
    
    /**
     * Tests the ProfileModel(String, ProfileModel) constructor, which is used for renaming a profile;
     */
    @Test
    public void testRenameProfile() {
        profile.setAvatar("a1");
        profile.setLanguage("en");
        profile.setLevelIndex(2);
        profile.setCoins(100);
        ProfileModel newProfile = new ProfileModel("testName2", profile);
        assertEquals("testName2", newProfile.getName());
        assertEquals("a1", newProfile.getAvatar());
        assertEquals("en", newProfile.getLanguage());
        assertEquals(2, newProfile.getLevelIndex());
        assertEquals(100, newProfile.getCoins());
        assertEquals(profile.getSettings(), newProfile.getSettings());
        assertEquals(profile.getShop(), newProfile.getShop());
        assertEquals(profile.getStatistics(), newProfile.getStatistics()); 
    }
    
    /**
     * Tests the getters and setters of a ProfileModel-object and makes
     * sure that the correct ProfileModelOberver-methods from the obervers are called. 
     */
    @Test
    public void testSettersGetters() {
        assertEquals("testName", profile.getName());
        profile.setAvatar("a1");
        assertEquals("a1", profile.getAvatar());
        profile.setLanguage("en");
        assertEquals("en", profile.getLanguage());
        profile.setLevelIndex(2);
        if (!calledChangedLevelIndex) {
            fail("setLevelIndex didn't call changedLevelIndex()");
        }
        assertEquals(2, profile.getLevelIndex());
        profile.setCoins(100);
        if (!calledChangedCoins) {
            fail("setCoins didn't call changedCoins()");
        }
        assertEquals(100, profile.getCoins());
        assertNotNull(profile.getSettings());
        assertNotNull(profile.getShop());
        assertNotNull(profile.getStatistics());
    }
    
    @Override
    public void changedLevelIndex() {
        calledChangedLevelIndex = true;
    }

    @Override
    public void changedCoins() {
        calledChangedCoins = true;
    }

}
