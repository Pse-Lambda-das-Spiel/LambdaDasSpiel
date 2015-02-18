package lambda.viewcontroller.mainmenu;

import lambda.LambdaGame;
import lambda.model.profiles.ProfileManager;
import lambda.viewcontroller.ViewController;
import lambda.viewcontroller.settings.SettingsViewController;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class MainMenuViewController extends ViewController implements Screen {
	private final Stage stage;
	private final String skinJson = "data/skins/MainMenu.json";
	

	private Skin skin;

	private ImageButton backButton, settingsButton ,sound_unmuted ,sound_muted ;
	private Label coins;
	private Image coinBar;
	
	/**
     * Creates a object of the class without initializing the screen.
     */
	public MainMenuViewController() {
		stage = new Stage(new ScreenViewport());
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 1, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act();
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {

		Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void hide() {
		dispose();
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
		stage.dispose();
		skin.dispose();
	}

	@Override
	public void queueAssets(AssetManager manager) {
		manager.load(skinJson, Skin.class, new SkinLoader.SkinParameter(
				"data/skins/MasterSkin.atlas"));

	}

	@Override
	public void create(AssetManager manager) {
		skin = manager.get(skinJson, Skin.class);
		backButton = new ImageButton(skin, "backButton");
		settingsButton = new ImageButton(skin, "settings");
		sound_unmuted = new ImageButton(skin, "sound_unmuted");
		sound_muted = new ImageButton(skin, "sound_muted");

		Container<ImageButton> settingsButtonContainer = new Container<ImageButton>();
		settingsButtonContainer.pad(15);
		settingsButtonContainer.align(Align.bottomLeft);
		settingsButtonContainer.setActor(settingsButton);

		stage.addActor(settingsButtonContainer);
		settingsButtonContainer.setFillParent(true);

		Container<ImageButton> soundButtonContainer = new Container<ImageButton>();
		soundButtonContainer.pad(15);
		soundButtonContainer.align(Align.bottomRight);
		soundButtonContainer.setActor(sound_unmuted);

		stage.addActor(soundButtonContainer);
		soundButtonContainer.setFillParent(true);

		coins = new Label(String.valueOf(ProfileManager.getManager()
				.getCurrentProfile().getCoins()), skin);
		Container<Label> labelContainer = new Container<Label>();
		labelContainer.align(Align.topRight);
		labelContainer.setActor(coins);

		coinBar = new Image(skin, "coin_bar");
		Container<Image> imageContainer = new Container<Image>();
		imageContainer.align(Align.topRight);
		imageContainer.setActor(coinBar);
		stage.addActor(imageContainer);
		imageContainer.setFillParent(true);
		Container<ImageButton> backButtonContainer = new Container<ImageButton>();
		backButtonContainer.pad(25);
		backButtonContainer.align(Align.topLeft);
		backButtonContainer.setActor(backButton);

		stage.addActor(backButtonContainer);
		backButtonContainer.setFillParent(true);

		backButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Gdx.app.exit();

			}
		});
		settingsButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				getGame().setScreen(SettingsViewController.class);
				

			}
		});
		sound_unmuted.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				soundButtonContainer.setActor(sound_muted);

			}
		});
		sound_muted.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				soundButtonContainer.setActor(sound_unmuted);

			}
		});

	}

}