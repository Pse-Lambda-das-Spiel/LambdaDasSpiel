package lambda.model.levels;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
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
    private Animation cloudAnimation;
    private Animation magicAnimation;

    /**
     * Creates a new instance of this class
     */
    public LevelContext(LevelModel levelModel) {
        this.levelModel = levelModel;
        this.manager = LevelManager.getLevelManager();
        this.magicAnimation = new Animation(1/15f, manager.getAssetManager().get(LevelManager.MAGIC_ANIMATIONPATH, TextureAtlas.class).getRegions());
        this.cloudAnimation = new Animation(1/15f, manager.getAssetManager().get(LevelManager.CLOUD_ANIMATIONPATH, TextureAtlas.class).getRegions());
        elementUIContextFamily = ShopModel.getShop().getElementUIContextFamilies().getActivatedItem();
        
        /*not needed anymore
        // Should be loaded centrally
        String magicPath = "data/animation/magic/Magic_Animation.atlas";
        String cloudPath = "data/animation/cloud/cloud.atlas";
        
        this.magicAnimation = new Animation(1/15f, manager.getAssetManager().get(magicPath, TextureAtlas.class).getRegions());
        this.cloudAnimation = new Animation(1/15f, manager.getAssetManager().get(cloudPath, TextureAtlas.class).getRegions());
        */
     
        if(levelModel.getId() != 0) {
            // for standard levels
        	
            difficultySetting = manager.getDifficultySetting(levelModel.getDifficulty());
            music = manager.getAssetManager().get(difficultySetting.getMusicString(), Music.class);
        	bgImage = new Image(manager.getAssetManager().get(difficultySetting.getBgImageString(), Texture.class));
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
    
    /**
     * Returns the animation for the magic effect
     * 
     * @return magicAnimation which is the effect of the magic stick
     */
    public Animation getMagicAnimation() {
        return magicAnimation;
    }
    
    /**
     * Returns the animation for the cloud effect
     * 
     * @return cloudAnimation which is the effect of a magic
     */
    public Animation getCloudAnimation() {
        return cloudAnimation;
    }

}
