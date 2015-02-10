package lambda.viewcontroller.profiles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import lambda.model.profiles.ProfileEditModel;
import lambda.model.profiles.ProfileEditObserver;
import lambda.model.profiles.ProfileManager;
import lambda.model.profiles.ProfileModel;
import lambda.viewcontroller.ViewController;

/**
 * Represents a screen of the profile configuration/creation.
 * It allows the user to change the profile's name.
 * 
 * @author Kai Fieger
 */
public class ProfileEditName extends ViewController implements ProfileEditObserver {

    private final String skinJson = "data/skins/ProfileEditSkin.json";
    private final Stage stage;
    private final ProfileEditModel profileEdit;
    private AssetManager manager;
    private TextField nameField;
    private Label enterName;
    private boolean newProfile;
    
    /**
     * Creates a object of the class without initializing the screen.
     */
	public ProfileEditName() {
	    stage = new Stage(new ScreenViewport());
        ProfileManager.getManager().addObserver(this);
        profileEdit = ProfileManager.getManager().getProfileEdit();
	}
    
    @Override
    public void queueAssets(AssetManager assets) {
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
        Table nameSelection = new Table();
        nameSelection.align(Align.top);
        stage.addActor(nameSelection);
        nameSelection.setFillParent(true);
        
        nameSelection.row().height(stage.getHeight() / 20);
        nameSelection.add();
        nameSelection.row().height(stage.getHeight() / 5);        
        enterName = new Label(null, manager.get(skinJson, Skin.class));
        enterName.setFontScale(0.7f);
        nameSelection.add(enterName).width(stage.getHeight() * 0.8f).space(10);
        enterName.setAlignment(Align.center);
        nameSelection.row().height(stage.getHeight() / 3);
        nameField = new TextField("", manager.get(skinJson, Skin.class));
        nameField.setMaxLength(16);
        nameField.getStyle().background.setLeftWidth(15);
        nameField.getStyle().background.setRightWidth(15);
        nameSelection.add(nameField).width(stage.getWidth() * 0.8f).space(10);
        
        ImageButton backButton = new ImageButton(manager.get(skinJson, Skin.class), "leftButton");
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
        ProfileModel current = ProfileManager.getManager().getCurrentProfile();
        newProfile = current.getName().equals("");
        nameField.setText(current.getName());
    }
    
    @Override 
    public void changedLanguage() {
        enterName.setText(manager.get(profileEdit.getLang(), I18NBundle.class).get("enterName"));
    }
    
    private class continueClickListener extends ClickListener {
        @Override
        public void clicked(InputEvent event, float x, float y) {
            String name = nameField.getText().trim();
            if (!name.equals("")) {
                if (ProfileManager.getManager().changeCurrentName(name)) {
                    getGame().setScreen(ProfileEditAvatar.class);
                } else {
                    new NameDialog("nameTaken").show(stage);
                }
            } else {
                new NameDialog("nameEmpty").show(stage);
            }
        }
    }
    
    private class backClickListener extends ClickListener {
        @Override
        public void clicked(InputEvent event, float x, float y) {
            String name = nameField.getText().trim();
            if (newProfile) {
                getGame().setScreen(ProfileEditLang.class);
            } else if (!name.equals("")) {
                if (ProfileManager.getManager().changeCurrentName(name)) {
                    getGame().setScreen(ProfileEditLang.class);
                } else {
                    new NameDialog("nameTaken").show(stage);
                }
            } else {
                new NameDialog("nameEmpty").show(stage);   
            }
        }
    }
    
    private class NameDialog extends Dialog {
        public NameDialog(String key) {
            super("", manager.get("data/skins/DialogTemp.json", Skin.class));
            Label enterName = new Label(manager.get(profileEdit.getLang(), I18NBundle.class).get(key),
                    manager.get("data/skins/DialogTemp.json", Skin.class));
            enterName.setFontScale(0.5f);
            text(enterName).pad(15);
            button(manager.get(profileEdit.getLang(), I18NBundle.class).get("ok")).pad(30);
        }
    }

}
