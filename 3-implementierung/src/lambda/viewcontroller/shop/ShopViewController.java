package lambda.viewcontroller.shop;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.TextureLoader.TextureParameter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton.ImageTextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import lambda.model.profiles.ProfileManager;
import lambda.model.profiles.ProfileModel;
import lambda.model.profiles.ProfileModelObserver;
import lambda.model.shop.BackgroundImageItemModel;
import lambda.model.shop.ElementUIContextFamily;
import lambda.model.shop.MusicItemModel;
import lambda.model.shop.ShopModel;
import lambda.viewcontroller.StageViewController;
import lambda.viewcontroller.mainmenu.MainMenuViewController;


/**
 * @author Kay Schmitteckert
 */
public class ShopViewController extends StageViewController implements ProfileModelObserver {

    private ShopModel shop;
    private ProfileModel profile;
    private Label coins;
    private VerticalGroup table;
    private boolean musicB;
    private boolean imageB;
    private boolean elementsB;
    
    private DropDownMenuViewController<MusicItemModel> music;
    private DropDownMenuViewController<BackgroundImageItemModel> bgImages;
    private DropDownMenuViewController<ElementUIContextFamily> elementUIs;

    private final String masterSkin = "data/skins/MasterSkin.json";
    
    private static Skin skin;
    
    private VerticalGroup musicTable;
    private VerticalGroup imagesTable;
    private VerticalGroup elementsTable;

    public ShopViewController() {
        shop = ShopModel.getShop();
        profile = new ProfileModel("");
        profile.addObserver(this);
        ProfileManager.getManager().addObserver(this);
        musicB = false;
        imageB = false;
        elementsB = false;
        
    }

    @Override
    public void queueAssets(AssetManager assets) {
        shop.queueAssets(assets);
        assets.load("data/backgrounds/default.png", Texture.class, new TextureParameter());
    }

    @Override
    public void show() {
       super.show();
        coins.setText(Integer.toString(ProfileManager.getManager().getCurrentProfile().getCoins()));
    }

    @Override
    public void create(final AssetManager manager) {
    	setLastViewController(MainMenuViewController.class);
        Image background = new Image(manager.get("data/backgrounds/default.png", Texture.class));
        background.setWidth(getStage().getWidth());
        background.setHeight(getStage().getHeight());
        getStage().addActor(background);
        
        skin = manager.get(masterSkin, Skin.class); 
        shop.setAllAssets(manager);
        Table mainTable = new Table();
        mainTable.setFillParent(true);
        getStage().addActor(mainTable);
        music = new DropDownMenuViewController<MusicItemModel>(shop.getMusic(), getStage());
        bgImages = new DropDownMenuViewController<BackgroundImageItemModel>(shop.getImages(), getStage());
        elementUIs = new DropDownMenuViewController<ElementUIContextFamily>(shop.getElementUIContextFamilies(), getStage());
        
        /*
         * BACK-BUTTON
         */
        ImageButton backButton = new ImageButton(getImageButtonStyle("back"));
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
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
         *  CATEGORY: MUSIC
         */
        final ImageButton musicTypeButton = new ImageButton(getImageButtonStyle("musicType"));
        musicTypeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                musicB = !musicB;
                if (musicB) {
                    table.addActorAfter(musicTypeButton, musicTable);
                }
                else {
                    table.removeActor(musicTable);
                }
            }
        });

        /*
         * CATEGORY: IMAGES
         */
        final ImageButton bgImageTypeButton = new ImageButton(manager.get(masterSkin, Skin.class), "imageType");
        bgImageTypeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                imageB = !imageB;
                if (imageB) {
                    table.addActorAfter(bgImageTypeButton, imagesTable);
                }
                else {
                    table.removeActor(imagesTable);
                }
            }
        });
       
        
        /*
         * CATEGORY: ELEMENTS
         */
        final ImageButton elementUITypeButton = new ImageButton(manager.get(masterSkin, Skin.class), "elementType");
        elementUITypeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                elementsB = !elementsB;
                if (elementsB) {
                    table.addActorAfter(elementUITypeButton, elementsTable);
                }
                else {
                    table.removeActor(elementsTable);
                }
            }
        });
                
        /*
         *  COINS
         */
        coins = new Label("", skin);
        ImageTextButton coinBar = new ImageTextButton("", skin);
        coinBar.add(new Image(skin, "coin"));
        coinBar.add(coins);
        
        // VerticalGroup which includes the different categories
        table = new VerticalGroup().align(Align.top).pad(15);
        getStage().addActor(table);
        table.addActor(musicTypeButton);
        
        //MUSIC        
        musicTable = new VerticalGroup().align(Align.center);
        musicTable = music.getGroup();
        //IMAGES
        table.addActor(bgImageTypeButton);
        imagesTable = bgImages.getGroup();
        //ELEMENTS
        table.addActor(elementUITypeButton);
        elementsTable = elementUIs.getGroup();
        //scroll pane and other buttons
        mainTable.add(backButton).pad(15).align(Align.bottomLeft);
        ScrollPane scrollpane = new ScrollPane(table);
        mainTable.add(scrollpane).expand().center();
        mainTable.add(coinBar).pad(15).align(Align.topRight);
        
       
    }
    public static TextButtonStyle getTextButtonStyle(String icon) {
        TextButtonStyle style = new TextButtonStyle(skin.get(TextButtonStyle.class));
        style.up = skin.getDrawable(icon);
        style.down = skin.getDrawable(icon);
        return style;
    }
    
    public static ImageButtonStyle getImageButtonStyle(String icon) {
        ImageButtonStyle style = new ImageButtonStyle(skin.get(ImageButtonStyle.class));
        style.imageUp = skin.getDrawable(icon);
        return style;
    }
    public static ImageTextButtonStyle getImageTextButtonStyle(String icon) {
        ImageTextButtonStyle style = new ImageTextButtonStyle(skin.get(ImageTextButtonStyle.class));
        style.imageUp = skin.getDrawable(icon);
        return style;
    }

    public void changedProfile() {
        profile.removeObserver(this);
        profile = ProfileManager.getManager().getCurrentProfile();
        profile.addObserver(this);
        musicTable = music.updateButtons();
        imagesTable = bgImages.updateButtons();
        elementsTable = elementUIs.updateButtons();
    }
    
    public void changedCoins() {
        coins.setText(String.valueOf(ProfileManager.getManager().getCurrentProfile().getCoins()));
        for (Actor item : music.getGroupVCs().getChildren()) {
            ShopItemViewController<MusicItemModel> itemVC = (ShopItemViewController<MusicItemModel>) item;
            itemVC.setCurrentState();
            table.removeActor(musicTable);
            musicB = !musicB;
            musicTable = music.updateButtons();
        }
        for (Actor item : bgImages.getGroupVCs().getChildren()) {
            ShopItemViewController<BackgroundImageItemModel> itemVC = (ShopItemViewController<BackgroundImageItemModel>) item;
            itemVC.setCurrentState();
            table.removeActor(imagesTable);
            imageB = !imageB;
            imagesTable = bgImages.updateButtons();
        }
        for (Actor item : elementUIs.getGroupVCs().getChildren()) {
            ShopItemViewController<ElementUIContextFamily> itemVC = (ShopItemViewController<ElementUIContextFamily>) item;
            itemVC.setCurrentState();
            table.removeActor(elementsTable);
            elementsB = !elementsB;
            elementsTable = elementUIs.updateButtons();
        }
    }
    
    public static Skin getSkin() {
        return skin;
    }

	@Override
	public void changedLevelIndex() {		
	}
}
