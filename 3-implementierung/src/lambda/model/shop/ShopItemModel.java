package lambda.model.shop;

import com.badlogic.gdx.audio.Music;

/**
 * Represents a item which is available in the shop
 *
 * @author Kay Schmitteckert
 */
public class ShopItemModel implements ShopItemModelObserver {

    private String id;
    private int price;
    private ShopModel shop;
    private ShopItemTypeModel shopItemType;

    /**
     *
     */
    public ShopItemModel() {
        //TODO
    }

    /**
     * Changes the state of this item to purchased if there are enough coins
     */
    public void buy() {
        //TODO
    }

    /**
     * Activates this item if its already purchased
     */
    public void acitvate() {
        //TODO
    }

    /**
     * Returns the category of this item
     *
     * @return shopItemType
     */
    public ShopItemTypeModel getShopItemType() {
        return shopItemType;
    }

    /**
     * Returns the ID of this item
     *
     * @return ID
     */
    public String getID() {
        return id;
    }

    /**
     * Returns the price of this item
     *
     * @return PRICE
     */
    public int getPRICE() {
        return price;
    }

    /**
     * Returns the shop in which this item is available
     *
     * @return shop
     */
    public ShopModel getShop() {
        return shop;
    }
}
