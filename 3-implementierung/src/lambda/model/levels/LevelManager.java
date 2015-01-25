package lambda.model.levels;

import lambda.util.LevelLoadHelper;

import java.util.List;

/**
 *
 * LevelManager holds two lists: One list which contains all LevelModels
 * and another one which contains all DifficultySettings.
 *
 * @author: Kay Schmitteckert
 */
public class LevelManager {

    private static LevelManager manager;
    private List<LevelModel> levels;
    private List<DifficultySetting> difficultySettings;

    /**
     * Creates an instance of this class and loads the two required lists
     */
    private LevelManager() {
        manager = new LevelManager();
        levels = LevelLoadHelper.loadAllLevels();
        difficultySettings = LevelLoadHelper.loadAllDifficulties();
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
     * Returns a list which contains all LevelModels
     *
     * @return a list which contains all LevelModels
     */
    public List<LevelModel> getLevels() {
        return levels;
    }

    /**
     * Returns a list which contains all DifficultySettings
     *
     * @return a list which contains all DifficultySettings
     */
    public List<DifficultySetting> getDifficultySettings() {
        return difficultySettings;
    }

}
