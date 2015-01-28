package lambda.model.shop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;


/**
 * Represents a shop with three categories "music", "images" and ElementUIContextFamilies (which represents the actors
 * in editor-mode). This class is a singleton.
 *
 * @author Kay Schmitteckert
 */
public class ShopModel {

    private static ShopModel shop;
    private AssetManager assetManager;
    private ShopItemTypeModel<MusicItemModel> music;
    private ShopItemTypeModel<BackgroundImageItemModel> images;
    private ShopItemTypeModel<ElementUIContextFamily> elementUIContextFamilies;

    private String[] musicFilePaths;
    private String[] imagesFilePaths;
    private String[] elementUIContextFamilyPaths;

    /**
     * Private Constructor of this class. It will be call by "getShop()"
     */
    private ShopModel() {
        music = new ShopItemTypeModel<MusicItemModel>("music");
        images = new ShopItemTypeModel<BackgroundImageItemModel>("images");
        elementUIContextFamilies = new ShopItemTypeModel<ElementUIContextFamily>("sprites");

        musicFilePaths = loadMusicPaths();
        imagesFilePaths = loadImagePaths();
        elementUIContextFamilyPaths = loadElementUIPaths();
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


    public void setAssetManager(AssetManager assetManager) {
        this.assetManager = assetManager;
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
     * Reads the json and creates an "MusicItemModel"-item.
     *
     * @param file the json-file
     * @return new "MusicItemModel"-item with the dates from the json
     */
    public MusicItemModel loadMusicItem(FileHandle file) {
        //FileHandle file = Gdx.files.internal("data/items/music" + String.format("%02d", id) + ".json");
        JsonReader reader = new JsonReader();
        JsonValue jsonFile = reader.parse(file);
        JsonValue music = jsonFile.child();

        String id = music.getString("id");
        int price = music.getInt("price");
        String filepath = music.getString("filepath");
        assetManager.load(filepath, Music.class);
        MusicItemModel musicItem = new MusicItemModel(id, price, filepath);
        musicItem.setMusic(assetManager.get(filepath));

        return musicItem;
    }


    /**
     * Reads the json and creates an "BackgroundImageItemModel"-item.
     *
     * @param file the json-file
     * @return new "BackgroundImageItemModel"-item with the dates from the json
     */
    public BackgroundImageItemModel loadBackgroundImageItem(FileHandle file) {
        //FileHandle file = Gdx.files.internal("data/items/backgroundimages" + String.format("%02d", id) + ".json");
        JsonReader reader = new JsonReader();
        JsonValue jsonFile = reader.parse(file);
        JsonValue image = jsonFile.child();

        String id = image.getString("id");
        int price = image.getInt("price");
        String filepath = image.getString("filepath");
        assetManager.load(filepath, Texture.class);
        BackgroundImageItemModel bgImage = new BackgroundImageItemModel(id, price, filepath);
        bgImage.setImage(assetManager.get(filepath));

        return bgImage;
    }

    //TODO: ElementUIContextFamily wird über AssetLoader geladen, aber die Attribute innerhalb (noch) nicht.
    /**
     * Reads the json and creates an "ElementUIContextFamily"-item.
     *
     * @param file the id to load the correct json
     * @return new "ElementUIContextFamily"-item with the dates from the json
     */
    public ElementUIContextFamily loadElementUIContextFamily(FileHandle file) {
        //FileHandle file = Gdx.files.internal("data/items/elementuis" + String.format("%02d", id) + ".json");
        JsonReader reader = new JsonReader();
        JsonValue jsonFile = reader.parse(file);
        JsonValue family = jsonFile.child();

        String id = family.getString("id");
        int price = family.getInt("price");

        String paranthesisPath = family.getString("paranthesisPath");
        String variablePath = family.getString("variablePath");
        String abstractionPath = family.getString("abstractionPath");
        ElementUIContextFamily elementUIContextFamily = new ElementUIContextFamily(id, price, paranthesisPath,
                variablePath, abstractionPath);
        return elementUIContextFamily;
    }

    /**
     * Loads all items and adds them into the correct list
     *
     * @param assets the AssetManager which loads the assets
     */
    public void queueAssets(AssetManager assets) {
        assets.setLoader(MusicItemModel.class, new MusicItemModelLoader(new InternalFileHandleResolver()));
        for (String musicFilePath : musicFilePaths) {
            assets.load(musicFilePath, MusicItemModel.class);
        }
        assets.setLoader(BackgroundImageItemModel.class, new BackgroundImageItemModelLoader(new InternalFileHandleResolver()));
        for (String imageFilePath : imagesFilePaths) {
            assets.load(imageFilePath, BackgroundImageItemModel.class);
        }

        /*
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
        */
    }


    /**
     *
     * @return
     */
    public String[] loadMusicPaths() {
        FileHandle file = Gdx.files.internal("data/itmes/items.json");
        JsonReader reader = new JsonReader();
        JsonValue jsonFile = reader.parse(file);
        int numberOfMusic = jsonFile.getInt("musicItems");
        String[] musicFilePaths = new String[numberOfMusic];
        for (int i = 0; i < numberOfMusic; i++) {
            musicFilePaths[i] = "data/items/music" + String.format("%02d", i) + ".json";
        }
        return musicFilePaths;
    }

    /**
     *
     * @return
     */
    public String[] loadImagePaths() {
        FileHandle file = Gdx.files.internal("data/itmes/items.json");
        JsonReader reader = new JsonReader();
        JsonValue jsonFile = reader.parse(file);
        int numberOfImages = jsonFile.getInt("backgroundImageItems");
        String[] imageFilePaths = new String[numberOfImages];
        for (int i = 0; i < numberOfImages; i++) {
            imageFilePaths[i] = "data/items/images" + String.format("%02d", i) + ".json";
        }
        return imageFilePaths;
    }

    /**
     *
     * @return
     */
    public String[] loadElementUIPaths() {
        FileHandle file = Gdx.files.internal("data/itmes/items.json");
        JsonReader reader = new JsonReader();
        JsonValue jsonFile = reader.parse(file);
        int numberOfFamilies = jsonFile.getInt("elementUIContextFamilies");
        String[] elementUIContextFamilyPaths = new String[numberOfFamilies];
        for (int i = 0; i < numberOfFamilies; i++) {
            elementUIContextFamilyPaths[i] = "data/items/elementuis" + String.format("%02d", i) + ".json";
        }
        return elementUIContextFamilyPaths;
    }
}
