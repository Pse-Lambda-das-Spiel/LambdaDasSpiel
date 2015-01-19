package lambda.model.levels;

/**
 * Represents the whole level context. Includes everything about the level and the difficulty
 *
 * @author Kay Schmitteckert
 */
public class LevelContext {

    private LevelModel levelModel;
    private String music;
    private String image;
    private List<String> tutorials;
    private ElementUIContextFamily elementUIContextFamily;

    /**
     * Abhängig von den anderen zwei Models in diesem Paket
     */
    public LevelContext() {

    }

    /**
     * Returns the levelModel which is used to set all attributes
     *
     * @return levelModel
     */
    public LevelModel getLevelModel() {
        return levelModel;
    }

    /**
     * Returns the music which is played in the background during the level is active
     *
     * @return music
     */
    public String getMusic() {
        return music;
    }

    /**
     * Returns the image which is shown in the background during the level is active
     *
     * @return image
     */
    public String getImage() {
        return image;
    }

    /**
     * Returns a list of tutorials which are played at the beginning of the level
     *
     * @return tutorials
     */
    public List<String> getTutorials() {
        return tutorials;
    }

    /**
     * Returns the element-sprites which are activated in the shop
     *
     * @return elementUIContextFamily
     */
    public ElementUIContextFamily getElementUIContextFamily() {
        return elementUIContextFamily;
    }

}
