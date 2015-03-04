package lambda.android;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import lambda.LambdaGame;

/**
 * This class serves as a the android backend for {@link LambdaGame} and sets some default settings for android.
 * 
 * @author Robert Hochweiss
 */
public class AndroidLauncher extends AndroidApplication {
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.r = 8;
		config.g = 8;
		config.b = 8;
		config.a = 8;
		config.useAccelerometer = false;
		config.useCompass = false;
		config.useWakelock = true;
		initialize(new LambdaGame(), config);
	}
}
