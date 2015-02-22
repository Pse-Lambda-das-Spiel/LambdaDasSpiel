package lambda.viewcontroller.shop;


import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import lambda.model.shop.ShopItemModel;
import lambda.model.shop.ShopItemTypeModel;



/**
 * @author: Kay Schmitteckert
 */
public class DropDownMenuViewController<T extends ShopItemModel> extends Actor {

    private ShopItemTypeModel<T> shopItemTypeModel;
    private VerticalGroup group;

    public DropDownMenuViewController(ShopItemTypeModel<T> shopItemTypeModel) {
        this.shopItemTypeModel = shopItemTypeModel;
        group = new VerticalGroup().align(Align.center);

        //Creates a view-controller for each item in this category and puts them into the list "itemVCList"
        for (int i = 0; i < shopItemTypeModel.getItems().size(); i++) {
            ShopItemViewController<T> itemVC = new ShopItemViewController<T>(shopItemTypeModel.getItems().get(i));
            group.addActor(itemVC);
        }
    }


    /**
     * Returns the category
     *
     * @return the category
     */
    public ShopItemTypeModel<T> getShopItemTypeModel() {
        return shopItemTypeModel;
    }

    public void draw(float width, float height) {

    }
    
    public VerticalGroup getGroup() {
        return group;
    }

/*
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
    */
}
