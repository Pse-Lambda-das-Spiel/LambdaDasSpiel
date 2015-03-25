package lambda.model.shop;


import static org.junit.Assert.*;
import lambda.model.profiles.ProfileManager;
import lambda.model.profiles.ProfileModel;
import lambda.model.profiles.ProfileSaveHelper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.backends.lwjgl.LwjglFiles;
import com.badlogic.gdx.files.FileHandle;
import com.libgdxtesting.GdxTestRunner;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;


/**
 * This test covers all Models from the shop package
 * 
 * @author: Kay Schmitteckert
 */
@RunWith(GdxTestRunner.class)
public class ShopModelTest {

    private static AssetManager assets;
    private static ShopModel shop;
    private static ProfileManager manager;

    private static String[] testNames = {"testName0", "testName1", "testName2"};
    private static String unusedName = "unusedName";
    private static FileHandle profileFolder;
    private static FileHandle nameFile;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        Gdx.files = new LwjglFiles();
        assets = new AssetManager();
        manager = ProfileManager.getManager();
        shop = ShopModel.getShop();
        shop.queueAssets(assets);
        assets.finishLoading();
        shop.setAllAssets(assets);
    }
    
    @Before
    public void setUp() throws Exception { 
    }

    @Test
    public void testIfDefaultEqualsActivated() {
        assertEquals("default", shop.getMusic().getActivatedItem().getFilename());
        assertEquals("default", shop.getImages().getActivatedItem().getFilename());
        assertEquals("default", shop.getElementUIContextFamilies().getActivatedItem().getFilename());
    }


    @Test
    public void testTypeNames() {
        assertEquals("music", shop.getMusic().getTypeName());
        assertEquals("images", shop.getImages().getTypeName());
        assertEquals("elements", shop.getElementUIContextFamilies().getTypeName());
    }
    
    @Test
    public void checkIfItemsAvailable() {
        //MUSIC
        //default not null
        assertNotNull(shop.getMusic().getActivatedItem().getMusic());
        //items in shop not null
        int n = shop.getMusic().getItems().size();
        for (int i = 0; i < n; i++) {
            assertNotNull(shop.getMusic().getItems().get(i).getMusic());
        }
        //IMAGES
        //default not null
        assertNotNull(shop.getImages().getActivatedItem().getImage());
        //items in shop not null
        n = shop.getImages().getItems().size();
        for (int i = 0; i < n; i++) {
            assertNotNull(shop.getImages().getItems().get(i).getImage());
        }
        //ELEMENTS
        //default not null
        assertNotNull(shop.getElementUIContextFamilies().getActivatedItem().getAbstraction());
        assertNotNull(shop.getElementUIContextFamilies().getActivatedItem().getVariable());
        assertNotNull(shop.getElementUIContextFamilies().getActivatedItem().getParenthesis());
        //items in shop not null
        n = shop.getElementUIContextFamilies().getItems().size();
        for (int i = 0; i < n; i++) {
            assertNotNull(shop.getElementUIContextFamilies().getItems().get(i).getAbstraction());
            assertNotNull(shop.getElementUIContextFamilies().getItems().get(i).getVariable());
            assertNotNull(shop.getElementUIContextFamilies().getItems().get(i).getParenthesis());
        }
    }
    
    @Test
    public void checkItemDetails() {
        //Price
        assertEquals(10, shop.getMusic().getItems().get(0).getPrice());
        assertEquals(10, shop.getImages().getItems().get(0).getPrice());
        assertEquals(30, shop.getElementUIContextFamilies().getItems().get(0).getPrice());
        
        //Id
        assertEquals("0", shop.getMusic().getItems().get(0).getId());
        assertEquals("0", shop.getImages().getItems().get(0).getId());
        assertEquals("0", shop.getElementUIContextFamilies().getItems().get(0).getId());
    }
    
    @Test 
    public void buyAndActivate() {
        
        manager.createProfile();
        manager.setCurrentProfile("");
        manager.getCurrentProfile().setCoins(20);
        
        // check that the item isn't already purchased
        assertFalse(shop.getMusic().getItems().get(0).isPurchased());
        // buy and check if it's purchased now
        shop.getMusic().getItems().get(0).buy();
        assertTrue(shop.getMusic().getItems().get(0).isPurchased());
        // check that the item isn't already activated
        assertFalse(shop.getMusic().getItems().get(0).isActivated());
        assertEquals("default", shop.getMusic().getActivatedItem().getFilename());
        // activate and check activated status
        shop.getMusic().getItems().get(0).activate();
        assertTrue(shop.getMusic().getItems().get(0).isActivated());
        // deactivate and check if activated item equals default
        shop.getMusic().getActivatedItem().deactivate();
        assertEquals("default", shop.getMusic().getActivatedItem().getFilename()); 
    }

    
    @After
    public void tearDown() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        shop = null;
    }

}
