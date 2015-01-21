package lambda;

import lambda.viewcontroller.achievements.AchievementMenuViewController;
import lambda.viewcontroller.editor.EditorViewController;
import lambda.viewcontroller.level.LevelSelectionViewController;
import lambda.viewcontroller.mainmenu.MainMenuViewController;
import lambda.viewcontroller.profiles.ProfileEditAvatar;
import lambda.viewcontroller.profiles.ProfileEditLang;
import lambda.viewcontroller.profiles.ProfileEditName;
import lambda.viewcontroller.profiles.ProfileSelection;
import lambda.viewcontroller.reduction.ReductionViewController;
import lambda.viewcontroller.settings.SettingsViewController;
import lambda.viewcontroller.shop.DropDownMenuViewController;
import lambda.viewcontroller.shop.ShopViewController;
import lambda.viewcontroller.statistics.StatisticViewController;

import com.badlogic.gdx.Game;

/**
 * The main class of this application.
 * 
 * @author Robert Hochweiss
 */
public class LambdaGame extends Game {

	private AssetModel assets;
	
	private AchievementMenuViewController achievementVC;
	private DropDownMenuViewController dropDownMenuVC;
	private StatisticViewController statisticVC;
	private MainMenuViewController mainMenuVC;
	private SettingsViewController settingsVC;
	private ShopViewController shopVC;
	private ProfileSelection profileSelectionVC;
	private ProfileEditLang langEditVC;
	private ProfileEditName nameEditVC;
	private ProfileEditAvatar avatarEditVC;
	private LevelSelectionViewController levelSelectionVC;
	private EditorViewController editorVC;
	private ReductionViewController reductionVC;
	
	/**
	 * Creates a new instance of this game.
	 */
	public LambdaGame() {
	}

	/**
	 * Returns the ViewController for the achievementmenu.
	 * 
	 * @return the achievementVC
	 */
	public AchievementMenuViewController getAchievementVC() {
		return achievementVC;
	}

	/**
	 * Returns the ViewController for a dropdownmenu.
	 * 
	 * @return the dropDownMenuVC
	 */
	public DropDownMenuViewController getDropDownMenuVC() {
		return dropDownMenuVC;
	}

	/**
	 * Returns the ViewController for the statisticmenu.
	 * 
	 * @return the statisticVC
	 */
	public StatisticViewController getStatisticVC() {
		return statisticVC;
	}

	/**
	 * Returns the ViewController for the mainmenu.
	 * 
	 * @return the mainMenuVC
	 */
	public MainMenuViewController getMainMenuVC() {
		return mainMenuVC;
	}

	/**
	 * Returns the ViewController for the settingsmenu.
	 * 
	 * @return the settingsVC
	 */
	public SettingsViewController getSettingsVC() {
		return settingsVC;
	}

	/**
	 * Returns the ViewController for the shop.
	 * 
	 * @return the shopVC
	 */
	public ShopViewController getShopVC() {
		return shopVC;
	}

	/**
	 * Returns the ViewController for the profile selection.
	 * 
	 * @return the profileSelectionVC
	 */
	public ProfileSelection getProfileSelectionVC() {
		return profileSelectionVC;
	}

	/**
	 * Returns the ViewController for the language selection.
	 * 
	 * @return the langEditVC
	 */
	public ProfileEditLang getLangEditVC() {
		return langEditVC;
	}

	/**
	 * Returns the ViewController for the name selection.
	 * 
	 * @return the nameEditVC
	 */
	public ProfileEditName getNameEditVC() {
		return nameEditVC;
	}

	/**
	 * Returns the ViewController for the avatar selection.
	 * 
	 * @return the avatarEditVC
	 */
	public ProfileEditAvatar getAvatarEditVC() {
		return avatarEditVC;
	}

	/**
	 * Returns the ViewController for the level selection.
	 * 
	 * @return the levelSelectionVC
	 */
	public LevelSelectionViewController getLevelSelectionVC() {
		return levelSelectionVC;
	}

	/**
	 * Returns the ViewController for the editor mode.
	 * 
	 * @return the editorVC
	 */
	public EditorViewController getEditorVC() {
		return editorVC;
	}

	/**
	 * Returns the ViewController for the reduction mode.
	 * 
	 * @return the reductionVC
	 */
	public ReductionViewController getReductionVC() {
		return reductionVC;
	}

	/**
	 * Called when the Application is first created.
	 */
	@Override
	public void create() {	
		// Maybe initializeAssets() is better?
		assets = AssetModel.getAssets();
		
		achievementVC = new AchievementMenuViewController();
		achievementVC.setGame(this);
		dropDownMenuVC = new DropDownMenuViewController();
		dropDownMenuVC.setGame(this);
		statisticVC = new StatisticViewController();
		statisticVC.setGame(this);
		mainMenuVC = new MainMenuViewController();
		mainMenuVC.setGame(this);
		settingsVC = new SettingsViewController();
		settingsVC.setGame(this);
		shopVC = new ShopViewController();
		shopVC.setGame(this);
		profileSelectionVC = new ProfileSelection();
		profileSelectionVC.setGame(this);
		langEditVC = new ProfileEditLang();
		langEditVC.setGame(this);
		nameEditVC = new ProfileEditName();
		nameEditVC.setGame(this);
		avatarEditVC = new ProfileEditAvatar();
		avatarEditVC.setGame(this);
		levelSelectionVC = new LevelSelectionViewController();
		levelSelectionVC.setGame(this);
		editorVC = new EditorViewController();
		editorVC.setGame(this);
		reductionVC = new ReductionViewController();
		reductionVC.setGame(this);
		// Will be updated after AssetModel is complete
		setScreen(profileSelectionVC);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void render() {
		super.render();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void resize(int width, int height) {		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void pause() {		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void resume() {
		super.resume();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void dispose() {
		assets.dispose();
	}
	
}
