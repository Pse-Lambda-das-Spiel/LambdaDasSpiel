package lambda.viewcontroller.profiles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.assets.loaders.TextureLoader.TextureParameter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.I18NBundle;

import lambda.model.profiles.ProfileEditModel;
import lambda.model.profiles.ProfileEditObserver;
import lambda.model.profiles.ProfileManager;
import lambda.viewcontroller.AudioManager;
import lambda.viewcontroller.StageViewController;

/**
 * Represents a screen of the profile configuration/creation. It allows the user
 * to change the profile's language.
 * 
 * @author Kai Fieger
 */
public class ProfileEditLang extends StageViewController implements
        ProfileEditObserver {

    private final String skinJson = "data/skins/ProfileEditSkin.json";
    private final ProfileEditModel profileEdit;
    private Image langPic;
    private Label lang;
    private ImageButton backButton;
    private AssetManager manager;
    private boolean deleteOnBack;
    private final float space;

    /**
     * Creates a object of the class without initializing the screen.
     */
    public ProfileEditLang() {
        ProfileManager.getManager().addObserver(this);
        profileEdit = ProfileManager.getManager().getProfileEdit();
        space = getStage().getWidth() / 64;
    }

    @Override
    public void queueAssets(AssetManager assets) {
        String start = profileEdit.getLang();
        String current = start;
        TextureParameter param = new TextureParameter();
        param.magFilter = TextureFilter.Linear;
        param.minFilter = TextureFilter.Linear;
        do {
            assets.load(current, I18NBundle.class);
            assets.load(profileEdit.getLangPic(), Texture.class, param);
            profileEdit.nextLang();
            current = profileEdit.getLang();
        } while (!current.equals(start));
        assets.load(skinJson, Skin.class, new SkinLoader.SkinParameter(
                "data/skins/MasterSkin.atlas"));
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
        Table langSelection = new Table();
        getStage().addActor(langSelection);
        langSelection.setFillParent(true);
        langSelection.row().height(getStage().getHeight() / 2);
        ImageButton selectLeft = new ImageButton(manager.get(skinJson,
                Skin.class), "leftButton");
        langSelection.add(selectLeft).size(getStage().getHeight() / 5)
                .space(space);
        selectLeft.addListener(new SelectLeftClickListener());
        langPic = new Image();
        langSelection.add(langPic).width(getStage().getWidth() / 2)
                .space(space);
        ImageButton selectRight = new ImageButton(manager.get(skinJson,
                Skin.class), "rightButton");
        langSelection.add(selectRight).size(getStage().getHeight() / 5)
                .space(space);
        selectRight.addListener(new SelectRightClickListener());
        lang = new Label(null, manager.get(skinJson, Skin.class));
        lang.setAlignment(Align.center);
        langSelection.row().height(getStage().getHeight() / 5);
        langSelection.add();
        langSelection.add(lang).width(getStage().getWidth() / 2);
        lang.setFontScale(getStage().getHeight() / 720 * lang.getFontScaleY());
        
        backButton = new ImageButton(manager.get(skinJson, Skin.class),
                "leftButton");
        Container<ImageButton> buttonContainer = new Container<ImageButton>();
        buttonContainer.pad(space * 5 / 2).maxSize(getStage().getHeight() / 5).minSize(getStage().getHeight() / 5);
        buttonContainer.align(Align.bottomLeft);
        buttonContainer.setActor(backButton);
        backButton.addListener(new BackClickListener());
        getStage().addActor(buttonContainer);
        buttonContainer.setFillParent(true);

        ImageButton continueButton = new ImageButton(manager.get(skinJson,
                Skin.class), "rightButton");
        buttonContainer = new Container<ImageButton>();
        buttonContainer.pad(space * 5 / 2).maxSize(getStage().getHeight() / 5).minSize(getStage().getHeight() / 5);
        buttonContainer.align(Align.bottomRight);
        buttonContainer.setActor(continueButton);
        continueButton.addListener(new ContinueClickListener());
        getStage().addActor(buttonContainer);
        buttonContainer.setFillParent(true);
    }

    @Override
    public void changedProfile() {
        ProfileManager m = ProfileManager.getManager();
        deleteOnBack = m.getCurrentProfile().getName().equals("");
        backButton.setVisible(m.getNames().size() != 1 || !deleteOnBack);
        profileEdit.setLang(m.getCurrentProfile().getLanguage());
    }

    @Override
    public void changedLanguage() {
        langPic.setDrawable(new SpriteDrawable(new Sprite(manager.get(
                profileEdit.getLangPic(), Texture.class))));
        lang.setText(manager.get(profileEdit.getLang(), I18NBundle.class).get(
                "language"));
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
                    removeLastDialog();
                    ProfileManager m = ProfileManager.getManager();
                    if (m.getNames().size() != 1 || !deleteOnBack) {
                        if (deleteOnBack) {
                            m.delete(m.getCurrentProfile().getName());
                        } else {
                            m.getCurrentProfile().setLanguage(
                                    profileEdit.getLang());
                            m.save(m.getCurrentProfile().getName());
                        }
                        getGame().setScreen(ProfileSelection.class);
                    }
                }
                return false;
            }
        };
        InputMultiplexer multiplexer = new InputMultiplexer(getStage(),
                backProcessor);
        Gdx.input.setInputProcessor(multiplexer);
    }

    private class SelectLeftClickListener extends ClickListener {
        @Override
        public void clicked(InputEvent event, float x, float y) {
            AudioManager.playSound("buttonClick");
            profileEdit.previousLang();
        }
    }

    private class SelectRightClickListener extends ClickListener {
        @Override
        public void clicked(InputEvent event, float x, float y) {
            AudioManager.playSound("buttonClick");
            profileEdit.nextLang();
        }
    }

    private class ContinueClickListener extends ClickListener {
        @Override
        public void clicked(InputEvent event, float x, float y) {
            AudioManager.playSound("buttonClick");
            ProfileManager.getManager().getCurrentProfile()
                    .setLanguage(profileEdit.getLang());
            getGame().setScreen(ProfileEditName.class);
        }
    }

    private class BackClickListener extends ClickListener {
        @Override
        public void clicked(InputEvent event, float x, float y) {
            AudioManager.playSound("buttonClick");
            ProfileManager m = ProfileManager.getManager();
            if (deleteOnBack) {
                m.delete(m.getCurrentProfile().getName());
            } else {
                m.getCurrentProfile().setLanguage(profileEdit.getLang());
                m.save(m.getCurrentProfile().getName());
            }
            getGame().setScreen(ProfileSelection.class);
        }
    }

    @Override
    public void changedAvatar() {
    }

}
