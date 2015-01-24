package lambda.model.shop;

import com.badlogic.gdx.audio.Music;

/**
 *
 *
 * Created by kay on 19.01.15.
 */
public class MusicItemModel extends ShopItemModel {

    private Music music;

    /**
     *
     */
    public MusicItemModel() {
        super(null, 0); // TODO
    }

    /**
     *
     * @return sprite
     */
    public Music getMusic() {
        return music;
    }
}
