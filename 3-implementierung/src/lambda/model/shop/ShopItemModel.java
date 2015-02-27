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
    private String filepath;
    private ShopModel shop;
    @SuppressWarnings("rawtypes")
    protected ShopItemTypeModel shopItemType;
    protected boolean purchased;
    protected boolean activated;

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
            shopItemType.setActivatedItem(null);
            setActivated(false);
        }
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
	public void setPurchased(final boolean purchased) {
		this.purchased = purchased;
		 notify(new Consumer<ShopItemModelObserver>(){
             @Override
             public void accept(ShopItemModelObserver observer) {
                 observer.purchasedChanged(purchased);
             }
         });
	}

    public String getFilepath() {
        return filepath;
    }
    
    public ShopItemTypeModel<?> getShopItemType() {
        return shopItemType;
    }

    public boolean isActivated() {
        return activated;
    }
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
