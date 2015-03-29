package lambda.model.profiles;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.lang.reflect.Field;
import java.util.List;

import lambda.model.shop.BackgroundImageItemModel;
import lambda.model.shop.ElementUIContextFamily;
import lambda.model.shop.MusicItemModel;
import lambda.model.shop.ShopItemTypeModel;
import lambda.model.shop.ShopModel;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.libgdxtesting.GdxTestRunner;

/**
 * Tests the functionality of the ProfileManager and with it also the Profile{Save,Load}Helper.
 * 
 * @author Kai Fieger
 */
@RunWith(GdxTestRunner.class)
public class ProfileManagerTest implements ProfileManagerObserver {

    private boolean calledChangedProfile;
    private boolean calledChangedProfileList;
    private ProfileManager manager;

    private static String[] testNames = {"testName0", "testName1", "testName2"};
    private static String unusedName = "unusedName";
    private static FileHandle profileFolder;
    private static FileHandle nameFile;

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        deleteProfileFolder();
        deleteNames();
    }

    @Before
    public void setUp() throws Exception {
        profileFolder = Gdx.files.local(ProfileManager.PROFILE_FOLDER);
        nameFile = Gdx.files.local(ProfileManager.PROFILE_FOLDER + ".json");
        calledChangedProfile = false;
        calledChangedProfileList = false;
        Field a = ProfileManager.class.getDeclaredField("manager");
        a.setAccessible(true);
        a.set(null, null);
    }

    @After
    public void tearDown() throws Exception {
        deleteProfileFolder();
        deleteNames();
        if (manager != null) {
            manager.removeObserver(this);
            manager = null;
        }
    }

    /**
     * Tests for an exception if the profileFolder isn't a directory.
     */
    @Test(expected = InvalidProfilesException.class)
    public void testNoDir() {
        profileFolder = Gdx.files.local(ProfileManager.PROFILE_FOLDER);
        profileFolder.writeString("This should be a folder", false);
        init();
    }

    /**
     * Tests for an exception if the profileFolder contains too many profiles.
     */
    @Test(expected = InvalidProfilesException.class)
    public void testTooManyProfiles() {
        String[] maxProfiles = new String[ProfileManager.MAX_NUMBER_OF_PROFILES + 1];
        for (int i = 0; i < maxProfiles.length; i++) {
            maxProfiles[i] = "testName" + i;
        }
        saveProfiles(maxProfiles);
        init();
    }
    
    /**
     * Tests, if ProfileManager can load correctly without savefiles. 
     */
    @Test
    public void testLoadWithoutSaves() {
        init();
        assertTrue(manager == ProfileManager.getManager());
        assertEquals(0, manager.getNames().size());
        assertTrue(profileFolder.exists());
        assertTrue(nameFile.exists());
    }

    /**
     * Tests if the ProfileManager loads profiles.
     */
    @Test
    public void testLoadWithoutNames() {
        saveProfiles(testNames);
        init();
        List<String> names = manager.getNames();
        assertEquals(testNames.length, names.size());
        for (int i = 0; i < testNames.length; i++) {
            assertTrue(names.contains(testNames[i]));
        }
    }
    
    /**
     * Tests if the ProfileManager loads and keeps a list of profiles in correct order. (given in the nameFile)
     */
    @Test
    public void testLoadWithCorrectNames() {
        saveProfiles(testNames);
        saveNames(testNames);
        init();
        List<String> names = manager.getNames();
        assertEquals(testNames.length, names.size());
        for (int i = 0; i < testNames.length; i++) {
            assertEquals(testNames[i], names.get(i));
        }
    }
    
    /**
     * Tests if the ProfileManager loads all profiles even if not all names are in the nameFile.
     */
    @Test
    public void testLoadWithMissingNames() {
        saveProfiles(testNames);
        saveNames(new String[] {testNames[0], testNames[1]});
        init();
        List<String> names = manager.getNames();
        assertEquals(testNames.length, names.size());
        for (int i = 0; i < testNames.length; i++) {
            assertTrue(names.contains(testNames[i]));
        }
    }
    
    /**
     * Tests if the ProfileManager loads all profiles even if names in the nameFile are doubled.
     */
    @Test
    public void testLoadWithSameNames() {
        saveProfiles(testNames);
        saveNames(new String[] {testNames[0], testNames[0], testNames[0]});
        init();
        List<String> names = manager.getNames();
        assertEquals(testNames.length, names.size());
        for (int i = 0; i < testNames.length; i++) {
            assertTrue(names.contains(testNames[i]));
        }
    }
    
    /**
     * Tests if the ProfileManager loads all profiles even if names in the nameFile are wrong.
     */
    @Test
    public void testLoadWithWrongNames() {
        saveProfiles(testNames);
        saveNames(new String[] {unusedName, testNames[1], testNames[2]});
        init();
        List<String> names = manager.getNames();
        assertEquals(testNames.length, names.size());
        for (int i = 0; i < testNames.length; i++) {
            assertTrue(names.contains(testNames[i]));
        }
    }
    
    /**
     * Tests if the ProfileManager loads all profiles even if a name in the nameFile is empty/"".
     */
    @Test
    public void testLoadWithEmptyName() {
        saveProfiles(testNames);
        saveNames(new String[] {"", testNames[1], testNames[2]});
        init();
        List<String> names = manager.getNames();
        assertEquals(testNames.length, names.size());
        for (int i = 0; i < testNames.length; i++) {
            assertTrue(names.contains(testNames[i]));
        }
    }
    
    /**
     * Tests if the ProfileManager loads all profiles even if nameFile contains an error.
     */
    @Test
    public void testLoadWithCorruptedNames() {
        saveProfiles(testNames);
        nameFile.writeString("dead", false);
        init();
        List<String> names = manager.getNames();
        assertEquals(testNames.length, names.size());
        for (int i = 0; i < testNames.length; i++) {
            assertTrue(names.contains(testNames[i]));
        }
    }
    
    /**
     * Tests if the ProfileManager ignores files that aren't directories while loading. 
     */
    @Test
    public void testLoadExtraFiles() {
        saveProfiles(testNames);
        FileHandle extra = new FileHandle(profileFolder.name() + "/NotSupposedToBeHere.json");
        extra.writeString("ButThatShouldn'tMatter", false);
        assertTrue(extra.exists());
        saveNames(testNames);
        init();
        List<String> names = manager.getNames();
        assertEquals(testNames.length, names.size());
        for (int i = 0; i < testNames.length; i++) {
            assertEquals(testNames[i], names.get(i));
        }
    }

    /**
     * Tests setting and getting the game's current profile
     */
    @Test
    public void testCurrentProfile() {
        saveProfiles(testNames);
        init();
        assertFalse(manager.setCurrentProfile(unusedName));
        assertFalse(calledChangedProfile);
        assertTrue(manager.setCurrentProfile(testNames[0]));
        assertTrue(calledChangedProfile);
        assertEquals(testNames[0], manager.getCurrentProfile().getName());
    }

    /**
     * Tests the creation of a profile.
     */
    @Test
    public void testCreateProfile() {
        saveProfiles(testNames);
        init();
        ProfileModel newProfile = manager.createProfile();
        assertTrue(calledChangedProfileList);
        calledChangedProfileList = false;
        assertNotNull(newProfile);
        assertNull(manager.createProfile());
        assertFalse(calledChangedProfileList);
        assertTrue(manager.setCurrentProfile(""));
        //tests for error. profiles with name "" can't be saved or deleted.
        manager.save(newProfile.getName());
        manager.delete(newProfile.getName());
        assertTrue(calledChangedProfileList);
        calledChangedProfileList = false;
        assertFalse(manager.setCurrentProfile(""));
    }

    /**
     * Tries to create a new profile, while the maximum number is reached.
     */
    @Test
    public void testCreateTooManyProfiles() {
        String[] maxProfiles = new String[ProfileManager.MAX_NUMBER_OF_PROFILES];
        for (int i = 0; i < maxProfiles.length; i++) {
            maxProfiles[i] = "testName" + i;
        }
        saveProfiles(maxProfiles);
        init();
        assertNull(manager.createProfile());
    }
    
    /**
     * Tests the renaming of a profile.
     */
    @Test
    public void testRenaming() {
        saveProfiles(testNames);
        saveNames(testNames);
        init();
        // Sets current profile to testNames[1]
        FileHandle save = Gdx.files.local(ProfileManager.PROFILE_FOLDER + "/" + testNames[1]);
        FileHandle saveTemp = Gdx.files.local(ProfileManager.PROFILE_FOLDER + "/" + unusedName);
        assertTrue(save.exists());
        List<String> names = manager.getNames();
        assertEquals(testNames[1], names.get(1));
        assertTrue(manager.setCurrentProfile(testNames[1]));
        // Changes name of current profile (testNames[1]) to unusedName
        assertTrue(manager.changeCurrentName(unusedName));
        assertEquals(unusedName, manager.getCurrentProfile().getName());
        assertTrue(calledChangedProfileList);
        calledChangedProfileList = false;
        names = manager.getNames();
        assertEquals(testNames.length, names.size());
        assertEquals(unusedName, names.get(1));
        assertFalse(save.exists());
        assertTrue(saveTemp.exists());
    }
    
    /**
     * Tests the renaming of a profile to null.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRenamingNull() {
        saveProfiles(testNames);
        saveNames(testNames);
        init();
        manager.setCurrentProfile(testNames[0]);
        manager.changeCurrentName(null);
    }
    
    /**
     * Tests the renaming of a profile to "".
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRenamingEmpty() {
        saveProfiles(testNames);
        saveNames(testNames);
        init();
        manager.setCurrentProfile(testNames[0]);
        manager.changeCurrentName("");
    }
    
    /**
     * Tests the renaming of a profile into an existent name.
     */
    @Test
    public void testRenamingSame() {
        saveProfiles(testNames);
        init();
        manager.setCurrentProfile(testNames[0]);
        assertTrue(manager.changeCurrentName(testNames[0]));
        assertFalse(manager.changeCurrentName(testNames[1]));
    }

    /**
     * Tests deleting profiles.
     */
    @Test
    public void testDeleteProfile() {
        saveProfiles(testNames);
        saveNames(testNames);
        init();
        // deletes currentProfile
        manager.setCurrentProfile(testNames[0]);
        manager.delete(testNames[0]);
        assertTrue(calledChangedProfileList);
        calledChangedProfileList = false;
        assertNull(manager.getCurrentProfile());
        List<String> names = manager.getNames();
        assertEquals(testNames.length - 1, names.size());
        for (int i = 1; i < testNames.length; i++) {
            assertEquals(testNames[i], names.get(i - 1));
        }
        assertFalse(Gdx.files.local(ProfileManager.PROFILE_FOLDER + "/" + testNames[0]).exists());
        // deletes a profile
        manager.delete(testNames[1]);
        assertTrue(calledChangedProfileList);
        calledChangedProfileList = false;
        names = manager.getNames();
        assertEquals(testNames.length - 2, names.size());
        for (int i = 2; i < testNames.length; i++) {
            assertEquals(testNames[i], names.get(i - 2));
        }
        assertFalse(Gdx.files.local(ProfileManager.PROFILE_FOLDER + "/" + testNames[1]).exists());
    }

    /**
     * Checks if trying to save or delete a profile with an unused name (=> a
     * nonexistent profile) has unwanted side effects.
     */
    @Test
    public void testDeleteSaveWrongProfile() {
        saveProfiles(testNames);
        init();
        FileHandle save = Gdx.files.local(ProfileManager.PROFILE_FOLDER + "/" + unusedName);
        assertFalse(save.exists());
        manager.save(unusedName);
        assertFalse(save.exists());
        manager.delete(unusedName);
        assertFalse(save.exists());
    }
    
    /**
     * Tests for an exception when calling setCurrentProfile() with null as argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetCurrentNull() {
        init();
        manager.setCurrentProfile(null);
    }
    
    /**
     * Tests for an exception when calling save() with null as argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSaveNull() {
        init();
        manager.save(null);
    }
    
    /**
     * Tests for an exception when calling delete() with null as argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testDeleteNull() {
        init();
        manager.delete(null);
    }
    
    /**
     * Checks if profileEdit is initialized.
     */
    @Test
    public void testProfileEdit() {
        init();
        assertNotNull(manager.getProfileEdit());
    }
    
    /**
     * Tests some basic functions of the Profile{Load,Save}Helper concerning the profile's shop state.
     */
    @Test
    public void testShopLoadSave() {
        ProfileLoadHelper.loadIntoShop("doesnotexist");
        ShopModel shop = ShopModel.getShop();
        ShopItemTypeModel<?>[] types = {shop.getElementUIContextFamilies(), shop.getImages(), shop.getMusic()};
        ElementUIContextFamily element = new ElementUIContextFamily("0", 1, "test");
        BackgroundImageItemModel background = new BackgroundImageItemModel("0", 1, "test");
        MusicItemModel music = new MusicItemModel("0", 1, "test");
        for (ShopItemTypeModel<?> type : types) {
            type.getItems().clear();
            type.setActivatedItem(null);
        }
        shop.getElementUIContextFamilies().getItems().add(element);
        shop.getImages().getItems().add(background);
        shop.getMusic().getItems().add(music);
        saveProfiles(testNames);
        init();
        manager.setCurrentProfile(testNames[0]);
        shop.getElementUIContextFamilies().setActivatedItem(element);
        shop.getImages().setActivatedItem(background);
        shop.getMusic().setActivatedItem(music);
        manager.save(testNames[0]);
        manager.setCurrentProfile(testNames[1]);
        for (ShopItemTypeModel<?> type : types) {
            assertNull(type.getActivatedItem());
        }
        manager.save(testNames[1]);
        manager.setCurrentProfile(testNames[0]);
        for (ShopItemTypeModel<?> type : types) {
            assertNotNull(type.getActivatedItem());
        }
        manager.save(testNames[0]);
        manager.createProfile();
        manager.setCurrentProfile("");
        for (ShopItemTypeModel<?> type : types) {
            assertNull(type.getActivatedItem());
        }
    }

    @Override
    public void changedProfile() {
        calledChangedProfile = true;
    }

    @Override
    public void changedProfileList() {
        calledChangedProfileList = true;
    }

    private void init() {
        manager = ProfileManager.getManager();
        manager.addObserver(this);
    }

    private static void saveProfiles(String[] names) {
        if (profileFolder.exists()) {
            profileFolder.deleteDirectory();
        }
        profileFolder.mkdirs();
        for (int i = 0; i < names.length; i++) {
            ProfileSaveHelper.saveProfile(new ProfileModel(names[i]));
        }
    }

    private static void deleteProfileFolder() {
        if (profileFolder.exists()) {
            profileFolder.deleteDirectory();
        }
    }

    private static void saveNames(String[] names) {
        nameFile.writeString(new Json().prettyPrint(names), false);
    }

    private static void deleteNames() {
        if (nameFile.exists()) {
            nameFile.delete();
        }
    }

}
