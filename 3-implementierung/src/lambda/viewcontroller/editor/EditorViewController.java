package lambda.viewcontroller.editor;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.I18NBundle;

import java.util.ArrayList;
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
import lambda.model.levels.LevelContext;
import lambda.model.levels.ReductionStrategy;
import lambda.model.profiles.ProfileManager;
import lambda.viewcontroller.StageViewController;
import lambda.viewcontroller.lambdaterm.LambdaTermViewController;
import lambda.viewcontroller.lambdaterm.draganddrop.LambdaTermDragSource;
import lambda.viewcontroller.mainmenu.MainMenuViewController;
import lambda.viewcontroller.reduction.ReductionViewController;

/**
 * The viewconroller for the editor getStage() of a level.
 * 
 * @author Florian Fervers
 */
public final class EditorViewController extends StageViewController implements EditorModelObserver, LambdaTermObserver {
	
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
    private final LambdaTermViewController[] toolbarElements;
    
    /**
     * Creates a new instance of EditorViewController.
     */
    public EditorViewController() {
        term = null;
        model = new EditorModel();
        background = null;
        toolbarElements = new LambdaTermViewController[3];
    }
    
    @Override
    public void queueAssets(AssetManager manager) {
        // TODO master skin
        manager.load("data/skins/levelSkin.pack", TextureAtlas.class);
        manager.load("data/skins/levelSkin.json", Skin.class, new SkinLoader.SkinParameter("data/skins/levelSkin.pack"));
    }
    
