package lambda.viewcontroller.settings;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import lambda.model.profiles.ProfileManager;
import lambda.model.profiles.ProfileModel;
import lambda.model.settings.SettingsModel;
import lambda.model.settings.SettingsModelObserver;
import lambda.viewcontroller.ViewController;
import lambda.viewcontroller.mainmenu.MainMenuViewController;

/**
 * Represents a screen that is used to depict and change profile-settings.
 * 
 * @author Kai Fieger
 */
public class SettingsViewController extends ViewController implements SettingsModelObserver {

    private final String skinJson = "data/skins/SettingsSkin.json";
    private final Stage stage;
    private SettingsModel settings;
    private TextButton statistics;
    private Slider musicSlider;
    private Slider soundSlider;
    private Label musicLabel;
    private Label soundLabel;
    private AssetManager manager;
    
    /**
     * Creates a object of the class without initializing the screen.
     */
	public SettingsViewController() {
        stage = new Stage(new ScreenViewport());
        ProfileManager.getManager().addObserver(this);
        settings = new SettingsModel();
        settings.addObserver(this);
	}

    @Override
    public void queueAssets(AssetManager assets) {
        assets.load(skinJson, Skin.class,
                new SkinLoader.SkinParameter("data/skins/MasterSkin.atlas"));
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
        this.manager = manager;
        Table settingsView = new Table();
        settingsView.align(Align.top);
        stage.addActor(settingsView);
        settingsView.setFillParent(true);
        float height = stage.getHeight() / 10;
        float width = stage.getWidth() * 0.8f;
        settingsView.row().height(height);
        settingsView.add();
        settingsView.row().height(height);
        musicLabel = new Label(null, manager.get(skinJson, Skin.class));
        settingsView.add(musicLabel).width(width);
        settingsView.row().height(height);
        musicSlider = new Slider(0, 1, 0.01f, false,  manager.get(skinJson, Skin.class));
        musicSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                settings.setMusicVolume(musicSlider.getValue());
            }
        });
        settingsView.add(musicSlider).width(width).space(10);
        settingsView.row().height(height);
        soundLabel = new Label(null, manager.get(skinJson, Skin.class));
        settingsView.add(soundLabel).width(width).space(20);
        settingsView.row().height(height);
        soundSlider = new Slider(0, 1, 0.01f, false,  manager.get(skinJson, Skin.class));
        soundSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                settings.setSoundVolume(soundSlider.getValue());
            }
        });
        settingsView.add(soundSlider).width(width).space(10);
        settingsView.row().height(height);
        statistics = new TextButton("", manager.get(skinJson, Skin.class));
        settingsView.add(statistics).width(width).space(40);
        
        ImageButton backButton = new ImageButton(manager.get(skinJson, Skin.class), "backButton");
        Container<ImageButton> buttonContainer = new Container<ImageButton>();
        buttonContainer.pad(25);
        buttonContainer.align(Align.bottomLeft);
        buttonContainer.setActor(backButton);
        backButton.addListener(new backClickListener());
        stage.addActor(buttonContainer);
        buttonContainer.setFillParent(true);
        changedProfileList();
    }
    
    @Override
    public void changedProfile() {
        ProfileModel current = ProfileManager.getManager().getCurrentProfile();
        I18NBundle lang = manager.get(current.getLanguage(), I18NBundle.class);
        settings.removeObserver(this);
        settings = current.getSettings();
        settings.addObserver(this);
        musicLabel.setText(lang.get("musicLabel") + ":");
        soundLabel.setText(lang.get("soundLabel") + ":");
        statistics.setText(lang.get("statistics"));
        musicSlider.setValue(settings.getMusicVolume());
        soundSlider.setValue(settings.getSoundVolume());
    }
    
    @Override
    public void changedMusicOn() {
        //TODO ?
    }

    @Override
    public void changedMusicVolume() {
        //TODO ?
    }

    @Override
    public void changedSoundVolume() {
        //TODO ?
    }

    private class backClickListener extends ClickListener {
        @Override
        public void clicked(InputEvent event, float x, float y) {
            getGame().setScreen(MainMenuViewController.class);
        }
    }
    
}