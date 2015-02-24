package lambda.model.shop;

import com.badlogic.gdx.graphics.Texture;

/**
 * Created by kay_meth on 22.01.15.
 * 
 * @author Kay Schmitteckert
 */
public class BackgroundImageItemModel extends ShopItemModel {

    private Texture image;

    /**
     * 
     * @param id
     * @param price
     */
    public BackgroundImageItemModel(String id, int price, String filepath) {
    	super(id, price, filepath);
    	shopItemType = ShopModel.getShop().getImages();
    }

    /**
     * Returns the backgroundimage
     *
     * @return image
     */
    public Texture getImage() {
        return image;
    }

    public void setImage(Texture image) {
        this.image = image;
    }
}
