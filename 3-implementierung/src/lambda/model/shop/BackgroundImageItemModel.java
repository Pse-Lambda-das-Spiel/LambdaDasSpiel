package lambda.model.shop;

import com.badlogic.gdx.scenes.scene2d.ui.Image;

/**
 * Created by kay_meth on 22.01.15.
 */
public class BackgroundImageItemModel extends ShopItemModel {

    private Image image;

    // Why not a constructor with 2 parameter ?
    /**
     *
     */
    public BackgroundImageItemModel() {

    }
    
    /**
     * 
     * @param id
     * @param price
     */
    public BackgroundImageItemModel(String id, int price) {
    	super(id, price);
    }

    /**
     * Returns the backgroundimage
     *
     * @return image
     */
    public Image getImage() {
        return image;
    }
}
