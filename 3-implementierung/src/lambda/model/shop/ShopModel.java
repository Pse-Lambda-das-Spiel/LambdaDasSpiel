package lambda.model.shop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import lambda.viewcontroller.level.AbstractionUIContext;
import lambda.viewcontroller.level.ParanthesisUIContext;
import lambda.viewcontroller.level.VariableUIContext;


/**
 * Represents a shop with three categories "music", "images" and
 * ElementUIContextFamilies (which represents the actors in editor-mode).
 * This class is a singleton.
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


    /**
     * Sets the global AssetManager
     *
     * @param assetManager the global AssetManager
     */
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
     * @param file which holds the json
     * @return new "MusicItemModel"-item with the dates from the json
     */
    public MusicItemModel loadMusicItem(FileHandle file) {
        //Parse the file to json
        JsonReader reader = new JsonReader();
        JsonValue jsonFile = reader.parse(file);
        JsonValue music = jsonFile.child();
        //Read the json file and set the attributes
        String id = music.getString("id");
        int price = music.getInt("price");
        String filepath = music.getString("filepath");
        MusicItemModel musicItem = new MusicItemModel(id, price, filepath);
        musicItem.setMusic(assetManager.get(filepath));

        return musicItem;
    }


    /**
     * Reads the json and creates an "BackgroundImageItemModel"-item.
     *
     * @param file which holds the json
     * @return new "BackgroundImageItemModel"-item with the dates from the json
     */
    public BackgroundImageItemModel loadBackgroundImageItem(FileHandle file) {
        //Parse the file to json
        JsonReader reader = new JsonReader();
        JsonValue jsonFile = reader.parse(file);
        JsonValue image = jsonFile.child();
        //Read the json file and set the attributes
        String id = image.getString("id");
        int price = image.getInt("price");
        String filepath = image.getString("filepath");
        BackgroundImageItemModel bgImage = new BackgroundImageItemModel(id, price, filepath);
        bgImage.setImage(assetManager.get(filepath));

        return bgImage;
    }

    //TODO: ElementUIContextFamily wird über AssetLoader geladen, aber die Attribute innerhalb (noch) nicht.
    /**
     * Reads the json and creates an "ElementUIContextFamily"-item.
     *
     * @param file which holds the json
     * @return new "ElementUIContextFamily"-item with the dates from the json
     */
    public ElementUIContextFamily loadElementUIContextFamily(FileHandle file) {
        //Parse the file to json
        JsonReader reader = new JsonReader();
        JsonValue jsonFile = reader.parse(file);
        JsonValue family = jsonFile.child();
        //Read the json file and set the attributes
        String id = family.getString("id");
        int price = family.getInt("price");
        String paranthesisPath = family.getString("paranthesisPath");
        assetManager.setLoader(ParanthesisUIContext.class,
                new ParanthesisUIContextLoader((new InternalFileHandleResolver())));
        assetManager.load(paranthesisPath, ParanthesisUIContext.class);
        String variablePath = family.getString("variablePath");
        assetManager.setLoader(VariableUIContext.class, new VariableUIContextLoader(new InternalFileHandleResolver()));
        assetManager.load(variablePath, VariableUIContext.class);
        String abstractionPath = family.getString("abstractionPath");
        assetManager.setLoader(AbstractionUIContext.class,
                new AbstractionUIContextLoader(new InternalFileHandleResolver()));
        assetManager.load(abstractionPath, AbstractionUIContext.class);

        ParanthesisUIContext paranthesis = assetManager.get(paranthesisPath);
        VariableUIContext variable = assetManager.get(variablePath);
        AbstractionUIContext abstraction = assetManager.get(abstractionPath);

        ElementUIContextFamily elementUIContextFamily = new ElementUIContextFamily(id, price, paranthesis,
                variable, abstraction);

        return elementUIContextFamily;
    }

    /**
     * Adds all items to the right category
     *
     * @param assets
     */
    public void setAllItems(AssetManager assets) {
        for(int i = 0; i < musicFilePaths.length; i++) {
            MusicItemModel musicFile = assets.get(musicFilePaths[i], MusicItemModel.class);
            music.getItems().add(i, musicFile);
        }
        for(int i = 0; i < imagesFilePaths.length; i++) {
            BackgroundImageItemModel imageFile = assets.get(imagesFilePaths[i], BackgroundImageItemModel.class);
            images.getItems().add(i, imageFile);
        }
        for(int i = 0; i < elementUIContextFamilyPaths.length; i++) {
            ElementUIContextFamily elementFamilyFile = assets.get(elementUIContextFamilyPaths[i], ElementUIContextFamily.class);
            elementUIContextFamilies.getItems().add(i, elementFamilyFile);
        }
    }

    /**
     * Loads all items
     *
     * @param assets the AssetManager which loads the assets
     */
    public void queueAssets(AssetManager assets) {
        //Set loader for MusicItemModel and load every path
        assets.setLoader(MusicItemModel.class, new MusicItemModelLoader(new InternalFileHandleResolver()));
        for (String musicFilePath : musicFilePaths) {
            assets.load(musicFilePath, MusicItemModel.class);
        }
        //Set loader for BackgroundImageItemModel and load every path
        assets.setLoader(BackgroundImageItemModel.class,
                new BackgroundImageItemModelLoader(new InternalFileHandleResolver()));
        for (String imageFilePath : imagesFilePaths) {
            assets.load(imageFilePath, BackgroundImageItemModel.class);
        }
        //Set loader for ElementUIContext and load every path
        assets.setLoader(ElementUIContextFamily.class,
                new ElementUIContextFamilyLoader(new InternalFileHandleResolver()));
        for (String elementUIContextPath: elementUIContextFamilyPaths) {
            assets.load(elementUIContextPath, ElementUIContextFamily.class);
        }
    }


    /**
     * Loads every path for the music items and add them into the array
     *
     * @return Array of all paths
     */
    public String[] loadMusicPaths() {
        FileHandle file = Gdx.files.internal("data/items/items.json");
        JsonReader reader = new JsonReader();
        JsonValue jsonFile = reader.parse(file);
        JsonValue items = jsonFile.child();
        int numberOfMusic = items.getInt("musicItems");
        String[] musicFilePaths = new String[numberOfMusic];
        for (int i = 0; i < numberOfMusic; i++) {
            musicFilePaths[i] = "data/items/music/" + String.format("%02d", i) + ".json";
        }
        return musicFilePaths;
    }

    /**
     * Loads every path for the image items and add them into the array
     *
     * @return Array of all paths
     */
    public String[] loadImagePaths() {
        FileHandle file = Gdx.files.internal("data/items/items.json");
        JsonReader reader = new JsonReader();
        JsonValue jsonFile = reader.parse(file);
        JsonValue items = jsonFile.child();
        int numberOfImages = items.getInt("backgroundImageItems");
        String[] imageFilePaths = new String[numberOfImages];
        for (int i = 0; i < numberOfImages; i++) {
            imageFilePaths[i] = "data/items/images/" + String.format("%02d", i) + ".json";
        }
        return imageFilePaths;
    }

    /**
     * Loads every path for the element items and add them into the array
     *
     * @return Array of all paths
     */
    public String[] loadElementUIPaths() {
        FileHandle file = Gdx.files.internal("data/items/items.json");
        JsonReader reader = new JsonReader();
        JsonValue jsonFile = reader.parse(file);
        JsonValue items = jsonFile.child();
        int numberOfFamilies = items.getInt("elementUIContextFamilies");
        String[] elementUIContextFamilyPaths = new String[numberOfFamilies];
        for (int i = 0; i < numberOfFamilies; i++) {
            elementUIContextFamilyPaths[i] = "data/items/elementuis/" + String.format("%02d", i) + ".json";
        }
        return elementUIContextFamilyPaths;
    }

    /**
     *
     * @param file which holds the json
     * @return
     */
    public ParanthesisUIContext loadParanthesisUIContext(FileHandle file) {
        //Parse the file to json
        JsonReader reader = new JsonReader();
        JsonValue jsonFile = reader.parse(file);
        JsonValue paranthesis = jsonFile.child();
        //Read the json file and set the attributes
        String front = paranthesis.getString("front");
        String center = paranthesis.getString("center");
        String back = paranthesis.getString("back");
        ParanthesisUIContext paranthesisUIContext = new ParanthesisUIContext(assetManager.get(front),
                assetManager.get(center), assetManager.get(back));

        return paranthesisUIContext;
    }

    /**
     *
     * @param file which holds the json
     * @return
     */
    public AbstractionUIContext loadAbstractionUIContext(FileHandle file) {
        //Parse the file to json
        JsonReader reader = new JsonReader();
        JsonValue jsonFile = reader.parse(file);
        JsonValue abstraction = jsonFile.child();
        //Read the json file and set the attributes
        String front = abstraction.getString("front");
        String center = abstraction.getString("center");
        String back = abstraction.getString("back");
        AbstractionUIContext abstractionUIContext = new AbstractionUIContext(assetManager.get(front),
                assetManager.get(center), assetManager.get(back));

        return abstractionUIContext;
    }

    /**
     *
     * @param file which holds the json
     * @return
     */
    public VariableUIContext loadVariableUIContext(FileHandle file) {
        //Parse the file to json
        JsonReader reader = new JsonReader();
        JsonValue jsonFile = reader.parse(file);
        JsonValue variable = jsonFile.child();
        //Read the json file and set the attributes
        String variableSheet = variable.getString("variable");
        VariableUIContext variableUIContext = new VariableUIContext(assetManager.get(variableSheet));

        return variableUIContext;
    }

    /**
     * Returns an array of all music file paths
     *
     * @return array of all music file paths
     */
    public String[] getMusicFilePaths() {
        return musicFilePaths;
    }

    /**
     * Returns an array of all image file paths
     *
     * @return array of all image file paths
     */
    public String[] getImagesFilePaths() {
        return imagesFilePaths;
    }

    /**
     * Returns an array of all element file paths
     *
     * @return array of all element file paths
     */
    public String[] getElementUIContextFamilyPaths() {
        return elementUIContextFamilyPaths;
    }
}
