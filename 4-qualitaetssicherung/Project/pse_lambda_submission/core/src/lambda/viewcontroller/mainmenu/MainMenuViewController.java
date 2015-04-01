package lambda.viewcontroller.mainmenu;

import lambda.model.levels.LevelManager;
import lambda.model.profiles.ProfileManager;
import lambda.model.profiles.ProfileModelObserver;
import lambda.viewcontroller.AudioManager;
import lambda.viewcontroller.StageViewController;
import lambda.viewcontroller.achievements.AchievementMenuViewController;
import lambda.viewcontroller.level.LevelSelectionViewController;
import lambda.viewcontroller.profiles.ProfileSelection;
import lambda.viewcontroller.settings.SettingsViewController;
import lambda.viewcontroller.shop.ShopViewController;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.assets.loaders.TextureLoader.TextureParameter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

/**
 * Represents the main menu.
 * 
 * @author Farid, Robert Hochweiss
 */
public class MainMenuViewController extends StageViewController implements
        ProfileModelObserver {

    private Skin skin;
    private Label coins;
    private Label profileName;
    private Image profileImg;
    private AssetManager manager;
    private Container<ImageButton> soundButtonContainer;
    private ImageButton soundMuted;
    private ImageButton soundUnmuted;
    private final float buttonSize;
    private final float padSpace;

    /**
     * Creates a object of the class without initializing the screen.
     */
    public MainMenuViewController() {
        buttonSize = (getStage().getWidth() / 8);
        padSpace = (getStage().getHeight() / 48);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void changedProfile() {
        ProfileManager pManager = ProfileManager.getManager();
        profileImg.setDrawable(new SpriteDrawable(new Sprite(manager.get(
                "data/avatar/" + pManager.getCurrentProfile().getAvatar()
                        + ".png", Texture.class))));
        profileName.setText(pManager.getCurrentProfile().getName());
        coins.setText(Integer.toString(ProfileManager.getManager()
                .getCurrentProfile().getCoins()));
        ProfileManager.getManager().getCurrentProfile().addObserver(this);
        soundButtonContainer.setActor((pManager.getCurrentProfile()
                .getSettings().isMusicOn() ? soundUnmuted : soundMuted));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void queueAssets(AssetManager manager) {
        TextureParameter param = new TextureParameter();
        param.magFilter = TextureFilter.Linear;
        param.minFilter = TextureFilter.Linear;
        manager.load("data/backgrounds/main.png", Texture.class, param);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void show() {
        super.show();
        AudioManager.playDefaultMusic();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void create(final AssetManager manager) {
        this.manager = manager;
        ProfileManager.getManager().addObserver(this);
        setLastViewController(ProfileSelection.class);
        skin = manager.get("data/skins/MasterSkin.json", Skin.class);
        Image background = new Image(manager.get("data/backgrounds/main.png",
                Texture.class));
        background.setWidth(getStage().getWidth());
        background.setHeight(getStage().getHeight());
        getStage().addActor(background);

        ImageButton logoutButton = new ImageButton(skin, "backButton");
        ImageButton settingsButton = new ImageButton(skin, "settingsButton");
        soundUnmuted = new ImageButton(skin, "unmutedButton");
        soundMuted = new ImageButton(skin, "mutedButton");

        ImageButton startButton = new ImageButton(skin, "startButton");
        ImageButton levelMenuButton = new ImageButton(skin, "levelMenuButton");
        ImageButton achievementsButton = new ImageButton(skin,
                "achievementsButton");
        coins = new Label("Initial string", skin);
        ImageTextButton coinButton = new ImageTextButton("", skin);
        coinButton.add(new Image(skin, "coin"));
        coinButton.add(coins);
        profileName = new Label("Initial string", skin, "roboto");
        profileImg = new Image();

        Container<ImageButton> settingsButtonContainer = new Container<>();
        settingsButtonContainer.pad(padSpace).align(Align.bottomLeft);
        settingsButtonContainer.setActor(settingsButton);
        getStage().addActor(settingsButtonContainer);
        settingsButtonContainer.setFillParent(true);
        settingsButtonContainer.maxSize(buttonSize);

        soundButtonContainer = new Container<>();
        soundButtonContainer.pad(padSpace).align(Align.bottomRight);
        soundButtonContainer.setActor(soundUnmuted);
        getStage().addActor(soundButtonContainer);
        soundButtonContainer.setFillParent(true);
        soundButtonContainer.maxSize(buttonSize);

        Container<ImageTextButton> coinButtonContainer = new Container<>();
        coinButtonContainer.pad(padSpace).align(Align.topRight)
                .setSize(buttonSize, buttonSize);
        coinButtonContainer.setActor(coinButton);
        getStage().addActor(coinButtonContainer);
        coinButtonContainer.setFillParent(true);
        coinButtonContainer.maxSize(buttonSize * 2f);

        Table profileTable = new Table();
        profileTable.pad(padSpace * 1.5f).align(Align.topLeft);
        profileTable.add(logoutButton).align(Align.left)
                .spaceBottom(getStage().getHeight() / 50).maxSize(buttonSize)
                .row();
        profileTable.add(profileImg).align(Align.left).size(buttonSize).row();
        profileTable.add(profileName).align(Align.left);
        getStage().addActor(profileTable);
        profileTable.setFillParent(true);

        Table centerTable = new Table();
        centerTable.align(Align.center);
        centerTable.add().row();
        centerTable.add(startButton).colspan(2).align(Align.center).size(buttonSize * 1.22f)
                .spaceBottom(getStage().getHeight() / 20).row();
        centerTable.add(levelMenuButton).size(buttonSize).align(Align.left)
                .spaceRight(getStage().getWidth() / 20);
        centerTable.add(achievementsButton).size(buttonSize).align(Align.right);
        getStage().addActor(centerTable);
        centerTable.setFillParent(true);

        logoutButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                AudioManager.playSound("buttonClick");
                ProfileManager pManager = ProfileManager.getManager();
                pManager.save(pManager.getCurrentProfile().getName());
                AudioManager.setLoggedIn(false);
                getGame().setScreen(ProfileSelection.class);
            }
        });
        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                AudioManager.playSound("buttonClick");
                int levelIndex = ProfileManager.getManager()
                        .getCurrentProfile().getLevelIndex();
                // start with the first level again, if all level have been
                // solved
                if (levelIndex > LevelManager.getLevelManager()
                        .getNumberOfLevels()) {
                    levelIndex = 1;
                }
                getGame().getController(LevelSelectionViewController.class)
                        .startLevel(
                                LevelManager.getLevelManager().getLevel(
                                        levelIndex));
            }
        });
        levelMenuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                AudioManager.playSound("buttonClick");
                getGame().setScreen(LevelSelectionViewController.class);
            }
        });
        achievementsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                AudioManager.playSound("buttonClick");
                getGame().setScreen(AchievementMenuViewController.class);
            }
        });
        coinButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                AudioManager.playSound("buttonClick");
                getGame().setScreen(ShopViewController.class);
            }
        });
        settingsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                AudioManager.playSound("buttonClick");
                getGame().setScreen(SettingsViewController.class);
            }
        });
        soundUnmuted.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ProfileManager.getManager().getCurrentProfile().getSettings()
                        .setMusicOn(false);
                soundButtonContainer.setActor(soundMuted);
            }
        });
        soundMuted.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ProfileManager.getManager().getCurrentProfile().getSettings()
                        .setMusicOn(true);
                soundButtonContainer.setActor(soundUnmuted);
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void changedCoins() {
        // Update coin label
        coins.setText(Integer.toString(ProfileManager.getManager()
                .getCurrentProfile().getCoins()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void changedLevelIndex() {
    }
}