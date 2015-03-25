package lambda.model.shop;

import lambda.Consumer;
import lambda.Observable;
import lambda.model.profiles.ProfileManager;

/**
 * Represents a item which is available in the shop
 *
 * @author Kay Schmitteckert
 */
public class ShopItemModel extends Observable<ShopItemModelObserver> {

    private String id;
    private int price;
    private String filename;
    @SuppressWarnings("rawtypes")
    protected ShopItemTypeModel shopItemType;
    protected boolean purchased;
    protected boolean activated;


    /**
     * Creates a new instance of this class
     * 
     * @param id the if of this item
     * @param price the price of this item
     * @param filepath the path to the asset
     */
    public ShopItemModel(String id, int price, String filename) {
        this.id = id;
        this.price = price;
        this.filename = filename;
        purchased = false;
        activated = false;
    }

    /**
     * Changes the state of this item to purchased if there are enough coins
     */
    public void buy() {
        if(ProfileManager.getManager().getCurrentProfile().getCoins() >= getPrice()) {
            purchased = true;
            ProfileManager.getManager().getCurrentProfile().setCoins(
                    ProfileManager.getManager().getCurrentProfile().getCoins() - getPrice());
            notify(new Consumer<ShopItemModelObserver>(){
                @Override
                public void accept(ShopItemModelObserver observer) {
                    observer.purchasedChanged(true);
                }
            });
        }
    }

    /**
     * Activates this item if its already purchased
     */
    @SuppressWarnings("unchecked")
    public void activate() {
        if(purchased){
            shopItemType.setActivatedItem(this);
            setActivated(true);
        }
    }
    /**
     * Deactivates this item if its activated and sets the activated item on the category to "null"
     */
    @SuppressWarnings("unchecked")
    public void deactivate() {
        if(purchased){
            shopItemType.setActivatedItem(shopItemType.getDefaultItem());
            setActivated(false);
        }
    }

    /**
     * Returns the id of this item
     *
     * @return the id of this item
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the price of this item
     *
     * @return the price of this item
     */
    public int getPrice() {
        return price;
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
     * Sets the new state of purchased and notifies the other classes
     *
	 * @param purchased the boolean to set
	 */
	public void setPurchased(final boolean purchased) {
		this.purchased = purchased;
		 notify(new Consumer<ShopItemModelObserver>(){
             @Override
             public void accept(ShopItemModelObserver observer) {
                 observer.purchasedChanged(purchased);
             }
         });
	}

	/**
	 * Returns the filename of this item 
	 * 
	 * @return the filename of the asset
	 */
    public String getFilename() {
        return filename;
    }
    
    /**
     * Returns the category in which this item is available
     * 
     * @return the category of this item
     */
    public ShopItemTypeModel<?> getShopItemType() {
        return shopItemType;
    }

    /**
     * Returns the state of this item.
     * 
     * @return true if activated, false if not
     */
    public boolean isActivated() {
        return activated;
    }
    
    /**
     * Sets the new state of this item and notifies the other classes about this change
     * 
     * @param activated is the boolean to set
     */
    public void setActivated(final boolean activated) {
        this.activated = activated;
        notify(new Consumer<ShopItemModelObserver>(){
            @Override
            public void accept(ShopItemModelObserver observer) {
                observer.activatedChanged(activated);
            }
        });
    }
}
