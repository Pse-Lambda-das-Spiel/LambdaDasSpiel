package lambda.viewcontroller.profiles;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.graphics.Texture;
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
 * Represents a screen of the profile configuration/creation.
 * It allows the user to change the profile's language.
 * 
 * @author Kai Fieger
 */
public class ProfileEditLang extends StageViewController implements ProfileEditObserver {

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
        do {
            assets.load(current, I18NBundle.class);
            assets.load(profileEdit.getLangPic(), Texture.class);
            profileEdit.nextLang();
            current = profileEdit.getLang();
        } while (!current.equals(start));
        assets.load(skinJson, Skin.class,
                new SkinLoader.SkinParameter("data/skins/MasterSkin.atlas"));
    }

    @Override
    public void create(final AssetManager manager) {
        profileEdit.addObserver(this);
        this.manager = manager;
        Table langSelection = new Table();
        getStage().addActor(langSelection);
        langSelection.setFillParent(true);
        langSelection.row().height(getStage().getHeight() / 2);
        ImageButton selectLeft = new ImageButton(manager.get(skinJson, Skin.class), "leftButton");
        langSelection.add(selectLeft).size(getStage().getHeight() / 5).space(space);
        selectLeft.addListener(new selectLeftClickListener());
        langPic = new Image();
        langSelection.add(langPic).width(getStage().getWidth() / 2).space(space);
        ImageButton selectRight = new ImageButton(manager.get(skinJson, Skin.class), "rightButton");
        langSelection.add(selectRight).size(getStage().getHeight() / 5).space(space);
        selectRight.addListener(new selectRightClickListener());
        lang = new Label(null ,manager.get(skinJson, Skin.class));
        lang.setAlignment(Align.center);
        langSelection.row().height(getStage().getHeight() / 5);
        langSelection.add();
        langSelection.add(lang).width(getStage().getWidth() / 2);
        
        backButton = new ImageButton(manager.get(skinJson, Skin.class), "leftButton");
        Container<ImageButton> buttonContainer = new Container<ImageButton>();
        buttonContainer.pad(space * 5 / 2).maxSize(getStage().getHeight() / 5);
        buttonContainer.align(Align.bottomLeft);
        buttonContainer.setActor(backButton);
        backButton.addListener(new backClickListener());
        getStage().addActor(buttonContainer);
        buttonContainer.setFillParent(true);
        
        ImageButton continueButton = new ImageButton(manager.get(skinJson, Skin.class), "rightButton");
        buttonContainer = new Container<ImageButton>();
        buttonContainer.pad(space * 5 / 2).maxSize(getStage().getHeight() / 5);
        buttonContainer.align(Align.bottomRight);
        buttonContainer.setActor(continueButton);
        continueButton.addListener(new continueClickListener());
        getStage().addActor(buttonContainer);
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
            AudioManager.playSound("buttonClick");
            profileEdit.previousLang();
        }
    }
    
    private class selectRightClickListener extends ClickListener {
        @Override
        public void clicked(InputEvent event, float x, float y) {
            AudioManager.playSound("buttonClick");
            profileEdit.nextLang();
        }
    }
    
    private class continueClickListener extends ClickListener {
        @Override
        public void clicked(InputEvent event, float x, float y) {
            AudioManager.playSound("buttonClick");
            ProfileManager.getManager().getCurrentProfile().setLanguage(profileEdit.getLang());
            getGame().setScreen(ProfileEditName.class);
        }
    }
    
    private class backClickListener extends ClickListener {
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
