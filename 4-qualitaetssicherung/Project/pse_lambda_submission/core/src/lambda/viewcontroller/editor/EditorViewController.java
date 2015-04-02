package lambda.viewcontroller.editor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.I18NBundle;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import lambda.model.editormode.EditorModel;
import lambda.model.editormode.EditorModelObserver;
import lambda.model.lambdaterm.LambdaAbstraction;
import lambda.model.lambdaterm.LambdaApplication;
import lambda.model.lambdaterm.LambdaRoot;
import lambda.model.lambdaterm.LambdaTerm;
import lambda.model.lambdaterm.LambdaTermObserver;
import lambda.model.lambdaterm.LambdaValue;
import lambda.model.lambdaterm.LambdaVariable;
import lambda.model.lambdaterm.visitor.ColorCollectionVisitor;
import lambda.model.lambdaterm.visitor.IsValidVisitor;
import lambda.model.levels.ElementType;
import lambda.model.levels.LevelContext;
import lambda.model.levels.ReductionStrategy;
import lambda.model.levels.TutorialMessageModel;
import lambda.model.profiles.ProfileManager;
import lambda.viewcontroller.AudioManager;
import lambda.viewcontroller.StageViewController;
import lambda.viewcontroller.assets.AssetViewController;
import lambda.viewcontroller.lambdaterm.LambdaTermViewController;
import lambda.viewcontroller.lambdaterm.draganddrop.LambdaTermDragSource;
import lambda.viewcontroller.level.LevelSelectionViewController;
import lambda.viewcontroller.level.TutorialMessage;
import lambda.viewcontroller.mainmenu.MainMenuViewController;
import lambda.viewcontroller.reduction.ReductionViewController;

/**
 * The viewconroller for the editor getStage() of a level.
 *
 * @author Florian Fervers
 */
