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
public class ElementUIContextFamilyLoader extends AsynchronousAssetLoader<ElementUIContextFamily, ElementUIContextFamilyLoader.ElementUIContextFamilyParameter> {

    private ElementUIContextFamily elementFamily;

    public ElementUIContextFamilyLoader(FileHandleResolver resolver) {
        super(resolver);
    }

    @Override
    public void loadAsync(AssetManager assetManager, String s, FileHandle fileHandle, ElementUIContextFamilyParameter elementUIContextFamilyParameter) {
        elementFamily = null;
        elementFamily = ShopModel.getShop().loadElementUIContextFamily(fileHandle);
    }

    @Override
    public ElementUIContextFamily loadSync(AssetManager assetManager, String s, FileHandle fileHandle, ElementUIContextFamilyParameter elementUIContextFamilyParameter) {
        ElementUIContextFamily elementFamily = this.elementFamily;
        this.elementFamily = null;
        return elementFamily;
    }

    @Override
    public Array<AssetDescriptor> getDependencies(String s, FileHandle fileHandle, ElementUIContextFamilyParameter elementUIContextFamilyParameter) {
        return null;
    }

    /**
     * A parameter class for a {@link ElementUIContextFamily}.
     */
    public static class ElementUIContextFamilyParameter extends AssetLoaderParameters<ElementUIContextFamily> {
    }
}
