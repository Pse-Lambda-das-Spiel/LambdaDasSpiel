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
import lambda.viewcontroller.level.VariableUIContext;


/**
 *
 * @author: Kay Schmitteckert
 */
public class VariableUIContextLoader extends AsynchronousAssetLoader<VariableUIContext, VariableUIContextLoader.VariableUIContextParameter> {

    private VariableUIContext variableUIContext;

    public VariableUIContextLoader(FileHandleResolver resolver) {
        super(resolver);
    }

    @Override
    public void loadAsync(AssetManager assetManager, String s, FileHandle fileHandle, VariableUIContextParameter variableUIContextParameter) {
        variableUIContext = null;
        variableUIContext = ShopModel.getShop().loadVariableUIContext(fileHandle);
    }

    @Override
    public VariableUIContext loadSync(AssetManager assetManager, String s, FileHandle fileHandle, VariableUIContextParameter variableUIContextParameter) {
        VariableUIContext variableUIContext = this.variableUIContext;
        this.variableUIContext = null;
        return variableUIContext;
    }

    @Override
    public Array<AssetDescriptor> getDependencies(String s, FileHandle fileHandle, VariableUIContextParameter variableUIContextParameter) {
        return null;
    }

    /**
     * A parameter class for a {@link VariableUIContext}.
     */
    public static class VariableUIContextParameter extends AssetLoaderParameters<VariableUIContext> {
    }
}
