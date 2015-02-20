package lambda.viewcontroller.mainmenu;

import lambda.model.profiles.ProfileManager;
import lambda.viewcontroller.ViewController;
import lambda.viewcontroller.achievements.AchievementMenuViewController;
import lambda.viewcontroller.level.LevelSelectionViewController;
import lambda.viewcontroller.profiles.ProfileSelection;
import lambda.viewcontroller.settings.SettingsViewController;
import lambda.viewcontroller.shop.ShopViewController;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

/**
 * Represents the main menu.
 * 
 * @author Farid, Robert Hochweiss
 */
public class MainMenuViewController extends ViewController {

	private final Stage stage;
	private final String skinJson = "data/skins/MainMenuSkin.json";
	private Skin skin;
	private Label coins;
	private Label profileName;
	private Image profileImg;
	private AssetManager manager;

	/**
	 * Creates a object of the class without initializing the screen.
	 */
	public MainMenuViewController() {
		stage = new Stage(new ScreenViewport());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void changedProfile() {
		ProfileManager pManager = ProfileManager.getManager();
		profileImg.setDrawable(new SpriteDrawable(new Sprite(
				manager.get("data/avatar/" + pManager.getCurrentProfile().getAvatar() + ".jpg", Texture.class))));
		profileName.setText(pManager.getCurrentProfile().getName());
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
		// Update coin label
		coins.setText(Integer.toString(ProfileManager.getManager().getCurrentProfile().getCoins()));
	}

	@Override
	public void hide() {
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
	}

	@Override
	public void queueAssets(AssetManager manager) {
		manager.load(skinJson, Skin.class, new SkinLoader.SkinParameter("data/skins/MasterSkin.atlas"));
	}

	@Override
	public void create(AssetManager manager) {
		this.manager = manager;
		ProfileManager.getManager().addObserver(this);
		skin = manager.get(skinJson, Skin.class);
		// TODO: Replace with logoutButton when its finished
		ImageButton logoutButton = new ImageButton(skin, "backButton");
		ImageButton settingsButton = new ImageButton(skin, "settingsButton");
		ImageButton sound_unmuted = new ImageButton(skin, "sound_unmuted");
		ImageButton sound_muted = new ImageButton(skin, "sound_muted");
		ImageButton startButton = new ImageButton(skin, "startButton");
		// Only tmp until the levelButton for the main menu is finished
		ImageButton levelButton = new ImageButton(skin, "startButton");
		ImageButton achievementsButton = new ImageButton(skin, "achievementsButton");
		coins = new Label("Initial string", skin);
		ImageTextButton coinButton = new ImageTextButton("",skin);
		coinButton.add(new Image(skin, "coin"));
		coinButton.add(coins);
		profileName = new Label("Initial string", skin, "roboto");
		profileImg = new Image();
		
		Container<ImageButton> settingsButtonContainer = new Container<>();
		settingsButtonContainer.pad(15).align(Align.bottomLeft);
		settingsButtonContainer.setActor(settingsButton);
		stage.addActor(settingsButtonContainer);
		settingsButtonContainer.setFillParent(true);

		Container<ImageButton> soundButtonContainer = new Container<>();
		soundButtonContainer.pad(15).align(Align.bottomRight);
		soundButtonContainer.setActor(sound_unmuted);
		stage.addActor(soundButtonContainer);
		soundButtonContainer.setFillParent(true);

		Container<ImageTextButton> coinButtonContainer = new Container<>();
		coinButtonContainer.pad(15).align(Align.topRight);
		coinButtonContainer.setActor(coinButton);
		stage.addActor(coinButtonContainer);
		coinButtonContainer.setFillParent(true);
		
		Table profileTable = new Table();
		profileTable.pad(25).align(Align.topLeft);
		profileTable.add(logoutButton).align(Align.left).spaceBottom(stage.getHeight() / 8).row();
		profileTable.add(profileImg).row();
		profileTable.add(profileName);
		stage.addActor(profileTable);
		profileTable.setFillParent(true);

		Table centerTable = new Table();
		centerTable.align(Align.center);
		centerTable.add(startButton).colspan(2).align(Align.center).spaceBottom(stage.getHeight() / 20).row();
		centerTable.add(levelButton).align(Align.left).spaceRight(stage.getWidth() / 20);
		centerTable.add(achievementsButton).align(Align.right);
		stage.addActor(centerTable);
		centerTable.setFillParent(true);
		
		logoutButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				ProfileManager pManager = ProfileManager.getManager();
				pManager.save(pManager.getCurrentProfile().getName());
				getGame().setScreen(ProfileSelection.class);
			}
		});
		// tmp until LevelSelectionVC is finished
		startButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				getGame().setScreen(LevelSelectionViewController.class);
			}
		});
		levelButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				getGame().setScreen(LevelSelectionViewController.class);
			}
		});
		achievementsButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				getGame().setScreen(AchievementMenuViewController.class);
			}
		});
		coinButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				getGame().setScreen(ShopViewController.class);
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