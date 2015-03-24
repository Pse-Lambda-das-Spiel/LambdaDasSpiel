package lambda.viewcontroller.statistics;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.TextureLoader.TextureParameter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.I18NBundle;

import lambda.model.levels.LevelManager;
import lambda.model.profiles.ProfileManager;
import lambda.model.profiles.ProfileModelObserver;
import lambda.model.statistics.DeltaStatisticProcessor;
import lambda.model.statistics.StatisticModel;
import lambda.model.statistics.StatisticModelObserver;
import lambda.viewcontroller.AudioManager;
import lambda.viewcontroller.StageViewController;
import lambda.viewcontroller.editor.EditorViewController;
import lambda.viewcontroller.reduction.ReductionViewController;
import lambda.viewcontroller.settings.SettingsViewController;

/**
 * This class serves as the ViewController of the StatisticModel and screen of the statistic menu.
 * 
 * @author Robert Hochweiss, Farid el-haddad
 */
public class StatisticViewController extends StageViewController implements StatisticModelObserver,
		ProfileModelObserver {

	private String skinPath = "data/skins/MasterSkin.json";
	private StatisticModel statistics;
	private AssetManager manager;
	private DeltaStatisticProcessor deltaProcessor;

	private Map<String, Label> labelTexts;
	private Label timePlayedValue;
	private Label levelCompletedValue;
	private Label difficultiyValue;
	private Label successfulLevelTriesValue;
	private Label hintsValue;
	private Label gemsEnchantedValue;
	private Label lambsEnchantedValue;
	private Label gemsPlacedValue;
	private Label lambsPlacedValue;
	private Label gemsEnchantedPerLevelValue;
	private Label lambsEnchantedPerLevelValue;
	private Label gemsPlacedPerLevelValue;
	private Label lambsPlacedPerLevelValue;
	private float buttonSize;
	private float space;

	/**
	 * Creates a new StatisticViewController.
	 */
	public StatisticViewController() {
		statistics = new StatisticModel();
		deltaProcessor = new DeltaStatisticProcessor();
		labelTexts = new HashMap<>();
		buttonSize = getStage().getWidth() / 10;
		space = getStage().getHeight() / 36;
	}

	@Override
	public void queueAssets(AssetManager assets) {
		assets.load("data/backgrounds/default.png", Texture.class, new TextureParameter());
	}

	@Override
	public void create(final AssetManager manager) {
		this.manager = manager;
		ProfileManager.getManager().addObserver(this);
		getGame().getController(EditorViewController.class).getModel().addObserver(deltaProcessor);
		getGame().getController(ReductionViewController.class).getModel().addObserver(deltaProcessor);
		Skin skin = manager.get(skinPath, Skin.class);
		Image background = new Image(manager.get("data/backgrounds/default.png", Texture.class));
		background.setWidth(getStage().getWidth());
		background.setHeight(getStage().getHeight());
		getStage().addActor(background);

		Table mainTable = new Table();
		getStage().addActor(mainTable);
		mainTable.setFillParent(true);

		Table statisticsView = new Table();
		LabelStyle style = new LabelStyle();
		style.font = skin.getFont("roboto-font");
		style.fontColor = Color.BLACK;

		Label timePlayedText = new Label("Initial string", style);
		statisticsView.add(timePlayedText);
		labelTexts.put("timePlayedLabel", timePlayedText);
		timePlayedValue = new Label("Initial string", style);
		statisticsView.add(timePlayedValue).row();

		Label levelCompletedText = new Label("Initial string", style);
		statisticsView.add(levelCompletedText);
		labelTexts.put("levelCompletedLabel", levelCompletedText);
		levelCompletedValue = new Label("Initial string", style);
		statisticsView.add(levelCompletedValue).row();

		Label difficultiyText = new Label("Initial string", style);
		statisticsView.add(difficultiyText);
		labelTexts.put("difficultiesLabel", difficultiyText);
		difficultiyValue = new Label("Initial string", style);
		statisticsView.add(difficultiyValue).row();

		Label successfulLevelTriesText = new Label("Initial string", style);
		statisticsView.add(successfulLevelTriesText);
		labelTexts.put("successfulLevelTriesLabel", successfulLevelTriesText);
		successfulLevelTriesValue = new Label("Initial string", style);
		statisticsView.add(successfulLevelTriesValue).row();

		Label hintsText = new Label("Initial string", style);
		statisticsView.add(hintsText);
		labelTexts.put("hintsLabel", hintsText);
		hintsValue = new Label("Initial string", style);
		statisticsView.add(hintsValue).row();

		Label gemsEnchantedText = new Label("Initial string", style);
		statisticsView.add(gemsEnchantedText);
		labelTexts.put("gemsEnchantedLabel", gemsEnchantedText);
		gemsEnchantedValue = new Label("Initial string", style);
		statisticsView.add(gemsEnchantedValue).row();

		Label lambsEnchantedText = new Label("Initial string", style);
		statisticsView.add(lambsEnchantedText);
		labelTexts.put("lambsEnchantedLabel", lambsEnchantedText);
		lambsEnchantedValue = new Label("Initial string", style);
		statisticsView.add(lambsEnchantedValue).row();

		Label gemsPlacedText = new Label("Initial string", style);
		statisticsView.add(gemsPlacedText);
		labelTexts.put("gemsPlacedLabel", gemsPlacedText);
		gemsPlacedValue = new Label("Initial string", style);
		statisticsView.add(gemsPlacedValue).row();

		Label lambsPlacedText = new Label("Initial string", style);
		statisticsView.add(lambsPlacedText);
		labelTexts.put("lambsPlacedLabel", lambsPlacedText);
		lambsPlacedValue = new Label("Initial string", style);
		statisticsView.add(lambsPlacedValue).row();

		Label gemsEnchantedPerLevelText = new Label("Initial string", style);
		statisticsView.add(gemsEnchantedPerLevelText);
		labelTexts.put("gemsEnchantedPerLevelLabel", gemsEnchantedPerLevelText);
		gemsEnchantedPerLevelValue = new Label("Initial string", style);
		statisticsView.add(gemsEnchantedPerLevelValue).row();

		Label lambsEnchantedPerLevelText = new Label("Initial string", style);
		statisticsView.add(lambsEnchantedPerLevelText);
		labelTexts.put("lambsEnchantedPerLevelLabel", lambsEnchantedPerLevelText);
		lambsEnchantedPerLevelValue = new Label("Initial string", style);
		statisticsView.add(lambsEnchantedPerLevelValue).row();

		Label gemsPlacedPerLevelText = new Label("Initial string", style);
		statisticsView.add(gemsPlacedPerLevelText);
		labelTexts.put("gemsPlacedPerLevelLabel", gemsPlacedPerLevelText);
		gemsPlacedPerLevelValue = new Label("Initial string", style);
		statisticsView.add(gemsPlacedPerLevelValue).row();

		Label lambsPlacedPerLevelText = new Label("Initial string", style);
		statisticsView.add(lambsPlacedPerLevelText);
		labelTexts.put("lambsPlacedPerLevelLabel", lambsPlacedPerLevelText);
		lambsPlacedPerLevelValue = new Label("Initial string", style);
		statisticsView.add(lambsPlacedPerLevelValue).row();

		ScrollPane scrollPane = new ScrollPane(statisticsView);
		ImageButton backButton = new ImageButton(skin, "backButton");
		backButton.addListener(new backClickListener());
		mainTable.add(backButton).align(Align.bottomLeft).space(space).size(buttonSize);
		mainTable.add(scrollPane).expand().fill().left();
	}

	@Override
	public void changedProfile() {
		statistics = ProfileManager.getManager().getCurrentProfile().getStatistics();
		statistics.addObserver(this);
		ProfileManager.getManager().getCurrentProfile().addObserver(this);
		I18NBundle language = manager.get(ProfileManager.getManager().getCurrentProfile().getLanguage(),
				I18NBundle.class);
		updateLabels(language);
	}

	private void updateLabels(I18NBundle language) {
		// update Label texts
		for (Map.Entry<String, Label> entry : labelTexts.entrySet()) {
			entry.getValue().setText(language.get(entry.getKey()) + ":");
		}
		// update Label values
		timePlayedValue.setText(statistics.convertTimeToString());
		levelCompletedValue.setText(Integer
				.toString(ProfileManager.getManager().getCurrentProfile().getLevelIndex() - 1));
		int difficultiyCompleted = (ProfileManager.getManager().getCurrentProfile().getLevelIndex() - 1)
				/ LevelManager.LEVEL_PER_DIFFICULTY;
		difficultiyValue.setText(Integer.toString(difficultiyCompleted));
		successfulLevelTriesValue.setText(Integer.toString(statistics.getSuccessfulLevelTries()) + " / "
				+ Integer.toString(statistics.getLevelTries()));
		hintsValue.setText(Integer.toString(statistics.getHintsNotUsed()));
		gemsEnchantedValue.setText(Integer.toString(statistics.getGemsEnchanted()));
		lambsEnchantedValue.setText(Integer.toString(statistics.getLambsEnchanted()));
		gemsPlacedValue.setText(Integer.toString(statistics.getGemsPlaced()));
		lambsPlacedValue.setText(Integer.toString(statistics.getLambsPlaced()));
		gemsEnchantedPerLevelValue.setText(Integer.toString(statistics.getGemsEnchantedPerLevel()));
		lambsEnchantedPerLevelValue.setText(Integer.toString(statistics.getLambsEnchantedPerLevel()));
		gemsPlacedPerLevelValue.setText(Integer.toString(statistics.getGemsPlacedPerLevel()));
		lambsPlacedPerLevelValue.setText(Integer.toString(statistics.getLambsPlacedPerLevel()));
	}

	private class backClickListener extends ClickListener {
		@Override
		public void clicked(InputEvent event, float x, float y) {
			AudioManager.playSound("buttonClick");
			getGame().setScreen(SettingsViewController.class);
		}
	}

	@Override
	public void changedLambsEnchanted() {
		lambsEnchantedValue.setText(Integer.toString(statistics.getLambsEnchanted()));
	}

	@Override
	public void changedGemsEnchanted() {
		gemsEnchantedValue.setText(Integer.toString(statistics.getGemsEnchanted()));
	}

	@Override
	public void changedLambsPlaced() {
		lambsPlacedValue.setText(Integer.toString(statistics.getLambsPlaced()));
	}

	@Override
	public void changedGemsPlaced() {
		gemsPlacedValue.setText(Integer.toString(statistics.getGemsPlaced()));
	}

	@Override
	public void changedLambsEnchantedPerLevel() {
		lambsEnchantedPerLevelValue.setText(Integer.toString(statistics.getLambsEnchantedPerLevel()));
	}

	@Override
	public void changedGemsEnchantedPerLevel() {
		gemsEnchantedPerLevelValue.setText(Integer.toString(statistics.getGemsEnchantedPerLevel()));
	}

	@Override
	public void changedLambsPlacedPerLevel() {
		lambsPlacedPerLevelValue.setText(Integer.toString(statistics.getLambsPlacedPerLevel()));
	}

	@Override
	public void changedGemsPlacedPerLevel() {
		gemsPlacedPerLevelValue.setText(Integer.toString(statistics.getGemsPlacedPerLevel()));
	}

	@Override
	public void changedLevelIndex() {
		levelCompletedValue.setText(Integer
				.toString(ProfileManager.getManager().getCurrentProfile().getLevelIndex() - 1));
		int difficultiesCompleted = (ProfileManager.getManager().getCurrentProfile().getLevelIndex() - 1)
				/ LevelManager.LEVEL_PER_DIFFICULTY;
		difficultiyValue.setText(Integer.toString(difficultiesCompleted));
	}

	@Override
	public void changedHintsNotUsed() {
		hintsValue.setText(Integer.toString(statistics.getHintsNotUsed()));
	}

	@Override
	public void changedTimePlayed() {
		timePlayedValue.setText(statistics.convertTimeToString());

	}

	@Override
	public void changedLevelTries() {
		successfulLevelTriesValue.setText(Integer.toString(statistics.getSuccessfulLevelTries()) + " / "
				+ Integer.toString(statistics.getLevelTries()));
	}

	@Override
	public void changedSuccessfulLevelTries() {
		successfulLevelTriesValue.setText(Integer.toString(statistics.getSuccessfulLevelTries()) + " / "
				+ Integer.toString(statistics.getLevelTries()));
	}

	@Override
	public void changedCoins() {
	}
}
