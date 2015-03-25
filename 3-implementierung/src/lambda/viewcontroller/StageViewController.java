package lambda.viewcontroller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

/**
 * This class serves as a specialized ViewController that uses a stage for
 * rendering.
 * 
 * @author Robert Hochweiss
 */
public abstract class StageViewController extends ViewController {

    private final Stage stage;
    private ViewController lastViewController;

    /**
     * Creates an new instance of this class.
     */
    public StageViewController() {
        stage = new Stage(new ScreenViewport());
        lastViewController = null;
    }

    /**
     * Return the stage that contains all actors that are displayed in this
     * viewcontroller.
     * 
     * @return the stage the stage that contains all actors that are displayed
     *         in this viewcontroller.
     */
    public Stage getStage() {
        return stage;
    }

    /**
     * Returns the reference to the ViewController that is shown when the
     * Android back key is pressed while this ViewController is active.
     * 
     * @return the ViewController that was shown before this one
     */
    public ViewController getLastViewController() {
        return lastViewController;
    }

    /**
     * Sets the ViewController with the given name as ViewController that is
     * shown when the Android back key is pressed while this ViewController is
     * active.
     * 
     * @param vcClass
     *            the class of the to be shown ViewController
     */
    public void setLastViewController(Class vcClass) {
        // The order of the ViewController is fixed
        this.lastViewController = getGame().getController(vcClass);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void show() {
        InputProcessor backProcessor = new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                if (keycode == Keys.BACK) {
                    getGame().setScreen(lastViewController.getClass());
                }
                return false;
            }
        };
        InputMultiplexer multiplexer = new InputMultiplexer(stage,
                backProcessor);
        Gdx.input.setInputProcessor(multiplexer);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void resize(int width, int height) {
        stage.getViewport().setScreenSize(width, height);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void pause() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void resume() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void dispose() {
        stage.dispose();
    }

}
