package lambda.viewcontroller.level;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.backends.gwt.preloader.Preloader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import lambda.model.levels.LevelManager;
import lambda.viewcontroller.ViewController;

import java.io.File;

public class LevelSelectionViewController extends ViewController {

    private Stage stage;
    private AssetManager assetManager;
    private LevelManager levelManager;

	public LevelSelectionViewController() {
        stage = new Stage(new ScreenViewport());
        assetManager = new AssetManager();
        levelManager = LevelManager.getLevelManager();
	}

    @Override
    public void queueAssets(AssetManager assets) {
        File f = new File("data/levels/music");
        File[] allFiles = f.listFiles();
        int numberOfFiles = allFiles.length;
        for(int i = 0; i < numberOfFiles; i++) {
            assetManager.load("data/levels/music" + String.format("%02d", i) + ".mp3", Music.class);
            assetManager.finishLoading();
            levelManager.getDifficultySettings().get(i).setMusic(assetManager.get("data/levels/music" + String.format("%02d", i) + ".mp3"));
        }
        f = new File("data/levels/images");
        allFiles = f.listFiles();
        numberOfFiles = allFiles.length;
        for(int i = 0; i < numberOfFiles; i++) {
            assetManager.load("data/levels/images" + String.format("%02d", i) + ".mp3", Image.class);
            assetManager.finishLoading();
            levelManager.getDifficultySettings().get(i).setBgImage(assetManager.get("data/levels/images" + String.format("%02d", i) + ".mp3"));
        }
    }

    @Override
    public void show() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void render(float delta) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void resize(int width, int height) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void pause() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void resume() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void hide() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void dispose() {
        stage.dispose();
        assetManager.dispose();
    }

    @Override
    public void create(AssetManager manager) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
