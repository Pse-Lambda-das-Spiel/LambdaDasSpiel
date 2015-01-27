package lambda.viewcontroller.shop;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import lambda.model.shop.ShopModel;
import lambda.viewcontroller.ViewController;
import lambda.viewcontroller.assets.AssetViewController;

public class ShopViewController extends ViewController {

    private ShopModel shop;
    private AssetManager assetManager;

	public ShopViewController() {
        shop = ShopModel.getShop();
        assetManager = getGame().getController(AssetViewController.class).getManager();
	}

    @Override
    public void queueAssets(AssetManager assets) {
        shop.setAssetManager(assetManager);
        shop.queueAssets(assetManager);
    }

    @Override
    public void show() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void render(float delta) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void resize(int width, int height) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void pause() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void resume() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void hide() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void dispose() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void create(AssetManager manager) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
