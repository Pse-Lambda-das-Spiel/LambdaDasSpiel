package lambda.model.shop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;


/**
 *
 * @author: Kay Schmitteckert
 */
public class MusicItemModelLoader extends AsynchronousAssetLoader<MusicItemModel, MusicItemModelLoader.MusicItemModelParameter> {

    private MusicItemModel musicItem;

    /**
     * Creates an new LevelModelLoader.
     * @param resolver The {@link com.badlogic.gdx.assets.loaders.FileHandleResolver} that resolves a given filename to a {@link com.badlogic.gdx.files.FileHandle}
     */
    public MusicItemModelLoader(FileHandleResolver resolver) {
        super(resolver);
    }

    @Override
    public Array<AssetDescriptor> getDependencies(String s, FileHandle fileHandle, MusicItemModelParameter musicItemModelParameter) {
        return null;
    }

    @Override
    public void loadAsync(AssetManager assetManager, String s, FileHandle fileHandle, MusicItemModelParameter musicItemModelParameter) {
        musicItem = null;
        musicItem = ShopModel.getShop().loadMusicItem(fileHandle);
    }

    @Override
    public MusicItemModel loadSync(AssetManager assetManager, String s, FileHandle fileHandle, MusicItemModelParameter musicItemModelParameter) {
        MusicItemModel musicItem = this.musicItem;
        this.musicItem = null;
        return musicItem;
    }


    /**
     * A parameter class for a {@link MusicItemModel}.
     */
    public static class MusicItemModelParameter extends AssetLoaderParameters<MusicItemModel> {
    }
}
