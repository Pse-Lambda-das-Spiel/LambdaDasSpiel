package lambda.viewcontroller.shop;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import lambda.model.profiles.ProfileManager;
import lambda.model.shop.ShopItemModel;
import lambda.model.shop.ShopItemModelObserver;

/**
 * @author: Kay Schmitteckert
 */
public class ShopItemViewController<T extends ShopItemModel> extends Actor implements ShopItemModelObserver {

    private T model;
    private InputMultiplexer inputMultiplexer;

    public ShopItemViewController(T model) {
        this.model = model;
    }

    public T getModel() {
        return model;
    }

    public void purchasedChanged(boolean purchased) {

    }

    public void activatedChanged(boolean activated) {

    }

    public void draw(float width, String typeName, int i, Skin dropDownMenuSkin) {
        ImageTextButton itemButton;
        if (model.isPurchased()) {
            if (model.isActivated()) {
                //TODO: activatedGround into sheet.png
                itemButton = new ImageTextButton(typeName + i, dropDownMenuSkin, "activatedGround");
            }
            else {
                itemButton = new ImageTextButton(typeName + i, dropDownMenuSkin, "purchasedGround");
            }
        }
        else if (model.getPrice() > ProfileManager.getManager().getCurrentProfile().getCoins()) {
            itemButton = new ImageTextButton(typeName + i, dropDownMenuSkin, "buyableGround");
        }
        else {
            itemButton = new ImageTextButton(typeName + i, dropDownMenuSkin, "notBuyableGround");
        }

        itemButton.getLabel().setFontScale(0.5f);
        itemButton.addListener(new ItemClickListener());

    }

    private class ItemClickListener  extends ClickListener {
        @Override
        public void clicked(InputEvent event, float x, float y) {
            ImageButton clickedButton = (ImageButton) event.getListenerActor();
            if(model.isPurchased()) {
                model.activate();
            }
            else {
                model.buy();
            }

        }
    }
}
