package lambda;

import lambda.viewcontroller.editor.EditorViewController;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

import java.util.HashMap;
import java.util.Map;

import lambda.viewcontroller.AudioManager;
import lambda.viewcontroller.ViewController;
import lambda.viewcontroller.achievements.AchievementMenuViewController;
import lambda.viewcontroller.assets.AssetViewController;
import lambda.viewcontroller.level.LevelSelectionViewController;
import lambda.viewcontroller.mainmenu.MainMenuViewController;
import lambda.viewcontroller.profiles.ProfileEditAvatar;
import lambda.viewcontroller.profiles.ProfileEditLang;
import lambda.viewcontroller.profiles.ProfileEditName;
import lambda.viewcontroller.profiles.ProfileSelection;
import lambda.viewcontroller.reduction.ReductionViewController;
import lambda.viewcontroller.settings.SettingsViewController;
import lambda.viewcontroller.shop.ShopViewController;
import lambda.viewcontroller.statistics.StatisticViewController;

/**
 * The main class of this application.
 *
 * @author Florian Fervers, Robert Hochweiss
 */
public class LambdaGame extends Game {
    /**
     * Indicates whether debugging output is enabled.
     */
    public static final boolean DEBUG = false;
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
     * Returns the viewController with the given class or null if no such
     * viewController exists.
     *
     * @param <T>
     *            the type of the viewController
     * @param vcClass
     *            the class of the viewController
     * @return the viewController with the given class or null if no such
     *         viewController exists
     */
    public <T extends ViewController> T getController(Class<T> vcClass) {
        return (T) viewControllers.get(vcClass);
    }

    /**
     * Sets the viewController with the given name to be the current screen.
     *
     * @param vcClass
     *            the class of the viewController
     */
    public void setScreen(Class vcClass) {
        setScreen(getController(vcClass));
    }

    /**
     * Adds the given view controller to the map of view controllers.
     *
     * @param viewController
     *            the new view controller
     */
    private void addViewController(ViewController viewController) {
        viewController.setGame(this);
        viewControllers.put(viewController.getClass(), viewController);
    }

    /**
     * Calls the create() method of all view controllers. Called after assets
     * are loaded before profile selection is shown.
     */
    public void createViewControllers() {
        for (ViewController viewController : viewControllers.values()) {
            viewController.create(getController(AssetViewController.class)
                    .getManager());
        }
    }

    /**
     * Called when the Application is first created.
     */
    @Override
    public void create() {
        Gdx.input.setCatchBackKey(true);
        Gdx.input.setCatchMenuKey(true);

        // TODO Use reflection?
        addViewController(new AssetViewController());

        addViewController(new ProfileSelection());
        addViewController(new ProfileEditLang());
        addViewController(new ProfileEditName());
        addViewController(new ProfileEditAvatar());
        addViewController(new MainMenuViewController());
        addViewController(new AchievementMenuViewController());
        addViewController(new SettingsViewController());
        addViewController(new ShopViewController());
        addViewController(new LevelSelectionViewController());
        addViewController(new EditorViewController());
        addViewController(new ReductionViewController());
        addViewController(new StatisticViewController());
       
        AudioManager.queueAssets(getController(AssetViewController.class)
                .getManager());

        // Queue all assets for loading
        for (ViewController viewController : viewControllers.values()) {
            viewController.queueAssets(getController(AssetViewController.class)
                    .getManager());
        }

        // Show loading screen
        setScreen(AssetViewController.class);
    }

    /**
     * Called when the Application is paused, usually when it's not active or
     * visible on screen. An Application is also paused before it is destroyed.
     */
    @Override
    public void pause() {
        super.pause();
        for (ViewController viewController : viewControllers.values()) {
            viewController.pause();
        }
    }

    /**
     * Called when the Application is exited. Releases all resources used by
     * this game.
     */
    @Override
    public void dispose() {
        super.dispose();
        for (ViewController viewController : viewControllers.values()) {
            viewController.dispose();
        }
        Gdx.input.setCatchBackKey(false);
        Gdx.input.setCatchMenuKey(false);
    }

    /**
     * Called when the Application should render itself.
     */
    @Override
    public void render() {
        super.render();
    }

    /**
     * Called when the Application is resized. This can happen at any point
     * during a non-paused state but will never happen before a call to
     * create().
     *
     * @param width
     *            the new width in pixels
     * @param height
     *            the new height in pixels
     */
    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }

}
