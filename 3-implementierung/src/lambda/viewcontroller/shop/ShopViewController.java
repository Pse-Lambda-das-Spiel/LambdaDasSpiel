package lambda.viewcontroller.shop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton.ImageTextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import lambda.model.profiles.ProfileManager;
import lambda.model.profiles.ProfileModel;
import lambda.model.profiles.ProfileModelObserver;
import lambda.model.shop.BackgroundImageItemModel;
import lambda.model.shop.ElementUIContextFamily;
import lambda.model.shop.MusicItemModel;
import lambda.model.shop.ShopModel;
import lambda.viewcontroller.ViewController;
import lambda.viewcontroller.mainmenu.MainMenuViewController;


/**
 * @author Kay Schmitteckert
 */
public class ShopViewController extends ViewController implements ProfileModelObserver {

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

    private final Stage stage;
    private final String masterSkin = "data/skins/MasterSkin.json";
    
    private static Skin skin;
    
    private VerticalGroup musicTable;
    private VerticalGroup imagesTable;
    private VerticalGroup elementsTable;

    public ShopViewController() {
        shop = ShopModel.getShop();
        stage = new Stage(new ScreenViewport());
        profile = new ProfileModel("");
        profile.addObserver(this);
        ProfileManager.getManager().addObserver(this);
        musicB = false;
        imageB = false;
        elementsB = false;
        
    }

    @Override
    public void queueAssets(AssetManager assets) {

        // assets for the ShopModel (ShopItems etc.)
        //shop.setAssetManager(assets);
        shop.queueAssets(assets);
        
        

        assets.load(masterSkin, Skin.class,
                new SkinLoader.SkinParameter("data/skins/MasterSkin.atlas"));
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        coins.setText(Integer.toString(ProfileManager.getManager().getCurrentProfile().getCoins()));
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().setScreenSize(width, height);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    @Override
    public void create(AssetManager manager) {
       
        skin = manager.get(masterSkin, Skin.class);   
        Table mainTable = new Table();
        mainTable.setFillParent(true);
        stage.addActor(mainTable);
        music = new DropDownMenuViewController<MusicItemModel>(shop.getMusic());
        bgImages = new DropDownMenuViewController<BackgroundImageItemModel>(shop.getImages());
        elementUIs = new DropDownMenuViewController<ElementUIContextFamily>(shop.getElementUIContextFamilies());
        
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
                getGame().setScreen(MainMenuViewController.class);
            }
        });
        
        
        /*
         *  CATEGORY: MUSIC
         */
        ImageButton musicTypeButton = new ImageButton(getImageButtonStyle("musicType"));
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
        ImageButton bgImageTypeButton = new ImageButton(manager.get(masterSkin, Skin.class), "imageType");
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
        ImageButton elementUITypeButton = new ImageButton(manager.get(masterSkin, Skin.class), "elementType");
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
        stage.addActor(table);
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
    }
}
