package lambda.viewcontroller;

import lambda.LambdaGame;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import lambda.model.profiles.ProfileManagerObserver;

/**
 * This class is a superclass for all ViewController who represent a screen.
 * 
 * @author Robert Hochweiss, Florian Fervers
 */
public abstract class ViewController implements Screen, ProfileManagerObserver {
    /**
     * Stores the game's main class.
     */
    private LambdaGame game;

    /**
     * Creates a new instance of this class.
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
     * @return this
     */
    public ViewController setGame(LambdaGame game) {
        this.game = game;
        return this;
    }
    
    /**
     * Queues all assets needed by this viewController to be loaded by the given asset manager.
     * 
     * @param assets the asset manager
     */
    public abstract void queueAssets(AssetManager assets);
}
