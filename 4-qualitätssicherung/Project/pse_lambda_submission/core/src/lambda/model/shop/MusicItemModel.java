package lambda.model.shop;

import com.badlogic.gdx.audio.Music;

/**
 * Represents a music item in the shop
 * 
 * @author Kay Schmitteckert
 */
public class MusicItemModel extends ShopItemModel {

    private Music music;

    /**
     * Creates a new instance of this class
     * 
     * @param id the if of this item
     * @param price the price of this item
     * @param filepath the path to the asset
     */
    public MusicItemModel(String id, int price, String filepath) {
    	super(id, price, filepath);
    	shopItemType = ShopModel.getShop().getMusic();
    }

    /**
     * Returns the music of this item
     *
     * @return the music of this item
     */
    public Music getMusic() {
        return music;
    }

    /**
     * Sets the committed as new music
     * 
     * @param the new music
     */
    public void setMusic(Music music) {
        this.music = music;
    }
}
