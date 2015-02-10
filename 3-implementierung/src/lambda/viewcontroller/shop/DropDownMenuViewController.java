package lambda.viewcontroller.shop;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import lambda.model.shop.ShopItemModel;
import lambda.model.shop.ShopItemTypeModel;

import java.util.ArrayList;
import java.util.List;


/**
 * @author: Kay Schmitteckert
 */
public class DropDownMenuViewController<T extends ShopItemModel> extends Actor {

    private AssetManager manager;
    private ShopItemTypeModel<T> shopItemTypeModel;
    private List<ImageTextButton> itemButtons;
    private List<ImageButton> purchaseButtons;
    private List<CheckBox> checkBoxes;
    private List<ShopItemViewController<T>> itemVCList;
    private boolean open;

    private TextureAtlas dropDownMenuAtlas;
    private Skin dropDownMenuSkin;
    private Table itemTable;


    public DropDownMenuViewController(ShopItemTypeModel<T> shopItemTypeModel, TextureAtlas dropDownMenuAtlas,
                                      Skin dropDownMenuSkin, AssetManager manager) {
        this.shopItemTypeModel = shopItemTypeModel;
        itemButtons = new ArrayList();
        purchaseButtons = new ArrayList();
        checkBoxes = new ArrayList();
        itemVCList = new ArrayList();

        this.dropDownMenuAtlas = dropDownMenuAtlas;
        this.dropDownMenuSkin = dropDownMenuSkin;
        this.manager = manager;

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
     * Sets true if the menu is opened
     *
     * @param open true if the menu is opened; false if the menu is closed
     */
    public void setOpen(boolean open) {
        this.open = open;
    }


    public void draw(float width, float height) {

        //TODO: WHEN TO DRAW? WHEN TO LOAD ASSETS?

        itemTable = new Table();
        itemTable.setFillParent(true);
        for (int i = 0; i < itemVCList.size(); i++) {
            itemTable.row().height(height);

            itemTable.add(itemVCList.get(i)).width(width * 0.55f).space(10);
            itemVCList.get(i).draw(width, shopItemTypeModel.getTypeName(), i, dropDownMenuSkin);

            ImageButton purchaseButton = new ImageButton(dropDownMenuSkin, "purchaseButton");
            itemTable.add(purchaseButton).width(height).align(Align.top);
            purchaseButtons.add(purchaseButton);
            purchaseButton.addListener(new PurchaseClickListener());

            CheckBox activateButton = new CheckBox("", dropDownMenuSkin);
            itemTable.add(activateButton).width(height).align(Align.top);
            checkBoxes.add(activateButton);

        }

    }


    private class PurchaseClickListener extends ClickListener {
        @Override
        public void clicked(InputEvent event, float x, float y) {
            //TODO: optional: Dialog: Do you really want to buy this icon?
            ImageButton clickedButton = (ImageButton) event.getListenerActor();
            itemVCList.get(purchaseButtons.indexOf(clickedButton)).getModel().buy();
        }
    }

    private class ActivateClickListener extends ClickListener {
        @Override
        public void clicked(InputEvent event, float x, float y) {
            //TODO: optional: (Dialog: Do you really want to activate this icon?)
            ImageButton clickedButton = (ImageButton) event.getListenerActor();
            itemVCList.get(purchaseButtons.indexOf(clickedButton)).getModel().activate();
        }
    }
}
