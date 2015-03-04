package lambda.viewcontroller.shop;


import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.utils.SnapshotArray;

import lambda.model.shop.ShopItemModel;
import lambda.model.shop.ShopItemTypeModel;



/**
 * @author: Kay Schmitteckert
 */
public class DropDownMenuViewController<T extends ShopItemModel> extends Actor {

    private ShopItemTypeModel<T> shopItemTypeModel;
    private VerticalGroup groupButtons;
    private VerticalGroup groupVCs;

    public DropDownMenuViewController(ShopItemTypeModel<T> shopItemTypeModel, Stage stage) {
        this.shopItemTypeModel = shopItemTypeModel;
        groupButtons = new VerticalGroup().align(Align.center);
        groupVCs = new VerticalGroup().align(Align.center);

        //Creates a view-controller for each item in this category and puts them into the list "itemVCList"
        for (int i = 0; i < shopItemTypeModel.getItems().size(); i++) {
            ShopItemViewController<T> itemVC = new ShopItemViewController<T>(shopItemTypeModel.getItems().get(i), stage);
            groupButtons.addActor(itemVC.getCurrentState());
            groupVCs.addActor(itemVC);
        }
    }
    
    public VerticalGroup updateButtons() {
        SnapshotArray<Actor> itemVCs = groupVCs.getChildren();
        VerticalGroup updatedButtons = new VerticalGroup();
        for (int i = 0; i < itemVCs.size; i++) {
            ShopItemViewController<?> itemVC = (ShopItemViewController<?>) itemVCs.get(i);
            itemVC.setCurrentState();
            updatedButtons.addActor(itemVC.getCurrentState());
        }
        return updatedButtons;
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
        return groupButtons;
    }
    
    public VerticalGroup getGroupVCs() {
        return groupVCs;
    }

}
