package lambda.viewcontroller.editor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import lambda.viewcontroller.ViewController;

public class EditorViewController extends ViewController {
    private final Stage stage;
    
    public EditorViewController() {
        stage = new Stage(new ScreenViewport());
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
        
        Table leftToolBar = new Table();
        leftToolBar.add(pauseButton).size(0.15f * stage.getWidth(), 0.15f * stage.getWidth()).top();
        leftToolBar.row();
        leftToolBar.add(hintButton).size(0.15f * stage.getWidth(), 0.15f * stage.getWidth()).top();
        leftToolBar.row();
        leftToolBar.add(helpButton).size(0.15f * stage.getWidth(), 0.15f * stage.getWidth()).top();
        
        Table bottomToolBar = new Table();
        bottomToolBar.setBackground(new TextureRegionDrawable(manager.get("data/skins/levelSkin.pack", TextureAtlas.class).findRegion("bar")));
        
        main.add(leftToolBar).expandY().left().top();
        main.row();
        main.add(bottomToolBar).height(0.15f * stage.getHeight()).expandX().bottom();
        
        pauseButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
            }
        });
    }
    
    @Override
    public void show() {
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
