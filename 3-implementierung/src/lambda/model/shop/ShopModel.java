package lambda.model.shop;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.scenes.scene2d.ui.Image;


/**
 * Represents
 *
 * @author Kay Schmitteckert
 */
public class ShopModel {

    private ShopItemTypeModel<MusicItemModel> music;
    private ShopItemTypeModel<BackgroundImageItemModel> images;
    private ShopItemTypeModel<ElementUIContextFamily> elementUIContextFamilies;

    /**
     *
     */
    public ShopModel() {
        music = new ShopItemTypeModel<MusicItemModel>("music");
        images = new ShopItemTypeModel<BackgroundImageItemModel>("images");
        elementUIContextFamilies = new ShopItemTypeModel<ElementUIContextFamily>("sprites");

    }

    /**
     *
     * @return
     */
    public ShopItemTypeModel<MusicItemModel> getMusic() {
        return music;
    }

    /**
     *
     * @return
     */
    public ShopItemTypeModel<BackgroundImageItemModel> getImages() {
        return images;
    }

    /**
     *
     * @return
     */
    public ShopItemTypeModel<ElementUIContextFamily> getElementUIContextFamilies() {
        return elementUIContextFamilies;
    }
}
