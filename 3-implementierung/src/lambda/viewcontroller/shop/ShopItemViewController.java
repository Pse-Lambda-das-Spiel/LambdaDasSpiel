package lambda.viewcontroller.shop;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import lambda.model.shop.ShopItemModel;
import lambda.model.shop.ShopItemModelObserver;

/**
 * @author: Kay Schmitteckert
 */
public class ShopItemViewController<T extends ShopItemModel> extends Actor implements ShopItemModelObserver {

    private T model;
    private final Stage stage;
    private InputMultiplexer inputMultiplexer;

    public ShopItemViewController(T model) {
        stage = new Stage(new ScreenViewport());
        this.model = model;
    }

    public void purchasedChanged(boolean purchased) {

    }

    public void activatedChanged(boolean activated) {

    }

    public void draw() {

    }

}
