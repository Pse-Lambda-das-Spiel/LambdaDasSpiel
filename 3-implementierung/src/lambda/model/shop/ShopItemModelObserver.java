package lambda.model.shop;

/**
 * @author Kay Schmitteckert
 */
public interface ShopItemModelObserver {

    /**
     * @param purchased
     */
    default void purchasedChanged(boolean purchased) {

    }


    /**
     *
     * @param activated
     */
     default void activatedChanged(boolean activated) {

     }
}
