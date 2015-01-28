package lambda.model.shop;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;

/**
 * @author: Kay Schmitteckert
 */
public class BackgroundImageItemModelLoader extends AsynchronousAssetLoader<BackgroundImageItemModel, BackgroundImageItemModelLoader.BackgroundImageItemModelParameter> {

    private BackgroundImageItemModel bgImage;

    public BackgroundImageItemModelLoader(FileHandleResolver resolver) {
        super(resolver);
    }

    @Override
    public void loadAsync(AssetManager assetManager, String s, FileHandle fileHandle, BackgroundImageItemModelParameter backgroundImageItemModelParameter) {
        bgImage = null;
        bgImage = ShopModel.getShop().loadBackgroundImageItem(fileHandle);
    }

    @Override
    public BackgroundImageItemModel loadSync(AssetManager assetManager, String s, FileHandle fileHandle, BackgroundImageItemModelParameter backgroundImageItemModelParameter) {
        BackgroundImageItemModel bgImage = this.bgImage;
        this.bgImage = null;
        return bgImage;
    }

    @Override
    public Array<AssetDescriptor> getDependencies(String s, FileHandle fileHandle, BackgroundImageItemModelParameter backgroundImageItemModelParameter) {
        return null;
    }

    /**
     * A parameter class for a {@link BackgroundImageItemModel}.
     */
    public static class BackgroundImageItemModelParameter extends AssetLoaderParameters<BackgroundImageItemModel> {
    }
}
