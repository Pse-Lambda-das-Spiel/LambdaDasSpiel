package lambda.viewcontroller.profiles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

import lambda.model.profiles.ProfileEditModel;
import lambda.model.profiles.ProfileEditObserver;
import lambda.model.profiles.ProfileManager;
import lambda.viewcontroller.AudioManager;
import lambda.viewcontroller.StageViewController;
import lambda.viewcontroller.mainmenu.MainMenuViewController;

/**
 * Represents a screen of the profile configuration/creation. It allows the user
 * to change the profile's avatar picture.
 * 
 * @author Kai Fieger
 */
public class ProfileEditAvatar extends StageViewController implements
        ProfileEditObserver {

    private final String avatarPath = "data/avatar";
    private final String skinJson = "data/skins/ProfileEditSkin.json";
    private final ProfileEditModel profileEdit;
    private AssetManager manager;
    private Image avatarPic;
    private Label chooseAvatar;
    private boolean newProfile;
    private final float space;
    private boolean showsHelloDialog;

    /**
     * Creates a object of the class without initializing the screen.
     */
    public ProfileEditAvatar() {
        showsHelloDialog = false;
        ProfileManager.getManager().addObserver(this);
        profileEdit = ProfileManager.getManager().getProfileEdit();
        space = getStage().getWidth() / 64;
    }

    @Override
    public void queueAssets(AssetManager assets) {
        String start = profileEdit.getAvatar();
        String current = start;
        do {
            assets.load(avatarPath + "/" + profileEdit.getAvatar() + ".png",
                    Texture.class);
            profileEdit.nextAvatar();
            current = profileEdit.getAvatar();
        } while (!current.equals(start));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void show() {
        InputProcessor backProcessor = new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                if (keycode == Keys.BACK) {
                    if (!showsHelloDialog) {
                        removeLastDialog();
                        ProfileManager.getManager().getCurrentProfile()
                            .setAvatar(profileEdit.getAvatar());
                        getGame().setScreen(ProfileEditName.class);
                    }
                }
                return false;
            }
        };
        InputMultiplexer multiplexer = new InputMultiplexer(getStage(),
                backProcessor);
        Gdx.input.setInputProcessor(multiplexer);
    }

    @Override
    public void create(final AssetManager manager) {
        Image background = new Image(manager.get(
                "data/backgrounds/default.png", Texture.class));
        background.setWidth(getStage().getWidth());
        background.setHeight(getStage().getHeight());
        getStage().addActor(background);

        profileEdit.addObserver(this);
        this.manager = manager;
        setLastViewController(ProfileEditName.class);

        Table avatarSelection = new Table();
        avatarSelection.align(Align.top);
        getStage().addActor(avatarSelection);
        avatarSelection.setFillParent(true);

        chooseAvatar = new Label(null, manager.get(skinJson, Skin.class));
        chooseAvatar.setAlignment(Align.center);
        avatarSelection.row().height(getStage().getHeight() / 20);
        avatarSelection.add();
        avatarSelection.row().height(getStage().getHeight() / 5);
        avatarSelection.add();
        avatarSelection.add(chooseAvatar).width(getStage().getWidth() / 2);
        chooseAvatar.setFontScale(getStage().getHeight() / 720 * chooseAvatar.getFontScaleY());
        
        avatarSelection.row();
        ImageButton selectLeft = new ImageButton(manager.get(skinJson,
                Skin.class), "leftButton");
        avatarSelection.add(selectLeft).size(getStage().getHeight() / 5)
                .space(space);
        selectLeft.addListener(new SelectLeftClickListener());
        avatarPic = new Image();
        avatarSelection.add(avatarPic).size(getStage().getHeight() / 2)
                .space(space);
        ImageButton selectRight = new ImageButton(manager.get(skinJson,
                Skin.class), "rightButton");
        avatarSelection.add(selectRight).size(getStage().getHeight() / 5)
                .space(space);
        selectRight.addListener(new SelectRightClickListener());

        ImageButton backButton = new ImageButton(manager.get(skinJson,
                Skin.class), "leftButton");
        Container<ImageButton> buttonContainer = new Container<ImageButton>();
        buttonContainer.pad(space * 5 / 2).maxSize(getStage().getHeight() / 5);
        buttonContainer.align(Align.bottomLeft);
        buttonContainer.setActor(backButton);
        backButton.addListener(new BackClickListener());
        getStage().addActor(buttonContainer);
        buttonContainer.setFillParent(true);

        ImageButton continueButton = new ImageButton(manager.get(skinJson,
                Skin.class), "acceptButton");
        buttonContainer = new Container<ImageButton>();
        buttonContainer.pad(space * 5 / 2).maxSize(getStage().getHeight() / 5);
        buttonContainer.align(Align.bottomRight);
        buttonContainer.setActor(continueButton);
        continueButton.addListener(new AcceptClickListener());
        getStage().addActor(buttonContainer);
        buttonContainer.setFillParent(true);
    }

    @Override
    public void changedProfile() {
        ProfileManager m = ProfileManager.getManager();
        newProfile = m.getCurrentProfile().getName().equals("");
        profileEdit.setAvatar(m.getCurrentProfile().getAvatar());
    }

    @Override
    public void changedAvatar() {
        avatarPic.setDrawable(new SpriteDrawable(new Sprite(manager.get(
                avatarPath + "/" + profileEdit.getAvatar() + ".png",
                Texture.class))));
    }

    @Override
    public void changedLanguage() {
        chooseAvatar.setText(manager.get(profileEdit.getLang(),
                I18NBundle.class).get("chooseAvatar"));
    }

    private class SelectLeftClickListener extends ClickListener {
        @Override
        public void clicked(InputEvent event, float x, float y) {
            AudioManager.playSound("buttonClick");
            profileEdit.previousAvatar();
        }
    }

    private class SelectRightClickListener extends ClickListener {
        @Override
        public void clicked(InputEvent event, float x, float y) {
            AudioManager.playSound("buttonClick");
            profileEdit.nextAvatar();
        }
    }

    private class AcceptClickListener extends ClickListener {
        @Override
        public void clicked(InputEvent event, float x, float y) {
            AudioManager.playSound("buttonClick");
            final ProfileManager m = ProfileManager.getManager();
            m.getCurrentProfile().setAvatar(profileEdit.getAvatar());
            m.save(m.getCurrentProfile().getName());
            if (newProfile) {
                m.setCurrentProfile(m.getCurrentProfile().getName());
                final Skin dialogSkin = manager.get(
                        "data/skins/DialogTemp.json", Skin.class);
                final float height = getStage().getHeight();

                showsHelloDialog = true;
                showDialog(new Dialog("", dialogSkin) {
                    private boolean changedToMainMenu = false;
                    {
                        AudioManager.setLoggedIn(true);
                        clear();
                        Label greeting = new Label(manager.get(
                                profileEdit.getLang(), I18NBundle.class).get(
                                "hello")
                                + " " + m.getCurrentProfile().getName() + " !",
                                dialogSkin);
                        float scaleFactor = 1280 * 0.8f
                                / greeting.getStyle().font.getBounds(greeting.getText()).width / 1.5f;
                        greeting.setFontScale(1.5f * (scaleFactor < 1 ? scaleFactor : 1));
                        greeting.setFontScale(ProfileEditAvatar.this.getStage().getHeight() / 720
                                * greeting.getFontScaleY());
                        add(greeting).width(ProfileEditAvatar.this.getStage().getWidth() * 0.8f);
                        greeting.setAlignment(Align.center);
                        row().space(height / 8);
                        add(new Image(avatarPic.getDrawable()))
                                .size(height / 2);
                        Timer.schedule(new Task() {
                            @Override
                            public void run() {
                                toMainMenu();
                            }
                        }, 3);
                        addListener(new ClickListener() {
                            @Override
                            public void clicked(InputEvent event, float x,
                                    float y) {
                                toMainMenu();
                            }
                        });
                    }

                    private void toMainMenu() {
                        if (!changedToMainMenu) {
                            changedToMainMenu = true;
                            remove();
                            getGame().setScreen(MainMenuViewController.class);
                            showsHelloDialog = false;
                        }
                    }
                }).setFillParent(true);
            } else {
                getGame().setScreen(ProfileSelection.class);
            }
        }
    }

    private class BackClickListener extends ClickListener {
        @Override
        public void clicked(InputEvent event, float x, float y) {
            AudioManager.playSound("buttonClick");
            ProfileManager.getManager().getCurrentProfile()
                    .setAvatar(profileEdit.getAvatar());
            getGame().setScreen(ProfileEditName.class);
        }
    }

}
