package lambda.model.shop;

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
    private String filepath;
    //how to initialize theses attributes at the moment ? setter ? constructor ?
    private ShopModel shop;
    private ShopItemTypeModel shopItemType;
    private boolean purchased;
    private boolean activated;
    private ProfileModel profile;

    /**
     *
     */
    public ShopItemModel(String id, int price, String filepath) {
        this.id = id;
        this.price = price;
        this.filepath = filepath;
        purchased = false;
        activated = false;
    }

    /**
     * Changes the state of this item to purchased if there are enough coins
     */
    public void buy() {
        if(profile.getCoins() >= getPrice()) {
            purchased = true;
            notify((observer) -> observer.purchasedChanged(true));
        }
    }

    /**
     * Activates this item if its already purchased
     */
    public void activate() {
        if(purchased){
            shopItemType.setActivatedItem(this);
            activated = true;
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
     * Returns the Id of this item
     *
     * @return Id
     */
    public String getId() {
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


    /**
     * Returns if the item is already purchased.
     * true, if the item is purchased
     *
	 * @return the purchased
	 */
	public boolean isPurchased() {
		return purchased;
	}

	/**
     * Setter for purchased
     *
	 * @param purchased the boolean to set
	 */
	public void setPurchased(boolean purchased) {
		this.purchased = purchased;
		notify((observer) -> observer.purchasedChanged(purchased));
	}

    public String getFilepath() {
        return filepath;
    }

    public boolean isActivated() {
        return activated;
    }
    public void setActivated(boolean activated) {
        this.activated = activated;
        notify((observer) -> observer.activatedChanged(activated));
    }
}
