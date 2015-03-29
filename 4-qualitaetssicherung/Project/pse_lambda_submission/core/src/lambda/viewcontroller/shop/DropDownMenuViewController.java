package lambda.viewcontroller.shop;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.utils.SnapshotArray;

import lambda.model.shop.ShopItemModel;
import lambda.model.shop.ShopItemTypeModel;

/**
 * Represents the elements of the drop down menu in the shop and holds every
 * item of one category.
 * 
 * @author: Kay Schmitteckert
 */
public class DropDownMenuViewController<T extends ShopItemModel> extends Actor {

    private ShopItemTypeModel<T> shopItemTypeModel;
    private VerticalGroup groupButtons;
    private VerticalGroup groupVCs;

    /**
     * Creates a new instance of this class and creates a view controller for
     * every item
     * 
     * @param shopItemTypeModel
     *            the category of the items
     * @param stage
     *            the stage
     */
    public DropDownMenuViewController(ShopItemTypeModel<T> shopItemTypeModel,
            Stage stage) {
        this.shopItemTypeModel = shopItemTypeModel;
        groupButtons = new VerticalGroup().align(Align.center);
        groupVCs = new VerticalGroup().align(Align.center);

        // Creates a view-controller for each item in this category and puts
        // them into the list "itemVCList"
        for (int i = 0; i < shopItemTypeModel.getItems().size(); i++) {
            ShopItemViewController<T> itemVC = new ShopItemViewController<T>(
                    shopItemTypeModel.getItems().get(i), stage);
            groupButtons.addActor(itemVC.getCurrentState());
            groupVCs.addActor(itemVC);
        }
    }

    /**
     * Updates the vertical groups while call "setCurrentState()" of every item
     * 
     * @return the updated vertical group of the buttons
     */
    public VerticalGroup updateButtons() {
        SnapshotArray<Actor> itemVCs = groupVCs.getChildren();
        VerticalGroup updatedButtons = new VerticalGroup();
        for (int i = 0; i < itemVCs.size; i++) {
            ShopItemViewController<?> itemVC = (ShopItemViewController<?>) itemVCs
                    .get(i);
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

    /**
     * Returns the vertical group of the items buttons
     * 
     * @return vertical group of every button
     */
    public VerticalGroup getGroup() {
        return groupButtons;
    }

    /**
     * Returns the vertical group of the items view controller
     * 
     * @return vertical group of every view controller
     */
    public VerticalGroup getGroupVCs() {
        return groupVCs;
    }
}
