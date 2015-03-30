package lambda.viewcontroller.statistics;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.assets.AssetManager;
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
 * This class serves as the ViewController of the StatisticModel and screen of
 * the statistic menu.
 * 
 * @author Robert Hochweiss, Farid el-haddad
 */
public class StatisticViewController extends StageViewController implements
        StatisticModelObserver, ProfileModelObserver {

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
    private float textLabelWidth;

    /**
     * Creates a new StatisticViewController.
     */
    public StatisticViewController() {
        statistics = new StatisticModel();
        deltaProcessor = new DeltaStatisticProcessor();
        labelTexts = new HashMap<>();
        buttonSize = getStage().getHeight() / 5;
        space = getStage().getWidth() / 64;
        textLabelWidth = getStage().getWidth() / 2;
    }
    
    @Override
    public void pause() {
       deltaProcessor.gamePaused();
    }
    
    @Override
    public void resume() {
        deltaProcessor.gameResumed();
    }
    
    @Override
    public void queueAssets(AssetManager assets) {
    }

    @Override
    public void create(final AssetManager manager) {
        this.manager = manager;
        ProfileManager.getManager().addObserver(this);
        setLastViewController(SettingsViewController.class);
        getGame().getController(EditorViewController.class).getModel()
                .addObserver(deltaProcessor);
        getGame().getController(ReductionViewController.class).getModel()
                .addObserver(deltaProcessor);
        Skin skin = manager.get(skinPath, Skin.class);
        Image background = new Image(manager.get(
                "data/backgrounds/default.png", Texture.class));
        background.setWidth(getStage().getWidth());
        background.setHeight(getStage().getHeight());
        getStage().addActor(background);

        Table statisticsView = new Table();
        statisticsView.pad(space * 3);
        statisticsView.defaults().spaceBottom(space * 2);
        LabelStyle style = new LabelStyle();
        style.font = skin.getFont("default-font");
        style.fontColor = Color.BLACK;

        Label timePlayedText = new Label("Initial string", style);
        statisticsView.add(timePlayedText).width(textLabelWidth);
        labelTexts.put("timePlayedLabel", timePlayedText);
        timePlayedValue = new Label("Initial string", style);
        statisticsView.add(timePlayedValue).row();

        Label levelCompletedText = new Label("Initial string", style);
        statisticsView.add(levelCompletedText).width(textLabelWidth);
        labelTexts.put("levelCompletedLabel", levelCompletedText);
        levelCompletedValue = new Label("Initial string", style);
        statisticsView.add(levelCompletedValue).row();

        Label difficultiyText = new Label("Initial string", style);
        statisticsView.add(difficultiyText).width(textLabelWidth);
        labelTexts.put("difficultiesLabel", difficultiyText);
        difficultiyValue = new Label("Initial string", style);
        statisticsView.add(difficultiyValue).row();

        Label successfulLevelTriesText = new Label("Initial string", style);
        statisticsView.add(successfulLevelTriesText).width(textLabelWidth);
        labelTexts.put("successfulLevelTriesLabel", successfulLevelTriesText);
        successfulLevelTriesValue = new Label("Initial string", style);
        statisticsView.add(successfulLevelTriesValue).row();

        Label hintsText = new Label("Initial string", style);
        statisticsView.add(hintsText).width(textLabelWidth);
        labelTexts.put("hintsLabel", hintsText);
        hintsValue = new Label("Initial string", style);
        statisticsView.add(hintsValue).row();

        Label gemsEnchantedText = new Label("Initial string", style);
        statisticsView.add(gemsEnchantedText).width(textLabelWidth);
        labelTexts.put("gemsEnchantedLabel", gemsEnchantedText);
        gemsEnchantedValue = new Label("Initial string", style);
        statisticsView.add(gemsEnchantedValue).row();

        Label lambsEnchantedText = new Label("Initial string", style);
        statisticsView.add(lambsEnchantedText).width(textLabelWidth);
        labelTexts.put("lambsEnchantedLabel", lambsEnchantedText);
        lambsEnchantedValue = new Label("Initial string", style);
        statisticsView.add(lambsEnchantedValue).row();

        Label gemsPlacedText = new Label("Initial string", style);
        statisticsView.add(gemsPlacedText).width(textLabelWidth);
        labelTexts.put("gemsPlacedLabel", gemsPlacedText);
        gemsPlacedValue = new Label("Initial string", style);
        statisticsView.add(gemsPlacedValue).row();

        Label lambsPlacedText = new Label("Initial string", style);
        statisticsView.add(lambsPlacedText).width(textLabelWidth);
        labelTexts.put("lambsPlacedLabel", lambsPlacedText);
        lambsPlacedValue = new Label("Initial string", style);
        statisticsView.add(lambsPlacedValue).row();

        Label gemsEnchantedPerLevelText = new Label("Initial string", style);
        statisticsView.add(gemsEnchantedPerLevelText).width(textLabelWidth);
        labelTexts.put("gemsEnchantedPerLevelLabel", gemsEnchantedPerLevelText);
        gemsEnchantedPerLevelValue = new Label("Initial string", style);
        statisticsView.add(gemsEnchantedPerLevelValue).row();

        Label lambsEnchantedPerLevelText = new Label("Initial string", style);
        statisticsView.add(lambsEnchantedPerLevelText).width(textLabelWidth);
        labelTexts.put("lambsEnchantedPerLevelLabel",
                lambsEnchantedPerLevelText);
        lambsEnchantedPerLevelValue = new Label("Initial string", style);
        statisticsView.add(lambsEnchantedPerLevelValue).row();

        Label gemsPlacedPerLevelText = new Label("Initial string", style);
        statisticsView.add(gemsPlacedPerLevelText).width(textLabelWidth);
        labelTexts.put("gemsPlacedPerLevelLabel", gemsPlacedPerLevelText);
        gemsPlacedPerLevelValue = new Label("Initial string", style);
        statisticsView.add(gemsPlacedPerLevelValue).row();

        Label lambsPlacedPerLevelText = new Label("Initial string", style);
        statisticsView.add(lambsPlacedPerLevelText).width(textLabelWidth);
        labelTexts.put("lambsPlacedPerLevelLabel", lambsPlacedPerLevelText);
        lambsPlacedPerLevelValue = new Label("Initial string", style);
        statisticsView.add(lambsPlacedPerLevelValue).row();

        ScrollPane scrollPane = new ScrollPane(statisticsView);
        ImageButton backButton = new ImageButton(skin, "backButton");
        backButton.addListener(new BackClickListener());
        backButton.setBounds(space * 5 / 2, space * 5 / 2, buttonSize,
                buttonSize);
        getStage().addActor(scrollPane);
        getStage().addActor(backButton);
        scrollPane.setFillParent(true);
    }

    @Override
    public void changedProfile() {
        statistics = ProfileManager.getManager().getCurrentProfile()
                .getStatistics();
        statistics.addObserver(this);
        ProfileManager.getManager().getCurrentProfile().addObserver(this);
        I18NBundle language = manager.get(ProfileManager.getManager()
                .getCurrentProfile().getLanguage(), I18NBundle.class);
        updateLabels(language);
    }

    private void updateLabels(I18NBundle language) {
        float smallestScale = Float.POSITIVE_INFINITY;
        // update Label texts
        for (Map.Entry<String, Label> entry : labelTexts.entrySet()) {
            entry.getValue().setText(language.get(entry.getKey()) + ":");
            // finds factor to scale fonts
            float current = textLabelWidth
                    / entry.getValue().getStyle().font.getBounds(entry
                            .getValue().getText()).width;
            if (current < smallestScale) {
                smallestScale = current;
            }
        }
        // scales the fonts of all labels
        for (Map.Entry<String, Label> entry : labelTexts.entrySet()) {
            entry.getValue().setFontScale(smallestScale);
            entry.getValue().setAlignment(Align.center);
        }
        timePlayedValue.setFontScale(smallestScale);
        levelCompletedValue.setFontScale(smallestScale);
        difficultiyValue.setFontScale(smallestScale);
        successfulLevelTriesValue.setFontScale(smallestScale);
        hintsValue.setFontScale(smallestScale);
        gemsEnchantedValue.setFontScale(smallestScale);
        lambsEnchantedValue.setFontScale(smallestScale);
        gemsPlacedValue.setFontScale(smallestScale);
        lambsPlacedValue.setFontScale(smallestScale);
        gemsEnchantedPerLevelValue.setFontScale(smallestScale);
        lambsEnchantedPerLevelValue.setFontScale(smallestScale);
        gemsPlacedPerLevelValue.setFontScale(smallestScale);
        lambsPlacedPerLevelValue.setFontScale(smallestScale);

        // update Label values
        timePlayedValue.setText(statistics.convertTimeToString());
        levelCompletedValue.setText(Integer.toString(ProfileManager
                .getManager().getCurrentProfile().getLevelIndex() - 1));
        int difficultiyCompleted = (ProfileManager.getManager()
                .getCurrentProfile().getLevelIndex() - 1)
                / LevelManager.LEVEL_PER_DIFFICULTY;
        difficultiyValue.setText(Integer.toString(difficultiyCompleted));
        successfulLevelTriesValue.setText(Integer.toString(statistics
                .getSuccessfulLevelTries())
                + " / "
                + Integer.toString(statistics.getLevelTries()));
        hintsValue.setText(Integer.toString(statistics.getHintsNotUsed()));
        gemsEnchantedValue.setText(Integer.toString(statistics
                .getGemsEnchanted()));
        lambsEnchantedValue.setText(Integer.toString(statistics
                .getLambsEnchanted()));
        gemsPlacedValue.setText(Integer.toString(statistics.getGemsPlaced()));
        lambsPlacedValue.setText(Integer.toString(statistics.getLambsPlaced()));
        gemsEnchantedPerLevelValue.setText(Integer.toString(statistics
                .getGemsEnchantedPerLevel()));
        lambsEnchantedPerLevelValue.setText(Integer.toString(statistics
                .getLambsEnchantedPerLevel()));
        gemsPlacedPerLevelValue.setText(Integer.toString(statistics
                .getGemsPlacedPerLevel()));
        lambsPlacedPerLevelValue.setText(Integer.toString(statistics
                .getLambsPlacedPerLevel()));
    }

    private class BackClickListener extends ClickListener {
        @Override
        public void clicked(InputEvent event, float x, float y) {
            AudioManager.playSound("buttonClick");
            getGame().setScreen(SettingsViewController.class);
        }
    }

    @Override
    public void changedLambsEnchanted() {
        lambsEnchantedValue.setText(Integer.toString(statistics
                .getLambsEnchanted()));
    }

    @Override
    public void changedGemsEnchanted() {
        gemsEnchantedValue.setText(Integer.toString(statistics
                .getGemsEnchanted()));
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
        lambsEnchantedPerLevelValue.setText(Integer.toString(statistics
                .getLambsEnchantedPerLevel()));
    }

    @Override
    public void changedGemsEnchantedPerLevel() {
        gemsEnchantedPerLevelValue.setText(Integer.toString(statistics
                .getGemsEnchantedPerLevel()));
    }

    @Override
    public void changedLambsPlacedPerLevel() {
        lambsPlacedPerLevelValue.setText(Integer.toString(statistics
                .getLambsPlacedPerLevel()));
    }

    @Override
    public void changedGemsPlacedPerLevel() {
        gemsPlacedPerLevelValue.setText(Integer.toString(statistics
                .getGemsPlacedPerLevel()));
    }

    @Override
    public void changedLevelIndex() {
        levelCompletedValue.setText(Integer.toString(ProfileManager
                .getManager().getCurrentProfile().getLevelIndex() - 1));
        int difficultiesCompleted = (ProfileManager.getManager()
                .getCurrentProfile().getLevelIndex() - 1)
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
        successfulLevelTriesValue.setText(Integer.toString(statistics
                .getSuccessfulLevelTries())
                + " / "
                + Integer.toString(statistics.getLevelTries()));
    }

    @Override
    public void changedSuccessfulLevelTries() {
        successfulLevelTriesValue.setText(Integer.toString(statistics
                .getSuccessfulLevelTries())
                + " / "
                + Integer.toString(statistics.getLevelTries()));
    }

    @Override
    public void changedCoins() {
    }
}
