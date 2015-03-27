package lambda.viewcontroller.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.assets.loaders.TextureLoader.TextureParameter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import lambda.viewcontroller.AudioManager;
import lambda.viewcontroller.StageViewController;
import lambda.viewcontroller.profiles.ProfileSelection;

/**
 * Represents the loading screen at program start.
 * 
 * @author Florian Fervers
 */
public class AssetViewController extends StageViewController {

    /**
     * Libgdx class that manages asset loading. Asset model.
     */
    private final AssetManager manager;

    /**
     * Creates a new instance of AssetViewController and loads all assets
     * required for it. Blocks until loading is complete.
     */
    public AssetViewController() {
        manager = new AssetManager();

        // Load assets only for loading screen and block until finished
        manager.load("data/backgrounds/loading.png", Texture.class);
        manager.finishLoading();

        // TODO progress bar etc
        Image image = new Image(manager.get("data/backgrounds/loading.png",
                Texture.class));
        image.setWidth(getStage().getWidth());
        image.setHeight(getStage().getHeight());
        getStage().addActor(image);
    }

    /**
     * Returns the asset manager.
     * 
     * @return the asset manager
     */
    public AssetManager getManager() {
        return manager;
    }

    /**
     * Called when the view controller is created.
     */
    @Override
    public void create(final AssetManager manager) {
        // UI elements for loading screen are set up in the constructor

    }

    /**
     * Renders this screen.
     * 
     * @param delta
     *            the time in seconds since the last render
     */
    @Override
    public void render(float delta) {
        if (manager.update()) {
            // Loading finished => go to profile selection
            getGame().createViewControllers();
            AudioManager.init();

            getGame().setScreen(ProfileSelection.class);
        }

        // TODO manager.getProgress();
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        getStage().act(delta);
        getStage().draw();
    }

    /**
     * Called when this screen should release all resources.
     */
    @Override
    public void dispose() {
        super.dispose();
        manager.dispose();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void show() {
        InputProcessor backProcessor = new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                // DO nothing
                return false;
            }
        };
        InputMultiplexer multiplexer = new InputMultiplexer(getStage(),
                backProcessor);
        Gdx.input.setInputProcessor(multiplexer);
    }

    /**
     * Queues all assets needed by this viewController to be loaded by the given
     * asset manager.
     * 
     * @param manager
     *            the asset manager
     */
    @Override
    public void queueAssets(AssetManager manager) {
        // All assets needed by the loading screen are loaded separately in the
        // constructor
        manager.load("data/skins/MasterSkin.atlas", TextureAtlas.class);
        manager.load("data/skins/MasterSkin.json", Skin.class,
                new SkinLoader.SkinParameter("data/skins/MasterSkin.atlas"));
        TextureParameter textureParameter = new TextureParameter();
        textureParameter.minFilter = TextureFilter.Linear;
        textureParameter.magFilter = TextureFilter.Linear;
        manager.load("data/backgrounds/default.png", Texture.class,
                textureParameter);
    }
}
