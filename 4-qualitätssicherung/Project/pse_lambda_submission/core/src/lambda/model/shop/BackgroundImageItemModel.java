package lambda.model.shop;

import com.badlogic.gdx.graphics.Texture;

/**
 * Represents an background image item in the shop.
 * 
 * @author Kay Schmitteckert
 */
public class BackgroundImageItemModel extends ShopItemModel {

    private Texture image;

    /**
     * Creates a new instance of this class
     * 
     * @param id the if of this item
     * @param price the price of this item
     * @param filepath the path to the asset
     */
    public BackgroundImageItemModel(String id, int price, String filepath) {
    	super(id, price, filepath);
    	shopItemType = ShopModel.getShop().getImages();
    }

    /**
     * Returns the background image
     *
     * @return image
     */
    public Texture getImage() {
        return image;
    }

    /**
     * Sets the parameter as new image
     * 
     * @param image
     */
    public void setImage(Texture image) {
        this.image = image;
    }
}
