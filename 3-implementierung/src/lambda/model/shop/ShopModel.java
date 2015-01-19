package lambda.model.shop;

/**
 * Represents
 *
 * @author Kay Schmitteckert
 */
public class ShopModel {

    private ShopItemTypeModel<Music> musics;
    private ShopItemTypeModel<Image> images;
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
    public ShopItemTypeModel<Music> getMusics() {
        return musics;
    }

    /**
     *
     * @return
     */
    public ShopItemTypeModel<Image> getImages() {
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
