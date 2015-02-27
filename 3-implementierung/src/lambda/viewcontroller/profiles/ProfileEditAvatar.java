package lambda.viewcontroller.profiles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
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
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import lambda.model.profiles.ProfileEditModel;
import lambda.model.profiles.ProfileEditObserver;
import lambda.model.profiles.ProfileManager;
import lambda.viewcontroller.AudioManager;
import lambda.viewcontroller.ViewController;
import lambda.viewcontroller.mainmenu.MainMenuViewController;

/**
 * Represents a screen of the profile configuration/creation.
 * It allows the user to change the profile's avatar picture.
 * 
 * @author Kai Fieger
 */
public class ProfileEditAvatar extends ViewController implements ProfileEditObserver {

    private final String avatarPath = "data/avatar";
    private final String skinJson = "data/skins/ProfileEditSkin.json";
    private final Stage stage;
    private final ProfileEditModel profileEdit;
    private AssetManager manager;
    private Image avatarPic;
    private Label chooseAvatar;
    private boolean newProfile;
    private final float space;
    
    /**
     * Creates a object of the class without initializing the screen.
     */
	public ProfileEditAvatar() {
	    stage = new Stage(new ScreenViewport());
        ProfileManager.getManager().addObserver(this);
        profileEdit = ProfileManager.getManager().getProfileEdit();
        space = stage.getWidth() / 64;
	}

    @Override
    public void queueAssets(AssetManager assets) {
        String start = profileEdit.getAvatar();
        String current = start;
        do {
            assets.load(avatarPath + "/" + profileEdit.getAvatar() + ".jpg", Texture.class);
            profileEdit.nextAvatar();
            current = profileEdit.getAvatar();
        } while (!current.equals(start));
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
    public void create(final AssetManager manager) {
        profileEdit.addObserver(this);
        this.manager = manager;
        
        Table avatarSelection = new Table();
        avatarSelection.align(Align.top);
        stage.addActor(avatarSelection);
        avatarSelection.setFillParent(true);

        chooseAvatar = new Label(null ,manager.get(skinJson, Skin.class));
        chooseAvatar.setFontScale(0.7f);
        chooseAvatar.setAlignment(Align.center);
        avatarSelection.row().height(stage.getHeight() / 20);
        avatarSelection.add();
        avatarSelection.row().height(stage.getHeight() / 5);
        avatarSelection.add();
        avatarSelection.add(chooseAvatar).width(stage.getWidth() / 2);
        
        avatarSelection.row().height(stage.getHeight() / 2);
        ImageButton selectLeft = new ImageButton(manager.get(skinJson, Skin.class), "leftButton");
        float buttonWidth = selectLeft.getWidth();
        float buttonHeight = selectLeft.getHeight();
        avatarSelection.add(selectLeft).width(buttonWidth).height(buttonHeight).space(space);
        selectLeft.addListener(new selectLeftClickListener());
        avatarPic = new Image();
        avatarSelection.add(avatarPic).width(stage.getWidth() / 2).space(space);
        ImageButton selectRight = new ImageButton(manager.get(skinJson, Skin.class), "rightButton");
        avatarSelection.add(selectRight).width(buttonWidth).height(buttonHeight).space(space);
        selectRight.addListener(new selectRightClickListener());
        
        ImageButton backButton = new ImageButton(manager.get(skinJson, Skin.class), "leftButton");
        Container<ImageButton> buttonContainer = new Container<ImageButton>();
        buttonContainer.pad(space * 5 / 2);
        buttonContainer.align(Align.bottomLeft);
        buttonContainer.setActor(backButton);
        backButton.addListener(new backClickListener());
        stage.addActor(buttonContainer);
        buttonContainer.setFillParent(true);
        
        ImageButton continueButton = new ImageButton(manager.get(skinJson, Skin.class), "acceptButton");
        buttonContainer = new Container<ImageButton>();
        buttonContainer.pad(space * 5 / 2);
        buttonContainer.align(Align.bottomRight);
        buttonContainer.setActor(continueButton);
        continueButton.addListener(new acceptClickListener());
        stage.addActor(buttonContainer);
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
        avatarPic.setDrawable(new SpriteDrawable(
                new Sprite(manager.get(avatarPath + "/" + profileEdit.getAvatar() + ".jpg", Texture.class))));
    }
    
    @Override 
    public void changedLanguage() {
        chooseAvatar.setText(manager.get(profileEdit.getLang(), I18NBundle.class).get("chooseAvatar"));
    }
    
    private class selectLeftClickListener extends ClickListener {
        @Override
        public void clicked(InputEvent event, float x, float y) {
            profileEdit.previousAvatar();
        }
    }
    
    private class selectRightClickListener extends ClickListener {
        @Override
        public void clicked(InputEvent event, float x, float y) {
            profileEdit.nextAvatar();
        }
    }
    
    private class acceptClickListener extends ClickListener {
        @Override
        public void clicked(InputEvent event, float x, float y) {
            final ProfileManager m = ProfileManager.getManager();
            m.getCurrentProfile().setAvatar(profileEdit.getAvatar());
            m.save(m.getCurrentProfile().getName());
            if (newProfile) {
                m.setCurrentProfile(m.getCurrentProfile().getName());
                final Skin dialogSkin = manager.get("data/skins/DialogTemp.json", Skin.class);
                new Dialog("", dialogSkin) {
                    private boolean changedToMainMenu = false;
                    {
                        AudioManager.setLoggedIn(true);
                        clear();
                        Label greeting = new Label(manager.get(profileEdit.getLang(), I18NBundle.class).get("hello")
                                + " " + m.getCurrentProfile().getName() + " !", dialogSkin);
                        greeting.setFontScale(1.5f);
                        add(greeting);
                        row().space(stage.getHeight() / 8);
                        add(new Image(avatarPic.getDrawable())).width(
                                stage.getWidth() / 3).height(stage.getHeight() / 3);
                        Timer.schedule(new Task() {
                            @Override
                            public void run() {
                                toMainMenu();
                            }
                        }, 3);
                        addListener(new ClickListener() {
                            @Override
                            public void clicked(InputEvent event, float x, float y) {
                                toMainMenu();
                            }
                        });
                    }
                    private void toMainMenu() {
                        if (!changedToMainMenu) {
                            changedToMainMenu = true;
                            remove();
                            getGame().setScreen(MainMenuViewController.class);
                        }
                    }
                }.show(stage).setFillParent(true);
            } else {
                getGame().setScreen(ProfileSelection.class);
            }
        }
    }
    
    private class backClickListener extends ClickListener {
        @Override
        public void clicked(InputEvent event, float x, float y) {
            ProfileManager.getManager().getCurrentProfile().setAvatar(profileEdit.getAvatar());;
            getGame().setScreen(ProfileEditName.class);
        }
    }

}
