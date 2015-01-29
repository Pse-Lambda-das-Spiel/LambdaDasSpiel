package lambda.model.shop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.backends.lwjgl.LwjglFiles;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author: Kay Schmitteckert
 */
public class ShopModelTest {

    private static AssetManager assets;
    private ShopModel shop;

    @Before
    public void setUp() throws Exception {
        Gdx.files = new LwjglFiles();
        assets = new AssetManager();
        shop = ShopModel.getShop();
        shop.setAssetManager(assets);
    }

    @Test
    public void testpaths() {
        assertEquals(shop.getMusicFilePaths()[0], "data/items/music/00.json");
    }

    @Test
    public void testMusicItemLoad() {
        MusicItemModel music = shop.loadMusicItem(Gdx.files.internal("data/items/music/00.json"));
    }

    /*
    @Test
    public void testMusic() {
        ShopModel.getShop().queueAssets(assets);
        shop.setAllItems(assets);
        assets.load("data/items/music/00.mp3", Music.class);
        Music musicloaded = shop.getMusic().getItems().get(0).getMusic();
        Music music = assets.get("data/items/music/00.mp3");
        assertEquals(music , shop.getMusic().getItems().get(0).getMusic());
    }
*/
    @After
    public void tearDown() throws Exception {
    }


}
