package lambda.viewcontroller.editor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
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
import lambda.model.lambdaterm.visitor.IsValidVisitor;
import lambda.model.levels.ElementType;
import lambda.model.levels.LevelContext;
import lambda.model.levels.ReductionStrategy;
import lambda.model.levels.TutorialMessageModel;
import lambda.model.profiles.ProfileManager;
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
public final class EditorViewController extends StageViewController implements EditorModelObserver, LambdaTermObserver, InputProcessor {
    /**
     * The initial offset of the term from the top left corner in percentages of
     * screen size.
     */
    public static final Vector2 INITIAL_TERM_OFFSET = new Vector2(0.2f, 0.2f);
    /**
     * The viewcontroller of the term that is being edited.
     */
    private LambdaTermViewController term;
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
     * Creates a new instance of EditorViewController.
     */
    public EditorViewController() {
        term = null;
        model = new EditorModel();
        background = null;
        toolbarElements = new LinkedList<>();
        isDraggingScreen = false;
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
        main.setDebug(true); // TODO remove

        ImageButton pauseButton = new ImageButton(manager.get(
                "data/skins/MasterSkin.json", Skin.class), "pauseButton");
        ImageButton hintButton = new ImageButton(manager.get(
                "data/skins/MasterSkin.json", Skin.class), "infoButton");
        ImageButton helpButton = new ImageButton(manager.get(
                "data/skins/MasterSkin.json", Skin.class), "helpButton");
        ImageButton targetButton = new ImageButton(manager.get(
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
        bottomToolBar.setBackground(new TextureRegionDrawable(manager.get(
                "data/skins/MasterSkin.atlas", TextureAtlas.class).findRegion(
                        "elements_bar")));

        main.add(leftToolBar).expandY().left().top();
        main.add(targetButton).right().top();
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
                new PauseDialog(dialogSkin, manager.get(ProfileManager
                        .getManager().getCurrentProfile().getLanguage(),
                        I18NBundle.class), getStage().getWidth(), getStage()
                        .getHeight()).show(getStage());
            }
        });
        hintButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                new HintDialog(dialogSkin, EditorViewController.this.model
                        .getLevelContext(), getStage()).show(getStage());
            }
        });
        helpButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                new HelpDialog(dialogSkin, manager.get(ProfileManager
                        .getManager().getCurrentProfile().getLanguage(),
                        I18NBundle.class), getStage().getWidth(), getStage()
                        .getHeight()).show(getStage());
            }
        });
        targetButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                new TargetDialog(dialogSkin, EditorViewController.this.model
                        .getLevelContext(), getStage()).show(getStage());
            }
        });
        reductionStrategyButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                final float height = getStage().getHeight();
                new Dialog("", dialogSkin) {
                    {
                        clear();
                        List<ReductionStrategy> strategies = new ArrayList<>(); // replace
                        // with
                        // getlevelcontext
                        // .getLevelModel().getAvailableRedStrats();
                        strategies.add(ReductionStrategy.APPLICATIVE_ORDER);// test/delete
                        strategies.add(ReductionStrategy.CALL_BY_NAME);// test/delete
                        strategies.add(ReductionStrategy.CALL_BY_VALUE);// test/delete
                        strategies.add(ReductionStrategy.NORMAL_ORDER);// test/delete*/
                        int size = (int) Math.ceil(Math.sqrt(strategies.size()));
                        pad(height / 72 * size);
                        int i = 0;
                        for (final ReductionStrategy strategy : strategies) {
                            if (i++ % size == 0) {
                                row().size(height / 5).space(10);
                            }
                            ImageButton stratButton = new ImageButton(
                                    dialogSkin, strategy.name() + "_Button");
                            stratButton.addListener(new ClickListener() {
                                @Override
                                public void clicked(InputEvent event, float x,
                                        float y) {
                                    model.setStrategy(strategy);
                                    remove();
                                }
                            });
                            add(stratButton);
                        }
                        final Dialog dialog = this;
                        addListener(new ClickListener() {
                            @Override
                            public void clicked(InputEvent event, float x, float y) {
                                if (!(0 < x && 0 < y && x < dialog.getWidth() && y < dialog.getHeight())) {
                                    remove();
                                }
                            }
                        });
                    }
                }.show(getStage());
            }
        });
        finishedButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (model.getTerm().accept(new IsValidVisitor())) {
                    getGame().getController(ReductionViewController.class)
                            .reset(model);
                    getGame().setScreen(ReductionViewController.class);
                }
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

        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(getStage());
        multiplexer.addProcessor(this);
        Gdx.input.setInputProcessor(multiplexer);
        showStartDialogs();
    }

    private void showStartDialogs() {
        List<TutorialMessageModel> tutorialList = model.getLevelContext().getLevelModel().getTutorial();
        AssetManager assets = getGame().getController(AssetViewController.class).getManager();
        final Skin dialogSkin = assets.get("data/skins/DialogTemp.json", Skin.class);
        I18NBundle language = assets.get(ProfileManager.getManager().getCurrentProfile().getLanguage(), I18NBundle.class);
        final float width = getStage().getWidth();
        final float height = getStage().getHeight();
        final Dialog dialogs[] = new Dialog[tutorialList.size() + 1];
        for (int i = 0; i + 1 < dialogs.length; i++) {
            final int pos = i;
            dialogs[pos] = new TutorialMessage(tutorialList.get(i), dialogSkin, language, height, width);
            dialogs[pos].addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    dialogs[pos + 1].show(getStage());
                    dialogs[pos].remove();
                }
            });
        }
        dialogs[dialogs.length - 1] = new TargetDialog(dialogSkin, EditorViewController.this.model.getLevelContext(), getStage());
        dialogs[0].show(getStage());
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

        // Reset editor model
        model.reset(context); // TODO usable elements and reduction strategies

        // Reset lambda term viewcontroller
        if (term != null) {
            term.remove();
        }
        term = LambdaTermViewController.build(model.getTerm(), true, context,
                getStage(), false);
        term.setAssets(getGame().getController(AssetViewController.class)
                .getManager());
        getStage().addActor(term);
        term.toBack();
        term.setPosition(getStage().getWidth() * INITIAL_TERM_OFFSET.x,
                getStage().getHeight() * (1 - INITIAL_TERM_OFFSET.y));

        // Reset background image
        if (background != null) {
            background.remove();
        }
        background = context.getBgImage();
        getStage().addActor(background);
        background.toBack();

        // Reset toolbar elements
        toolbarElements.clear();
        bottomToolBar.clear();
        if (model.getLevelContext().getLevelModel().getUseableElements().contains(ElementType.ABSTRACTION)) {
            LambdaRoot abstraction = new LambdaRoot();
            abstraction.setChild(new LambdaAbstraction(abstraction, Color.WHITE, true));
            toolbarElements.add(LambdaTermViewController.build(abstraction, false, model.getLevelContext(), getStage(), false));
        }
        if (model.getLevelContext().getLevelModel().getUseableElements().contains(ElementType.PARANTHESIS)) {
            LambdaRoot application = new LambdaRoot();
            application.setChild(new LambdaApplication(application, true));
            toolbarElements.add(LambdaTermViewController.build(application, false, model.getLevelContext(), getStage(), true));
        }
        if (model.getLevelContext().getLevelModel().getUseableElements().contains(ElementType.VARIABLE)) {
            LambdaRoot variable = new LambdaRoot();
            variable.setChild(new LambdaVariable(variable, Color.WHITE, true));
            toolbarElements.add(LambdaTermViewController.build(variable, false, model.getLevelContext(), getStage(), false));
        }
        for (LambdaTermViewController toolbarElement : toolbarElements) {
            term.addPermanentDragSource(new LambdaTermDragSource(toolbarElement.getRoot().getChild(0), false, term));
            bottomToolBar.add(toolbarElement).top().left();
        }
        bottomToolBar.row();

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
        // TODO change strategy image
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Keys.BACK) {
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

    private class PauseDialog extends Dialog {
        public PauseDialog(Skin dialogSkin, I18NBundle language,
                float stageWidth, float stageHeight) {
            super("", dialogSkin);
            pad(stageWidth / 64);
            row().space(10);

            ImageButton menuButton = new ImageButton(dialogSkin, "menuButton");
            menuButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    // TODO
                    getGame().setScreen(MainMenuViewController.class);
                    remove();
                }
            });
            add(menuButton).size(stageHeight / 4);

            ImageButton resetButton = new ImageButton(dialogSkin, "resetButton");
            resetButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    EditorViewController.this.reset(model.getLevelContext());
                    showStartDialogs();
                    remove();
                }
            });
            add(resetButton).size(stageHeight / 4);

            ImageButton continueButton = new ImageButton(dialogSkin,
                    "continueButton");
            continueButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    remove();
                }
            });
            add(continueButton).size(stageHeight / 4);
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

}
