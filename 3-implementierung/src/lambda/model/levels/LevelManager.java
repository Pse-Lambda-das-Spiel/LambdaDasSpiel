package lambda.model.levels;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

/**
 * LevelManager holds two lists: One list which contains all LevelModels
 * and another one which contains all DifficultySettings.
 *
 * @author: Kay Schmitteckert
 */
public class LevelManager {

	/**
	 * The number of level per difficulty.
	 */
	public static final int LEVEL_PER_DIFFICULTY = 6;
	  /**
     * The path to the atlas file of the magic animation
     */
	public static final String MAGIC_ANIMATIONPATH = "data/animation/magic/Magic_Animation.atlas";
	
	/**
	 * The path to the atlas file of the cloud animation
	 */
	public static final String CLOUD_ANIMATIONPATH = "data/animation/cloud/cloud.atlas";
	
	/**
	 * The path to the atlas file of the glow
	 */
	public static final String GLOW = "data/animation/glow/glow.atlas";
	private AssetManager assetManager;
    private static LevelManager manager;
    private String[] levelFilePaths;
	private String[] difficultySettingFilePaths;
	private String[] difficultyBGImageFilePaths;
	private String[] difficultyMusicFilePaths;

    /**
     * Creates an instance of this class and loads the two required lists
     */
    private LevelManager() {
        levelFilePaths = LevelLoadHelper.loadAllLevelPaths();
        difficultySettingFilePaths = LevelLoadHelper.loadAllDifficultyPaths();
        difficultyMusicFilePaths = LevelLoadHelper.loadAllDifficultyMusicFilePaths();
        difficultyBGImageFilePaths = LevelLoadHelper.loadAllDifficultyBGImageFilePaths();
    }

    /**
     * Returns the LevelManager
     *
     * @return the LevelManager
     */
    public static LevelManager getLevelManager() {
        if (manager == null) {
            manager = new LevelManager();
        }
        return manager;
    }
    
    /**
     * Returns the number of levels (sandbox excluded).
     * 
     * @return the number of levels
     */
    public int getNumberOfLevels() {
    	return (levelFilePaths.length - 1);
    }

    /**
   	 * @return 
   	 */
   	public LevelModel getLevel(int id) {
   		LevelModel level = assetManager.get(levelFilePaths[id], LevelModel.class);
   		return level;
   	}

   	/**
   	 * @return 
   	 */
   	public DifficultySetting getDifficultySetting(int id) {
   		DifficultySetting difficultySetting = assetManager.get(difficultySettingFilePaths[id - 1], DifficultySetting.class);
   		return difficultySetting;
   	}
    
    public void queueAssets(AssetManager assets) {
    	assetManager = assets;
    	assets.setLoader(LevelModel.class, new LevelModelLoader(new InternalFileHandleResolver()));
    	assets.setLoader(DifficultySetting.class, new DifficultySettingLoader(new InternalFileHandleResolver()));
    	for (String levelFilePath : levelFilePaths) {
    		assets.load(levelFilePath, LevelModel.class);
    	}
    	for (String difficultySettingFilePath : difficultySettingFilePaths) {
    		assets.load(difficultySettingFilePath, DifficultySetting.class);
    	}
    	for (String difficultyMusicFilePath : difficultyMusicFilePaths) {
    		assets.load(difficultyMusicFilePath, Music.class);
    	}
    	for (String difficultyBGImageFilePath : difficultyBGImageFilePaths) {
    		assets.load(difficultyBGImageFilePath, Texture.class);
    	}
    	assets.load(MAGIC_ANIMATIONPATH, TextureAtlas.class);
    	assets.load(CLOUD_ANIMATIONPATH, TextureAtlas.class);
    	assets.load(GLOW, TextureAtlas.class);
    }
    	
   
    /**
     * Returns the {@link AssetManager} who holds all resources.
     * 
     * @return the {@link AssetManager}
     */
    public AssetManager getAssetManager() {
        return assetManager;
    }
    
}
