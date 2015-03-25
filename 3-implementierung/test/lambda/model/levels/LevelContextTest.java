package lambda.model.levels;

import org.junit.After;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.backends.lwjgl.LwjglFiles;
import com.badlogic.gdx.graphics.Color;

import java.util.LinkedList;

/**
 * @author Kay Schmitteckert
 */
public class LevelContextTest {

    private static LevelContext context;
    private static LevelModel level;
    private static AssetManager assets;
    private static LevelManager manager;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        Gdx.files = new LwjglFiles();
        assets = new AssetManager();
        manager = LevelManager.getLevelManager();
        // manager.queueAssets(assets);
    }

    @Before
    public void setUp() throws Exception {
    }

    /*
     * @Test public void testSandbox() { level = new LevelModel(0, null, null,
     * null, new LinkedList<TutorialMessageModel>(), new
     * LinkedList<ReductionStrategy>(), new LinkedList<ElementType>(), 3, 13,
     * 50, true, true, new LinkedList<Color>(), new LinkedList<Color>(),
     * ReductionStrategy.NORMAL_ORDER); //context = new LevelContext(level); }
     */

    @After
    public void tearDown() throws Exception {
    }

}
