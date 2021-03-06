package lambda.viewcontroller.shop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton.ImageTextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.I18NBundle;

import lambda.model.profiles.ProfileManager;
import lambda.model.profiles.ProfileModel;
import lambda.model.profiles.ProfileModelObserver;
import lambda.model.shop.BackgroundImageItemModel;
import lambda.model.shop.ElementUIContextFamily;
import lambda.model.shop.MusicItemModel;
import lambda.model.shop.ShopModel;
import lambda.viewcontroller.AudioManager;
import lambda.viewcontroller.StageViewController;
import lambda.viewcontroller.mainmenu.MainMenuViewController;

/**
 * This class represents the view of the shop
 * 
 * @author Kay Schmitteckert
 */
public class ShopViewController extends StageViewController implements
        ProfileModelObserver {

    private ShopModel shop;
    private ProfileModel profile;
    private Label coins;
    private VerticalGroup table;

    private DropDownMenuViewController<MusicItemModel> music;
    private DropDownMenuViewController<BackgroundImageItemModel> bgImages;
    private DropDownMenuViewController<ElementUIContextFamily> elementUIs;
    private VerticalGroup musicTable;
    private VerticalGroup imagesTable;
    private VerticalGroup elementsTable;
    private boolean musicB;
    private boolean imageB;
    private boolean elementsB;
    private AssetManager assets;

    private ImageButton musicTypeButton;
    private ImageButton bgImageTypeButton;
    private ImageButton elementUITypeButton;

    private final float buttonSize;
    private final float padSpace;
    
    /**
     * Path to the MasterSkin
     */
    private final String masterSkin = "data/skins/MasterSkin.json";
    private static Skin skin;
    private static I18NBundle language;

    /**
     * Creates a new instance of this class
     */
    public ShopViewController() {
        shop = ShopModel.getShop();
        profile = new ProfileModel("");
        profile.addObserver(this);
        ProfileManager.getManager().addObserver(this);
        musicB = false;
        imageB = false;
        elementsB = false;
        buttonSize = getStage().getWidth() / 8;
        padSpace = getStage().getHeight() / 48;
    }

    @Override
    public void queueAssets(AssetManager assets) {
        shop.queueAssets(assets);
    }

    @Override
    public void show() {
        InputProcessor backProcessor = new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                if (keycode == Keys.BACK) {
                    removeLastDialog();
                    table.removeActor(musicTable);
                    table.removeActor(imagesTable);
                    table.removeActor(elementsTable);
                    musicB = false;
                    imageB = false;
                    elementsB = false;
                    getGame().setScreen(MainMenuViewController.class);  
                }
                return false;
            }
        };
        InputMultiplexer multiplexer = new InputMultiplexer(getStage(),
                backProcessor);
        Gdx.input.setInputProcessor(multiplexer);
        coins.setText(Integer.toString(ProfileManager.getManager()
                .getCurrentProfile().getCoins()));
    }

    @Override
    public void create(final AssetManager manager) {
        this.assets = manager;
        setLastViewController(MainMenuViewController.class);
        Image background = new Image(manager.get(
                "data/backgrounds/default.png", Texture.class));
        background.setWidth(getStage().getWidth());
        background.setHeight(getStage().getHeight());
        getStage().addActor(background);
        language = manager.get(profile.getLanguage(), I18NBundle.class);

        skin = manager.get(masterSkin, Skin.class);
        new Dialog("", skin) {
            {
                this.getStyle().background.setMinHeight(0);
                this.getStyle().background.setMinWidth(0);;
            }
        };
        shop.setAllAssets(manager);
        Table mainTable = new Table();
        mainTable.setFillParent(true);
        getStage().addActor(mainTable);
        music = new DropDownMenuViewController<MusicItemModel>(shop.getMusic(),
                this);
        bgImages = new DropDownMenuViewController<BackgroundImageItemModel>(
                shop.getImages(), this);
        elementUIs = new DropDownMenuViewController<ElementUIContextFamily>(
                shop.getElementUIContextFamilies(), this);

        /*
         * BACK-BUTTON
         */
        ImageButton backButton = new ImageButton(getImageButtonStyle("back"));
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                AudioManager.playSound("buttonClick");
                table.removeActor(musicTable);
                table.removeActor(imagesTable);
                table.removeActor(elementsTable);
                musicB = false;
                imageB = false;
                elementsB = false;
                getGame().setScreen(MainMenuViewController.class);
            }
        });

        /*
         * CATEGORY: MUSIC
         */
        musicTypeButton = new ImageButton(getImageButtonStyle("musicType"));
        musicTypeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                AudioManager.playSound("buttonClick");
                musicB = !musicB;
                if (musicB) {
                    table.addActorAfter(musicTypeButton, musicTable);
                } else {
                    table.removeActor(musicTable);
                }
            }
        });

        /*
         * CATEGORY: IMAGES
         */
        bgImageTypeButton = new ImageButton(getImageButtonStyle("imageType"));
        bgImageTypeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                AudioManager.playSound("buttonClick");
                imageB = !imageB;
                if (imageB) {
                    table.addActorAfter(bgImageTypeButton, imagesTable);
                } else {
                    table.removeActor(imagesTable);
                }
            }
        });

        /*
         * CATEGORY: ELEMENTS
         */
        elementUITypeButton = new ImageButton(getImageButtonStyle("elementType"));
        elementUITypeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                AudioManager.playSound("buttonClick");
                elementsB = !elementsB;
                if (elementsB) {
                    table.addActorAfter(elementUITypeButton, elementsTable);
                } else {
                    table.removeActor(elementsTable);
                }
            }
        });

        /*
         * COINS
         */
        coins = new Label("", skin);
        coins.setFontScale(getStage().getHeight() / 720 * coins.getFontScaleY());
        ImageTextButton coinBar = new ImageTextButton("", skin);
        coinBar.clear();
        coinBar.add(new Image(skin, "coin")).size(buttonSize);
        coinBar.add(coins);

        // VerticalGroup which includes the different categories
        table = new VerticalGroup().align(Align.top).pad(padSpace);
        
        // MUSIC
        musicTypeButton.getImageCell().width(getStage().getHeight() / 720 
                * musicTypeButton.getImage().getPrefWidth()).height(
                getStage().getHeight() / 720 * musicTypeButton.getImage()
                .getPrefHeight());
        table.addActor(musicTypeButton);
        musicTable = music.getGroup();
        // IMAGES
        bgImageTypeButton.getImageCell().width(getStage().getHeight() / 720 
                * bgImageTypeButton.getImage().getPrefWidth()).height(
                getStage().getHeight() / 720 * bgImageTypeButton.getImage()
                .getPrefHeight());
        table.addActor(bgImageTypeButton);
        imagesTable = bgImages.getGroup();
        // ELEMENTS
        elementUITypeButton.getImageCell().width(getStage().getHeight() / 720 
                * elementUITypeButton.getImage().getPrefWidth()).height(
                getStage().getHeight() / 720 * elementUITypeButton.getImage()
                .getPrefHeight());
        table.addActor(elementUITypeButton);
        elementsTable = elementUIs.getGroup();
        // scroll pane and other buttons
        ScrollPane scrollpane = new ScrollPane(table);
        scrollpane.debug();
        scrollpane.setFillParent(true);
        getStage().addActor(scrollpane);
        
        backButton.setSize(buttonSize, buttonSize);
        backButton.setPosition(padSpace, padSpace);
        getStage().addActor(backButton);
        
        Container<ImageTextButton> coinButtonContainer = new Container<>();
        coinButtonContainer.pad(padSpace).align(Align.topRight);
        coinButtonContainer.setActor(coinBar);      
        getStage().addActor(coinButtonContainer);
        coinButtonContainer.setFillParent(true);
        
    }

    /**
     * Returns a text button style
     * 
     * @param icon
     *            the identifier for the style
     * @return the specific text button style of the identifier
     */
    public static TextButtonStyle getTextButtonStyle(String icon) {
        TextButtonStyle style = new TextButtonStyle(
                skin.get(TextButtonStyle.class));
        style.up = skin.getDrawable(icon);
        style.down = skin.getDrawable(icon);
        return style;
    }

    /**
     * Returns a image button style
     * 
     * @param icon
     *            the identifier for the style
     * @return the specific image button style of the identifier
     */
    public static ImageButtonStyle getImageButtonStyle(String icon) {
        ImageButtonStyle style = new ImageButtonStyle(
                skin.get(ImageButtonStyle.class));
        style.imageUp = skin.getDrawable(icon);
        return style;
    }

    /**
     * Returns a image text button style
     * 
     * @param icon
     *            the identifier for the style
     * @return the specific image text button style of the identifier
     */
    public static ImageTextButtonStyle getImageTextButtonStyle(String icon) {
        ImageTextButtonStyle style = new ImageTextButtonStyle(
                skin.get(ImageTextButtonStyle.class));
        style.imageUp = skin.getDrawable(icon);
        return style;
    }

    /**
     * Is called when the profile is changed
     */
    public void changedProfile() {
        profile.removeObserver(this);
        profile = ProfileManager.getManager().getCurrentProfile();
        language = assets.get(profile.getLanguage(), I18NBundle.class);
        ;
        profile.addObserver(this);
        musicTable = music.updateButtons();
        imagesTable = bgImages.updateButtons();
        elementsTable = elementUIs.updateButtons();
    }

    /**
     * Is called when the coins are changed
     */
    @SuppressWarnings("unchecked")
    public void changedCoins() {
        coins.setText(String.valueOf(ProfileManager.getManager()
                .getCurrentProfile().getCoins()));
        table.removeActor(musicTable);
        musicTable = music.updateButtons();
        if (musicB) {
            table.addActorAfter(musicTypeButton, musicTable);
        }
        table.removeActor(imagesTable);
        imagesTable = bgImages.updateButtons();
        if (imageB) {
            table.addActorAfter(bgImageTypeButton, imagesTable);
        }
        table.removeActor(elementsTable);
        elementsTable = elementUIs.updateButtons();
        if (elementsB) {
            table.addActorAfter(elementUITypeButton, elementsTable);
        }
    }

    /**
     * Returns the master skin
     * 
     * @return the master skin
     */
    public static Skin getSkin() {
        return skin;
    }

    /**
     * Returns the current language
     * 
     * @return the current language
     */
    public static I18NBundle getLanguage() {
        return language;
    }

    @Override
    public void changedLevelIndex() {
    }
}
