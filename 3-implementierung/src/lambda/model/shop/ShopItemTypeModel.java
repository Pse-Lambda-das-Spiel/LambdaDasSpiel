package lambda.model.shop;

import java.util.List;

import com.badlogic.gdx.audio.Music;

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

    }

    /**
     *
     * @return
     */
    public String getTypeName() {
        return typeName;
    }

    /**
     *
     * @return
     */
    public List<T> getItems() {
        return items;
    }

    /**
     *
     * @return
     */
    public T getActivatedItem() {
        return activatedItem;
    }

    public void setActivatedItem(T activate) {
        activatedItem = activate;
    }
}
