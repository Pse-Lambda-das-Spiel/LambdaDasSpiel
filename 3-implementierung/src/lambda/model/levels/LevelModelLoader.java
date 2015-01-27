package lambda.model.levels;

import lambda.model.levels.LevelModel;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;

/**
 * This class serves as an asynchronous AssetLoader for a {@link LevelModel}.
 *
 * @author Robert Hochweiss
 */
public class LevelModelLoader extends AsynchronousAssetLoader<LevelModel, LevelModelLoader.LevelModelParameter> {
	
	LevelModel level;
	
	/**
	 * Creates an new LevelModelLoader.
	 * @param resolver The {@link FileHandleResolver} that resolves a given filename to a {@link FileHandle}
	 */
	public LevelModelLoader(FileHandleResolver resolver) {
		super(resolver);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void loadAsync(AssetManager manager, String fileName, FileHandle file, LevelModelParameter parameter) {
		level = null;
		level = LevelLoadHelper.loadLevel(file);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public LevelModel loadSync(AssetManager manager, String fileName, FileHandle file, LevelModelParameter parameter) {
		LevelModel level = this.level;
		this.level = null;
		return level;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, LevelModelParameter parameter) {
		return null;
	}

	/**
	 * A parameter class for a {@link LevelModel}.
	 */
	public static class LevelModelParameter extends AssetLoaderParameters<LevelModel> {
	}
}
