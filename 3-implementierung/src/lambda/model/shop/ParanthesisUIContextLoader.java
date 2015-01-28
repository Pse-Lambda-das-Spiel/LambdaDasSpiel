package lambda.model.shop;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import lambda.viewcontroller.level.ParanthesisUIContext;

/**
 * @author: Kay Schmitteckert
 */
public class ParanthesisUIContextLoader extends AsynchronousAssetLoader<ParanthesisUIContext, ParanthesisUIContextLoader.ParanthesisUIContextParameter> {

    private ParanthesisUIContext paranthesis;

    public ParanthesisUIContextLoader(FileHandleResolver resolver) {
        super(resolver);
    }

    @Override
    public void loadAsync(AssetManager assetManager, String s, FileHandle fileHandle, ParanthesisUIContextParameter paranthesisUIContextParameter) {
        paranthesis = null;
        paranthesis = ShopModel.getShop().loadParanthesisUIContext(fileHandle);
    }

    @Override
    public ParanthesisUIContext loadSync(AssetManager assetManager, String s, FileHandle fileHandle, ParanthesisUIContextParameter paranthesisUIContextParameter) {
        ParanthesisUIContext paranthesis = this.paranthesis;
        this.paranthesis = null;
        return paranthesis;
    }

    @Override
    public Array<AssetDescriptor> getDependencies(String s, FileHandle fileHandle, ParanthesisUIContextParameter paranthesisUIContextParameter) {
        return null;
    }


    /**
     * A parameter class for a {@link ParanthesisUIContext}.
     */
    public static class ParanthesisUIContextParameter extends AssetLoaderParameters<ParanthesisUIContext> {
    }
}

