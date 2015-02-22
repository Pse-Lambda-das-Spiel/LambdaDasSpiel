package lambda.viewcontroller.shop;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import lambda.model.profiles.ProfileManager;
import lambda.model.shop.ShopItemModel;
import lambda.model.shop.ShopItemModelObserver;

/**
 * @author: Kay Schmitteckert
 */
public class ShopItemViewController<T extends ShopItemModel> extends Actor implements ShopItemModelObserver {

    private T model;
    private TextButton currentState;

    public ShopItemViewController(T model) {
        this.model = model;
        model.addObserver(this);
    }

    public T getModel() {
        return model;
    }

    public void purchasedChanged(boolean purchased) {
        if (purchased) {
            currentState.setStyle(ShopViewController.getImageTextButtonStyle("purchased"));
        }
        else if (model.getPrice() > ProfileManager.getManager().getCurrentProfile().getCoins()){
            currentState.setStyle(ShopViewController.getImageTextButtonStyle("buyable"));
        }
        else {
            currentState.setStyle(ShopViewController.getImageTextButtonStyle("notBuyable"));
        }
    }

    public void activatedChanged(boolean activated) {
        if (activated) {
            currentState.setStyle(ShopViewController.getImageTextButtonStyle("activated"));
        }
        else {
            currentState.setStyle(ShopViewController.getImageTextButtonStyle("purchased"));
        }
    }
    
    @Override
    public void draw(Batch batch, float parentAlpha) {
        currentState = new TextButton(model.getShopItemType().getTypeName() + " " + model.getId(), 
                ShopViewController.getImageTextButtonStyle("notBuyable") );
        
        if (model.isPurchased()) {
            if (model.isActivated()) {
                currentState.setStyle(ShopViewController.getImageTextButtonStyle("activated"));
            }
            else {
                currentState.setStyle(ShopViewController.getImageTextButtonStyle("purchased"));
            }
        }
        else if (model.getPrice() > ProfileManager.getManager().getCurrentProfile().getCoins()) {
            currentState.setStyle(ShopViewController.getImageTextButtonStyle("buyable"));
        }
        else {
            currentState.setStyle(ShopViewController.getImageTextButtonStyle("notBuyable"));
        }
        currentState.addListener(new ItemClickListener());
    }

    private class ItemClickListener  extends ClickListener {
        @Override
        public void clicked(InputEvent event, float x, float y) {
            if(model.isPurchased()) {
               model.activate();
            }
            else if (model.getPrice() > ProfileManager.getManager().getCurrentProfile().getCoins()) {
                model.buy();
            }
        }
    }
}
