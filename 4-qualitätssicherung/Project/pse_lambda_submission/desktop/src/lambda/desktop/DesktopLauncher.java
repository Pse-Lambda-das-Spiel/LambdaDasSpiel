package lambda.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import lambda.LambdaGame;

// Put this class as DesktopLauncher in your desktop sub project.

/**
 * This class serves as a the desktop backend for {@link LambdaGame} and sets some default settings for the desktop.
 * 
 * @author Robert Hochweiss
 */
public class DesktopLauncher {
	
	/**
	 * The main method.
	 * @param arg program parameter.
	 */
	public static void main(String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		/*
		 * We should use 720x1280 as minimum resolution according to phase 1-requirements 
		 * and optimze our screens for this resolution
		 */
		config.width = 1280;
		config.height = 720;
		config.r = 8;
		config.g = 8;
		config.b = 8;
		config.a = 8;
		// Resizable windows are not so optimal for libGDX desktop backends.
		config.resizable = false;
		new LwjglApplication(new LambdaGame(), config);
	}
}