public final class EditorViewController extends StageViewController implements
        EditorModelObserver, LambdaTermObserver, InputProcessor {
    /**
     * The initial offset of the term from the top left corner in percentages of
     * screen size.
     */
    public static final Vector2 INITIAL_TERM_OFFSET = new Vector2(0.2f, 0.2f);
    /**
     * The viewcontroller of the term that is being edited.
     */
    private static LambdaTermViewController term;
    /**
     * The model of the editor.
     */
    private final EditorModel model;
    /**
     * Background image of the editor.
     */
    private Image background;
    /**
     * The toolbar elements: Abstraction, parenthesis, variable
     */
    private final List<LambdaTermViewController> toolbarElements;
    /**
     * The toolbar ui element containing placable elements.
     */
    private Table bottomToolBar;
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
     * Displays a hint on click.
     */
    private ImageButton hintButton;
    /**
     * Displays the level goal on click.
     */
    private ImageButton targetButton;
    
    /**
     * Indicates whether the finish button was presed.
     */
    private boolean finishButtonPressed;
    
    /**
     * Creates a new instance of EditorViewController.
     */
    public EditorViewController() {
        term = null;
        model = new EditorModel();
        background = null;
        toolbarElements = new LinkedList<>();
        isDraggingScreen = false;
        finishButtonPressed = false;
    }

    @Override
    public void queueAssets(AssetManager manager) {
        manager.load("data/skins/MasterSkin.atlas", TextureAtlas.class);
        manager.load("data/skins/MasterSkin.json", Skin.class,
                new SkinLoader.SkinParameter("data/skins/MasterSkin.atlas"));
    }

    @Override
    public void create(final AssetManager manager) {
        model.addObserver(this);

        // Set up ui elements
        Table main = new Table();
        getStage().addActor(main);
        main.setFillParent(true);

        ImageButton pauseButton = new ImageButton(manager.get(
                "data/skins/MasterSkin.json", Skin.class), "pauseButton");
        hintButton = new ImageButton(manager.get("data/skins/MasterSkin.json",
                Skin.class), "infoButton");
        ImageButton helpButton = new ImageButton(manager.get(
                "data/skins/MasterSkin.json", Skin.class), "helpButton");
        targetButton = new ImageButton(manager.get(
                "data/skins/MasterSkin.json", Skin.class), "goal");
        ImageButton reductionStrategyButton = new ImageButton(manager.get(
                "data/skins/MasterSkin.json", Skin.class), "strategy");
        ImageButton finishedButton = new ImageButton(manager.get(
                "data/skins/MasterSkin.json", Skin.class), "playButton");

        Table leftToolBar = new Table();
        leftToolBar
                .add(pauseButton)
                .size(0.10f * getStage().getWidth(),
                        0.10f * getStage().getWidth()).top();
        leftToolBar.row();
        leftToolBar
                .add(hintButton)
                .size(0.10f * getStage().getWidth(),
                        0.10f * getStage().getWidth()).top();
        leftToolBar.row();
        leftToolBar
                .add(helpButton)
                .size(0.10f * getStage().getWidth(),
                        0.10f * getStage().getWidth()).top();

        bottomToolBar = new Table();
        TextureRegionDrawable bar = new TextureRegionDrawable(manager.get(
                "data/skins/MasterSkin.atlas", TextureAtlas.class).findRegion(
                        "elements_bar"));
        bar.setMinWidth(getStage().getHeight() / 720 * bar.getMinWidth());
        bar.setMinHeight(getStage().getHeight() / 720 * bar.getMinHeight());
        bottomToolBar.setBackground(bar);

        main.add(leftToolBar).expandY().left().top();
        main.add(targetButton)
                .size(0.10f * getStage().getWidth(),
                        0.10f * getStage().getWidth()).right().top();
        main.row();
        main.add(bottomToolBar).height(0.25f * getStage().getHeight())
                .expandX().bottom();
        main.add(reductionStrategyButton).size(0.10f * getStage().getWidth(),
                0.10f * getStage().getWidth());
        main.add(finishedButton).size(0.10f * getStage().getWidth(),
                0.10f * getStage().getWidth());

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
        hintButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                AudioManager.playSound("buttonClick");
                EditorViewController.this.model.hintIsUsed();
                showDialog(new HintDialog(dialogSkin, manager.get(ProfileManager
                        .getManager().getCurrentProfile().getLanguage(),
                        I18NBundle.class), EditorViewController.this.model
                        .getLevelContext(), getStage()));
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
        targetButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                AudioManager.playSound("buttonClick");
                showDialog(new TargetDialog(dialogSkin, manager.get(ProfileManager
                        .getManager().getCurrentProfile().getLanguage(),
                        I18NBundle.class), EditorViewController.this.model
                        .getLevelContext(), getStage()));
            }
        });
        reductionStrategyButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                AudioManager.playSound("buttonClick");
                final float buttonSize = getStage().getHeight() / 5;
                final float labelWidth = getStage().getWidth() / 2;
                showDialog(new Dialog("", dialogSkin) {
                    {
                        EditorViewController.disableDragAndDrop();
                        I18NBundle language = manager.get(
                                ProfileManager.getManager().getCurrentProfile()
                                .getLanguage(), I18NBundle.class);
                        clear();
                        final List<ReductionStrategy> strategies = EditorViewController.this.model
                                .getLevelContext().getLevelModel()
                                .getAvailableRedStrats();
                        pad((buttonSize + labelWidth) / 20);
                        Label labels[] = new Label[strategies.size()];
                        for (int n = 0; n < strategies.size(); n++) {
                            ImageButton stratButton = new ImageButton(
                                    dialogSkin, strategies.get(n).name()
                                    + "_Button");
                            final int t = n;
                            stratButton.addListener(new ClickListener() {
                                @Override
                                public void clicked(InputEvent event, float x,
                                        float y) {
                                    AudioManager.playSound("buttonClick");
                                    model.setStrategy(strategies.get(t));
                                    EditorViewController.enableDragAndDrop();
                                    remove();
                                }
                            });
                            add(stratButton).size(buttonSize);
                            labels[n] = new Label(language.get(strategies
                                    .get(n).name()), dialogSkin);
                            add(labels[n]).width(labelWidth);
                            row();
                        }
                        BitmapFont font = new Label("", dialogSkin).getStyle().font;
                        float smallestScale = Float.POSITIVE_INFINITY;
                        for (ReductionStrategy strat: ReductionStrategy.values()) {
                            float current = 2 * labelWidth
                                    / font.getBounds(language.get(strat.name())).width;
                            if (current < smallestScale) {
                                smallestScale = current;
                            }
                        }
                        for (Label label : labels) {
                            label.setWrap(true);
                            label.setFontScale(smallestScale);
                            label.setAlignment(Align.center);
                        }
                        final Dialog dialog = this;
                        addListener(new ClickListener() {
                            @Override
                            public void clicked(InputEvent event, float x,
                                    float y) {
                                if (!(0 < x && 0 < y && x < dialog.getWidth() && y < dialog
                                        .getHeight())) {
                                    EditorViewController.enableDragAndDrop();
                                    remove();
                                }
                            }
                        });
                    }
                });
            }
        });
        finishedButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                AudioManager.playSound("buttonClick");
                finishButtonPressed = true;
                I18NBundle language = manager.get(ProfileManager.getManager()
                        .getCurrentProfile().getLanguage(), I18NBundle.class);
                if (model.getTerm().getChild() == null) {
                    showHelpDialog(language.get("invalidTermEmpty"));
                } else if (!model.getTerm().accept(new IsValidVisitor())) {
                    showHelpDialog(language.get("invalidTermOther"));
                } else if (model
                        .getTerm()
                        .accept(new ColorCollectionVisitor(
                                        ColorCollectionVisitor.TYPE_ALL))
                        .contains(Color.WHITE)) {
                    showHelpDialog(language.get("invalidTermWhite"));
                } else {
                    getGame().getController(ReductionViewController.class)
                            .reset(model);
                    getGame().setScreen(ReductionViewController.class);
                }
            }

            private void showHelpDialog(final String message) {
                showDialog(new Dialog("", dialogSkin) {
                    {
                        EditorViewController.disableDragAndDrop();
                        clear();
                        pad(EditorViewController.this.getStage().getHeight() / 20);
                        Label errorLabel = new Label(message, dialogSkin);
                        errorLabel.setFontScale(EditorViewController.this.getStage().getHeight() / 720 * 0.7f);
                        errorLabel.setWrap(true);
                        add(errorLabel)
                                .width(EditorViewController.this.getStage()
                                        .getWidth() / 2);
                        addListener(new ClickListener() {
                            @Override
                            public void clicked(InputEvent event, float x,
                                    float y) {
                                EditorViewController.enableDragAndDrop();
                                remove();
                            }
                        });
                    }
                });
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void show() {
        if (term == null) {
            throw new IllegalStateException(
                    "Cannot show the editor viewController without calling reset before!");
        }
        AudioManager.playMusic(model.getLevelContext().getMusic());
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(term.getDragAndDrop());
        multiplexer.addProcessor(getStage());
        multiplexer.addProcessor(this);
        Gdx.input.setInputProcessor(multiplexer);
        model.levelIsStarted();
        showStartDialogs();
    }
    
    @Override
    public void hide() {
        model.leaveLevel(!finishButtonPressed);
    }

    private void showStartDialogs() {
        List<TutorialMessageModel> realTutorialList = model.getLevelContext()
                .getLevelModel().getTutorial();
        List<TutorialMessageModel> tutorialList = new ArrayList<TutorialMessageModel>();
        for (int i = 0; i < realTutorialList.size(); i++) {
            if (realTutorialList.get(i).isInEditorModel()) {
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
        final Dialog[] dialogs = new Dialog[tutorialList.size()
                + (targetButton.isVisible() ? 1 : 0)];
        for (int i = 0; i + (targetButton.isVisible() ? 1 : 0) < dialogs.length; i++) {
            final int pos = i;
            dialogs[pos] = new TutorialMessage(tutorialList.get(i), dialogSkin,
                    language, height, width);
            dialogs[pos].addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (pos + 1 < dialogs.length) {
                        showDialog(dialogs[pos + 1]);
                    } else {
                        EditorViewController.enableDragAndDrop();
                    }
                    dialogs[pos].remove();
                }
            });
        }
        if (targetButton.isVisible()) {
            dialogs[dialogs.length - 1] = new TargetDialog(dialogSkin,
                    language,
                    EditorViewController.this.model.getLevelContext(),
                    getStage());
        }
        if (dialogs.length > 0) {
            EditorViewController.disableDragAndDrop();
            showDialog(dialogs[0]);
        }
    }

    /**
     * Disables drag&drop in the EditorViewController.
     * 
     * @throws IllegalStateException if the editable term of the EditorViewController or its drag&drop handler is null
     */
    public static void disableDragAndDrop() {
        if (term == null) {
            throw new IllegalStateException("reset hast to be called before accessing term!");
        } 
        if (term.getDragAndDrop() == null) {
            throw new IllegalStateException("reset hast to be called before accessing the drag&drop of term!");
        } 
        term.getDragAndDrop().setEnabled(false);
    }
    
    /**
     * Enables drag&drop in the EditorViewController.
     * 
     * @throws IllegalStateException if the editable term of the EditorViewController or its drag&drop handler is null
     */
    public static void enableDragAndDrop() {
        if (term == null) {
            throw new IllegalStateException("reset hast to be called before accessing term!");
        } 
        if (term.getDragAndDrop() == null) {
            throw new IllegalStateException("reset hast to be called before accessing the drag&drop handler of term!");
        } 
        term.getDragAndDrop().setEnabled(true);
    }
    
    /**
     * Resets this view controller with the given values.
     *
     * @param context the current level context
     * @throws IllegalArgumentException if context is null
     */
    public void reset(LevelContext context) {
        if (context == null) {
            throw new IllegalArgumentException("Level context cannot be null!");
        }

        finishButtonPressed = false;
        
        // Reset editor model
        model.reset(context);

        // Reset lambda term viewcontroller
        if (term != null) {
            term.remove();
        }
        term = LambdaTermViewController.build(model.getTerm(), true, context,
                getStage());
        term.setStageVC(this);
        getStage().addActor(term);
        term.toBack();
        term.setPosition(getStage().getWidth() * INITIAL_TERM_OFFSET.x,
                getStage().getHeight() * (1 - INITIAL_TERM_OFFSET.y));

        // Reset background image
        if (background != null) {
            background.remove();
        }
        background = context.getBgImage();
        background.setWidth(getStage().getWidth());
        background.setHeight(getStage().getHeight());
        getStage().addActor(background);
        background.toBack();

        // Reset toolbar elements
        toolbarElements.clear();
        bottomToolBar.clear();
        if (model.getLevelContext().getLevelModel().getUseableElements()
                .contains(ElementType.VARIABLE)) {
            LambdaRoot variable = new LambdaRoot();
            variable.setChild(new LambdaVariable(variable, Color.WHITE, true));
            toolbarElements.add(LambdaTermViewController.build(variable, false,
                    model.getLevelContext(), getStage()));
        }
        if (model.getLevelContext().getLevelModel().getUseableElements()
                .contains(ElementType.ABSTRACTION)) {
            LambdaRoot abstraction = new LambdaRoot();
            abstraction.setChild(new LambdaAbstraction(abstraction,
                    Color.WHITE, true));
            toolbarElements.add(LambdaTermViewController.build(abstraction,
                    false, model.getLevelContext(), getStage()));
        }
        if (model.getLevelContext().getLevelModel().getUseableElements()
                .contains(ElementType.PARENTHESIS)) {
            LambdaRoot application = new LambdaRoot();
            application
                    .setChild(new LambdaApplication(application, true, true));
            toolbarElements.add(LambdaTermViewController.build(application,
                    false, model.getLevelContext(), getStage()));
        }
        for (LambdaTermViewController toolbarElement : toolbarElements) {
            toolbarElement.addOffset(0.0f, toolbarElement.getHeight() / 4);
            term.getDragAndDrop().addDragSource(
                    new LambdaTermDragSource(toolbarElement.getRoot().getChild(
                                    0), false, true));
            bottomToolBar.add(toolbarElement).left();
        }
        bottomToolBar.row();

        // Reset button visibilities
        hintButton.setVisible(model.getLevelContext().getLevelModel().getHint()
                .getChild() != null);
        targetButton.setVisible((model.getLevelContext().getLevelModel()
                .getGoal().getChild() != null) || !model.getLevelContext().getLevelModel().isStandardMode());

        model.getTerm().addObserver(this);
    }

    /**
     * Called when the a new reduction strategy is selected. Updates the
     * strategy button image.
     *
     * @param strategy the new strategy
     */
    @Override
    public void strategyChanged(ReductionStrategy strategy) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Keys.BACK) {
            removeLastDialog();
            getGame().setScreen(LevelSelectionViewController.class);
        }
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
            if (model.getTerm().getChild() == null) {
                // Reset screen position if no element is present
                term.setPosition(getStage().getWidth() * INITIAL_TERM_OFFSET.x,
                        getStage().getHeight() * (1 - INITIAL_TERM_OFFSET.y));
            } else {
                // Drag screen
                term.moveBy((screenX - lastX) / 2.0f, (lastY - screenY) / 2.0f);
            }
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
    public void alphaConversionFinished() {
    }

    @Override
    public void removingApplicant(LambdaTerm applicant) {
    }

    @Override
    public void termChanged(LambdaTerm term) {
    }

    private class PauseDialog extends Dialog {
        public PauseDialog(Skin dialogSkin, I18NBundle language) {
            super("", dialogSkin);
            EditorViewController.disableDragAndDrop();
            Label mainMenuLabel = new Label(language.get("mainMenu"),
                    dialogSkin);

            ImageButton menuButton = new ImageButton(dialogSkin, "menuButton");
            menuButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    AudioManager.playSound("buttonClick");
                    EditorViewController.enableDragAndDrop();
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
                    EditorViewController.enableDragAndDrop();
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
                    EditorViewController.enableDragAndDrop();
                    EditorViewController.this.reset(model.getLevelContext());
                    EditorViewController.this.show();
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
                    EditorViewController.enableDragAndDrop();
                    remove();
                }
            });

            clear();
            float buttonSize = EditorViewController.this.getStage().getHeight() / 4;
            float labelWidth = buttonSize * 3 / 2;
            float smallestScale = Float.POSITIVE_INFINITY;
            Label labels[] = {
                mainMenuLabel, continueLabel, levelMenuLabel, resetLabel};
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

    @Override
    public void setColor(LambdaValue term, Color color) {
    }

    @Override
    public void alphaConverted(LambdaValue term, Color color) {
    }

    @Override
    public void applicationStarted(LambdaAbstraction abstraction,
            LambdaTerm applicant) {
    }

    @Override
    public void variableReplaced(LambdaVariable variable, LambdaTerm replacing) {
    }

    @Override
    public void replaceTerm(LambdaTerm oldTerm, LambdaTerm newTerm) {
    }

    /**
     * Returns the model of the editor.
     *
     * @return the model of the editor
     */
    public EditorModel getModel() {
        return model;
    }

    @Override
    public void levelStarted(int levelId) {
    }

    @Override
    public void hintUsed() {
    }

    @Override
    public void levelLeft(boolean canSave) {
    }

}
