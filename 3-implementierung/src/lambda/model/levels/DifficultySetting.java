package lambda.model.levels;
/**
 * Represents the settings of levels with the same difficulty
 *
 * @author Kay Schmitteckert
 */
public class DifficultySetting {

    private String music;
    private String bgImage;

    /**
     *  JSON
     */
    public DifficultySetting() {

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
