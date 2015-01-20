package lambda.model.profiles;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.util.List;

import lambda.util.ProfileSaveHelper;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

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
    private static File profileFolder = new File(ProfileManager.PROFILE_FOLDER);
    
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        if (profileFolder.exists()) {
            deleteFolder(profileFolder);
        }
        profileFolder.mkdir();
        for (int i = 0; i < testNames.length; i++) {
            ProfileSaveHelper.saveProfile(new ProfileModel(testNames[i]));
        }
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        deleteFolder(profileFolder);
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
        File save = new File(ProfileManager.PROFILE_FOLDER + "/" + testNames[0]);
        File saveTemp = new File(ProfileManager.PROFILE_FOLDER + "/" + unusedName);
        assertTrue(save.exists());
        assertTrue(manager.setCurrentProfile(testNames[0]));
        assertTrue(manager.changeCurrentName(unusedName));
        assertEquals(unusedName, manager.getCurrentProfile().getName());
        assertTrue(calledChangedProfileList);
        calledChangedProfileList = false;
        List<String> names = manager.getNames();
        assertEquals(testNames.length, names.size());
        assertEquals(unusedName, names.get(names.size() - 1));
        assertFalse(save.exists());
        assertTrue(saveTemp.exists());
        
        assertTrue(manager.changeCurrentName(testNames[0]));
        assertEquals(testNames[0], manager.getCurrentProfile().getName());
        names = manager.getNames();
        assertEquals(testNames.length, names.size());
        assertEquals(testNames[0], names.get(names.size() - 1));
        assertFalse(saveTemp.exists());
        assertTrue(save.exists());
    }
    
    /**
     * Tests the scenario of deleting a current profile
     * and than creating a new one (with the same name).
     */
    @Test
    public void testDeleteCreateProfile() {
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
        File save = new File(ProfileManager.PROFILE_FOLDER + "/" + testNames[0]);
        assertFalse(save.exists());
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
     * Checks if trying to save or delete a profile with an unused name
     * (=> a nonexistent profile) has unwanted side effects.
     */
    @Test
    public void testDeleteSaveWrongProfile() {
        File save = new File(ProfileManager.PROFILE_FOLDER + "/" + unusedName);
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
    
    private static void deleteFolder(File folder) {
        for (File file : folder.listFiles()) {
            if (file.isDirectory()) {
                deleteFolder(file);
            }
            file.delete();
        }
        folder.delete();
    }
}
