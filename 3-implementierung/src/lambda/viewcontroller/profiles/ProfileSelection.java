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
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import lambda.model.profiles.ProfileManager;
import lambda.viewcontroller.ViewController;

public class ProfileSelection extends ViewController {

    private final String skinJson = "data/skins/ProfileSelectionSkin.json";
    private final String skinPack = "data/skins/ProfileSelectionSkin.atlas";
    private final Stage stage;
    private List<TextButton> profileButtons;
    private List<ImageButton> editButtons;
    private ImageButton addButton;
    
	public ProfileSelection() {
	    stage = new Stage(new ScreenViewport());
	    ProfileManager.getManager().addObserver(this);
	}

    @Override
    public void queueAssets(AssetManager assets) {
        assets.load(skinPack, TextureAtlas.class);
        assets.load(skinJson, Skin.class,
                new SkinLoader.SkinParameter(skinPack));
        //TODO?
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
        profileButtons = new ArrayList<TextButton>();
        editButtons = new ArrayList<ImageButton>();
        Table profileView = new Table();
        stage.addActor(profileView);
        profileView.setFillParent(true);
        for (int i = 0; i < ProfileManager.MAX_NUMBER_OF_PROFILES; i++) {
            profileView.row().height(stage.getHeight() * 3 / 5 / ProfileManager.MAX_NUMBER_OF_PROFILES);
            TextButton pButton = new TextButton("", manager.get(skinJson, Skin.class));
            profileView.add(pButton).width(stage.getWidth() * 3 / 5 * 0.9f).space(10);
            profileButtons.add(pButton);
            pButton.addListener(new selectProfileClickListener());
            ImageButton eButton = new ImageButton(manager.get(skinJson, Skin.class), "editButton");
            profileView.add(eButton).width(stage.getWidth() * 3 / 5 * 0.1f).space(10);
            editButtons.add(eButton);
            eButton.addListener(new editProfileClickListener());
        }
        addButton = new ImageButton(manager.get(skinJson, Skin.class), "addButton");
        addButton.setSize(stage.getWidth() * 0.1f, stage.getHeight() * 0.1f);
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
                //getGame().setScreen(MainMenuViewController.class);
            }
        }
    }
    
    private class editProfileClickListener extends ClickListener {
        @Override
        public void clicked(InputEvent event, float x, float y) {
            ImageButton clickedButton = (ImageButton) event.getListenerActor();
            String name = profileButtons.get(editButtons.indexOf(clickedButton)).getText().toString();
            //TODO
        }
    }
    
    private class addProfileClickListener extends ClickListener {
        @Override
        public void clicked(InputEvent event, float x, float y) {
            ProfileManager manager = ProfileManager.getManager();
            manager.createProfile();
            manager.setCurrentProfile("");
            //getGame().setScreen(ProfileEditLang.class);
        }
    }

}
