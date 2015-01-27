package lambda.model.shop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import lambda.model.levels.InvalidJsonException;
import lambda.viewcontroller.level.AbstractionUIContext;
import lambda.viewcontroller.level.ParanthesisUIContext;
import lambda.viewcontroller.level.VariableUIContext;


/**
 * Represents a shop with three categories "music", "images" and ElementUIContextFamilies (which represents the actors
 * in editor-mode). This class is a singleton.
 *
 * @author Kay Schmitteckert
 */
public class ShopModel {

    private static ShopModel shop;
    private ShopItemTypeModel<MusicItemModel> music;
    private ShopItemTypeModel<BackgroundImageItemModel> images;
    private ShopItemTypeModel<ElementUIContextFamily> elementUIContextFamilies;

    /**
     * Private Constructor of this class. It will be call by "getShop()"
     */
    private ShopModel() {
        music = new ShopItemTypeModel<MusicItemModel>("music");
        images = new ShopItemTypeModel<BackgroundImageItemModel>("images");
        elementUIContextFamilies = new ShopItemTypeModel<ElementUIContextFamily>("sprites");
    }

    /**
     * Getter of the singleton "ShopModel".
     *
     * @return the global shop
     */
    public static ShopModel getShop() {
        if (shop == null) {
            shop = new ShopModel();
        }
        return shop;
    }

    /**
     * Returns the item-category of the music
     *
     * @return the item-category of the music
     */
    public ShopItemTypeModel<MusicItemModel> getMusic() {
        return music;
    }

    /**
     * Returns the item-category of the images
     *
     * @return the item-category of the images
     */
    public ShopItemTypeModel<BackgroundImageItemModel> getImages() {
        return images;
    }

    /**
     * Returns the item-category of the ElementUiContextFamilies
     *
     * @return the item-category of the ElementUiContextFamilies
     */
    public ShopItemTypeModel<ElementUIContextFamily> getElementUIContextFamilies() {
        return elementUIContextFamilies;
    }


    /**
     * Reads the items.json to know how much items to load and calls every method to complete the item-lists
     */
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

    /**
     * Reads the json and creates an "MusicItemModel"-item.
     *
     * @param id the id to load the correct json
     * @return new "MusicItemModel"-item with the dates from the json
     */
    public MusicItemModel loadMusicItem(String id) {
        FileHandle file = Gdx.files.internal("data/items/music" + String.format("%02d", id) + ".json");
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

    /**
     * Reads the json and creates an "BackgroundImageItemModel"-item.
     *
     * @param id the id to load the correct json
     * @return new "BackgroundImageItemModel"-item with the dates from the json
     */
    public BackgroundImageItemModel loadBackgroundImageItem(String id) {
        FileHandle file = Gdx.files.internal("data/items/backgroundimages" + String.format("%02d", id) + ".json");
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

    /**
     * Reads the json and creates an "ElementUIContextFamily"-item.
     *
     * @param id the id to load the correct json
     * @return new "ElementUIContextFamily"-item with the dates from the json
     */
    public ElementUIContextFamily loadElementUIContextFamily(String id) {
        FileHandle file = Gdx.files.internal("data/itmes/elementuis" + String.format("%02d", id) + ".json");
        JsonReader reader = new JsonReader();
        JsonValue jsonFile = reader.parse(file);
        JsonValue family = jsonFile.child();

        if (!(family.getString("familyId").equalsIgnoreCase(id))) {
            throw new InvalidJsonException("The id of the json file does not match with its file name!");
        }
        int price = family.getInt("price");
        String paranthesisPath = family.getString("paranthesisPath");
        String variablePath = family.getString("variablePath");
        String abstractionPath = family.getString("abstractionPath");
        return new ElementUIContextFamily(id, price, paranthesisPath, variablePath, abstractionPath);
    }

    /**
     * Loads all items and adds them into the correct list
     *
     * @param assets the AssetManager which loads the assets
     */
    public void queueAssets(AssetManager assets) {
        int numberOfMusic = shop.getMusic().getItems().size();
        for (int i = 0; i < numberOfMusic; i++) {
            String musicpath = shop.getMusic().getItems().get(i).getFilepath();
            assets.load(musicpath, Music.class);
            shop.getMusic().getItems().get(i).setMusic(assets.get(musicpath));
            //assetManager.unload("data/levels/music" + String.format("%02d", i) + ".mp3");
        }
        int numberOfImages = shop.getImages().getItems().size();
        for (int i = 0; i < numberOfImages; i++) {
            String imagepath = shop.getImages().getItems().get(i).getFilepath();
            assets.load(imagepath, Image.class);
            shop.getImages().getItems().get(i).setImage(assets.get(imagepath));
            //assetManager.unload("data/levels/music" + String.format("%02d", i) + ".mp3");
        }
        // TODO: CHECK ANIMATION
        int numberOfFamilies = shop.getElementUIContextFamilies().getItems().size();
        for (int i = 0; i < numberOfFamilies; i++) {
            String paranthesisPath = shop.getElementUIContextFamilies().getItems().get(i).getParanthesisPath();
            String variablePath = shop.getElementUIContextFamilies().getItems().get(i).getVariablePath();
            String abstractionPath = shop.getElementUIContextFamilies().getItems().get(i).getAbstractionPath();
            assets.load(paranthesisPath, Animation.class);
            assets.load(variablePath, Animation.class);
            assets.load(abstractionPath, Animation.class);

            shop.getElementUIContextFamilies().getItems().get(i).setParanthesisUIContext(new ParanthesisUIContext());
            shop.getElementUIContextFamilies().getItems().get(i).setVariableUIContext(new VariableUIContext());
            shop.getElementUIContextFamilies().getItems().get(i).setAbstractionUIContext(new AbstractionUIContext());

            //assetManager.unload("data/levels/music" + String.format("%02d", i) + ".mp3");
        }
    }
}
