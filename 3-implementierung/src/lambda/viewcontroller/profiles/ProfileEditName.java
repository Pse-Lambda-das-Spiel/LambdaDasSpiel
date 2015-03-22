package lambda.viewcontroller.profiles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.I18NBundle;

import lambda.model.profiles.ProfileEditModel;
import lambda.model.profiles.ProfileEditObserver;
import lambda.model.profiles.ProfileManager;
import lambda.model.profiles.ProfileModel;
import lambda.viewcontroller.AudioManager;
import lambda.viewcontroller.StageViewController;

/**
 * Represents a screen of the profile configuration/creation.
 * It allows the user to change the profile's name.
 * 
 * @author Kai Fieger
 */
public class ProfileEditName extends StageViewController implements ProfileEditObserver {

    private final String skinJson = "data/skins/ProfileEditSkin.json";
    private final ProfileEditModel profileEdit;
    private AssetManager manager;
    private TextField nameField;
    private Label enterName;
    private boolean newProfile;
    private final float space;
    
    /**
     * Creates a object of the class without initializing the screen.
     */
	public ProfileEditName() {
        ProfileManager.getManager().addObserver(this);
        profileEdit = ProfileManager.getManager().getProfileEdit();
        space = getStage().getWidth() / 64;
	}
    
    @Override
    public void queueAssets(AssetManager assets) {
    }

    @Override
    public void create(final AssetManager manager) {
        Image background = new Image(manager.get("data/backgrounds/default.png", Texture.class));
        background.setWidth(getStage().getWidth());
        background.setHeight(getStage().getHeight());
        getStage().addActor(background);
        
        profileEdit.addObserver(this);
        this.manager = manager;
        Table nameSelection = new Table();
        nameSelection.align(Align.top);
        getStage().addActor(nameSelection);
        nameSelection.setFillParent(true);
        
        nameSelection.row().height(getStage().getHeight() / 20);
        nameSelection.add();
        nameSelection.row().height(getStage().getHeight() / 5);        
        enterName = new Label(null, manager.get(skinJson, Skin.class));
        enterName.setFontScale(0.7f);
        nameSelection.add(enterName).width(getStage().getHeight() * 0.8f).space(space);
        enterName.setAlignment(Align.center);
        nameSelection.row().height(getStage().getHeight() / 3);
        nameField = new TextField("", manager.get(skinJson, Skin.class));
        nameField.setMaxLength(20);
        nameField.setTextFieldListener(new TextField.TextFieldListener() {
            @Override
            public void keyTyped(TextField textField, char key) {
                if (key == '\n' || key == '\r') {
                    textField.getOnscreenKeyboard().show(false);
                    getStage().setKeyboardFocus(null);
                }
            }
        });
        nameField.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == Keys.BACK) {
                    nameField.getOnscreenKeyboard().show(false);
                    getStage().setKeyboardFocus(null);
                    Gdx.input.getInputProcessor().keyDown(keycode);
                }
                return false;
            }
        });
        nameField.getStyle().background.setLeftWidth(space * 3);
        nameField.getStyle().background.setRightWidth(space * 3);
        nameSelection.add(nameField).width(getStage().getWidth() * 2 / 3).space(space);

        ImageButton backButton = new ImageButton(manager.get(skinJson, Skin.class), "leftButton");
        Container<ImageButton> buttonContainer = new Container<ImageButton>();
        buttonContainer.pad(space * 5 / 2).maxSize(getStage().getHeight() / 5);
        buttonContainer.align(Align.bottomLeft);
        buttonContainer.setActor(backButton);
        backButton.addListener(new BackClickListener());
        getStage().addActor(buttonContainer);
        buttonContainer.setFillParent(true);
        
        ImageButton continueButton = new ImageButton(manager.get(skinJson, Skin.class), "rightButton");
        buttonContainer = new Container<ImageButton>();
        buttonContainer.pad(space * 5 / 2).maxSize(getStage().getHeight() / 5);
        buttonContainer.align(Align.bottomRight);
        buttonContainer.setActor(continueButton);
        continueButton.addListener(new ContinueClickListener());
        getStage().addActor(buttonContainer);
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
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void show() {
        InputProcessor backProcessor = new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
            	if (keycode == Keys.BACK) {
            		String name = nameField.getText().trim();
                    if (newProfile) {
                        getGame().setScreen(ProfileEditLang.class);
                    } else if (!name.equals("")) {
                        if (ProfileManager.getManager().changeCurrentName(name)) {
                            getGame().setScreen(ProfileEditLang.class);
                        } else {
                            new NameDialog("nameTaken", getStage().getHeight()).show(getStage());
                        }
                    } else {
                        new NameDialog("nameEmpty", getStage().getHeight()).show(getStage());   
                    }
            	}
    			return false;
            }
        };
        InputMultiplexer multiplexer = new InputMultiplexer(getStage(), backProcessor);
        Gdx.input.setInputProcessor(multiplexer);
    }
    
    private class ContinueClickListener extends ClickListener {
        @Override
        public void clicked(InputEvent event, float x, float y) {
            AudioManager.playSound("buttonClick");
            String name = nameField.getText().trim();
            if (!name.equals("")) {
                if (ProfileManager.getManager().changeCurrentName(name)) {
                    getGame().setScreen(ProfileEditAvatar.class);
                } else {
                    new NameDialog("nameTaken", getStage().getHeight()).show(getStage());
                }
            } else {
                new NameDialog("nameEmpty", getStage().getHeight()).show(getStage());
            }
        }
    }
    
    private class BackClickListener extends ClickListener {
        @Override
        public void clicked(InputEvent event, float x, float y) {
            AudioManager.playSound("buttonClick");
            String name = nameField.getText().trim();
            if (newProfile) {
                getGame().setScreen(ProfileEditLang.class);
            } else if (!name.equals("")) {
                if (ProfileManager.getManager().changeCurrentName(name)) {
                    getGame().setScreen(ProfileEditLang.class);
                } else {
                    new NameDialog("nameTaken", getStage().getHeight()).show(getStage());
                }
            } else {
                new NameDialog("nameEmpty", getStage().getHeight()).show(getStage());   
            }
        }
    }
    
    private class NameDialog extends Dialog {
        public NameDialog(String key, float stageHeight) {
            super("", manager.get("data/skins/DialogTemp.json", Skin.class));
            clear();
            pad(space);
            Label enterName = new Label(manager.get(profileEdit.getLang(), I18NBundle.class).get(key),
                    manager.get("data/skins/DialogTemp.json", Skin.class));
            enterName.setFontScale(0.5f);
            add(enterName);
            row();
            ImageButton yesButton = new ImageButton(manager.get("data/skins/DialogTemp.json", Skin.class), "yesButton");
            yesButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    AudioManager.playSound("buttonClick");
                    remove();
                }
            });
            add(yesButton).size(stageHeight / 5);
        }
    }

	@Override
	public void changedAvatar() {		
	}

}
