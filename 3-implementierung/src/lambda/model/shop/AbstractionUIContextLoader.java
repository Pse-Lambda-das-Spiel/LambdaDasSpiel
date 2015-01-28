package lambda.model.shop;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import lambda.viewcontroller.level.AbstractionUIContext;
import lambda.viewcontroller.level.ParanthesisUIContext;

/**
 * @author: Kay Schmitteckert
 */
public class AbstractionUIContextLoader extends AsynchronousAssetLoader<AbstractionUIContext, AbstractionUIContextLoader.AbstractionUIContextParameter> {

    private AbstractionUIContext abstraction;

    public AbstractionUIContextLoader(FileHandleResolver resolver) {
        super(resolver);
    }

    @Override
    public void loadAsync(AssetManager assetManager, String s, FileHandle fileHandle, AbstractionUIContextParameter abstractionUIContextParameter) {
        abstraction = null;
        abstraction = ShopModel.getShop().loadAbstractionUIContext(fileHandle);
    }

    @Override
    public AbstractionUIContext loadSync(AssetManager assetManager, String s, FileHandle fileHandle, AbstractionUIContextParameter abstractionUIContextParameter) {
        AbstractionUIContext abstraction = this.abstraction;
        this.abstraction = null;
        return abstraction;
    }

    @Override
    public Array<AssetDescriptor> getDependencies(String s, FileHandle fileHandle, AbstractionUIContextParameter abstractionUIContextParameter) {
        return null;
    }


    /**
     * A parameter class for a {@link ParanthesisUIContext}.
     */
    public static class AbstractionUIContextParameter extends AssetLoaderParameters<AbstractionUIContext> {
    }
}

