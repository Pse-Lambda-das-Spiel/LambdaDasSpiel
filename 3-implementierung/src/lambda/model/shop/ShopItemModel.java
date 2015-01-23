package lambda.model.shop;

import com.badlogic.gdx.audio.Music;
import lambda.Observable;
import lambda.model.profiles.ProfileModel;

/**
 * Represents a item which is available in the shop
 *
 * @author Kay Schmitteckert
 */
public class ShopItemModel extends Observable<ShopItemModelObserver> {

    private String id;
    private int price;
    private ShopModel shop;
    private ShopItemTypeModel shopItemType;
    private boolean purchased;
    private ProfileModel profile;

    /**
     *
     */
    public ShopItemModel(String id, int price) {
        this.id = id;
        this.price = price;
        purchased = false;

    }

    public ShopItemModel() {
    }

    /**
     * Changes the state of this item to purchased if there are enough coins
     */
    public void buy() {
        if(profile.getCoins() >= getPrice()) {
            purchased = true;
        }
    }

    /**
     * Activates this item if its already purchased
     */
    public void acitvate() {
        if(purchased){
            shopItemType.setActivatedItem(this);
        }
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
     * @return price
     */
    public int getPrice() {
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
