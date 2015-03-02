package lambda.viewcontroller.profiles;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
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
        nameField.getStyle().background.setLeftWidth(space * 3 / 2);
        nameField.getStyle().background.setRightWidth(space * 3 / 2);
        nameSelection.add(nameField).width(getStage().getWidth() * 2 / 3).space(space);
        
        ImageButton backButton = new ImageButton(manager.get(skinJson, Skin.class), "leftButton");
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
            AudioManager.playSound("buttonClick");
            String name = nameField.getText().trim();
            if (!name.equals("")) {
                if (ProfileManager.getManager().changeCurrentName(name)) {
                    getGame().setScreen(ProfileEditAvatar.class);
                } else {
                    new NameDialog("nameTaken").show(getStage());
                }
            } else {
                new NameDialog("nameEmpty").show(getStage());
            }
        }
    }
    
    private class backClickListener extends ClickListener {
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
                    new NameDialog("nameTaken").show(getStage());
                }
            } else {
                new NameDialog("nameEmpty").show(getStage());   
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
            addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    remove();
                }
            });
        }
    }

	@Override
	public void changedAvatar() {		
	}

}
