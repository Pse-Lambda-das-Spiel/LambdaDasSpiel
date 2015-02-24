package lambda.viewcontroller.shop;

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
        currentState = new TextButton(model.getShopItemType().getTypeName() + " " + model.getId(), 
                ShopViewController.getTextButtonStyle("not_buyable"));
    }

    public T getModel() {
        return model;
    }

    public void purchasedChanged(boolean purchased) {
        if (purchased) {
            currentState.setStyle(ShopViewController.getTextButtonStyle("purchased"));
        }
        else if (model.getPrice() > ProfileManager.getManager().getCurrentProfile().getCoins()){
            currentState.setStyle(ShopViewController.getTextButtonStyle("buyable"));
        }
        else {
            currentState.setStyle(ShopViewController.getTextButtonStyle("not_buyable"));
        }
    }

    public void activatedChanged(boolean activated) {
        if (activated) {
            currentState.setStyle(ShopViewController.getTextButtonStyle("activated"));
        }
        else {
            currentState.setStyle(ShopViewController.getTextButtonStyle("purchased"));
        }
    }
    
    public void setCurrentState() {
        currentState = new TextButton(model.getShopItemType().getTypeName() + " " + model.getId(), 
                ShopViewController.getTextButtonStyle("not_buyable") );
        
        if (model.isPurchased()) {
            if (model.isActivated()) {
                currentState.setStyle(ShopViewController.getTextButtonStyle("activated"));
            }
            else {
                currentState.setStyle(ShopViewController.getTextButtonStyle("purchased"));
            }
        }
        else if (model.getPrice() >= ProfileManager.getManager().getCurrentProfile().getCoins()) {
            currentState.setStyle(ShopViewController.getTextButtonStyle("buyable"));
        }
        else {
            currentState.setStyle(ShopViewController.getTextButtonStyle("not_buyable"));
        }
        currentState.addListener(new ItemClickListener());
    }
    
    public TextButton getCurrentState() {
        return currentState;
    }

    private class ItemClickListener extends ClickListener {
        @Override
        public void clicked(InputEvent event, float x, float y) {
            if(model.isPurchased()) {
               if(model.isActivated()) {
                   model.deactivate();
               }
               else {
                   model.activate();
               }
            }
            else if (model.getPrice() <= ProfileManager.getManager().getCurrentProfile().getCoins()) {
                model.buy();
            }
        }
    }
}
