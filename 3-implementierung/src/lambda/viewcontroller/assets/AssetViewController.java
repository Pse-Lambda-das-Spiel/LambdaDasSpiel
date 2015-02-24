package lambda.viewcontroller.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import lambda.model.levels.LevelContext;
import lambda.model.levels.LevelLoadHelper;
import lambda.model.levels.LevelManager;
import lambda.model.levels.LevelModel;
import lambda.model.shop.ElementUIContextFamily;
import lambda.model.shop.ShopModel;
import lambda.viewcontroller.ViewController;
import lambda.viewcontroller.achievements.AchievementMenuViewController;
import lambda.viewcontroller.editor.EditorViewController;
import lambda.viewcontroller.level.AbstractionUIContext;
import lambda.viewcontroller.level.ParanthesisUIContext;
import lambda.viewcontroller.level.VariableUIContext;
import lambda.viewcontroller.profiles.ProfileSelection;
import lambda.viewcontroller.shop.ShopViewController;

/**
 * Represents the loading screen at program start.
 * 
 * @author Florian Fervers
 */
public class AssetViewController extends ViewController {
    /**
     * Holds all actors that are displayed in this screen.
     */
    private final Stage stage;
    /**
     * Libgdx class that manages asset loading. Asset model.
     */
    private final AssetManager manager;
    private LevelContext context;
    
    /**
     * Creates a new instance of AssetViewController and loads all assets required for it. Blocks until loading is complete.
     */
    public AssetViewController() {
        stage = new Stage(new ScreenViewport());
        manager = new AssetManager();
        
        // Load assets only for loading screen and block until finished
        manager.load("data/loading.jpg", Texture.class);
        manager.finishLoading();
        
        // TODO progress bar etc
        Image image = new Image(manager.get("data/loading.jpg", Texture.class));
        image.setWidth(stage.getWidth());
        image.setHeight(stage.getHeight());
        stage.addActor(image);
        
        
        // level context example
        
        String defaultAtlas = "data/items/elementuis/default.atlas";
        manager.load(defaultAtlas, TextureAtlas.class);
        manager.finishLoading();

        TextureAtlas atlas = manager.get(defaultAtlas, TextureAtlas.class);
        VariableUIContext variable = new VariableUIContext(atlas.findRegion("gem").getTexture());
        AbstractionUIContext abstraction = new AbstractionUIContext(atlas.findRegion("front_magicstick").getTexture(), 
                atlas.findRegion("center").getTexture(), atlas.findRegion("back").getTexture());
        ParanthesisUIContext parenthesis = new ParanthesisUIContext(atlas.findRegion("front").getTexture(), 
                atlas.findRegion("center").getTexture(), atlas.findRegion("back").getTexture());
        
        ElementUIContextFamily family = new ElementUIContextFamily("default", 0, null);
        ShopModel.getShop().getElementUIContextFamilies().setDefaultItem(family);
        
        LevelManager.getLevelManager().queueAssets(manager);
        LevelModel model = LevelManager.getLevelManager().getLevel(3);
        context = new LevelContext(model);
        
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
    public void create(AssetManager manager) {
        // UI elements for loading screen are set up in the constructor
    }
    
    /**
     * Called when the screen is shown.
     */
    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    /**
     * Renders this screen.
     * 
     * @param delta the time in seconds since the last render
     */
    @Override
    public void render(float delta) {
        if (manager.update()) {
            // Loading finished => go to profile selection
            getGame().createViewControllers();
            /* 
            LevelSkin is missing so I can not test it properly
      
            getGame().getController(EditorViewController.class).reset(context);
            getGame().setScreen(EditorViewController.class);
             */
           
            getGame().setScreen(ProfileSelection.class);
        }

        // TODO manager.getProgress();
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    /**
     * Called when the viewport changes.
     * 
     * @param width the new width
     * @param height the new height
     */
    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height);
    }

    /**
     * Called when the application is paused.
     */
    @Override
    public void pause() {
    }

    /**
     * Called when the application resumes from a paused state.
     */
    @Override
    public void resume() {
    }

    /**
     * Called when another screen is shown, i.e. when this screen is hidden.
     */
    @Override
    public void hide() {
    }

    /**
     * Called when this screen should release all resources.
     */
    @Override
    public void dispose() {
        stage.dispose();
        manager.dispose();
    }

    /**
     * Queues all assets needed by this viewController to be loaded by the given asset manager.
     * 
     * @param manager the asset manager
     */
    @Override
    public void queueAssets(AssetManager manager) {
        // All assets needed by the loading screen are loaded separately in the constructor
    	manager.load("data/skins/MasterSkin.atlas", TextureAtlas.class);
    }
}
