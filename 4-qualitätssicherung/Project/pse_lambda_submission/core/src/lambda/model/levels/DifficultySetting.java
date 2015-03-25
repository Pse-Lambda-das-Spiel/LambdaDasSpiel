package lambda.model.levels;

/**
 * Represents the settings of levels with the same difficulty
 *
 * @author Kay Schmitteckert
 */
public class DifficultySetting {

    private int difficulty;
    private String musicString;
    private String bgImageString;

	/**
     * Creates a new instance of this class
     *
     * @param difficulty integer which shows the difficulty (ascending)
     * @param musicString id of the music which runs in the background
     * @param bgImageString id of the image which is shown in the background
     */
    public DifficultySetting(int difficulty, String musicString, String bgImageString) {
        this.difficulty = difficulty;
        this.musicString = musicString;
        this.bgImageString = bgImageString;
    }

    /**
     * Returns the difficulty in terms of a number
     *
     * @return difficulty
     */
    public int getDifficulty() {
        return difficulty;
    }

    /**
     * Returns the music which is played in the background while a level of this difficulty is active
     *
     * @return music
     */
    public String getMusicString() {
        return musicString;
    }

    /**
     * Returns the image which is shown in the background while a level of this difficulty is active
     *
     * @return bgImage
     */
    public String getBgImageString() {
        return bgImageString;
    }
    
}
