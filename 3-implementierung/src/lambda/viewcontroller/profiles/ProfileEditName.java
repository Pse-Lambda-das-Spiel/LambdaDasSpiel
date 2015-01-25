package lambda.viewcontroller.profiles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
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
import lambda.viewcontroller.ViewController;

public class ProfileEditName extends ViewController implements ProfileEditObserver {

    private final String skinJson = "data/skins/ProfileEditSkin.json";
    private final Stage stage;
    private final ProfileEditModel profileEdit;
    private AssetManager manager;
    private TextField nameField;
    private Label enterName;
    
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
        stage.getViewport().update(width, height, true);
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
        stage.addActor(nameSelection);
        nameSelection.setFillParent(true);
        nameSelection.row().height(stage.getHeight() / 3);
        nameField = new TextField("", manager.get(skinJson, Skin.class));
        nameSelection.add(nameField).width(stage.getHeight() / 2).space(10);
        enterName = new Label(null, manager.get(skinJson, Skin.class));
        nameSelection.row().height(stage.getHeight() / 3);
        nameSelection.add(enterName).width(stage.getHeight() / 2).space(10);
        enterName.setAlignment(Align.center);
        enterName.setFontScale(3);
        
        ImageButton backButton = new ImageButton(manager.get(skinJson, Skin.class), "leftButton");
        backButton.setSize(stage.getWidth() * 0.1f, stage.getHeight() * 0.1f);
        Container<ImageButton> buttonContainer = new Container<ImageButton>();
        buttonContainer.pad(25);
        buttonContainer.align(Align.bottomLeft);
        buttonContainer.setActor(backButton);
        backButton.addListener(new backClickListener());
        stage.addActor(buttonContainer);
        buttonContainer.setFillParent(true);
        
        ImageButton continueButton = new ImageButton(manager.get(skinJson, Skin.class), "rightButton");
        continueButton.setSize(stage.getWidth() * 0.1f, stage.getHeight() * 0.1f);
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
        nameField.setText(ProfileManager.getManager().getCurrentProfile().getName());
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
                    changedLanguage();
                    getGame().setScreen(ProfileEditAvatar.class);
                } else {
                    enterName.setText(manager.get(profileEdit.getLang(), I18NBundle.class).get("nameTaken"));
                }
            }
        }
    }
    
    private class backClickListener extends ClickListener {
        @Override
        public void clicked(InputEvent event, float x, float y) {
            String name = nameField.getText().trim();
            if (!name.equals("")) {
                if (ProfileManager.getManager().changeCurrentName(name)) {
                    changedLanguage();
                    getGame().setScreen(ProfileEditLang.class);
                } else {
                    enterName.setText(manager.get(profileEdit.getLang(), I18NBundle.class).get("nameTaken"));
                }
            }
        }
    }

}
