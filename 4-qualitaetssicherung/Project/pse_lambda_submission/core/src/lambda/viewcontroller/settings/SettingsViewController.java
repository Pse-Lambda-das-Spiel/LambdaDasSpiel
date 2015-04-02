package lambda.viewcontroller.settings;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.assets.loaders.TextureLoader.TextureParameter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
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
import lambda.viewcontroller.AudioManager;
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
        assets.load(skinJson, Skin.class, new SkinLoader.SkinParameter(
                "data/skins/MasterSkin.atlas"));
        TextureParameter textureParameter = new TextureParameter();
        textureParameter.minFilter = Texture.TextureFilter.Linear;
        textureParameter.magFilter = Texture.TextureFilter.Linear;
        assets.load("data/backgrounds/settings.png", Texture.class,
                textureParameter);
    }

    @Override
    public void create(final AssetManager manager) {
        setLastViewController(MainMenuViewController.class);
        Image background = new Image(manager.get(
                "data/backgrounds/settings.png", Texture.class));
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
        settingsView.row().height(height).space(space * 3);
        settingsView.add();
        settingsView.row().height(height);
        musicLabel = new Label(null, manager.get(skinJson, Skin.class));
        musicLabel.setFontScale(getStage().getHeight() / 720 * musicLabel.getFontScaleY());
        settingsView.add(musicLabel).width(width);
        settingsView.row().height(height);
        musicSlider = new Slider(0, 1, 0.01f, false, manager.get(skinJson,
                Skin.class));
        musicSlider.getStyle().background.setMinHeight(getStage().getHeight() / 720 
                * musicSlider.getStyle().background.getMinHeight());
        musicSlider.getStyle().knob.setMinHeight(getStage().getHeight() / 720 
                * musicSlider.getStyle().knob.getMinHeight());
        musicSlider.getStyle().knob.setMinWidth(getStage().getHeight() / 720 
                * musicSlider.getStyle().knob.getMinWidth());
        musicSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                settings.setMusicVolume(musicSlider.getValue());
            }
        });
        settingsView.add(musicSlider).width(width);
        settingsView.row().height(height);
        soundLabel = new Label(null, manager.get(skinJson, Skin.class));
        soundLabel.setFontScale(getStage().getHeight() / 720 * soundLabel.getFontScaleY());
        settingsView.add(soundLabel).width(width);
        settingsView.row().height(height);
        soundSlider = new Slider(0, 1, 0.01f, false, manager.get(skinJson,
                Skin.class));
        soundSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                settings.setSoundVolume(soundSlider.getValue());
            }
        });
        settingsView.add(soundSlider).width(width);
        settingsView.row().height(height);
        statistics = new TextButton("", manager.get(skinJson, Skin.class));
        statistics.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                AudioManager.playSound("buttonClick");
                getGame().setScreen(StatisticViewController.class);
            }
        });
        settingsView.add(statistics).width(width * 2 / 3).space(space * 4);
        statistics.getLabel().setFontScale(getStage().getHeight() / 720 * statistics.getLabel().getFontScaleY());
        
        ImageButton backButton = new ImageButton(manager.get(skinJson,
                Skin.class), "backButton");
        backButton.setBounds(space * 5 / 2, space * 5 / 2, getStage().getHeight() / 5,
                getStage().getHeight() / 5);
        backButton.addListener(new BackClickListener());
        getStage().addActor(backButton);
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

    private class BackClickListener extends ClickListener {
        @Override
        public void clicked(InputEvent event, float x, float y) {
            AudioManager.playSound("buttonClick");
            getGame().setScreen(MainMenuViewController.class);
        }
    }

}