    @Override
    public void create(final AssetManager manager) {
        model.addObserver(this);
        
        // Set up ui elements
        Table main = new Table();
        getStage().addActor(main);
        main.setFillParent(true);
        main.setDebug(true); // TODO remove
        
        // TODO master_skin
        ImageButton pauseButton = new ImageButton(manager.get("data/skins/levelSkin.json", Skin.class), "pauseButton");
        ImageButton hintButton = new ImageButton(manager.get("data/skins/levelSkin.json", Skin.class), "hintButton");
        ImageButton helpButton = new ImageButton(manager.get("data/skins/levelSkin.json", Skin.class), "questionButton");
        ImageButton targetButton = new ImageButton(manager.get("data/skins/levelSkin.json", Skin.class), "targetButton");
        ImageButton reductionStrategyButton = new ImageButton(manager.get("data/skins/levelSkin.json", Skin.class), "....");
        ImageButton finishedButton = new ImageButton(manager.get("data/skins/levelSkin.json", Skin.class), "......");
        
        Table leftToolBar = new Table();
        leftToolBar.add(pauseButton).size(0.10f * getStage().getWidth(), 0.10f * getStage().getWidth()).top();
        leftToolBar.row();
        leftToolBar.add(hintButton).size(0.10f * getStage().getWidth(), 0.10f * getStage().getWidth()).top();
        leftToolBar.row();
        leftToolBar.add(helpButton).size(0.10f * getStage().getWidth(), 0.10f * getStage().getWidth()).top();
        
        Table bottomToolBar = new Table();
        bottomToolBar.setBackground(new TextureRegionDrawable(manager.get("data/skins/levelSkin.pack", TextureAtlas.class).findRegion("bar")));
        
        main.add(leftToolBar).expandY().left().top();
        main.add(targetButton).right().top();
        main.row();
        main.add(bottomToolBar).height(0.15f * getStage().getHeight()).expandX().bottom();
        
        // Tool bar
        LambdaRoot abstraction = new LambdaRoot();
        abstraction.setChild(new LambdaAbstraction(abstraction, Color.WHITE, true));
        toolbarElements[0] = LambdaTermViewController.build(abstraction, false, model.getLevelContext());
        LambdaRoot application = new LambdaRoot();
        application.setChild(new LambdaApplication(application, true));
        toolbarElements[1] = LambdaTermViewController.build(application, false, model.getLevelContext());
        LambdaRoot variable = new LambdaRoot();
        variable.setChild(new LambdaVariable(variable, Color.WHITE, true));
        toolbarElements[2] = LambdaTermViewController.build(variable, false, model.getLevelContext());
        // TODO toolbar background
        
        final Skin dialogSkin = manager.get("data/skins/DialogTemp.json", Skin.class);
        pauseButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                new PauseDialog(dialogSkin, manager.get(ProfileManager.getManager().getCurrentProfile().getLanguage(),
                        I18NBundle.class)).show(getStage());
            }
        });
        hintButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                new HintDialog(dialogSkin, /*get levelcontext*/null, getStage().getWidth(), getStage().getHeight()).show(getStage());
            }
        });
        helpButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                new HelpDialog(dialogSkin, manager.get(ProfileManager.getManager().getCurrentProfile().getLanguage(),
                        I18NBundle.class), getStage().getWidth(), getStage().getHeight()).show(getStage());
            }
        });
        targetButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                new TargetDialog(dialogSkin, /*get levelcontext*/null, getStage().getWidth(), getStage().getHeight()).show(getStage());
            }
        });
        reductionStrategyButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                new Dialog("", dialogSkin) {
                    {
                        clear();
                        List<ReductionStrategy> strategies = new ArrayList<ReductionStrategy>(); //replace with getlevelcontext .getLevelModel().getAvailableRedStrats();
                        strategies.add(ReductionStrategy.APPLICATIVE_ORDER);//test/delete
                        strategies.add(ReductionStrategy.CALL_BY_NAME);//test/delete
                        strategies.add(ReductionStrategy.CALL_BY_VALUE);//test/delete
                        strategies.add(ReductionStrategy.NORMAL_ORDER);//test/delete*/
                        int size = (int) Math.ceil(Math.sqrt(strategies.size()));
                        pad(size * 6);
                        int i = 0;
                        for (final ReductionStrategy strategy: strategies) {
                            if (i++ % size == 0) {
                                row().space(10);
                            }
                            ImageButton stratButton = new ImageButton(dialogSkin, strategy.name() + "_Button");
                            stratButton.addListener(new ClickListener() {
                                @Override
                                public void clicked(InputEvent event, float x, float y) {
                                    model.setStrategy(strategy);
                                    remove();
                                }
                            });
                            add(stratButton);
                        }
                    }
                }.show(getStage());
            }
        });
        finishedButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                if (model.getTerm().accept(new IsValidVisitor())) {
                    getGame().getController(ReductionViewController.class).reset(model);
                    getGame().setScreen(ReductionViewController.class);
                }
            }
        });
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
        model.reset(context);
        
        // Reset lambda term viewcontroller
        if (term != null) {
            term.remove();
        }
        term = LambdaTermViewController.build(context.getLevelModel().getStart(), true, context);
        getStage().addActor(term);
        term.toBack();
        
        // Reset background image
        if (background != null) {
            background.remove();
        }
        background = context.getBgImage();
        getStage().addActor(background);
        background.toBack();
        
        model.getTerm().addObserver(this);
    }
    
    /**
     * Is called when the lambdaterm is changed. Updates drag&drop sources for toolbar elements.
     * 
     * @param oldTerm the old term to be replaced
     * @param newTerm the new replacing term
     */
    @Override
    public void replaceTerm(LambdaTerm oldTerm, LambdaTerm newTerm) {
        // Add drag&drop sources for toolbar elements
        for (LambdaTermViewController toolbarElement : toolbarElements) {
            term.getDragAndDrop().addSource(new LambdaTermDragSource(toolbarElement.getRoot().getChild(0), false));
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void show() {
    	super.show();
        if (term == null) {
            throw new IllegalStateException("Cannot show the editor viewController without calling reset before!");
        }
    }

    /**
     * Called when the a new reduction strategy is selected. Updates the strategy button image.
     * 
     * @param strategy the new strategy
     */
    @Override
    public void strategyChanged(ReductionStrategy strategy) {
        // TODO change strategy image
    }
    
    private class PauseDialog extends Dialog {
        public PauseDialog(Skin dialogSkin, I18NBundle language) {
            super("", dialogSkin);
            float width = getStage().getWidth()*0.7f;
            float height = getStage().getHeight()/5;
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
                    //TODO reset Level
                    remove();
                }
            });
            add(resetButton).width(width).height(height).pad(10);
            
            row();
            TextButton menuButton = new TextButton(language.get("mainMenu"), dialogSkin);
            menuButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    //TODO ?
                    getGame().setScreen(MainMenuViewController.class);
                    remove();
                }
            });
            add(menuButton).width(width).height(height).padBottom(35).padLeft(25).padRight(25);
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
}
