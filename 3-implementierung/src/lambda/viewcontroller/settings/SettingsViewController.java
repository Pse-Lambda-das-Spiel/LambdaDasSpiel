package lambda.viewcontroller.settings;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.assets.loaders.TextureLoader.TextureParameter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
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

import lambda.model.profiles.ProfileManager;
import lambda.model.profiles.ProfileModel;
import lambda.model.settings.SettingsModel;
import lambda.viewcontroller.StageViewController;
import lambda.viewcontroller.mainmenu.MainMenuViewController;
import lambda.viewcontroller.statistics.StatisticViewController;

/**
 * Represents a screen that is used to depict and change profile-settings.
 * 
 * @author Kai Fieger
 */
public class SettingsViewController extends StageViewController {

    private final String skinJson = "data/skins/SettingsSkin.json";
    private SettingsModel settings;
    private TextButton statistics;
    private Slider musicSlider;
    private Slider soundSlider;
    private Label musicLabel;
    private Label soundLabel;
    private AssetManager manager;
    private final float space;
    
    /**
     * Creates a object of the class without initializing the screen.
     */
	public SettingsViewController() {
        ProfileManager.getManager().addObserver(this);
        settings = new SettingsModel();
        space = getStage().getWidth() / 64;
	}

    @Override
    public void queueAssets(AssetManager assets) {
        assets.load(skinJson, Skin.class,
                new SkinLoader.SkinParameter("data/skins/MasterSkin.atlas"));
        assets.load("data/backgrounds/settings.png", Texture.class, new TextureParameter());
    }

    @Override
    public void create(final AssetManager manager) {
        
        Image background = new Image(manager.get("data/backgrounds/settings.png", Texture.class));
        background.setWidth(getStage().getWidth());
        background.setHeight(getStage().getHeight());
        getStage().addActor(background);
        
        this.manager = manager;
        Table settingsView = new Table();
        settingsView.align(Align.top);
        getStage().addActor(settingsView);
        settingsView.setFillParent(true);
        float height = getStage().getHeight() / 10;
        float width = getStage().getWidth() * 0.8f;
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
        settingsView.add(musicSlider).width(width).space(space);
        settingsView.row().height(height);
        soundLabel = new Label(null, manager.get(skinJson, Skin.class));
        settingsView.add(soundLabel).width(width).space(space * 2);
        settingsView.row().height(height);
        soundSlider = new Slider(0, 1, 0.01f, false,  manager.get(skinJson, Skin.class));
        soundSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                settings.setSoundVolume(soundSlider.getValue());
            }
        });
        settingsView.add(soundSlider).width(width).space(space);
        settingsView.row().height(height);
        statistics = new TextButton("", manager.get(skinJson, Skin.class));
        statistics.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                getGame().setScreen(StatisticViewController.class);
            }
        });
        settingsView.add(statistics).width(width * 2 / 3).space(space * 4);
        
        ImageButton backButton = new ImageButton(manager.get(skinJson, Skin.class), "backButton");
        Container<ImageButton> buttonContainer = new Container<ImageButton>();
        buttonContainer.pad(space * 5 / 2);
        buttonContainer.align(Align.bottomLeft);
        buttonContainer.setActor(backButton);
        backButton.addListener(new backClickListener());
        getStage().addActor(buttonContainer);
        buttonContainer.setFillParent(true);
        changedProfileList();
    }
    
    @Override
    public void changedProfile() {
        ProfileModel current = ProfileManager.getManager().getCurrentProfile();
        I18NBundle lang = manager.get(current.getLanguage(), I18NBundle.class);
        settings = current.getSettings();
        musicLabel.setText(lang.get("musicLabel") + ":");
        soundLabel.setText(lang.get("soundLabel") + ":");
        statistics.setText(lang.get("statistics"));
        musicSlider.setValue(settings.getMusicVolume());
        soundSlider.setValue(settings.getSoundVolume());
    }

    private class backClickListener extends ClickListener {
        @Override
        public void clicked(InputEvent event, float x, float y) {
            getGame().setScreen(MainMenuViewController.class);
        }
    }
    
}