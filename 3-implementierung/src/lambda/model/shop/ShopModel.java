package lambda.model.shop;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.scenes.scene2d.ui.Image;


/**
 * Represents
 *
 * @author Kay Schmitteckert
 */
public class ShopModel {

    private ShopItemTypeModel<MusicItemModel> musics;
    private ShopItemTypeModel<BackgroundImageItemModel> images;
    private ShopItemTypeModel<ElementUIContextFamily> elementUIContextFamilies;

    /**
     *
     */
    public ShopModel() {

    }

    /**
     *
     * @return
     */
    public ShopItemTypeModel<MusicItemModel> getMusics() {
        return musics;
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
