package lambda.model.levels;

import static org.junit.Assert.*;
import lambda.model.profiles.ProfileManager;
import lambda.model.shop.ShopModel;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.libgdxtesting.GdxTestRunner;

@RunWith(GdxTestRunner.class)
public class LevelContextTest {

    private static LevelContext context;
    private static LevelManager manager;
    private static AssetManager assets;
    private static ShopModel shop;
    
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        assets = new AssetManager();
        manager = LevelManager.getLevelManager();
        manager.queueAssets(assets);
        shop = ShopModel.getShop();
        shop.queueAssets(assets);
        assets.finishLoading();
        shop.setAllAssets(assets);
    }
    
    @Before
    public void setUp() throws Exception {
        
    }
    
    @Test
    public void testLevelIds() {
        for (int i = 1; i < manager.getNumberOfLevels(); i++) {
            assertEquals(i, manager.getLevel(i).getId());
        }
    }
    
    @Test(expected = InvalidLevelIdException.class)
    public void invalidLevelId() {
        manager.getLevel(48);
    }
    
    @Test
    public void testAnimationsNotNull() {
        context = new LevelContext(manager.getLevel(1));
        assertNotNull(context.getMagicAnimation());
        assertNotNull(context.getCloudAnimation());
    }
    
    @Test
    public void testItemsForSandbox() {
        context = new LevelContext(manager.getLevel(0));
        assertEquals(shop.getMusic().getActivatedItem(), shop.getMusic().getDefaultItem());
        assertNotNull(context.getMusic());
        assertNotNull(context.getBgImage());
    }
    
    @Test
    public void testIfNotNull() {
        context = new LevelContext(manager.getLevel(13));
        assertNotNull(context.getLevelModel());
        assertNotNull(context.getGlow());
        assertNotNull(context.getElementUIContextFamily());
        assertNotNull(manager.getAllColors());
        assertNotNull(manager.getColorsToVariables());
        assertNotNull(manager.getVariablesToColors());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void noVariableForColor() {
        manager.convertColorToVariable(new Color(0, 0, 0, 0));
    }

    @After
    public void tearDown() throws Exception {
        context = null;
    }
    
    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        
    }
    
}
