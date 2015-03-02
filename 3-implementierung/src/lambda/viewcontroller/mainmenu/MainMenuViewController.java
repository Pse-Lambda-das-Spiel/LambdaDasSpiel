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
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.assets.loaders.TextureLoader.TextureParameter;
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

	private Skin skin;
	private Label coins;
	private Label profileName;
	private Image profileImg;
	private AssetManager manager;
    private Container<ImageButton> soundButtonContainer;
    private ImageButton sound_muted;
	private ImageButton sound_unmuted;
	private final float buttonSize;

	/**
	 * Creates a object of the class without initializing the screen.
	 */
	public MainMenuViewController() {
		buttonSize =  (getStage().getWidth() / 8);
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
		TextureParameter param = new TextureParameter();
		param.magFilter = TextureFilter.Linear;
		param.minFilter = TextureFilter.Linear;
		manager.load("data/backgrounds/mainmenu.png", Texture.class, param);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void create(final AssetManager manager) {
		this.manager = manager;
		ProfileManager.getManager().addObserver(this);
		skin = manager.get("data/skins/MasterSkin.json", Skin.class);
		Image background = new Image(manager.get("data/backgrounds/mainmenu.png", Texture.class));
		background.setWidth(getStage().getWidth());
		background.setHeight(getStage().getHeight());
		getStage().addActor(background);
		
		// TODO: Replace with logoutButton when its finished
		ImageButton logoutButton = new ImageButton(skin, "backButton");
		ImageButton settingsButton = new ImageButton(skin, "settingsButton");
		sound_unmuted = new ImageButton(skin, "unmutedButton");
		sound_muted = new ImageButton(skin, "mutedButton");
		
		ImageButton startButton = new ImageButton(skin, "startButton");
		ImageButton levelMenuButton = new ImageButton(skin, "levelMenuButton");
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
		settingsButton.setSize(buttonSize, buttonSize);
		settingsButton.scaleBy(buttonSize);
		settingsButtonContainer.size(buttonSize);

		soundButtonContainer = new Container<>();
		soundButtonContainer.pad(15).align(Align.bottomRight);
		sound_unmuted.setSize(buttonSize, buttonSize);
		sound_muted.setSize(buttonSize, buttonSize);
		soundButtonContainer.setActor(sound_unmuted);
		getStage().addActor(soundButtonContainer);
		soundButtonContainer.setFillParent(true);

		Container<ImageTextButton> coinButtonContainer = new Container<>();
		coinButtonContainer.pad(15).align(Align.topRight).setSize(buttonSize, buttonSize);
		coinButton.setSize(buttonSize, buttonSize);
		coinButtonContainer.setActor(coinButton);
		getStage().addActor(coinButtonContainer);
		coinButtonContainer.setFillParent(true);
		
		Table profileTable = new Table();
		profileTable.pad(25).align(Align.topLeft);
		logoutButton.setSize(buttonSize, buttonSize);
		profileTable.add(logoutButton).align(Align.left).spaceBottom(getStage().getHeight() / 8).row();
		profileTable.add(profileImg).row();
		profileTable.add(profileName);
		getStage().addActor(profileTable);
		profileTable.setFillParent(true);

		Table centerTable = new Table();
		centerTable.align(Align.center);
		centerTable.add().row();
		centerTable.add(startButton).colspan(2).align(Align.center).spaceBottom(getStage().getHeight() / 20).row();
		centerTable.add(levelMenuButton).size(buttonSize).align(Align.left).spaceRight(getStage().getWidth() / 20);
		centerTable.add(achievementsButton).size(buttonSize).align(Align.right);
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
		levelMenuButton.addListener(new ClickListener() {
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