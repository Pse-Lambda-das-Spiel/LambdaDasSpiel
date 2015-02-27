package lambda.model.shop;

/**
 * @author Kay Schmitteckert
 */
public interface ShopItemModelObserver {

    /**
     * @param purchased
     */
    public void purchasedChanged(boolean purchased);


    /**
     *
     * @param activated
     */
     public void activatedChanged(boolean activated);
}
