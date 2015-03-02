package lambda.model.shop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
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
     * Loads all items
     *
     * @param assets the AssetManager which loads and holds the assets
     */
    public void queueAssets(AssetManager assets) {
        loadAllMusicItems(assets);
        loadAllImageItems(assets);
        loadAllElementItems(assets);
    }

    /**
     * loads all music items and sets them into the list of the music category
     * 
     * @param assets the asset manager
     */
    public void loadAllMusicItems(AssetManager assets) {
        FileHandle file = Gdx.files.internal("data/items/music/music.json");
        JsonReader reader = new JsonReader();
        JsonValue jsonFile = reader.parse(file);
        int i = 0;
        // loads every section from the json
        while (jsonFile.hasChild(String.format("%02d", i))) {
            JsonValue music = jsonFile.get(String.format("%02d", i));
            //Read the json file and set the attributes
            String id = music.getString("id");
            int price = music.getInt("price");
            String filepath = music.getString("filepath");
            MusicItemModel musicItem = new MusicItemModel(id, price, filepath);
            assets.load(filepath, Music.class);
            this.music.getItems().add(musicItem);
            i++;
        }
        //default
        assets.load("data/levels/music/default.mp3", Music.class);
    }
    
    /**
     * loads all image items and sets them into the list of the image category
     * 
     * @param assets the asset manager
     */
    public void loadAllImageItems(AssetManager assets) {
        FileHandle file = Gdx.files.internal("data/items/images/images.json");
        JsonReader reader = new JsonReader();
        JsonValue jsonFile = reader.parse(file);
        int i = 0;
        // loads every section from the json
        while (jsonFile.hasChild(String.format("%02d", i))) {
            JsonValue image = jsonFile.get(String.format("%02d", i));
            //Read the json file and set the attributes
            String id = image.getString("id");
            int price = image.getInt("price");
            String filepath = image.getString("filepath");
            BackgroundImageItemModel imageItem = new BackgroundImageItemModel(id, price, filepath);
            assets.load(filepath, Texture.class);
            this.images.getItems().add(imageItem);
            i++;
        }
        //default
        assets.load("data/levels/images/default.png", Texture.class);
    }
    
    /**
     * loads all element items and sets them into the list of the element category
     * 
     * @param assets the asset manager
     */
    public void loadAllElementItems(AssetManager assets) {
        FileHandle file = Gdx.files.internal("data/items/elementuis/elementuis.json");
        JsonReader reader = new JsonReader();
        JsonValue jsonFile = reader.parse(file);
        int i = 0;
        // loads every section from the json
        while (jsonFile.hasChild(String.format("%02d", i))) {
            JsonValue image = jsonFile.get(String.format("%02d", i));
            //Read the json file and set the attributes
            String id = image.getString("id");
            int price = image.getInt("price");
            String filepath = image.getString("filepath");
            assets.load(filepath, TextureAtlas.class);
            ElementUIContextFamily familyItem = new ElementUIContextFamily(id, price, filepath);
            elementUIContextFamilies.getItems().add(i, familyItem);
            i++;
        }
        //default
        assets.load("data/items/elementuis/default.atlas", TextureAtlas.class);
    }
    
    public void setAllAssets(AssetManager assets) {
        // sets every asset to the item
        for (MusicItemModel musicItem : this.music.getItems()) {
            musicItem.setMusic(assets.get(musicItem.getFilepath(), Music.class));
        }
        // set the default music
        MusicItemModel musicItem = new MusicItemModel("default", 0, "data/levels/music/default.mp3");
        musicItem.setMusic(assets.get(musicItem.getFilepath(), Music.class));
        music.setDefaultItem(musicItem);
        
        // sets every asset to the image items
        for (BackgroundImageItemModel imageItem : this.images.getItems()) {
            imageItem.setImage(assets.get(imageItem.getFilepath(), Texture.class));
        }
        // set the default image
        BackgroundImageItemModel imageItem = new BackgroundImageItemModel("default", 0, "data/levels/images/default.png");
        imageItem.setImage(assets.get(imageItem.getFilepath(), Texture.class));
        images.setDefaultItem(imageItem);
        
        // sets every asset to the element items
        for (ElementUIContextFamily familyItem : this.elementUIContextFamilies.getItems()) {
            TextureAtlas atlas = assets.get(familyItem.getFilepath());
            VariableUIContext variable = new VariableUIContext(atlas.findRegion("gem"), atlas.findRegion("gem_mask"));
            AbstractionUIContext abstraction = new AbstractionUIContext(atlas.findRegion("front_magicstick"), 
                    atlas.findRegion("center"), 
                    atlas.findRegion("back"),
                    atlas.findRegion("front_mask"),
                    atlas.findRegion("center_mask"),
                    atlas.findRegion("back_mask"));
            ParanthesisUIContext parenthesis = new ParanthesisUIContext(atlas.findRegion("front"), 
                    atlas.findRegion("center"), 
                    atlas.findRegion("back"),
                    atlas.findRegion("front_mask"),
                    atlas.findRegion("center_mask"),
                    atlas.findRegion("back_mask"));
            familyItem.setAbstractionUIContext(abstraction);
            familyItem.setParanthesisUIContext(parenthesis);
            familyItem.setVariableUIContext(variable);  
        }
        // set the default elements
        TextureAtlas atlas = assets.get("data/items/elementuis/default.atlas");
        VariableUIContext variable = new VariableUIContext(atlas.findRegion("gem"), atlas.findRegion("gem_mask"));
        AbstractionUIContext abstraction = new AbstractionUIContext(atlas.findRegion("front_magicstick"), 
                atlas.findRegion("center"), 
                atlas.findRegion("back"),
                atlas.findRegion("front_mask"),
                atlas.findRegion("center_mask"),
                atlas.findRegion("back_mask"));
        ParanthesisUIContext parenthesis = new ParanthesisUIContext(atlas.findRegion("front"), 
                atlas.findRegion("center"), 
                atlas.findRegion("back"),
                atlas.findRegion("front_mask"),
                atlas.findRegion("center_mask"),
                atlas.findRegion("back_mask"));
        ElementUIContextFamily familyItem = new ElementUIContextFamily("default", 0, "data/items/elementuis/default.atlas");
        familyItem.setAbstractionUIContext(abstraction);
        familyItem.setParanthesisUIContext(parenthesis);
        familyItem.setVariableUIContext(variable);  
        elementUIContextFamilies.setDefaultItem(familyItem);
    }
}
