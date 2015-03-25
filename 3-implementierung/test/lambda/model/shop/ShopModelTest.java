package lambda.model.shop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.backends.lwjgl.LwjglFiles;
import com.libgdxtesting.GdxTestRunner;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

/**
 * @author: Kay Schmitteckert
 */
@RunWith(GdxTestRunner.class)
public class ShopModelTest {

    private static AssetManager assets;
    private static ShopModel shop;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        Gdx.files = new LwjglFiles();
        assets = new AssetManager();
        shop = ShopModel.getShop();
        shop.queueAssets(assets);
        assets.finishLoading();
        shop.setAllAssets(assets);
    }

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testpaths() {

    }

    @Test
    public void testMusicItemLoad() {

    }

    /*
     * @Test public void testMusic() { ShopModel.getShop().queueAssets(assets);
     * shop.setAllItems(assets); assets.load("data/items/music/00.mp3",
     * Music.class); Music musicloaded =
     * shop.getMusic().getItems().get(0).getMusic(); Music music =
     * assets.get("data/items/music/00.mp3"); assertEquals(music ,
     * shop.getMusic().getItems().get(0).getMusic()); }
     */
    @After
    public void tearDown() throws Exception {
        shop = null;
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {

    }

}
