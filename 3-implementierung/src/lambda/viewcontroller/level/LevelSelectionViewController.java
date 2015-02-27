package lambda.viewcontroller.level;

import com.badlogic.gdx.assets.AssetManager;

import lambda.model.levels.LevelManager;
import lambda.viewcontroller.StageViewController;


public class LevelSelectionViewController extends StageViewController {

    private LevelManager levelManager;

    /**
     *
     */
	public LevelSelectionViewController() {
        levelManager = LevelManager.getLevelManager();
	}

    @Override
    public void queueAssets(AssetManager assets) {
    	levelManager.queueAssets(assets);
    	/*
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
        */
    }


    @Override
    public void create(final AssetManager manager) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
