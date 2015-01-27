package lambda.model.shop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.backends.gwt.preloader.Preloader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import lambda.util.InvalidJsonException;
import lambda.viewcontroller.level.AbstractionUIContext;
import lambda.viewcontroller.level.ParanthesisUIContext;
import lambda.viewcontroller.level.VariableUIContext;


/**
 * Represents
 *
 * @author Kay Schmitteckert
 */
public class ShopModel {

    private static ShopModel shop;
    private AssetManager assetManager;
    private ShopItemTypeModel<MusicItemModel> music;
    private ShopItemTypeModel<BackgroundImageItemModel> images;
    private ShopItemTypeModel<ElementUIContextFamily> elementUIContextFamilies;

    /**
     *
     */
    private ShopModel() {
        assetManager = new AssetManager();
        queueAssets(assetManager);
        music = new ShopItemTypeModel<MusicItemModel>("music");
        images = new ShopItemTypeModel<BackgroundImageItemModel>("images");
        elementUIContextFamilies = new ShopItemTypeModel<ElementUIContextFamily>("sprites");

    }

    /**
     *
     * @return
     */
    public static ShopModel getShop() {
        if (shop == null) {
            shop = new ShopModel();
        }
        return shop;
    }

    /**
     *
     * @return
     */
    public ShopItemTypeModel<MusicItemModel> getMusic() {
        return music;
    }

    /**
     *
     * @return
     */
    public ShopItemTypeModel<BackgroundImageItemModel> getImages() {
        return images;
    }

    /**
     *
     * @return
     */
    public ShopItemTypeModel<ElementUIContextFamily> getElementUIContextFamilies() {
        return elementUIContextFamilies;
    }

    public void loadAllItems() {
        FileHandle file = Gdx.files.internal("data/itmes/items.json");
        JsonReader reader = new JsonReader();
        JsonValue jsonFile = reader.parse(file);
        JsonValue items = jsonFile.child();

        int numberOfMusicItems = items.getInt("musicItems");
        int numberOfBackgroundImageItems = items.getInt("imageItems");
        int numberOfElementUIContextFamilies = items.getInt("elementUIContextFamilies");

        for (int i = 0; i < numberOfMusicItems; i++) {
            music.getItems().add(i, loadMusicItem(Integer.toString(i)));
        }

        for (int i = 0; i < numberOfBackgroundImageItems; i++) {
            images.getItems().add(i, loadBackgroundImageItem(Integer.toString(i)));
        }

        for (int i = 0; i < numberOfElementUIContextFamilies; i++) {
            elementUIContextFamilies.getItems().add(i, loadElementUIContextFamily(Integer.toString(i)));
        }

    }

    public MusicItemModel loadMusicItem(String id) {
        FileHandle file = Gdx.files.internal("data/itmes/music" + String.format("%02d", id) + ".json");
        JsonReader reader = new JsonReader();
        JsonValue jsonFile = reader.parse(file);
        JsonValue music = jsonFile.child();

        if (!(music.getString("musicId").equalsIgnoreCase(id))) {
            throw new InvalidJsonException("The id of the json file does not match with its file name!");
        }
        int price = music.getInt("price");
        String filepath = music.getString("filepath");
        return new MusicItemModel(id, price, filepath);
    }

    public BackgroundImageItemModel loadBackgroundImageItem(String id) {
        FileHandle file = Gdx.files.internal("data/itmes/backgroundimages" + String.format("%02d", id) + ".json");
        JsonReader reader = new JsonReader();
        JsonValue jsonFile = reader.parse(file);
        JsonValue image = jsonFile.child();

        if (!(image.getString("imageId").equalsIgnoreCase(id))) {
            throw new InvalidJsonException("The id of the json file does not match with its file name!");
        }
        int price = image.getInt("price");
        String filepath = image.getString("filepath");
        return new BackgroundImageItemModel(id, price, filepath);
    }

    public ElementUIContextFamily loadElementUIContextFamily(String id) {
        FileHandle file = Gdx.files.internal("data/itmes/elementuis" + String.format("%02d", id) + ".json");
        JsonReader reader = new JsonReader();
        JsonValue jsonFile = reader.parse(file);
        JsonValue family = jsonFile.child();

        if (!(family.getString("familyId").equalsIgnoreCase(id))) {
            throw new InvalidJsonException("The id of the json file does not match with its file name!");
        }
        int price = family.getInt("price");
        String paranthesisPath = family.getString("filepath");
        String variablePath = family.getString("variablePath");
        String abstractionPath = family.getString("abstractionPath");
        return new ElementUIContextFamily(id, price, null, paranthesisPath, variablePath, abstractionPath);
    }

    public void queueAssets(AssetManager assets) {
        int numberOfMusic = shop.getMusic().getItems().size();
        for (int i = 0; i < numberOfMusic; i++) {
            String musicpath = shop.getMusic().getItems().get(i).getFilepath();
            assetManager.load(musicpath, Music.class);
            shop.getMusic().getItems().get(i).setMusic(assetManager.get(musicpath));
            //assetManager.unload("data/levels/music" + String.format("%02d", i) + ".mp3");
        }
        int numberOfImages = shop.getImages().getItems().size();
        for (int i = 0; i < numberOfImages; i++) {
            String imagepath = shop.getImages().getItems().get(i).getFilepath();
            assetManager.load(imagepath, Image.class);
            shop.getImages().getItems().get(i).setImage(assetManager.get(imagepath));
            //assetManager.unload("data/levels/music" + String.format("%02d", i) + ".mp3");
        }
        // TODO: CHECK ANIMATION
        int numberOfFamilies = shop.getElementUIContextFamilies().getItems().size();
        for (int i = 0; i < numberOfFamilies; i++) {
            String paranthesisPath = shop.getElementUIContextFamilies().getItems().get(i).getParanthesisPath();
            String variablePath = shop.getElementUIContextFamilies().getItems().get(i).getVariablePath();
            String abstractionPath = shop.getElementUIContextFamilies().getItems().get(i).getAbstractionPath();
            assetManager.load(paranthesisPath, Animation.class);
            assetManager.load(variablePath, Animation.class);
            assetManager.load(abstractionPath, Animation.class);

            shop.getElementUIContextFamilies().getItems().get(i).setParanthesisUIContext(new ParanthesisUIContext());
            shop.getElementUIContextFamilies().getItems().get(i).setVariableUIContext(new VariableUIContext());
            shop.getElementUIContextFamilies().getItems().get(i).setAbstractionUIContext(new AbstractionUIContext());

            //assetManager.unload("data/levels/music" + String.format("%02d", i) + ".mp3");
        }
    }
}
