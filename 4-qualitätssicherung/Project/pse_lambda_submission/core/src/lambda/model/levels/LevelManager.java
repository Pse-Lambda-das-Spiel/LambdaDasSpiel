package lambda.model.levels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.Color;

/**
 * LevelManager manages everything which is needed for the level contexts.
 * This manager loads all levels, difficulty settings and several assets which are used in every level
 * as well as the mapping of variables to colors and reversed.
 *
 * @author: Kay Schmitteckert
 */
public class LevelManager {

	/**
	 * The maximum number of level per difficulty.
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
	private static Map<Character, String> variablesToColors;
	private static Map<String, Character> colorsToVariables;

	/**
     * Creates an instance of this class and loads the two required lists
     */
    private LevelManager() {
    	variablesToColors = new HashMap<>();
    	colorsToVariables = new HashMap<>();
    	LevelLoadHelper.loadAllColors(variablesToColors, colorsToVariables);
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
     * Returns the level with the id of the parameter
     * 
     * @param id the id of the level which will be returned
   	 * @return the level with this id
   	 */
   	public LevelModel getLevel(int id) throws InvalidLevelIdException {
   	    if (id >= 0 && id <= getNumberOfLevels()) {
   	        LevelModel level = assetManager.get(levelFilePaths[id], LevelModel.class);
   	        return level;
   	    }
   	    throw new InvalidLevelIdException("The level id is invalid");
   	}

   	/**
   	 * Returns the difficulty settings with the id of the parameter
   	 * 
   	 * @param id the id of the difficulty settings which whill be returned
   	 * @return the difficulty settings with this id
   	 */
   	public DifficultySetting getDifficultySetting(int id) {
   		DifficultySetting difficultySetting = assetManager.get(difficultySettingFilePaths[id - 1],
   																DifficultySetting.class);
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

	/**
	 * Returns the mapping of variables to colors (as hexstrings)  with the variables as key.
	 * 
	 * @return the variablesToColors
	 */
	public static Map<Character, String> getVariablesToColors() {
		return variablesToColors;
	}

	/**
	 * Returns the mapping of colors (as hexstrings) to variables with the colors as key.
	 * 
	 * @return the colorsToVariables
	 */
	public static Map<String, Character> getColorsToVariables() {
		return colorsToVariables;
	}
	
	
	/**
	 * Converts a variable to its specific color.
	 * 
	 * @param variable the to be converted variable
	 * @return the specific color for the given variable
	 * @throws IllegalArgumentException if there is no color stored for the given variable
	 */
	public static Color convertVariableToColor(char variable) {
		if (variablesToColors.get(variable) == null) {
			throw new IllegalArgumentException("There is no color for the variable: " + variable);
		}
		return Color.valueOf(variablesToColors.get(variable));
	}
	
	/**
	 * Converts a color to its specific variable.
	 * 
	 * @param color the to be converted color
	 * @return the specific variable for the given color
	 * @throws IllegalArgumentException if there is no variable stored for the given color
	 */
	public static char convertColorToVariable(Color color) {
		String hexString = color.toString();
		if (colorsToVariables.get(hexString) == null) {
			throw new IllegalArgumentException("There is no variable for the color: " + color.toString());
		}
		return colorsToVariables.get(hexString);
	}
	
	public static List<Color> getAllColors() {
		List<Color> colors = new ArrayList<>();
		for (String hex : colorsToVariables.keySet()) {
			colors.add(Color.valueOf(hex));
		}
		return colors;
	}

}
