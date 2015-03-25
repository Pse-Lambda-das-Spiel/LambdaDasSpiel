package lambda.model.levels;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.badlogic.gdx.assets.AssetManager;
import com.libgdxtesting.GdxTestRunner;

@RunWith(GdxTestRunner.class)
public class LevelContextTest {

    private static LevelContext context;
    private static LevelManager manager;
    private static AssetManager assets;
    
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        assets = new AssetManager();
        manager = LevelManager.getLevelManager();
        manager.queueAssets(assets);
        assets.finishLoading();
        //context = new LevelContext(manager.getLevel(0));
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
    public void testAnimationPaths() {
        context = new LevelContext(manager.getLevel(1));       
    }

    @After
    public void tearDown() throws Exception {
        
    }
    
    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        
    }
    
}
