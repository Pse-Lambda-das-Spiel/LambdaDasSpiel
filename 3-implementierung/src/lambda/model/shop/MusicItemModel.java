package lambda.model.shop;

import com.badlogic.gdx.audio.Music;

/**
 *
 *
 * Created by kay on 19.01.15.
 */
public class MusicItemModel extends ShopItemModel {

    private Music music;
    
    // Why not a constructor with 2 parameters ?
    /**
     *
     */
    public MusicItemModel() {
        super(null, 0); // TODO
    }
    
    /**
     * 
     * @param id
     * @param price
     */
    public MusicItemModel(String id,int price) {
    	super(id, price);
    }

    /**
     *
     * @return sprite
     */
    public Music getMusic() {
        return music;
    }
}
