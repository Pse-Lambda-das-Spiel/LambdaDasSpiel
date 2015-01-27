package lambda.viewcontroller.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import lambda.model.levels.LevelManager;
import lambda.viewcontroller.ViewController;


public class LevelSelectionViewController extends ViewController {

    private Stage stage;
    private AssetManager assetManager;
    private LevelManager levelManager;

    /**
     *
     */
	public LevelSelectionViewController() {
        stage = new Stage(new ScreenViewport());
        assetManager = new AssetManager();
        levelManager = LevelManager.getLevelManager();
	}

    @Override
    public void queueAssets(AssetManager assets) {
        FileHandle file = Gdx.files.internal("data/levels/music/numberOfMusic.json");
        JsonReader reader = new JsonReader();
        JsonValue jsonFile = reader.parse(file);
        int numberOfMusic = jsonFile.getInt("numberOfMusic");
        for (int i = 0; i < numberOfMusic; i++) {
            assetManager.load("data/levels/music" + String.format("%02d", i) + ".mp3", Music.class);
            levelManager.getDifficultySettings().get(i).setMusic(assetManager.get("data/levels/music" + String.format("%02d", i) + ".mp3"));
            //assetManager.unload("data/levels/music" + String.format("%02d", i) + ".mp3");
        }

        file = Gdx.files.internal("data/difficulties/numberOfImages.json");
        reader = new JsonReader();
        jsonFile = reader.parse(file);
        int numberOfImages = jsonFile.getInt("numberOfImages");
        for (int i = 0; i < numberOfImages; i++) {
            assetManager.load("data/levels/images" + String.format("%02d", i) + ".jpg", Image.class);
            levelManager.getDifficultySettings().get(i).setBgImage(assetManager.get("data/levels/images" + String.format("%02d", i) + ".jpg"));
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
