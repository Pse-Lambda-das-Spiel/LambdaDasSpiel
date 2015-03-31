package lambda.viewcontroller.reduction;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.I18NBundle;

import lambda.LambdaGame;
import lambda.model.editormode.EditorModel;
import lambda.model.lambdaterm.LambdaTerm;
import lambda.model.levels.LevelManager;
import lambda.model.levels.LevelModel;
import lambda.model.levels.TutorialMessageModel;
import lambda.model.profiles.ProfileManager;
import lambda.model.profiles.ProfileModel;
import lambda.model.reductionmode.ReductionModel;
import lambda.model.reductionmode.ReductionModelObserver;
import lambda.viewcontroller.AudioManager;
import lambda.viewcontroller.StageViewController;
import lambda.viewcontroller.assets.AssetViewController;
import lambda.viewcontroller.editor.EditorViewController;
import lambda.viewcontroller.lambdaterm.LambdaTermViewController;
import lambda.viewcontroller.level.LevelSelectionViewController;
import lambda.viewcontroller.level.TutorialMessage;
import lambda.viewcontroller.mainmenu.MainMenuViewController;

/**
 * The viewconroller for the reduction getStage() of a level.
 *
 * @author Florian Fervers
 */
public class ReductionViewController extends StageViewController implements
        ReductionModelObserver, InputProcessor {
    /**
     * For debugging outputs.
     */
    public static final boolean DEBUG = false && LambdaGame.DEBUG;
    /**
     * The initial offset of the term from the top left corner in percentages of
     * screen size.
     */
    public static final Vector2 INITIAL_TERM_OFFSET = new Vector2(0.2f, 0.2f);
    /**
     * The viewcontroller of the term that is reduced.
     */
    private LambdaTermViewController term;
    /**
     * The model of the reduction.
     */
    private final ReductionModel model;
    /**
     * Background image of the editor.
     */
    private Image background;
    /**
     * Reverts the last reduction step.
     */
    private ImageButton stepRevertButton;
    /**
     * Performs the next reduction step.
     */
    private ImageButton stepButton;
    /**
     * Toggles the automatic reduction.
     */
    private ImageButton playPauseButton;
    // needed in reductionFinished(...)
    private AssetManager assets;
    /**
     * Last down cursor x position.
     */
    private int lastX = 0;
    /**
     * Last down cursor y position.
     */
    private int lastY = 0;
    /**
     * Indicates whether the screen is currently being dragged.
     */
    private boolean isDraggingScreen;
    
    /**
     * Creates a new instance of ReductionViewController.
     */
    public ReductionViewController() {
        term = null;
        model = new ReductionModel();
        background = null;
    }

    @Override
    public void queueAssets(AssetManager assets) {
    }

    @Override
    public void create(final AssetManager manager) {
        // needed in reductionFinished(...)
        this.assets = manager;
        setLastViewController(EditorViewController.class);
        model.addObserver(this);

        // Set up ui elements
        Table main = new Table();
        getStage().addActor(main);
        main.setFillParent(true);

        ImageButton pauseButton = new ImageButton(manager.get(
                "data/skins/MasterSkin.json", Skin.class), "pauseButton");
        ImageButton helpButton = new ImageButton(manager.get(
                "data/skins/MasterSkin.json", Skin.class), "helpButton");
        stepRevertButton = new ImageButton(manager.get(
                "data/skins/MasterSkin.json", Skin.class), "prevButton");
        stepButton = new ImageButton(manager.get("data/skins/MasterSkin.json",
                Skin.class), "forwardButton");
        playPauseButton = new ImageButton(manager.get(
                "data/skins/MasterSkin.json", Skin.class), "playButton");
        ImageButton backToEditorButton = new ImageButton(manager.get(
                "data/skins/MasterSkin.json", Skin.class), "backButton");

        Table leftToolBar = new Table();
        leftToolBar
                .add(pauseButton)
                .size(0.10f * getStage().getWidth(),
                        0.10f * getStage().getWidth()).top();
        leftToolBar.row();
        leftToolBar
                .add(helpButton)
                .size(0.10f * getStage().getWidth(),
                        0.10f * getStage().getWidth()).top();

        Table bottomToolBar = new Table();
        bottomToolBar.setBackground(new TextureRegionDrawable(manager.get(
                "data/skins/MasterSkin.atlas", TextureAtlas.class).findRegion(
                "elements_bar")));
        bottomToolBar
                .add(stepRevertButton)
                .size(0.10f * getStage().getWidth(),
                        0.10f * getStage().getWidth()).left();
        bottomToolBar
                .add(playPauseButton)
                .size(0.10f * getStage().getWidth(),
                        0.10f * getStage().getWidth()).center();
        bottomToolBar
                .add(stepButton)
                .size(0.10f * getStage().getWidth(),
                        0.10f * getStage().getWidth()).right();

        main.add(leftToolBar).expandY().left().top();
        main.row();
        main.add(bottomToolBar).height(0.25f * getStage().getHeight())
                .expandX().bottom();

        final Skin dialogSkin = manager.get("data/skins/DialogTemp.json",
                Skin.class);
        pauseButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                AudioManager.playSound("buttonClick");
                showDialog(new PauseDialog(dialogSkin, manager.get(ProfileManager
                        .getManager().getCurrentProfile().getLanguage(),
                        I18NBundle.class)));
            }
        });
        helpButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                AudioManager.playSound("buttonClick");
                showDialog(new HelpDialog(dialogSkin, manager.get(ProfileManager
                        .getManager().getCurrentProfile().getLanguage(),
                        I18NBundle.class), getStage()));
            }
        });
        stepRevertButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                AudioManager.playSound("buttonClick");
                if (!stepRevertButton.isDisabled()) { // How come this is even
                                                      // necessary!?
                    model.stepRevert();
                }
            }
        });
        stepButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                AudioManager.playSound("buttonClick");
                if (!stepButton.isDisabled()) {
                    model.step();
                }
            }
        });
        playPauseButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                AudioManager.playSound("buttonClick");
                if (!playPauseButton.isDisabled()) {
                    model.togglePlay();
                }
            }
        });
        backToEditorButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // TODO
            }
        });
    }

    /**
     * Resets this view controller with the given values.
     *
     * @param editorModel
     *            the model of the editor in which the term was created that is
     *            to be reduced
     * @throws IllegalArgumentException
     *             if editorModel is null
     */
    public void reset(EditorModel editorModel) {
        if (editorModel == null) {
            throw new IllegalArgumentException("Editor model cannot be null!");
        }

        // Reset reduction model
        editorModel.update(model);

        // Reset lambda term viewcontroller
        if (term != null) {
            term.remove();
        }
        term = LambdaTermViewController.build(model.getTerm(), true,
                model.getContext(), getStage());
        getStage().addActor(term);
        term.toBack();
        term.setPosition(getStage().getWidth() * INITIAL_TERM_OFFSET.x,
                getStage().getHeight() * (1 - INITIAL_TERM_OFFSET.y));

        // Reset background image
        if (background != null) {
            background.remove();
        }
        background = model.getContext().getBgImage();
        background.setWidth(getStage().getWidth());
        background.setHeight(getStage().getHeight());
        getStage().addActor(background);
        background.toBack();

        stepButton.setDisabled(false);
        playPauseButton.setDisabled(false);
        stepRevertButton.setDisabled(true);
        if (DEBUG) {
            System.out.println("Disabled stepRevert in reset");
            System.out.println("Enabled step in reset");
            System.out.println("Enabled pausePlay in reset");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void show() {
        if (term == null) {
            throw new IllegalStateException(
                    "Cannot show the reduction viewController without calling reset before!");
        }

        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(getStage());
        multiplexer.addProcessor(this);
        Gdx.input.setInputProcessor(multiplexer);
        showStartDialogs();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void hide() {
        model.resetState();
    }

    private void showStartDialogs() {
        List<TutorialMessageModel> realTutorialList = model.getContext()
                .getLevelModel().getTutorial();
        List<TutorialMessageModel> tutorialList = new ArrayList<TutorialMessageModel>();
        for (int i = 0; i < realTutorialList.size(); i++) {
            if (!realTutorialList.get(i).isInEditorModel()) {
                tutorialList.add(realTutorialList.get(i));
            }
        }
        AssetManager assets = getGame()
                .getController(AssetViewController.class).getManager();
        final Skin dialogSkin = assets.get("data/skins/DialogTemp.json",
                Skin.class);
        I18NBundle language = assets.get(ProfileManager.getManager()
                .getCurrentProfile().getLanguage(), I18NBundle.class);
        final float width = getStage().getWidth();
        final float height = getStage().getHeight();
        final Dialog[] dialogs = new Dialog[tutorialList.size()];
        for (int i = 0; i < dialogs.length; i++) {
            final int pos = i;
            dialogs[pos] = new TutorialMessage(tutorialList.get(i), dialogSkin,
                    language, height, width);
            dialogs[pos].addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (pos + 1 < dialogs.length) {
                        showDialog(dialogs[pos + 1]);
                    }
                    dialogs[pos].remove();
                }
            });
        }
        if (dialogs.length > 0) {
            showDialog(dialogs[0]);
        }
    }

    /**
     * Called when the model state changes.
     *
     * @param busy
     *            indicates whether the model is currently performing a
     *            reduction step
     * @param historySize
     *            the history size (>= 0)
     * @param paused
     *            indicates whether the automatic reduction is currently paused
     * @param pauseRequested
     *            indicates whether a pause of the automatic reduction is
     *            requested
     */
    @Override
    public void stateChanged(boolean busy, int historySize, boolean paused,
            boolean pauseRequested) {
        stepButton.setDisabled(busy || !paused);
        playPauseButton.setDisabled(busy && paused || pauseRequested);
        stepRevertButton.setDisabled(busy || !paused || historySize == 0);

        if (paused) {
            playPauseButton.setStyle(assets.get("data/skins/MasterSkin.json",
                    Skin.class).get("playButton", ImageButtonStyle.class));
        } else {
            playPauseButton.setStyle(assets.get("data/skins/MasterSkin.json",
                    Skin.class).get("pauseButton", ImageButtonStyle.class));
        }
    }

    /**
     * Called when the reduction reached a minimal term or the maximum number of
     * reduction steps. Shows the level-completion dialog.
     *
     * @param levelComplete
     *            true if the final term is alpha equivalent to the level's
     *            target term, false otherwise
     */
    @Override
    public void reductionFinished(boolean levelComplete) {
        // add coins to player etc.
        I18NBundle language = assets.get(ProfileManager.getManager()
                .getCurrentProfile().getLanguage());
        showDialog(new FinishDialog(levelComplete, model.getContext().getLevelModel()
                .getCoins(), assets.get("data/skins/DialogTemp.json",
                Skin.class), language, null));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        lastX = screenX;
        lastY = screenY;
        isDraggingScreen = true;
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        isDraggingScreen = false;
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (isDraggingScreen) {
            term.moveBy((screenX - lastX) / 2.0f, (lastY - screenY) / 2.0f);
            lastX = screenX;
            lastY = screenY;
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    @Override
    public void termChanged(LambdaTerm term) {
    }

    /**
     * Returns the model of the reduction.
     * 
     * @return the model of the reduction.
     */
    public ReductionModel getModel() {
        return model;
    }

    private class PauseDialog extends Dialog {
        public PauseDialog(Skin dialogSkin, I18NBundle language) {
            super("", dialogSkin);

            Label mainMenuLabel = new Label(language.get("mainMenu"),
                    dialogSkin);

            ImageButton menuButton = new ImageButton(dialogSkin, "menuButton");
            menuButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    AudioManager.playSound("buttonClick");
                    getGame().setScreen(MainMenuViewController.class);
                    remove();
                }
            });

            Label levelMenuLabel = new Label(language.get("levelMenu"),
                    dialogSkin);

            ImageButton levelMenuButton = new ImageButton(dialogSkin,
                    "levelMenuButton");
            levelMenuButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    AudioManager.playSound("buttonClick");
                    getGame().setScreen(LevelSelectionViewController.class);
                    remove();
                }
            });

            Label resetLabel = new Label(language.get("reset"), dialogSkin);

            ImageButton resetButton = new ImageButton(dialogSkin, "resetButton");
            resetButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    AudioManager.playSound("buttonClick");
                    getGame().getController(LevelSelectionViewController.class)
                            .startLevel(model.getContext().getLevelModel());
                    remove();
                }
            });

            Label continueLabel = new Label(language.get("continue"),
                    dialogSkin);

            ImageButton continueButton = new ImageButton(dialogSkin,
                    "continueButton");
            continueButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    AudioManager.playSound("buttonClick");
                    remove();
                }
            });
            clear();
            float buttonSize = ReductionViewController.this.getStage().getHeight() / 4;
            float labelWidth = buttonSize * 3 / 2;
            float smallestScale = Float.POSITIVE_INFINITY;
            Label labels[] = {
                    mainMenuLabel, continueLabel, levelMenuLabel, resetLabel };
            for (Label label : labels) {
                float current = labelWidth
                        / label.getStyle().font.getBounds(label.getText()).width;
                if (current < smallestScale) {
                    smallestScale = current;
                }
            }
            for (Label label : labels) {
                label.setFontScale(smallestScale);
            }
            pad(buttonSize / 4);
            add(menuButton).size(buttonSize);
            add(mainMenuLabel).width(labelWidth);
            add(continueButton).size(buttonSize);
            add(continueLabel).width(labelWidth);
            row();
            add(levelMenuButton).size(buttonSize);
            add(levelMenuLabel).width(labelWidth);
            add(resetButton).size(buttonSize);
            add(resetLabel).width(labelWidth);
        }
    }

    private class FinishDialog extends Dialog {
        public FinishDialog(boolean levelComplete, int coins, Skin dialogSkin,
                I18NBundle language, String specialMessage) {
            super("", dialogSkin);
            removeLastDialog();
            List<Label> labels = new ArrayList<Label>();
            final LevelModel playedLevel = model.getContext().getLevelModel();
            ProfileModel currentProfile = ProfileManager.getManager()
                    .getCurrentProfile();

            Label levelLabel = new Label(
                    specialMessage != null ? specialMessage
                            : language.get(levelComplete ? "levelCompleted"
                                    : "levelFailed"), dialogSkin);

            Label mainMenuLabel = new Label(language.get("mainMenu"),
                    dialogSkin);
            labels.add(mainMenuLabel);

            ImageButton menuButton = new ImageButton(dialogSkin, "menuButton");
            menuButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    AudioManager.playSound("buttonClick");
                    getGame().setScreen(MainMenuViewController.class);
                    remove();
                }
            });

            Label levelMenuLabel = new Label(language.get("levelMenu"),
                    dialogSkin);
            labels.add(levelMenuLabel);

            ImageButton levelMenuButton = new ImageButton(dialogSkin,
                    "levelMenuButton");
            levelMenuButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    AudioManager.playSound("buttonClick");
                    getGame().setScreen(LevelSelectionViewController.class);
                    remove();
                }
            });

            Label restartLabel = new Label(language.get("restart"), dialogSkin);
            labels.add(restartLabel);

            ImageButton restartButton = new ImageButton(dialogSkin,
                    "restartButton");
            restartButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    AudioManager.playSound("buttonClick");
                    getGame().getController(LevelSelectionViewController.class)
                            .startLevel(playedLevel);
                    remove();
                }
            });

            Label nextLevelLabel = new Label(language.get("nextLevel"),
                    dialogSkin);

            ImageButton nextLevelButton = new ImageButton(dialogSkin,
                    "nextLevelButton");
            nextLevelButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    AudioManager.playSound("buttonClick");
                    LevelManager levelManager = LevelManager.getLevelManager();
                    // if the last level was solved, start with level 1 again
                    if (playedLevel.getId() == levelManager.getNumberOfLevels()) {
                        getGame().getController(
                                LevelSelectionViewController.class).startLevel(
                                levelManager.getLevel(1));
                    } else {
                        getGame().getController(
                                LevelSelectionViewController.class).startLevel(
                                levelManager.getLevel(playedLevel.getId() + 1));
                    }
                    remove();
                }
            });

            Label coinsLabel = new Label(language.format("coinsGained", coins),
                    dialogSkin);

            // if the level is complete and not the sandbox
            if (levelComplete && (playedLevel.getId() != 0)) {
                labels.add(nextLevelLabel);
            }

            float buttonSize = ReductionViewController.this.getStage().getHeight() / 4;
            float labelWidth = buttonSize * 3 / 2;
            float smallestScale = Float.POSITIVE_INFINITY;
            for (Label label : labels) {
                float current = labelWidth
                        / label.getStyle().font.getBounds(label.getText()).width;
                if (current < smallestScale) {
                    smallestScale = current;
                }
            }
            for (Label label : labels) {
                label.setFontScale(smallestScale);
            }

            clear();
            pad(buttonSize / 4);
            // if the level is not the sandbox
            if (playedLevel.getId() != 0 || specialMessage != null) {
                levelLabel.setFontScale(2
                        * labelWidth
                        / levelLabel.getStyle().font.getBounds(levelLabel
                                .getText()).width);
                add(levelLabel).colspan(2).width(2 * labelWidth);
            }
            row();
            Table left = new Table();
            left.add(menuButton).size(buttonSize);
            left.add(mainMenuLabel).width(labelWidth);
            left.row();
            left.add(levelMenuButton).size(buttonSize);
            left.add(levelMenuLabel).width(labelWidth);
            add(left);
            Table right = new Table();
            if (levelComplete && (playedLevel.getId() != 0)) {
                right.add(nextLevelButton).size(buttonSize);
                right.add(nextLevelLabel).width(labelWidth);
                right.row();
            }
            right.add(restartButton).size(buttonSize);
            right.add(restartLabel).width(labelWidth);
            add(right);
            // if the level is not the sandbox
            if (levelComplete && (playedLevel.getId() != 0) && (playedLevel.getId() == currentProfile.getLevelIndex())) {
                row();
                coinsLabel.setFontScale(2
                        * labelWidth
                        / coinsLabel.getStyle().font.getBounds(coinsLabel
                                .getText()).width);
                add(coinsLabel).colspan(2).width(2 * labelWidth);
                // update levelindex and coins only if a new level was
                // solved
                currentProfile
                        .setLevelIndex(currentProfile.getLevelIndex() + 1);
                currentProfile.setCoins(currentProfile.getCoins() + coins);
            }
        }
    }

    @Override
    public void maxNodesReached() {
        I18NBundle language = assets.get(ProfileManager.getManager()
                .getCurrentProfile().getLanguage());
        showDialog(new FinishDialog(false, model.getContext().getLevelModel()
                .getCoins(), assets.get("data/skins/DialogTemp.json",
                Skin.class), language, language.get("maxNodesReached")));
    }

    @Override
    public void maxStepsReached() {
        I18NBundle language = assets.get(ProfileManager.getManager()
                .getCurrentProfile().getLanguage());
        showDialog(new FinishDialog(false, model.getContext().getLevelModel()
                .getCoins(), assets.get("data/skins/DialogTemp.json",
                Skin.class), language, language.get("maxStepsReached")));
    }

}
