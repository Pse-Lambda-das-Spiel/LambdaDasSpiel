package lambda.model.shop;

import java.util.LinkedList;
import java.util.List;

/**
 *
 *
 * @author Kay Schmitteckert
 */
public class ShopItemTypeModel<T extends ShopItemModel> {

    private String typeName;
    private List<T> items;
    private T activatedItem;

    /**
     *
     */
    public ShopItemTypeModel(String typeName) {
        this.typeName = typeName;
        items = new LinkedList<T>();

    }

    /**
     * Returns the name of the category, the type of all items inside this category.
     *
     * @return the name of the category
     */
    public String getTypeName() {
        return typeName;
    }

    /**
     * Returns the list with all items of this type
     *
     * @return list with all items of this type
     */
    public List<T> getItems() {
        return items;
    }

    /**
     * Returns the currently activated item
     *
     * @return the currently activated item
     */
    public T getActivatedItem() {
        return activatedItem;
    }

    /**
     * Sets an item as activated
     *
     * @param activate the item which will be the activated item
     */
    public void setActivatedItem(T activate) {
        activatedItem = activate;
    }
}
