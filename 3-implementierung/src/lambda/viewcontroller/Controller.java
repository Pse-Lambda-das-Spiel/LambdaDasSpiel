package lambda.viewcontroller;

import lambda.LambdaGame;

import com.badlogic.gdx.Screen;

/**
 * This class is a superclass for all ViewController who represent a screen.
 * 
 * @author Robert Hochweiss
 */
public class Controller implements Screen {

	LambdaGame game;
	
	/**
	 * Creates a new instance of this class.
	 */
	public Controller() {
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
	 * {@inheritDoc}
	 */
	@Override
	public void show() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void render(float delta) {
	}

	// Since the libgdx api lacks some documentation
	/**
	 * Called when the Application is resized. 
	 * This can happen at any point during a non-paused state but will never happen before a call to create().
	 * 
	 * @param width the new width in pixels
	 * @param height the new height i pixels
	 */
	@Override
	public void resize(int width, int height) {		
	}

	/**
	 * Called when the screen is paused.
	 * A screen is paused before it is destroyed.
	 */
	@Override
	public void pause() {		
	}

	/**
	 * Called when the screen is resumed from a paused state, usually when it regains focus.
	 */
	@Override
	public void resume() {		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void hide() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void dispose() {
	}

}
