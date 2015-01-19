package lambda.model.shop;

/**
 * Created by kay_meth on 19.01.15.
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
