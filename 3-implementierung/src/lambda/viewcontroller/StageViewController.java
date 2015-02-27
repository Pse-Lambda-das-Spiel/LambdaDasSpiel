package lambda.viewcontroller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

/**
 * This class serves as a specialized ViewController that uses a stage for rendering.
 * 
 * @author Robert Hochweiss
 */
public abstract class StageViewController extends ViewController {

	private final Stage stage;
	
	/** 
	 * Creates an new instance of this class.
	 */
	public StageViewController() {
		stage = new Stage(new ScreenViewport());
	}
    
    /**
     * Return the stage that contains all actors that are displayed in this viewcontroller.
     * 
	 * @return the stage the stage that contains all actors that are displayed in this viewcontroller.
	 */
	public Stage getStage() {
		return stage;
	}

	/**
     * {@inheritDoc}
     */
    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
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
