package lambda.viewcontroller.reduction;

import com.badlogic.gdx.assets.AssetManager;
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

import lambda.model.editormode.EditorModel;
import lambda.model.profiles.ProfileManager;
import lambda.model.reductionmode.ReductionModel;
import lambda.model.reductionmode.ReductionModelObserver;
import lambda.viewcontroller.StageViewController;
import lambda.viewcontroller.lambdaterm.LambdaTermViewController;
import lambda.viewcontroller.mainmenu.MainMenuViewController;

/**
 * The viewconroller for the reduction getStage() of a level.
 * 
 * @author Florian Fervers
 */
public class ReductionViewController extends StageViewController implements ReductionModelObserver {
   
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
        term = null;
        model = new ReductionModel();
        background = null;
    }

    @Override
    public void queueAssets(AssetManager assets) {
        // TODO master skin
    }
    
    @Override
    public void create(final AssetManager manager) {
        //needed in reductionFinished(...)
        this.assets = manager;
        
        model.addObserver(this);
        
        // Set up ui elements
        Table main = new Table();
        getStage().addActor(main);
        main.setFillParent(true);
        main.setDebug(true); // TODO remove
        
        ImageButton pauseButton = new ImageButton(manager.get("data/skins/levelSkin.json", Skin.class), "pauseButton");
        ImageButton helpButton = new ImageButton(manager.get("data/skins/levelSkin.json", Skin.class), "questionButton");
        stepRevertButton = new ImageButton(manager.get("data/skins/levelSkin.json", Skin.class), "...");
        stepButton = new ImageButton(manager.get("data/skins/levelSkin.json", Skin.class), "...");
        playPauseButton = new ImageButton(manager.get("data/skins/levelSkin.json", Skin.class), "...");
        ImageButton backToEditorButton = new ImageButton(manager.get("data/skins/levelSkin.json", Skin.class), "...");
        
        // TODO add ui elements to getStage()
        
        final Skin dialogSkin = manager.get("data/skins/DialogTemp.json", Skin.class);
        pauseButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                new PauseDialog(dialogSkin, manager.get(ProfileManager.getManager().getCurrentProfile().getLanguage(),
                        I18NBundle.class), getStage().getWidth(), getStage().getHeight()).show(getStage());
            }
        });
        helpButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                new HelpDialog(dialogSkin, manager.get(ProfileManager.getManager().getCurrentProfile().getLanguage(),
                        I18NBundle.class), getStage().getWidth(), getStage().getHeight()).show(getStage());
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
        getStage().addActor(term);
        term.toBack();
        
        // Reset background image
        if (background != null) {
            background.remove();
        }
        background = model.getContext().getBgImage();
        getStage().addActor(background);
        background.toBack();
        
        stepRevertButton.setDisabled(true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void show() {
    	super.show();
        if (term == null) {
            throw new IllegalStateException("Cannot show the reduction viewController without calling reset before!");
        }
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
                .getManager().getCurrentProfile().getLanguage(), I18NBundle.class), getStage().getWidth(), getStage().getHeight()).show(getStage());
    }
    
    private class PauseDialog extends Dialog {
        public PauseDialog(Skin dialogSkin, I18NBundle language, float stageWidth, float stageHeight) {
            super("", dialogSkin);
            pad(stageWidth / 64);
            row().space(10);
            
            ImageButton menuButton = new ImageButton(dialogSkin, "menuButton");
            menuButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    //TODO
                    getGame().setScreen(MainMenuViewController.class);
                    remove();
                }
            });
            add(menuButton).size(stageHeight / 4);
            
            ImageButton resetButton = new ImageButton(dialogSkin, "resetButton");
            resetButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    //TODO
                    remove();
                }
            });
            add(resetButton).size(stageHeight / 4);

            ImageButton continueButton = new ImageButton(dialogSkin, "continueButton");
            continueButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    remove();
                }
            });
            add(continueButton).size(stageHeight / 4);
        }
    }
    
    private class FinishDialog extends Dialog {
        public FinishDialog(boolean levelComplete, int coins, Skin dialogSkin, I18NBundle language, float stageWidth, float stageHeight) {
            super("", dialogSkin);
            clear();
            pad(stageWidth / 64);
            
            Label levelLabel;
            levelLabel = new Label(language.get(levelComplete ? "levelCompleted" : "levelFailed"), dialogSkin);
            levelLabel.setFontScale(0.6f);
            add(levelLabel).colspan(levelComplete ? 3 : 2);
            
            row().space(10);
            ImageButton menuButton = new ImageButton(dialogSkin, "menuButton");
            menuButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    //TODO
                    getGame().setScreen(MainMenuViewController.class);
                    remove();
                }
            });
            add(menuButton).size(stageHeight / 4);
            
            ImageButton restartButton = new ImageButton(dialogSkin, "restartButton");
            restartButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    //TODO reset
                    remove();
                }
            });
            add(restartButton).size(stageHeight / 4);
            
            if (levelComplete) {
                ImageButton nextLevelButton = new ImageButton(dialogSkin, "nextLevelButton");
                nextLevelButton.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        // TODO nextlevel
                        remove();
                    }
                });
                add(nextLevelButton).size(stageHeight / 4);
                
                row();
                Label coinsLabel = new Label(language.format("coinsGained", coins), dialogSkin);
                coinsLabel.setFontScale(0.6f);
                add(coinsLabel).colspan(3);
            }
        }
    }
    
}
