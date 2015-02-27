package lambda.viewcontroller.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Image;


import lambda.model.levels.LevelContext;
import lambda.model.levels.LevelManager;
import lambda.model.shop.ElementUIContextFamily;
import lambda.model.shop.ShopModel;
import lambda.viewcontroller.AudioManager;
import lambda.viewcontroller.StageViewController;
import lambda.viewcontroller.level.AbstractionUIContext;
import lambda.viewcontroller.level.ParanthesisUIContext;
import lambda.viewcontroller.level.VariableUIContext;
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
    private LevelContext context;
    
    /**
     * Creates a new instance of AssetViewController and loads all assets required for it. Blocks until loading is complete.
     */
    public AssetViewController() {
        manager = new AssetManager();
        
        // Load assets only for loading screen and block until finished
        manager.load("data/loading.jpg", Texture.class);
        manager.finishLoading();
        
        // TODO progress bar etc
        Image image = new Image(manager.get("data/loading.jpg", Texture.class));
        image.setWidth(getStage().getWidth());
        image.setHeight(getStage().getHeight());
        getStage().addActor(image);
        
        
        // level context example
        
        String defaultAtlas = "data/items/elementuis/default.atlas";
        manager.load(defaultAtlas, TextureAtlas.class);
        manager.finishLoading();

        TextureAtlas atlas = manager.get(defaultAtlas, TextureAtlas.class);
        VariableUIContext variable = new VariableUIContext(atlas.findRegion("gem").getTexture(), atlas.findRegion("gem_mask").getTexture());
        AbstractionUIContext abstraction = new AbstractionUIContext(atlas.findRegion("front_magicstick").getTexture(), 
                atlas.findRegion("center").getTexture(), 
                atlas.findRegion("back").getTexture(),
                atlas.findRegion("front_mask").getTexture(),
                atlas.findRegion("center_mask").getTexture(),
                atlas.findRegion("back_mask").getTexture());
        ParanthesisUIContext parenthesis = new ParanthesisUIContext(atlas.findRegion("front").getTexture(), 
                atlas.findRegion("center").getTexture(), 
                atlas.findRegion("back").getTexture(),
                atlas.findRegion("front_mask").getTexture(),
                atlas.findRegion("center_mask").getTexture(),
                atlas.findRegion("back_mask").getTexture());
        
        ElementUIContextFamily family = new ElementUIContextFamily("default", 0, null);
        family.setAbstractionUIContext(abstraction);
        family.setParanthesisUIContext(parenthesis);
        family.setVariableUIContext(variable);
        ShopModel.getShop().getElementUIContextFamilies().setDefaultItem(family);
        
        LevelManager.getLevelManager().queueAssets(manager);
        // tmp, should be loaded centrally
        String magicPath = "data/animation/magic/Magic_Animation.atlas";
        String cloudPath = "data/animation/cloud/cloud.atlas";
        manager.load(magicPath, TextureAtlas.class);
        manager.load(cloudPath, TextureAtlas.class);
        // rest of example context was moved to create() 
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
    	
    	// only tmp moved until levelvc is finished
        context = new LevelContext(LevelManager.getLevelManager().getLevel(3));
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
            AudioManager.init();
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
