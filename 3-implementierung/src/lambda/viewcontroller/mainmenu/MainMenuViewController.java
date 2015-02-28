package lambda.viewcontroller.mainmenu;

import lambda.model.profiles.ProfileManager;
import lambda.viewcontroller.AudioManager;
import lambda.viewcontroller.StageViewController;
import lambda.viewcontroller.achievements.AchievementMenuViewController;
import lambda.viewcontroller.level.LevelSelectionViewController;
import lambda.viewcontroller.profiles.ProfileSelection;
import lambda.viewcontroller.settings.SettingsViewController;
import lambda.viewcontroller.shop.ShopViewController;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
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

/**
 * Represents the main menu.
 * 
 * @author Farid, Robert Hochweiss
 */
public class MainMenuViewController extends StageViewController {

	private final String SKINJSON = "data/skins/MainMenuSkin.json";
	private Skin skin;
	private Label coins;
	private Label profileName;
	private Image profileImg;
	private AssetManager manager;
    private Container<ImageButton> soundButtonContainer;
    private ImageButton sound_muted;
	private ImageButton sound_unmuted;

	/**
	 * Creates a object of the class without initializing the screen.
	 */
	public MainMenuViewController() {
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
		soundButtonContainer.setActor((pManager.getCurrentProfile().getSettings().isMusicOn() ? sound_unmuted : sound_muted));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void show() {
		super.show();
		// Update coin label
		coins.setText(Integer.toString(ProfileManager.getManager().getCurrentProfile().getCoins()));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void queueAssets(AssetManager manager) {
		manager.load(SKINJSON, Skin.class, new SkinLoader.SkinParameter("data/skins/MasterSkin.atlas"));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void create(final AssetManager manager) {
		this.manager = manager;
		ProfileManager.getManager().addObserver(this);
		skin = manager.get(SKINJSON, Skin.class);
		// TODO: Replace with logoutButton when its finished
		ImageButton logoutButton = new ImageButton(skin, "backButton");
		ImageButton settingsButton = new ImageButton(skin, "settingsButton");
		sound_unmuted = new ImageButton(skin, "sound_unmuted");
		sound_muted = new ImageButton(skin, "sound_muted");
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
		getStage().addActor(settingsButtonContainer);
		settingsButtonContainer.setFillParent(true);

		soundButtonContainer = new Container<>();
		soundButtonContainer.pad(15).align(Align.bottomRight);
		soundButtonContainer.setActor(sound_unmuted);
		getStage().addActor(soundButtonContainer);
		soundButtonContainer.setFillParent(true);

		Container<ImageTextButton> coinButtonContainer = new Container<>();
		coinButtonContainer.pad(15).align(Align.topRight);
		coinButtonContainer.setActor(coinButton);
		getStage().addActor(coinButtonContainer);
		coinButtonContainer.setFillParent(true);
		
		Table profileTable = new Table();
		profileTable.pad(25).align(Align.topLeft);
		profileTable.add(logoutButton).align(Align.left).spaceBottom(getStage().getHeight() / 8).row();
		profileTable.add(profileImg).row();
		profileTable.add(profileName);
		getStage().addActor(profileTable);
		profileTable.setFillParent(true);

		Table centerTable = new Table();
		centerTable.align(Align.center);
		centerTable.add(startButton).colspan(2).align(Align.center).spaceBottom(getStage().getHeight() / 20).row();
		centerTable.add(levelButton).align(Align.left).spaceRight(getStage().getWidth() / 20);
		centerTable.add(achievementsButton).align(Align.right);
		getStage().addActor(centerTable);
		centerTable.setFillParent(true);
		
		logoutButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				ProfileManager pManager = ProfileManager.getManager();
				pManager.save(pManager.getCurrentProfile().getName());
				AudioManager.setLoggedIn(false);
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
			    ProfileManager.getManager().getCurrentProfile().getSettings().setMusicOn(false);
				soundButtonContainer.setActor(sound_muted);
			}
		});
		sound_muted.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
                ProfileManager.getManager().getCurrentProfile().getSettings().setMusicOn(true);
				soundButtonContainer.setActor(sound_unmuted);
			}
		});
	}
	
}