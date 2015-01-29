package lambda.model.shop;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

/**
 * Created by kay_meth on 22.01.15.
 */
public class BackgroundImageItemModel extends ShopItemModel {

    private Image image;

    /**
     * 
     * @param id
     * @param price
     */
    public BackgroundImageItemModel(String id, int price, String filepath) {
    	super(id, price, filepath);
    }

    /**
     * Returns the backgroundimage
     *
     * @return image
     */
    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }
}
