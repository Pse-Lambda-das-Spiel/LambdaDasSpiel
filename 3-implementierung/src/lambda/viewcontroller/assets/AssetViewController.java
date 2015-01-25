package lambda.viewcontroller.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import lambda.viewcontroller.ViewController;
import lambda.viewcontroller.editor.EditorViewController;

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
            getGame().setScreen(EditorViewController.class);
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
    }
}
