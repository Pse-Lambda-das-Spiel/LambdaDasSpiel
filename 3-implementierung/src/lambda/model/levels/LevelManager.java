package lambda.model.levels;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

/**
 *
 * LevelManager holds two lists: One list which contains all LevelModels
 * and another one which contains all DifficultySettings.
 *
 * @author: Kay Schmitteckert
 */
public class LevelManager {

	private AssetManager assetManager;
    private static LevelManager manager;
    String[] levelFilePaths;
	String[] difficultySettingFilePaths;
//    private List<LevelModel> levels;
//    private List<DifficultySetting> difficultySettings;

    /**
     * Creates an instance of this class and loads the two required lists
     */
    private LevelManager() {
        levelFilePaths = LevelLoadHelper.loadAllLevelPaths();
        difficultySettingFilePaths = LevelLoadHelper.loadAllDifficultyPaths();
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
    
//    /**
//     * Returns a list which contains all LevelModels
//     *
//     * @return a list which contains all LevelModels
//     */
//    public List<LevelModel> getLevels() {
//        return levels;
//    }

//    /**
//     * Returns a list which contains all DifficultySettings
//     *
//     * @return a list which contains all DifficultySettings
//     */
//    public List<DifficultySetting> getDifficultySettings() {
//        return difficultySettings;
//    }
    
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
    	
    	
    	assets.finishLoading();
    	// I am not sure about the rest of your resources here, either use normal AssetLoaders or write your own for them
    	
//        FileHandle file = Gdx.files.internal("data/levels/music/numberOfMusic.json");
//        JsonReader reader = new JsonReader();
//        JsonValue jsonFile = reader.parse(file);
//        int numberOfMusic = jsonFile.getInt("numberOfMusic");
//        for (int i = 0; i < numberOfMusic; i++) {
//            assetManager.load("data/levels/music" + String.format("%02d", i) + ".mp3", Music.class);
//            levelManager.getDifficultySettings().get(i).setMusic(assetManager.get("data/levels/music" + String.format("%02d", i) + ".mp3"));
//            //assetManager.unload("data/levels/music" + String.format("%02d", i) + ".mp3");
//        }
//
//        file = Gdx.files.internal("data/difficulties/numberOfImages.json");
//        reader = new JsonReader();
//        jsonFile = reader.parse(file);
//        int numberOfImages = jsonFile.getInt("numberOfImages");
//        for (int i = 0; i < numberOfImages; i++) {
//            assetManager.load("data/levels/images" + String.format("%02d", i) + ".jpg", Image.class);
//            levelManager.getDifficultySettings().get(i).setBgImage(assetManager.get("data/levels/images" + String.format("%02d", i) + ".jpg"));
//        }
    }

}
