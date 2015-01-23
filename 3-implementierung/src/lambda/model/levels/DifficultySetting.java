package lambda.model.levels;
/**
 * Represents the settings of levels with the same difficulty
 *
 * @author Kay Schmitteckert
 */
public class DifficultySetting {

    private int difficulty;
    private String music;
    private String bgImage;

    /**
     *  JSON
     */
    public DifficultySetting(int difficulty, String music, String bgImage) {
        this.difficulty = difficulty;
        this.music = music;
        this.bgImage = bgImage;
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
    public String getMusic() {
        return music;
    }

    /**
     * Returns the image which is shown in the background while a level of this difficulty is active
     *
     * @return bgImage
     */
    public String getBgImage() {
        return bgImage;
    }
}
