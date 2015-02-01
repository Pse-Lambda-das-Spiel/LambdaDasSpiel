package lambda.viewcontroller.editor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import lambda.model.editormode.EditorModel;
import lambda.model.levels.LevelContext;
import lambda.viewcontroller.ViewController;
import lambda.viewcontroller.lambdaterm.LambdaTermViewController;

/**
 * 
 * @author Florian Fervers
 */
public class EditorViewController extends ViewController {
    private final Stage stage;
    private LambdaTermViewController term;
    private final EditorModel model;
    /**
     * Background image of the editor.
     */
    private Image background;
    
    public EditorViewController() {
        stage = new Stage(new ScreenViewport());
        term = null;
        model = new EditorModel();
        background = null;
    }
    
    @Override
    public void queueAssets(AssetManager manager) {
        manager.load("data/skins/levelSkin.pack", TextureAtlas.class);
        manager.load("data/skins/levelSkin.json", Skin.class, new SkinLoader.SkinParameter("data/skins/levelSkin.pack"));
    }
    
    @Override
    public void create(AssetManager manager) {
        // Set up ui elements
        Table main = new Table();
        stage.addActor(main);
        main.setFillParent(true);
        main.setDebug(true); // TODO remove
        
        ImageButton pauseButton = new ImageButton(manager.get("data/skins/levelSkin.json", Skin.class), "pauseButton");
        ImageButton hintButton = new ImageButton(manager.get("data/skins/levelSkin.json", Skin.class), "hintButton");
        ImageButton helpButton = new ImageButton(manager.get("data/skins/levelSkin.json", Skin.class), "questionButton");
        ImageButton targetButton = new ImageButton(manager.get("data/skins/levelSkin.json", Skin.class), "targetButton");
        
        Table leftToolBar = new Table();
        leftToolBar.add(pauseButton).size(0.10f * stage.getWidth(), 0.10f * stage.getWidth()).top();
        leftToolBar.row();
        leftToolBar.add(hintButton).size(0.10f * stage.getWidth(), 0.10f * stage.getWidth()).top();
        leftToolBar.row();
        leftToolBar.add(helpButton).size(0.10f * stage.getWidth(), 0.10f * stage.getWidth()).top();
        
        Table bottomToolBar = new Table();
        bottomToolBar.setBackground(new TextureRegionDrawable(manager.get("data/skins/levelSkin.pack", TextureAtlas.class).findRegion("bar")));
        
        main.add(leftToolBar).expandY().left().top();
        main.add(targetButton).right().top();
        main.row();
        main.add(bottomToolBar).height(0.15f * stage.getHeight()).expandX().bottom();
        
        pauseButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
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
        stage.addActor(term);
        term.toBack();
        
        // Reset background image
        if (background != null) {
            background.remove();
        }
        background = context.getBgImage();
        stage.addActor(background);
        background.toBack();
    }
    
    @Override
    public void show() {
        if (term == null) {
            throw new IllegalStateException("Cannot show the editor viewController without calling reset before!");
        }
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {		
    }

    @Override
    public void resume() {		
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
