package lambda.viewcontroller.profiles;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import lambda.model.profiles.ProfileManager;
import lambda.viewcontroller.AudioManager;
import lambda.viewcontroller.StageViewController;
import lambda.viewcontroller.mainmenu.MainMenuViewController;

/**
 * Represents the screen of the profile-selection.
 * It allows the user to choose, add, remove or edit profiles. 
 * 
 * @author Kai Fieger
 */
public class ProfileSelection extends StageViewController {

    private final String skinJson = "data/skins/ProfileSelectionSkin.json";
    private List<TextButton> profileButtons;
    private List<ImageButton> editButtons;
    private ImageButton addButton;
    private AssetManager manager;
    private final float space;
    
    /**
     * Creates a object of the class without initializing the screen.
     */
	public ProfileSelection() {
	    ProfileManager.getManager().addObserver(this);
	    space = getStage().getWidth() / 64;
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
        super.show();
        if (ProfileManager.getManager().getNames().size() == 0) {
            new addProfileClickListener().clicked(null, 0, 0);
        }
    }


    @Override
    public void dispose() {
    	super.dispose();
        ProfileManager m = ProfileManager.getManager();
        if (m.getCurrentProfile() != null) {
            m.save(m.getCurrentProfile().getName());
        }
    }

    @Override
    public void create(final AssetManager manager) {
        //without this the background of all dialogs/the window's size is never smaller than 600,400
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
        getStage().addActor(profileView);
        profileView.setFillParent(true);
        //Profilebuttons + their edit buttons
        for (int i = 0; i < ProfileManager.MAX_NUMBER_OF_PROFILES; i++) {
            float height = getStage().getHeight() * 3 / 5 / ProfileManager.MAX_NUMBER_OF_PROFILES;
            profileView.row().height(height);
            TextButton pButton = new TextButton("", manager.get(skinJson, Skin.class));
            pButton.getLabel().setFontScale(0.5f);
            profileView.add(pButton).width(getStage().getWidth() * 0.55f).space(space);
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
        buttonContainer.pad(space * 5 / 2);
        buttonContainer.align(Align.bottomRight);
        buttonContainer.setActor(addButton);
        addButton.addListener(new addProfileClickListener());
        getStage().addActor(buttonContainer);
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
            if (!name.equals("")) {
                ProfileManager.getManager().setCurrentProfile(name);
                AudioManager.setLoggedIn(true);
                getGame().setScreen(MainMenuViewController.class);
            }
        }
    }
    
    private class editProfileClickListener extends ClickListener {
        @Override
        public void clicked(InputEvent event, float x, float y) {
            ImageButton clickedButton = (ImageButton) event.getListenerActor();
            final String name = profileButtons.get(editButtons.indexOf(clickedButton)).getText().toString();
            final Skin temp = manager.get("data/skins/DialogTemp.json", Skin.class);
            //dialog to choose between editing the profile or deleting it. 
            new Dialog("", temp) {
                {
                    clear();
                    Label nameLabel = new Label(name, temp);
                    add(nameLabel).pad(space * 3 / 2).padBottom(0).colspan(2);
                    row();
                    //configuration/edit option
                    ImageButton configButton = new ImageButton(temp, "configButton");
                    configButton.addListener(new ClickListener() {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            remove();
                            ProfileManager.getManager().setCurrentProfile(name);
                            getGame().setScreen(ProfileEditLang.class);
                        }
                    });
                    add(configButton).pad(space).padBottom(space * 3 / 2).align(Align.bottomRight);
                    //delete option. opens confirm dialog
                    ImageButton deleteButton = new ImageButton(temp, "deleteButton");
                    deleteButton.addListener(new ClickListener() {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            confirm();
                        }
                    });
                    add(deleteButton).pad(space).padBottom(space * 3 / 2).align(Align.bottomLeft);
                }
                
                //asks for confirmation if the profile should be deleted
                private void confirm() {
                    clear();
                    Label nameLabel = new Label(name, temp);
                    add(nameLabel).pad(space * 3 / 2).padBottom(0).colspan(2);
                    row();
                    //yes
                    ImageButton yesButton = new ImageButton(temp, "yesButton");
                    yesButton.addListener(new ClickListener() {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            remove();
                            ProfileManager.getManager().delete(name);
                        }
                    });
                    add(yesButton).pad(space).padBottom(space * 3 / 2).align(Align.bottomRight);
                    //no
                    ImageButton noButton = new ImageButton(temp, "noButton");
                    noButton.addListener(new ClickListener() {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            remove();
                        }
                    });
                    add(noButton).pad(space).padBottom(space * 3 / 2).align(Align.bottomLeft);
                }
            }.show(getStage());
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
