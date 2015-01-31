package lambda.viewcontroller.profiles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
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
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import lambda.model.profiles.ProfileEditModel;
import lambda.model.profiles.ProfileEditObserver;
import lambda.model.profiles.ProfileManager;
import lambda.viewcontroller.ViewController;

/**
 * Represents a screen of the profile configuration/creation.
 * It allows the user to change the profile's language.
 * 
 * @author Kai Fieger
 */
public class ProfileEditLang extends ViewController implements ProfileEditObserver {

    private final String skinJson = "data/skins/ProfileEditSkin.json";
    private final String skinAtlas = "data/skins/ProfileEditSkin.atlas";
    private final Stage stage;
    private final ProfileEditModel profileEdit;
    private Image langPic;
    private Label lang;
    private ImageButton backButton;
    private AssetManager manager;
    private boolean deleteOnBack;
    
    /**
     * Creates a object of the class without initializing the screen.
     */
	public ProfileEditLang() {
	    stage = new Stage(new ScreenViewport());
        ProfileManager.getManager().addObserver(this);
        profileEdit = ProfileManager.getManager().getProfileEdit();
	}

    @Override
    public void queueAssets(AssetManager assets) {
        String start = profileEdit.getLang();
        String current = start;
        do {
            assets.load(current, I18NBundle.class);
            assets.load(profileEdit.getLangPic(), Texture.class);
            profileEdit.nextLang();
            current = profileEdit.getLang();
        } while (!current.equals(start));
        assets.load(skinAtlas, TextureAtlas.class);
        assets.load(skinJson, Skin.class,
                new SkinLoader.SkinParameter(skinAtlas));
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
        profileEdit.addObserver(this);
        this.manager = manager;
        Table langSelection = new Table();
        stage.addActor(langSelection);
        langSelection.setFillParent(true);
        langSelection.row().height(stage.getHeight() / 2);
        ImageButton selectLeft = new ImageButton(manager.get(skinJson, Skin.class), "leftButton");
        float buttonWidth = selectLeft.getWidth();
        float buttonHeight = selectLeft.getHeight();
        langSelection.add(selectLeft).width(buttonWidth).height(buttonHeight).space(10);
        selectLeft.addListener(new selectLeftClickListener());
        langPic = new Image();
        langSelection.add(langPic).width(stage.getWidth() / 2).space(10);
        ImageButton selectRight = new ImageButton(manager.get(skinJson, Skin.class), "rightButton");
        langSelection.add(selectRight).width(buttonWidth).height(buttonHeight).space(10);
        selectRight.addListener(new selectRightClickListener());
        lang = new Label(null ,manager.get(skinJson, Skin.class));
        lang.setAlignment(Align.center);
        langSelection.row().height(stage.getHeight() / 5);
        langSelection.add();
        langSelection.add(lang).width(stage.getWidth() / 2);
        
        backButton = new ImageButton(manager.get(skinJson, Skin.class), "leftButton");
        Container<ImageButton> buttonContainer = new Container<ImageButton>();
        buttonContainer.pad(25);
        buttonContainer.align(Align.bottomLeft);
        buttonContainer.setActor(backButton);
        backButton.addListener(new backClickListener());
        stage.addActor(buttonContainer);
        buttonContainer.setFillParent(true);
        
        ImageButton continueButton = new ImageButton(manager.get(skinJson, Skin.class), "rightButton");
        buttonContainer = new Container<ImageButton>();
        buttonContainer.pad(25);
        buttonContainer.align(Align.bottomRight);
        buttonContainer.setActor(continueButton);
        continueButton.addListener(new continueClickListener());
        stage.addActor(buttonContainer);
        buttonContainer.setFillParent(true);
    }

    @Override 
    public void changedProfile() {
        ProfileManager m = ProfileManager.getManager();
        deleteOnBack = m.getCurrentProfile().getName().equals("");
        backButton.setVisible(m.getNames().size() != 1 || !deleteOnBack );
        profileEdit.setLang(m.getCurrentProfile().getLanguage());
    }
    
    @Override 
    public void changedLanguage() {
        langPic.setDrawable(new SpriteDrawable(
                new Sprite(manager.get(profileEdit.getLangPic(), Texture.class))));
        lang.setText(manager.get(profileEdit.getLang(), I18NBundle.class).get("language"));
    }
    
    private class selectLeftClickListener extends ClickListener {
        @Override
        public void clicked(InputEvent event, float x, float y) {
            profileEdit.previousLang();
        }
    }
    
    private class selectRightClickListener extends ClickListener {
        @Override
        public void clicked(InputEvent event, float x, float y) {
            profileEdit.nextLang();
        }
    }
    
    private class continueClickListener extends ClickListener {
        @Override
        public void clicked(InputEvent event, float x, float y) {
            ProfileManager.getManager().getCurrentProfile().setLanguage(profileEdit.getLang());
            getGame().setScreen(ProfileEditName.class);
        }
    }
    
    private class backClickListener extends ClickListener {
        @Override
        public void clicked(InputEvent event, float x, float y) {
            ProfileManager m = ProfileManager.getManager();
            if (deleteOnBack) {
                m.delete(m.getCurrentProfile().getName());
            } else {
                m.getCurrentProfile().setLanguage(profileEdit.getLang());
            }
            getGame().setScreen(ProfileSelection.class);
        }
    }
    
}
