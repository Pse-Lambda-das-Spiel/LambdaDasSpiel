package lambda.model.shop;

import com.badlogic.gdx.audio.Music;

/**
 * Created by kay on 19.01.15.
 * 
 * @author Kay Schmitteckert
 */
public class MusicItemModel extends ShopItemModel {

    private Music music;

    /**
     * 
     * @param id
     * @param price
     */
    public MusicItemModel(String id, int price, String filepath) {
    	super(id, price, filepath);
    }

    /**
     *
     * @return sprite
     */
    public Music getMusic() {
        return music;
    }

    public void setMusic(Music music) {
        this.music = music;
    }
}
