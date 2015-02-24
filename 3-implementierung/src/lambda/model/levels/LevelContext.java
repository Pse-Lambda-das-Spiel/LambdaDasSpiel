package lambda.model.levels;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import lambda.model.shop.ElementUIContextFamily;
import lambda.model.shop.ShopModel;

/**
 * Represents the whole level context. Includes everything about the level and the difficulty
 *
 * @author Kay Schmitteckert
 */
public class LevelContext {

    private LevelModel levelModel;
    private Music music;
    private Image bgImage;
    private ElementUIContextFamily elementUIContextFamily;
    private LevelManager manager;
    private DifficultySetting difficultySetting;

    /**
     * Creates a new instance of this class
     */
    public LevelContext(LevelModel levelModel) {
        elementUIContextFamily = ShopModel.getShop().getElementUIContextFamilies().getActivatedItem();
        this.levelModel = levelModel;

        if(levelModel.getId() != 0) {
            // for standard levels
            manager = LevelManager.getLevelManager();
            difficultySetting = manager.getDifficultySetting(levelModel.getDifficulty());
            music = difficultySetting.getMusic();
            bgImage = difficultySetting.getBgImage();
        }
        else {
            // for sandbox
            music = ShopModel.getShop().getMusic().getActivatedItem().getMusic();
            bgImage = new Image(ShopModel.getShop().getImages().getActivatedItem().getImage());
        }
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
    public Music getMusic() {
        return music;
    }

    /**
     * Returns the image which is shown in the background during the level is active
     *
     * @return image
     */
    public Image getBgImage() {
        return bgImage;
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
