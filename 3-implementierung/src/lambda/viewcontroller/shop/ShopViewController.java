package lambda.viewcontroller.shop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import lambda.model.profiles.ProfileManager;
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
    private AssetManager manager;
    private DropDownMenuViewController<MusicItemModel> music;
    private DropDownMenuViewController<BackgroundImageItemModel> bgImages;
    private DropDownMenuViewController<ElementUIContextFamily> elementUIContextFamilies;

    private final Stage stage;
    private ImageButton backButton;
    private ImageButton musicTypeButton;
    private ImageButton bgImageTypeButton;
    private ImageButton elementUITypeButton;
    private Label coins;
    private Image coinBar;

    private final String shopSkin = "data/skins/ShopViewControllerSkin.json";
    private final String shopAtlas = "data/skins/ShopViewControllerSkin.atlas";
    private final String dropDownMenuSkin = "data/skins/dropDownMenuSkin.json";
    private final String dropDownMenuAtlas = "data/skins/dropDownMenuAtlas.atlas";

    public ShopViewController() {
        shop = ShopModel.getShop();
        stage = new Stage(new ScreenViewport());
    }

    @Override
    public void queueAssets(AssetManager assets) {

        // assets for the ShopModel (ShopItems etc.)
        shop.setAssetManager(manager);
        shop.queueAssets(manager);

        assets.load(shopAtlas, TextureAtlas.class);
        assets.load(shopSkin, Skin.class,
                new SkinLoader.SkinParameter(shopSkin));

        assets.load(dropDownMenuSkin, Skin.class);
        assets.load(dropDownMenuAtlas, TextureAtlas.class);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
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
        ProfileManager.getManager().getCurrentProfile().addObserver(this);
        this.manager = manager;

        backButton = new ImageButton(manager.get(shopSkin, Skin.class), "back");
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                getGame().setScreen(MainMenuViewController.class);
            }
        });

        //TODO: images for the next three buttons!
        musicTypeButton = new ImageButton(manager.get(shopSkin, Skin.class), "musicType");
        musicTypeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                music.setOpen(!music.isOpen());
            }
        });
        bgImageTypeButton = new ImageButton(manager.get(shopSkin, Skin.class), "imageType");
        bgImageTypeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                bgImages.setOpen(!bgImages.isOpen());
            }
        });
        elementUITypeButton = new ImageButton(manager.get(shopSkin, Skin.class), "elementType");
        elementUITypeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                elementUIContextFamilies.setOpen(!elementUIContextFamilies.isOpen());
            }
        });

        Container<ImageButton> buttonContainer = new Container();
        buttonContainer.align(Align.bottomLeft);
        buttonContainer.setActor(backButton);
        buttonContainer.setActor(musicTypeButton);
        buttonContainer.setActor(bgImageTypeButton);
        buttonContainer.setActor(elementUITypeButton);
        stage.addActor(buttonContainer);

        coins = new Label(String.valueOf(ProfileManager.getManager().getCurrentProfile().getCoins()),
                manager.get(shopSkin, Skin.class));
        Container<Label> labelContainer = new Container();
        labelContainer.align(Align.topRight);
        labelContainer.setActor(coins);
        stage.addActor(labelContainer);

        coinBar = new Image(manager.get(shopSkin, Skin.class), "coin_bar");
        Container<Image> imageContainer = new Container();
        imageContainer.align(Align.topRight);
        imageContainer.setActor(coinBar);
        stage.addActor(imageContainer);


        music = new DropDownMenuViewController(shop.getMusic(), manager.get(dropDownMenuAtlas, TextureAtlas.class),
                manager.get(dropDownMenuSkin, Skin.class), manager);
        bgImages = new DropDownMenuViewController(shop.getImages(), manager.get(dropDownMenuAtlas, TextureAtlas.class),
                manager.get(dropDownMenuSkin, Skin.class), manager);
        elementUIContextFamilies = new DropDownMenuViewController(shop.getElementUIContextFamilies(),
                manager.get(dropDownMenuAtlas, TextureAtlas.class), manager.get(dropDownMenuSkin, Skin.class), manager);

        music.draw(stage.getWidth(), stage.getHeight());
        bgImages.draw(stage.getWidth(), stage.getHeight());
        elementUIContextFamilies.draw(stage.getWidth(), stage.getHeight());

    }
}
