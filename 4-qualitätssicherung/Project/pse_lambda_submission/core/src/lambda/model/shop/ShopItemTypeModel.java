package lambda.model.shop;

import java.util.LinkedList;
import java.util.List;

/**
 * Represents a category of a ShopItemModel. 
 * This class holds a list of all its items and also the activated item.
 *
 * @author Kay Schmitteckert
 */
public class ShopItemTypeModel<T extends ShopItemModel> {

    private String typeName;
    private List<T> items;
    private T activatedItem;
    private T defaultItem;

    /**
     * Creates a new instance of this class
     * 
     * @param typeName the identifier of this category
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
        if (activatedItem == null) {
            return getDefaultItem();
        }
        else {
            return activatedItem;
        }
    }
    
    /**
     * Sets the default item
     * 
     * @param item which is the default one
     */
    public void setDefaultItem(T defaultItem) {
        this.defaultItem = defaultItem;
    }
    

    /**
     * Sets an item as activated
     *
     * @param activate the item which will be the activated item
     */
    public void setActivatedItem(T activate) {
        activatedItem = activate;
        if (activate != null) {
            activate.setActivated(true);
        }
    }
    
    /**
     * Returns the default Item
     * 
     * @return the default Item
     */
    public T getDefaultItem() {
        return defaultItem;
    }
}
