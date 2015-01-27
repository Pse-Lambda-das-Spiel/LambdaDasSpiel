package lambda.model.profiles;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglFiles;
import com.badlogic.gdx.files.FileHandle;

/**
 * Tests the functionality of the ProfileManager.
 * 
 * @author Kai Fieger
 */
public class ProfileManagerTest implements ProfileManagerObserver {

    private boolean calledChangedProfile;
    private boolean calledChangedProfileList;
    private ProfileManager manager;

    private static String[] testNames = {"testName0", "testName1", "testName2"};
    private static String unusedName = "unusedName";
    private static FileHandle profileFolder;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        Gdx.files = new LwjglFiles();
        profileFolder = Gdx.files.local(ProfileManager.PROFILE_FOLDER);
        if (profileFolder.exists()) {
            profileFolder.deleteDirectory();
        }
        profileFolder.mkdirs();
        for (int i = 0; i < testNames.length; i++) {
            ProfileSaveHelper.saveProfile(new ProfileModel(testNames[i]));
        }
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        profileFolder.deleteDirectory();
    }

    @Before
    public void setUp() throws Exception {
        calledChangedProfile = false;
        calledChangedProfileList = false;
        manager = ProfileManager.getManager();
        manager.addObserver(this);
    }

    @After
    public void tearDown() throws Exception {
        manager.removeObserver(this);
    }

    /**
     * Tests if the ProfileManager loads and keeps a correct list of profiles
     */
    @Test
    public void testProfileLoad() {
        List<String> names = manager.getNames();
        assertEquals(testNames.length, names.size());
        for (int i = 0; i < testNames.length; i++) {
            assertTrue(names.contains(testNames[i]));
        }
    }

    /**
     * Tests setting and getting the game's current profile
     */
    @Test
    public void testCurrentProfile() {
        assertFalse(manager.setCurrentProfile(unusedName));
        assertFalse(calledChangedProfile);
        assertTrue(manager.setCurrentProfile(testNames[0]));
        assertTrue(calledChangedProfile);
        assertEquals(testNames[0], manager.getCurrentProfile().getName());
    }

    /**
     * Tests the renaming of profiles.
     */
    @Test
    public void testRenaming() {
        // Sets current profile to testNames[0]
        FileHandle save = Gdx.files.local(ProfileManager.PROFILE_FOLDER + "/" + testNames[0]);
        FileHandle saveTemp = Gdx.files.local(ProfileManager.PROFILE_FOLDER + "/" + unusedName);
        assertTrue(save.exists());
        List<String> names = manager.getNames();
        assertEquals(testNames.length, names.size());
        int profilePosition = -1;
        int j = 0;
        for (String name : names) {
            if (name.equals(testNames[0])) {
                profilePosition = j;
            }
            j++;
        }
        assertTrue(manager.setCurrentProfile(testNames[0]));
        assertNotEquals(-1, profilePosition);
        // Changes name of current profile (testNames[0]) to unusedName
        assertTrue(manager.changeCurrentName(unusedName));
        assertEquals(unusedName, manager.getCurrentProfile().getName());
        assertTrue(calledChangedProfileList);
        calledChangedProfileList = false;
        names = manager.getNames();
        assertEquals(testNames.length, names.size());
        assertEquals(unusedName, names.get(profilePosition));
        assertFalse(save.exists());
        assertTrue(saveTemp.exists());
        // Changes name of current profile (unusedName) back to testNames[0]
        assertTrue(manager.changeCurrentName(testNames[0]));
        assertEquals(testNames[0], manager.getCurrentProfile().getName());
        names = manager.getNames();
        assertEquals(testNames.length, names.size());
        assertEquals(testNames[0], names.get(profilePosition));
        assertFalse(saveTemp.exists());
        assertTrue(save.exists());
    }

    /**
     * Tests the scenario of deleting a current profile and than creating a new
     * one (with the same name).
     */
    @Test
    public void testDeleteCreateProfile() {
        // deletes currentProfile
        manager.setCurrentProfile(testNames[0]);
        manager.delete(testNames[0]);
        assertTrue(calledChangedProfileList);
        calledChangedProfileList = false;
        assertNull(manager.getCurrentProfile());
        List<String> names = manager.getNames();
        assertEquals(testNames.length - 1, names.size());
        for (int i = 0; i < names.size(); i++) {
            assertNotEquals(testNames[0], names.get(i));
        }
        FileHandle save = Gdx.files.local(ProfileManager.PROFILE_FOLDER + "/" + testNames[0]);
        assertFalse(save.exists());
        // creates deleted profile again (same name)
        ProfileModel newProfile = manager.createProfile();
        assertTrue(calledChangedProfileList);
        calledChangedProfileList = false;
        assertNotNull(newProfile);
        assertTrue(manager.setCurrentProfile(newProfile.getName()));
        assertTrue(manager.changeCurrentName(testNames[0]));
        assertEquals(testNames[0], manager.getCurrentProfile().getName());
        assertTrue(calledChangedProfileList);
        calledChangedProfileList = false;
        assertTrue(save.exists());
        names = manager.getNames();
        assertEquals(testNames.length, names.size());
        assertEquals(testNames[0], names.get(names.size() - 1));
    }

    /**
     * Checks if trying to save or delete a profile with an unused name (=> a
     * nonexistent profile) has unwanted side effects.
     */
    @Test
    public void testDeleteSaveWrongProfile() {
        FileHandle save = Gdx.files.local(ProfileManager.PROFILE_FOLDER + "/" + unusedName);
        assertFalse(save.exists());
        manager.save(unusedName);
        assertFalse(save.exists());
        manager.delete(unusedName);
        assertFalse(save.exists());
    }

    @Override
    public void changedProfile() {
        calledChangedProfile = true;
    }

    @Override
    public void changedProfileList() {
        calledChangedProfileList = true;
    }

}
