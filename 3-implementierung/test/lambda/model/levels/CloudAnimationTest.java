package lambda.model.levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.backends.lwjgl.LwjglFiles;
import com.badlogic.gdx.graphics.Texture;
import lambda.viewcontroller.level.CloudAnimation;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author: Kay Schmitteckert
 */
public class CloudAnimationTest {

    private CloudAnimation cloud;
    private static AssetManager assets;


    @Before
    public void setUp() throws Exception {
        Gdx.files = new LwjglFiles();
        assets = new AssetManager();
        assets.load("data/sheets/cloud.png", Texture.class);
        while(!(assets.update())) {
            System.out.print(".");
        }
            cloud = new CloudAnimation(assets.get("data/sheets/cloud.png"));
    }

    @Test
    public void testVariableUIContext() {


    }

    @After
    public void tearDown() throws Exception {
    }




}
