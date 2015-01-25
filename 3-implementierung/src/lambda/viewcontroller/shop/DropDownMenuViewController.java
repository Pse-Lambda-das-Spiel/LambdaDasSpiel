package lambda.viewcontroller.shop;

import com.badlogic.gdx.assets.AssetManager;
import lambda.model.shop.ShopItemModel;
import lambda.model.shop.ShopItemTypeModel;
import lambda.viewcontroller.ViewController;


/**
 * @author: Kay Schmitteckert
 */
public class DropDownMenuViewController extends ViewController {

    private ShopItemTypeModel<ShopItemModel> shopItemTypeModel;
    private boolean open;

    public DropDownMenuViewController(ShopItemTypeModel<ShopItemModel> shopItemTypeModel) {
        this.shopItemTypeModel = shopItemTypeModel;
        open = false;
    }


    /**
     * Returns the category
     *
     * @return the category
     */
    public ShopItemTypeModel<ShopItemModel> getShopItemTypeModel() {
        return shopItemTypeModel;
    }

    /**
     * Returns the status of the dropdownmenu
     *
     * @return the status of the dropdownmenu
     */
    public boolean isOpen() {
        return open;
    }

    @Override
    public void queueAssets(AssetManager manager) {

    }

    @Override
    public void create(AssetManager manager) {

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float v) {

    }

    @Override
    public void resize(int i, int i1) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
