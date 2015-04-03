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
import lambda.viewcontroller.AudioManager;

/**
 * Represents the ImageTextButtons of an item in the shop This class updates the
 * button when coins are changed, any item is purchased or activated
 * 
 * @author: Kay Schmitteckert
 */
public class ShopItemViewController<T extends ShopItemModel> extends Actor
        implements ShopItemModelObserver {

    private T model;
    private TextButton currentState;
    private ShopViewController vc;
    private Stage stage;
    private final float buttonSize;
    /**
     * Creates a new instance of this class
     * 
     * @param model
     *            the model of the item
     * @param vc
     *            the ShopViewController
     */
    public ShopItemViewController(T model, ShopViewController vc) {
        this.model = model;
        this.vc = vc;
        this.stage = vc.getStage();
        this.buttonSize = stage.getWidth() / 6;
        model.addObserver(this);
        currentState = new TextButton(model.getShopItemType().getTypeName()
                + " " + model.getId(),
                ShopViewController.getTextButtonStyle("not_buyable"));
        fitButton();
    }

    /**
     * Returns the model of this item
     * 
     * @return the model of this item
     */
    public T getModel() {
        return model;
    }

    /**
     * Is called when the state of this item has changed
     * 
     * @param purchased
     *            the new state
     */
    public void purchasedChanged(boolean purchased) {
        if (purchased) {
            currentState.setStyle(ShopViewController
                    .getTextButtonStyle("purchased"));
        } else if (model.getPrice() > ProfileManager.getManager()
                .getCurrentProfile().getCoins()) {
            currentState.setStyle(ShopViewController
                    .getTextButtonStyle("buyable"));
        } else {
            currentState.setStyle(ShopViewController
                    .getTextButtonStyle("not_buyable"));
        }
        fitButton();
    }

    /**
     * Is called when the state of this item has changed
     * 
     * @param activated
     *            the new state
     */
    public void activatedChanged(boolean activated) {
        if (activated) {
            currentState.setStyle(ShopViewController
                    .getTextButtonStyle("activated"));
        } else {
            currentState.setStyle(ShopViewController
                    .getTextButtonStyle("purchased"));
        }
        fitButton();
    }

    /**
     * Updates the view of the button
     */
    public void setCurrentState() {
        currentState = new TextButton(model.getFilename(),
                ShopViewController.getTextButtonStyle("not_buyable"));
        if (model.isPurchased()) {
            if (model.isActivated()) {
                currentState.setStyle(ShopViewController
                        .getTextButtonStyle("activated"));
            } else {
                currentState.setStyle(ShopViewController
                        .getTextButtonStyle("purchased"));
            }
        } else if (model.getPrice() <= ProfileManager.getManager()
                .getCurrentProfile().getCoins()) {
            currentState.setStyle(ShopViewController
                    .getTextButtonStyle("buyable"));
        } else {
            currentState.setStyle(ShopViewController
                    .getTextButtonStyle("not_buyable"));
        }
        currentState.addListener(new ItemClickListener());
        fitButton();
    }

    /**
     * Returns the current button view of this item
     * 
     * @return a text button of the current state
     */
    public TextButton getCurrentState() {
        return currentState;
    }

    private void fitButton() {
        float temp = stage.getHeight() / 720;
        currentState.getLabel().setFontScale(temp);
        currentState.getStyle().up.setMinHeight(temp * 100);
        currentState.getStyle().up.setMinWidth(temp * 481);
        currentState.getStyle().down.setMinHeight(temp * 100);
        currentState.getStyle().down.setMinWidth(temp * 481);
    }
    
    /**
     * ClickListener for the button of this item
     */
    private class ItemClickListener extends ClickListener {
        @Override
        public void clicked(InputEvent event, float x, float y) {
            AudioManager.playSound("buttonClick");
            if (model.isPurchased()) {
                if (model.isActivated()) {
                    model.deactivate();
                } else {
                    vc.showDialog(new Dialog("", ShopViewController.getSkin()) {
                        {
                            clear();
                            // getImage() removes the image from its button so
                            // the button has to be copied before that
                            float space = stage.getWidth() / 64;
                            pad(space * 2);
                            Label label = new Label(ShopViewController
                                    .getLanguage().format("item_activate",
                                            model.getFilename()),
                                    ShopViewController.getSkin());
                            add(label).width(stage.getWidth() * 2 / 3).colspan(2);
                            label.setFontScale(stage.getHeight() / 720 * label.getFontScaleY());
                            row();

                            label.setWrap(true);
                            ImageButton activate = new ImageButton(
                                    ShopViewController
                                            .getImageButtonStyle("accept"));
                            activate.addListener(new ClickListener() {
                                @Override
                                public void clicked(InputEvent event, float x,
                                        float y) {
                                    AudioManager.playSound("buttonClick");
                                    model.getShopItemType().getActivatedItem()
                                            .deactivate();
                                    model.activate();
                                    setVisible(false);
                                    hide();
                                }
                            });
                            add(activate).pad(space).align(Align.right).size(buttonSize);
                            // no
                            ImageButton abort = new ImageButton(
                                    ShopViewController
                                            .getImageButtonStyle("abort"));
                            abort.addListener(new ClickListener() {
                                @Override
                                public void clicked(InputEvent event, float x,
                                        float y) {
                                    AudioManager.playSound("buttonClick");
                                    setVisible(false);
                                    hide();
                                }
                            });
                            add(abort).pad(space).align(Align.left).size(buttonSize);
                        }

                    });
                }
            } else if (model.getPrice() <= ProfileManager.getManager()
                    .getCurrentProfile().getCoins()) {
                vc.showDialog(new Dialog("", ShopViewController.getSkin()) {
                    {
                        clear();
                        // getImage() removes the image from its button so the
                        // button has to be copied before that
                        float space = stage.getWidth() / 64;
                        pad(space * 2);
                        Label label = new Label(ShopViewController
                                .getLanguage().format("item_buy",
                                        model.getPrice(), model.getFilename()),
                                ShopViewController.getSkin());
                        label.setFontScale(stage.getHeight() / 720 * label.getFontScaleY());
                        add(label).width(stage.getWidth() * 2 / 3).colspan(2);
                        row();

                        label.setWrap(true);
                        // yes
                        ImageButton accept = new ImageButton(
                                ShopViewController
                                        .getImageButtonStyle("accept"));
                        accept.addListener(new ClickListener() {
                            @Override
                            public void clicked(InputEvent event, float x,
                                    float y) {
                                AudioManager.playSound("buttonClick");
                                model.buy();
                                setVisible(false);
                                hide();
                            }
                        });
                        add(accept).pad(space).align(Align.right).size(buttonSize);
                        
                        // no
                        ImageButton noButton = new ImageButton(
                                ShopViewController.getImageButtonStyle("abort"));
                        noButton.addListener(new ClickListener() {
                            @Override
                            public void clicked(InputEvent event, float x,
                                    float y) {
                                AudioManager.playSound("buttonClick");
                                setVisible(false);
                                hide();
                            }
                        });
                        add(noButton).pad(space).align(Align.left).size(buttonSize);
                    }
                });
            } else {
                vc.showDialog(new Dialog("", ShopViewController.getSkin()) {
                    {
                        clear();
                        // getImage() removes the image from its button so the
                        // button has to be copied before that
                        float space = stage.getWidth() / 64;
                        pad(space * 2);
                        Label label = new Label(ShopViewController
                                .getLanguage().format("item_notBuyable",
                                        model.getFilename(), model.getPrice()),
                                ShopViewController.getSkin());
                        label.setFontScale(stage.getHeight() / 720 * label.getFontScaleY());
                        add(label).width(stage.getWidth() * 2 / 3);
                        row();

                        label.setWrap(true);
                        // yes
                        ImageButton accept = new ImageButton(
                                ShopViewController
                                        .getImageButtonStyle("accept"));
                        accept.addListener(new ClickListener() {
                            @Override
                            public void clicked(InputEvent event, float x,
                                    float y) {
                                AudioManager.playSound("buttonClick");
                                setVisible(false);
                                hide();
                            }
                        });
                        add(accept).size(buttonSize);
                    }
                });
            }
        }
    }
}
