package lambda.viewcontroller.shop;

import com.badlogic.gdx.assets.AssetManager;
import lambda.model.shop.ShopItemModel;
import lambda.model.shop.ShopItemTypeModel;
import lambda.viewcontroller.ViewController;

import java.util.ArrayList;
import java.util.List;


/**
 * @author: Kay Schmitteckert
 */
public class DropDownMenuViewController<T extends ShopItemModel> extends ViewController {

    private ShopItemTypeModel<T> shopItemTypeModel;
    private List<ShopItemViewController<T>> itemVCList;
    private boolean open;

    public DropDownMenuViewController(ShopItemTypeModel<T> shopItemTypeModel) {
        this.shopItemTypeModel = shopItemTypeModel;
        itemVCList = new ArrayList<ShopItemViewController<T>>();
        //Creates a view-controller for each item in this category and puts them into the list "itemVCList"
        for (int i = 0; i < shopItemTypeModel.getItems().size(); i++) {
            ShopItemViewController<T> itemVC = new ShopItemViewController(shopItemTypeModel.getItems().get(i));
            itemVCList.add(i, itemVC);
        }
        open = false;
    }


    /**
     * Returns the category
     *
     * @return the category
     */
    public ShopItemTypeModel<T> getShopItemTypeModel() {
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

    /**
     * Sets true if the menu is openend
     *
     * @param open true if the menu is opened; false if the menu is closed
     */
    public void setOpen(boolean open) {
        this.open = open;
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
