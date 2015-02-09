package lambda.viewcontroller.profiles;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import lambda.model.profiles.ProfileManager;
import lambda.viewcontroller.ViewController;
import lambda.viewcontroller.mainmenu.MainMenuViewController;
import lambda.viewcontroller.settings.SettingsViewController;

/**
 * Represents the screen of the profile-selection.
 * It allows the user to choose, add, remove or edit profiles. 
 * 
 * @author Kai Fieger
 */
public class ProfileSelection extends ViewController {

    private final String skinJson = "data/skins/ProfileSelectionSkin.json";
    private final Stage stage;
    private List<TextButton> profileButtons;
    private List<ImageButton> editButtons;
    private ImageButton addButton;
    private AssetManager manager; 
    
    /**
     * Creates a object of the class without initializing the screen.
     */
	public ProfileSelection() {
	    stage = new Stage(new ScreenViewport());
	    ProfileManager.getManager().addObserver(this);
	}

    @Override
    public void queueAssets(AssetManager assets) {
        assets.load("data/skins/MasterSkin.atlas", TextureAtlas.class);
        assets.load(skinJson, Skin.class,
                new SkinLoader.SkinParameter("data/skins/MasterSkin.atlas"));
        
        //temp vvv
        assets.load("data/skins/DialogTemp.json", Skin.class,
                new SkinLoader.SkinParameter("data/skins/MasterSkin.atlas"));
    }
    
    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        if (ProfileManager.getManager().getNames().size() == 0) {
            new addProfileClickListener().clicked(null, 0, 0);
        }
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
        ProfileManager m = ProfileManager.getManager();
        if (m.getCurrentProfile() != null) {
            m.save(m.getCurrentProfile().getName());
        }
        stage.dispose();
    }

    @Override
    public void create(AssetManager manager) {
        new Dialog("", manager.get("data/skins/DialogTemp.json", Skin.class)) {
            {
                this.getBackground().setMinWidth(0.0f);
                this.getBackground().setMinHeight(0.0f);
            }
        };
        this.manager = manager;
        profileButtons = new ArrayList<TextButton>();
        editButtons = new ArrayList<ImageButton>();
        Table profileView = new Table();
        stage.addActor(profileView);
        profileView.setFillParent(true);
        //Profilebuttons + their edit buttons
        for (int i = 0; i < ProfileManager.MAX_NUMBER_OF_PROFILES; i++) {
            float height = stage.getHeight() * 3 / 5 / ProfileManager.MAX_NUMBER_OF_PROFILES;
            profileView.row().height(height);
            TextButton pButton = new TextButton("", manager.get(skinJson, Skin.class));
            pButton.getLabel().setFontScale(0.5f);
            profileView.add(pButton).width(stage.getWidth() * 0.55f).space(10);
            profileButtons.add(pButton);
            pButton.addListener(new selectProfileClickListener());
            ImageButton eButton = new ImageButton(manager.get(skinJson, Skin.class), "editButton");
            profileView.add(eButton).width(height).align(Align.top);
            editButtons.add(eButton);
            eButton.addListener(new editProfileClickListener());
        }
        //addProfile-Button
        addButton = new ImageButton(manager.get(skinJson, Skin.class), "addButton");
        Container<ImageButton> buttonContainer = new Container<ImageButton>();
        buttonContainer.pad(25);
        buttonContainer.align(Align.bottomRight);
        buttonContainer.setActor(addButton);
        addButton.addListener(new addProfileClickListener());
        stage.addActor(buttonContainer);
        buttonContainer.setFillParent(true);
        changedProfileList();
    }
    
    @Override
    public void changedProfileList() {
        List<String> names = ProfileManager.getManager().getNames();
        int i = 0;
        while (i < names.size()) {
            profileButtons.get(i).setText(names.get(i));
            editButtons.get(i).setVisible(true);
            i++;
        }
        while (i < profileButtons.size()) {
            profileButtons.get(i).setText(null);
            editButtons.get(i).setVisible(false);
            i++;
        }
        addButton.setVisible(names.size() != profileButtons.size());
    }
    
    private class selectProfileClickListener extends ClickListener {
        @Override
        public void clicked(InputEvent event, float x, float y) {
            String name = ((TextButton) event.getListenerActor()).getText().toString();
            if (name != null) {
                ProfileManager.getManager().setCurrentProfile(name);
                //getGame().setScreen(SettingsViewController.class);
                getGame().setScreen(MainMenuViewController.class);
            }
        }
    }
    
    private class editProfileClickListener extends ClickListener {
        @Override
        public void clicked(InputEvent event, float x, float y) {
            ImageButton clickedButton = (ImageButton) event.getListenerActor();
            String name = profileButtons.get(editButtons.indexOf(clickedButton)).getText().toString();
            Skin temp = manager.get("data/skins/DialogTemp.json", Skin.class);
            //dialog to choose between editing the profile or deleting it. 
            new Dialog("", temp) {
                {
                    clear();
                    //configuration/edit option
                    ImageButton configButton = new ImageButton(temp, "configButton");
                    configButton.addListener(new ClickListener() {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            setVisible(false);
                            hide();
                            ProfileManager.getManager().setCurrentProfile(name);
                            getGame().setScreen(ProfileEditLang.class);
                        }
                    });
                    add(configButton).pad(10);
                    
                    //delete option. opens confirm dialog
                    ImageButton deleteButton = new ImageButton(temp, "deleteButton");
                    deleteButton.addListener(new ClickListener() {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            confirm();
                        }
                    });
                    add(deleteButton).pad(10);
                }
                
                //asks for confirmation if the profile should be deleted
                private void confirm() {
                    clear();
                    //yes
                    ImageButton yesButton = new ImageButton(temp, "yesButton");
                    yesButton.addListener(new ClickListener() {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            setVisible(false);
                            hide();
                            ProfileManager.getManager().delete(name);
                        }
                    });
                    add(yesButton).pad(10);
                    //no
                    ImageButton noButton = new ImageButton(temp, "noButton");
                    noButton.addListener(new ClickListener() {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            setVisible(false);
                            hide();
                        }
                    });
                    add(noButton).pad(10);
                }
            }.show(stage);
        }
    }
    
    private class addProfileClickListener extends ClickListener {
        @Override
        public void clicked(InputEvent event, float x, float y) {
            ProfileManager m = ProfileManager.getManager();
            m.createProfile();
            m.setCurrentProfile("");
            getGame().setScreen(ProfileEditLang.class);
        }
    }

}
