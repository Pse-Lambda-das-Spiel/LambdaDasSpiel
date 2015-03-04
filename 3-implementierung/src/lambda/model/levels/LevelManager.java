package lambda.model.levels;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.Color;

/**
 * LevelManager manages everything which is needed for the level contexts.
 * This manager loads all leveld, difficulty settings and several assets which are used in every level.
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
	private Map<Color, String> allLevelColors;

	/**
     * Creates an instance of this class and loads the two required lists
     */
    private LevelManager() {
        levelFilePaths = LevelLoadHelper.loadAllLevelPaths();
        difficultySettingFilePaths = LevelLoadHelper.loadAllDifficultyPaths();
        difficultyMusicFilePaths = LevelLoadHelper.loadAllDifficultyMusicFilePaths();
        difficultyBGImageFilePaths = LevelLoadHelper.loadAllDifficultyBGImageFilePaths();
        allLevelColors = new HashMap<>();
        setAllLevelColors();
    }

    private void setAllLevelColors() {
    	allLevelColors.put(Color.BLUE, "a");
    	allLevelColors.put(Color.RED, "b");
    	allLevelColors.put(Color.GREEN, "c");
    	allLevelColors.put(Color.CYAN, "d");
    	allLevelColors.put(Color.ORANGE, "e");
    	allLevelColors.put(Color.YELLOW, "f");
    	allLevelColors.put(Color.PINK, "g");
    	allLevelColors.put(Color.PURPLE, "h");
    	allLevelColors.put(Color.OLIVE, "i");
    	allLevelColors.put(Color.BLACK, "j");
    	allLevelColors.put(Color.WHITE, "k");
    	allLevelColors.put(Color.GRAY, "l");
    	allLevelColors.put(Color.MAGENTA, "m");
    	allLevelColors.put(Color.MAROON, "n");
    	allLevelColors.put(Color.NAVY, "o");
    	allLevelColors.put(Color.TEAL, "p");
    	allLevelColors.put(Color.CLEAR, "q");
    }
    
    /**
     * Returns a map of all possible colors in a level, with the color as the key and the name of the color as value.
     * 
   	 * @return the allLevelColors all posible colors in a level
   	 */
   	public Map<Color, String> getAllLevelColors() {
   		return allLevelColors;
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
     * Returns the level with the id of the parameter
     * 
     * @param id the id of the level which will be returned
   	 * @return the level with this id
   	 */
   	public LevelModel getLevel(int id) {
   		LevelModel level = assetManager.get(levelFilePaths[id], LevelModel.class);
   		return level;
   	}

   	/**
   	 * Returns the difficulty settings with the id of the parameter
   	 * 
   	 * @param id the id of the difficulty settings which whill be returned
   	 * @return the difficulty settings with this id
   	 */
   	public DifficultySetting getDifficultySetting(int id) {
   		DifficultySetting difficultySetting = assetManager.get(difficultySettingFilePaths[id - 1], DifficultySetting.class);
   		return difficultySetting;
   	}
    
   	/**
   	 * Loads everything which is needed for the level contexts
   	 * 
   	 * @param assets the static asset manager
   	 */
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
