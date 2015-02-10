package lambda;

import lambda.viewcontroller.editor.EditorViewController;

import com.badlogic.gdx.Game;

import java.util.HashMap;
import java.util.Map;

import lambda.viewcontroller.ViewController;
import lambda.viewcontroller.assets.AssetViewController;
import lambda.viewcontroller.reduction.ReductionViewController;

/**
 * The main class of this application.
 * 
 * @author Florian Fervers, Robert Hochweiss
 */
public class LambdaGame extends Game {
    /**
     * Stores all controllers mapped by their names.
     */
    private final Map<Class, ViewController> viewControllers;

    /**
     * Creates a new instance of this game.
     */
    public LambdaGame() {
        viewControllers = new HashMap<>();
    }

    /**
     * Returns the viewController with the given class or null if no such viewController exists.
     * 
     * @param <T> the type of the viewController
     * @param vcClass the class of the viewController
     * @return the viewController with the given class or null if no such viewController exists
     */
    public <T extends ViewController> T getController(Class<T> vcClass) {
        return (T) viewControllers.get(vcClass);
    }
    
    /**
     * Sets the viewController with the given name to be the current screen.
     * 
     * @param vcClass the class of the viewController
     */
    public void setScreen(Class vcClass) {
        setScreen(getController(vcClass));
    }
    
    /**
     * Adds the given view controller to the map of view controllers.
     * 
     * @param viewController the new view controller
     */
    private void addViewController(ViewController viewController) {
        viewController.setGame(this);
        viewControllers.put(viewController.getClass(), viewController);
    }
    
    /**
     * Calls the create() method of all view controllers. Called after assets are loaded before profile selection is shown.
     */
    public void createViewControllers() {
        for (ViewController viewController : viewControllers.values()) {
            viewController.create(getController(AssetViewController.class).getManager());
        }
    }

    /**
     * Called when the Application is first created.
     */
    @Override
    public void create() {
        // TODO Use reflection?
        addViewController(new AssetViewController());
        addViewController(new EditorViewController());
        addViewController(new ReductionViewController());
        
        /*addViewController(new ProfileSelection());
        addViewController(new ProfileEditLang());
        addViewController(new ProfileEditName());
        addViewController(new ProfileEditAvatar());
        addViewController(new MainMenuViewController());
        addViewController(new LevelSelectionViewController());
        
        addViewController(new ReductionViewController());
        addViewController(new SettingsViewController());
        addViewController(new ShopViewController());
        addViewController(new StatisticViewController());
        addViewController(new AchievementMenuViewController());*/
        
        // Queue all assets for loading
        for (ViewController viewController : viewControllers.values()) {
            viewController.queueAssets(getController(AssetViewController.class).getManager());
        }
        
        // Show loading screen
        setScreen(AssetViewController.class);
    }

    /**
     * Releases all resources used by this game.
     */
    @Override
    public void dispose() {
        super.dispose();
        for (ViewController viewController : viewControllers.values()) {
            viewController.dispose();
        }
    }
    
    // Bot methods are necessary for rendering and resizing the screens correctly.
    
    /**
     * Called when the Application should render itself.
     */
    @Override
	public void render() {
		super.render();
	}
    
    /**
     * Called when the Application is resized. 
     * This can happen at any point during a non-paused state 
     * but will never happen before a call to create().
     * 
     * @param width the new width in pixels
     * @param height the new height in pixels
     */
	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
	}
}
