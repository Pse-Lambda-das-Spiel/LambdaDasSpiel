package lambda;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import lambda.model.levels.DifficultySetting;
import lambda.model.levels.TutorialMessage;
import lambda.model.levels.LevelModel;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import java.util.Map;
/**
 * Keeps all the resources which are needed for the whole game
 *
 * @author Kay Schmitteckert
 */
public class AssetModel extends AssetManager {

	private static AssetModel assets;
	private Map<String, Sound> sounds;
	private Map<String, Music> music;
	private Map<Integer, DifficultySetting> difficultySettings;
	private Map<String, Image> images;
	private Map<String, TutorialMessage> tutorials;
	private Map<Integer, LevelModel> levels;
	
	private AssetModel() {
	}
	
	public static AssetModel getAssets() {
		if (assets == null) {
			assets = new AssetModel();
		}
		return assets;
	}
	
	public String getString(String id) {
		//TODO load the string with the specific id from the current I18N-bundle
		return "";
	}

	/**
	 * Returns a single sound from the map using the key
	 *
	 * @return the value to key
	 */
	public Sound getSoundByKey(String key) {
		try {
			return sounds.get(key);
		}
		catch(NullPointerException e) {
			System.out.println("there is no value which belongs to the key:" + key);

		}
		return null;
	}

	/**
	 * Returns a single music from the map using the key
	 *
	 * @return the value to key
	 */
	public Music getMusicByKey(String key) {
		try {
			return music.get(key);
		}
		catch(NullPointerException e) {
			System.out.println("there is no value which belongs to the key:" + key);

		}
		return null;
	}

	/**
	 *
	 * @return the value to key
	 */
	public DifficultySetting getDifficultySettingByKey(int key) {
		try {
			return difficultySettings.get(key);
		}
		catch(NullPointerException e) {
			System.out.println("there is no value which belongs to the key:" + key);

		}
		return null;
	}

	/**
	 * Returns a single image from the map using the key
	 *
	 * @return the value to key
	 */
	public Image getImageByKey(String key) {
		try {
			return images.get(key);
		}
		catch(NullPointerException e) {
			System.out.println("there is no value which belongs to the key:" + key);

		}
		return null;
	}

	/**
	 * Returns a single tutorial from the map using the key
	 *
	 * @return the value to key
	 */
	public TutorialMessage getTutorialByKey(String key) {
		try {
			return tutorials.get(key);
		}
		catch(NullPointerException e) {
			System.out.println("there is no value which belongs to the key:" + key);

		}
		return null;
	}

	/**
	 * Returns a single level from the map using the key
	 *
	 * @return the value to key
	 */
	public LevelModel getLevelByKey(int key) {
		try {
			return levels.get(key);
		}
		catch(NullPointerException e) {
			System.out.println("there is no value which belongs to the key:" + key);

		}
		return null;
	}
}
