package lambda.viewcontroller;

import lambda.LambdaGame;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import lambda.model.profiles.ProfileManagerObserver;

/**
 * This class is a superclass for all ViewController who represent a screen. The order in which the view controllers are initialized is:
 * 1. constructor: create class instance and model
 * 2. queueAssets: queue assets for loading
 * 3. create: create ui elements using loaded assets
 * 
 * @author Robert Hochweiss, Florian Fervers
 */
public abstract class ViewController implements Screen, ProfileManagerObserver {
    /**
     * Stores the game's main class.
     */
    private LambdaGame game;

    /**
     * Creates a new instance of this class. At this point no assets are loaded.
     */
    public ViewController() {
        game = null;
    }

    /**
     * Returns the reference to the main class.
     * 
     * @return the reference to the main class
     */
    public LambdaGame getGame() {
        return game;
    }

    /**
     * Sets the reference to the main class.
     * 
     * @param game the reference to the main class to set
     */
    public void setGame(LambdaGame game) {
        this.game = game;
    }
    
    /**
     * Queues all assets needed by this viewController to be loaded by the given asset manager.
     * 
     * @param manager the asset manager
     */
    public abstract void queueAssets(AssetManager manager);
    
    /**
     * Called when the view conroller is created after the assets are loaded. Create ui elements.
     * 
     * @param manager the asset manager with loaded assets
     */
    public abstract void create(AssetManager manager);
}
