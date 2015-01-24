package lambda;

import lambda.viewcontroller.achievements.AchievementMenuViewController;
import lambda.viewcontroller.editor.EditorViewController;
import lambda.viewcontroller.level.LevelSelectionViewController;
import lambda.viewcontroller.mainmenu.MainMenuViewController;
import lambda.viewcontroller.profiles.ProfileEditLang;
import lambda.viewcontroller.profiles.ProfileSelection;
import lambda.viewcontroller.reduction.ReductionViewController;
import lambda.viewcontroller.settings.SettingsViewController;
import lambda.viewcontroller.shop.ShopViewController;
import lambda.viewcontroller.statistics.StatisticViewController;

import com.badlogic.gdx.Game;
import java.util.HashMap;
import java.util.Map;
import lambda.viewcontroller.ViewController;
import lambda.viewcontroller.assets.AssetViewController;
import lambda.viewcontroller.profiles.ProfileEditAvatar;
import lambda.viewcontroller.profiles.ProfileEditName;

/**
 * The main class of this application.
 * 
 * @author Florian Fervers
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
     * Called when the Application is first created.
     */
    @Override
    public void create() {
        // TODO Use reflection?
        addViewController(new AssetViewController());
        /*addViewController(new ProfileSelection());
        addViewController(new ProfileEditLang());
        addViewController(new ProfileEditName());
        addViewController(new ProfileEditAvatar());
        addViewController(new MainMenuViewController());
        addViewController(new LevelSelectionViewController());
        addViewController(new EditorViewController());
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
     * {@inheritDoc}
     */
    @Override
    public void dispose() {
        super.dispose();
        for (ViewController viewController : viewControllers.values()) {
            viewController.dispose();
        }
    }
}
