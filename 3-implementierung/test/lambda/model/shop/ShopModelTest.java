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
    MusicItemModel musicItem;
    BackgroundImageItemModel bgItem;
    ElementUIContextFamily elementItem;
    ShopItemTypeModel<MusicItemModel> music;
    ShopItemTypeModel<BackgroundImageItemModel> bgImages;
    ShopItemTypeModel<ElementUIContextFamily> elements;

    @Before
    public void setUp() throws Exception {
        Gdx.files = new LwjglFiles();
        assets = new AssetManager();
        /*
        music = new ShopItemTypeModel<MusicItemModel>("Music");
        bgImages = new ShopItemTypeModel<BackgroundImageItemModel>("Images");
        elements = new ShopItemTypeModel<ElementUIContextFamily>("Elements");
        musicItem = new MusicItemModel("0", 10, "Happy Day.mp3");
        music.getItems().add(musicItem);
        bgItem = new BackgroundImageItemModel("0", 20, "Jungle.jpg");
        bgImages.getItems().add(bgItem);
        elementItem = new ElementUIContextFamily("0", 30, "Hat.atlas");
        elements.getItems().add(elementItem);
        */
        shop = ShopModel.getShop();
        shop.queueAssets(assets);
        assets.finishLoading();
        
    }

    @Test
    public void testpaths() {
        
    }

    @Test
    public void testMusicItemLoad() {
        
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
