package lambda.viewcontroller.shop;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
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
    private Stage stage;

    public ShopItemViewController(T model, Stage stage) {
        this.model = model;
        this.stage = stage;
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
        else if (model.getPrice() <= ProfileManager.getManager().getCurrentProfile().getCoins()) {
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
                   new Dialog("", ShopViewController.getSkin()) {
                       {
                           clear();
                           // getImage() removes the image from its button so the button has to be copied before that
                           float space = stage.getWidth() / 64;
                           Label label = new Label("Willst du Item " + model.getId() + "\n aktivieren?", ShopViewController.getSkin());
                           add(label).pad(stage.getWidth() / 64).padBottom(0).padRight(400);
                           row();
                           
                           label.setWrap(true);
                           ImageButton activate = new ImageButton(ShopViewController.getImageButtonStyle("accept"));
                           activate.addListener(new ClickListener() {
                               @Override
                               public void clicked(InputEvent event, float x, float y) {
                                   model.activate();
                                   setVisible(false);
                                   hide();
                               }
                           });
                           add(activate).pad(space).padBottom(space * 3 / 2).align(Align.bottomRight);
                           //no
                           ImageButton abort = new ImageButton(ShopViewController.getImageButtonStyle("abort"));
                           abort.addListener(new ClickListener() {
                               @Override
                               public void clicked(InputEvent event, float x, float y) {
                                   setVisible(false);
                                   hide();
                               }
                           });
                           add(abort).pad(space).padBottom(space * 3 / 2).align(Align.bottomLeft);
                       }
                          
                   }.show(stage);
               }
            }
            else if (model.getPrice() <= ProfileManager.getManager().getCurrentProfile().getCoins()) {
                new Dialog("", ShopViewController.getSkin()) {
                    {
                        clear();
                        // getImage() removes the image from its button so the button has to be copied before that
                        float space = stage.getWidth() / 64;
                        Label label = new Label("Willst du " + model.getPrice() + " Münzen \n gegen dieses Item \n eintauschen?", ShopViewController.getSkin());
                        add(label).pad(stage.getWidth() / 64).padBottom(0).padRight(400);
                        row();
                        
                        label.setWrap(true);
                        //yes
                        ImageButton accept = new ImageButton(ShopViewController.getImageButtonStyle("accept"));
                        accept.addListener(new ClickListener() {
                            @Override
                            public void clicked(InputEvent event, float x, float y) {
                                model.buy();
                                setVisible(false);
                                hide();
                            }
                        });
                        add(accept).pad(space).padBottom(space * 3 / 2).align(Align.bottomRight);
                        //no
                        ImageButton noButton = new ImageButton(ShopViewController.getImageButtonStyle("abort"));
                        noButton.addListener(new ClickListener() {
                            @Override
                            public void clicked(InputEvent event, float x, float y) {
                                setVisible(false);
                                hide();
                            }
                        });
                        add(noButton).pad(space).padBottom(space * 3 / 2).align(Align.bottomLeft);
                    }
                       
                }.show(stage);
            }
        }
    }
}
