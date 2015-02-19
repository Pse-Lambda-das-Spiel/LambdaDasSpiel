package lambda.viewcontroller.reduction;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import lambda.model.editormode.EditorModel;
import lambda.model.profiles.ProfileManager;
import lambda.model.reductionmode.ReductionModel;
import lambda.model.reductionmode.ReductionModelObserver;
import lambda.viewcontroller.ViewController;
import lambda.viewcontroller.lambdaterm.LambdaTermViewController;
import lambda.viewcontroller.mainmenu.MainMenuViewController;

/**
 * The viewconroller for the reduction stage of a level.
 * 
 * @author Florian Fervers
 */
public class ReductionViewController extends ViewController implements ReductionModelObserver {
    /**
     * Contains all actors that are displayed in this viewcontroller.
     */
    private final Stage stage;
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
    
    //needed in reductionFinished(...)
    private AssetManager assets;
    
    /**
     * Creates a new instance of ReductionViewController.
     */
    public ReductionViewController() {
        stage = new Stage(new ScreenViewport());
        term = null;
        model = new ReductionModel();
        background = null;
    }

    @Override
    public void queueAssets(AssetManager assets) {
        // TODO master skin
    }
    
    @Override
    public void create(AssetManager manager) {
        //needed in reductionFinished(...)
        this.assets = manager;
        
        model.addObserver(this);
        
        // Set up ui elements
        Table main = new Table();
        stage.addActor(main);
        main.setFillParent(true);
        main.setDebug(true); // TODO remove
        
        ImageButton pauseButton = new ImageButton(manager.get("data/skins/levelSkin.json", Skin.class), "pauseButton");
        ImageButton helpButton = new ImageButton(manager.get("data/skins/levelSkin.json", Skin.class), "questionButton");
        stepRevertButton = new ImageButton(manager.get("data/skins/levelSkin.json", Skin.class), "...");
        stepButton = new ImageButton(manager.get("data/skins/levelSkin.json", Skin.class), "...");
        playPauseButton = new ImageButton(manager.get("data/skins/levelSkin.json", Skin.class), "...");
        ImageButton backToEditorButton = new ImageButton(manager.get("data/skins/levelSkin.json", Skin.class), "...");
        
        // TODO add ui elements to stage
        
        Skin dialogSkin = manager.get("data/skins/DialogTemp.json", Skin.class);
        pauseButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                new PauseDialog(dialogSkin, manager.get(ProfileManager.getManager().getCurrentProfile().getLanguage(),
                        I18NBundle.class)).show(stage);
            }
        });
        helpButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                new HelpDialog(dialogSkin, manager.get(ProfileManager.getManager().getCurrentProfile().getLanguage(),
                        I18NBundle.class), stage.getWidth(), stage.getHeight()).show(stage);
            }
        });
        stepRevertButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                model.stepRevert();
            }
        });
        stepButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                model.step();
            }
        });
        playPauseButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                model.togglePlay();
            }
        });
        backToEditorButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                // TODO
            }
        });
    }
    
    /**
     * Resets this view controller with the given values.
     * 
     * @param editorModel the model of the editor in which the term was created that is to be reduced
     * @throws IllegalArgumentException if editorModel is null
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
        term = LambdaTermViewController.build(model.getTerm(), true, model.getContext());
        stage.addActor(term);
        term.toBack();
        
        // Reset background image
        if (background != null) {
            background.remove();
        }
        background = model.getContext().getBgImage();
        stage.addActor(background);
        background.toBack();
        
        stepRevertButton.setDisabled(true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void show() {
        if (term == null) {
            throw new IllegalStateException("Cannot show the reduction viewController without calling reset before!");
        }
        Gdx.input.setInputProcessor(stage);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
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
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void hide() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void dispose() {
        stage.dispose();
    }

    /**
     * Called when the paused state is changed. Updates the play-pause button image.
     * 
     * @param paused the new paused state
     */
    @Override
    public void pauseChanged(boolean paused) {
        if (paused) {
            // TODO set playPauseButton image -> Play image
        } else {
            // TODO set playPauseButton image -> Pause image
        }
    }

    /**
     * Called when the busy state is changed. Updates whether reduction buttons are enabled.
     * 
     * @param busy the new busy state
     */
    @Override
    public void busyChanged(boolean busy) {
        stepButton.setDisabled(busy);
        playPauseButton.setDisabled(busy);
        stepRevertButton.setDisabled(busy && model.getHistorySize() > 0);
    }

    /**
     * Called when the reduction reached a minimal term or the maximum number of reduction steps. Shows the level-completion dialog.
     * 
     * @param levelComplete true if the final term is alpha equivalent to the level's target term, false otherwise
     */
    @Override
    public void reductionFinished(boolean levelComplete) {
        //add coins to player etc.
        new FinishDialog(levelComplete, /*getCoins*/0, assets.get("data/skins/DialogTemp.json", Skin.class), assets.get(ProfileManager
                .getManager().getCurrentProfile().getLanguage(), I18NBundle.class)).show(stage);
    }
    
    private class PauseDialog extends Dialog {
        public PauseDialog(Skin dialogSkin, I18NBundle language) {
            super("", dialogSkin);
            float width = stage.getWidth()*0.7f;
            float height = stage.getHeight()/5;
            row();
            TextButton continueButton = new TextButton(language.get("continue"), dialogSkin);
            continueButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    remove();
                }
            });
            add(continueButton).width(width).height(height).padTop(25).padLeft(25).padRight(25);
            
            row();
            TextButton resetButton = new TextButton(language.get("reset"), dialogSkin);
            resetButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    //TODO
                    remove();
                }
            });
            add(resetButton).width(width).height(height).pad(10);
            
            row();
            TextButton menuButton = new TextButton(language.get("mainMenu"), dialogSkin);
            menuButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    //TODO
                    getGame().setScreen(MainMenuViewController.class);
                    remove();
                }
            });
            add(menuButton).width(width).height(height).padBottom(35).padLeft(25).padRight(25);
        }
    }
    
    private class FinishDialog extends Dialog {
        public FinishDialog(boolean levelComplete, int coins, Skin dialogSkin, I18NBundle language) {
            super("", dialogSkin);
            float width = stage.getWidth()/2;
            float height = stage.getHeight()/8;
            clear();
            
            Label levelLabel;
            if (levelComplete) {
                levelLabel = new Label(language.get("levelCompleted"), dialogSkin);
            } else {
                levelLabel = new Label(language.get("levelFailed"), dialogSkin);
            }
            levelLabel.setFontScale(0.6f);
            add(levelLabel).pad(25).padBottom(0);
            
            row();
            TextButton restartButton = new TextButton(language.get("restart"), dialogSkin);
            restartButton.getLabel().setFontScale(0.6f);
            restartButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    //TODO reset
                    remove();
                }
            });
            add(restartButton).width(width).height(height).padLeft(25).padRight(25);
            
            row();
            TextButton menuButton = new TextButton(language.get("mainMenu"), dialogSkin);
            menuButton.getLabel().setFontScale(0.6f);
            menuButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    //TODO
                    getGame().setScreen(MainMenuViewController.class);
                    remove();
                }
            });
            if (levelComplete) {
                add(menuButton).width(width).height(height).pad(10);
                row();
                TextButton nextButton = new TextButton(language.get("nextLevel"), dialogSkin);
                nextButton.getLabel().setFontScale(0.6f);
                nextButton.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        // TODO nextlevel
                        remove();
                    }
                });
                add(nextButton).width(width).height(height).pad(10);
                
                row();
                Label coinsLabel = new Label(language.format("coinsGained", coins), dialogSkin);
                coinsLabel.setFontScale(0.6f);
                add(coinsLabel).pad(25).padTop(0);
            } else {
                add(menuButton).width(width).height(height).pad(25).padTop(0);
            }
        }
    }
    
}
