package lambda.model.levels;

import lambda.model.levels.DifficultySetting;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;


/**
 * This class serves as an asynchronous AssetLoader for a {@link DifficultySetting}.
 *
 * @author Robert Hochweiss
 */
public class DifficultySettingLoader extends AsynchronousAssetLoader<DifficultySetting, 
 												DifficultySettingLoader.DifficultySettingParameter> {

	DifficultySetting difficultySetting;
	
	/**
	 * Creates an new DifficultySettingLoader.
	 * @param resolver The {@link FileHandleResolver} that resolves a given filename to a {@link FileHandle}
	 */
	public DifficultySettingLoader(FileHandleResolver resolver) {
		super(resolver);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void loadAsync(AssetManager manager, String fileName, FileHandle file, 
			DifficultySettingParameter parameter) {
		difficultySetting = null;
		difficultySetting = LevelLoadHelper.loadDifficulty(file);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DifficultySetting loadSync(AssetManager manager, String fileName, FileHandle file, 
			DifficultySettingParameter parameter) {
		DifficultySetting difficultySetting = this.difficultySetting;
		this.difficultySetting = null;
		return difficultySetting;
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("rawtypes")
    @Override
	public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, 
			DifficultySettingParameter parameter) {
		return null;
	}
	
	/**
	 * A parameter class for a {@link DifficultySetting}.
	 */
	public static class DifficultySettingParameter extends AssetLoaderParameters<DifficultySetting> {
	}
}
