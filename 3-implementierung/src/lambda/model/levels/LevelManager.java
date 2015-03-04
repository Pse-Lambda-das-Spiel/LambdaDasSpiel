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
    	allLevelColors.put(Color.BLUE, "blue");
    	allLevelColors.put(Color.RED, "red");
    	allLevelColors.put(Color.GREEN, "green");
    	allLevelColors.put(Color.CYAN, "cyan");
    	allLevelColors.put(Color.ORANGE, "orange");
    	allLevelColors.put(Color.YELLOW, "yellow");
    	allLevelColors.put(Color.PINK, "pink");
    	allLevelColors.put(Color.PURPLE, "purple");
    	allLevelColors.put(Color.OLIVE, "olive");
    	allLevelColors.put(Color.BLACK, "black");
    	allLevelColors.put(Color.WHITE, "white");
    	allLevelColors.put(Color.GRAY, "gray");
    	allLevelColors.put(Color.MAGENTA, "magenta");
    	allLevelColors.put(Color.MAROON, "maroon");
    	allLevelColors.put(Color.NAVY, "navy");
    	allLevelColors.put(Color.TEAL, "teal");
    	allLevelColors.put(Color.CLEAR, "clear");
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
