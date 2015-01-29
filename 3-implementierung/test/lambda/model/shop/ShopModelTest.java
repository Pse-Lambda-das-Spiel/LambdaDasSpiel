package lambda.model.shop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.backends.lwjgl.LwjglFiles;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author: Kay Schmitteckert
 */
public class ShopModelTest {

    private static AssetManager assets;

    @Before
    public void setUp() throws Exception {
        Gdx.files = new LwjglFiles();
        assets = new AssetManager();
    }

    @Test
    public void singletonTest() {
        ShopModel shop = ShopModel.getShop();
    }

    @After
    public void tearDown() throws Exception {
    }


}
