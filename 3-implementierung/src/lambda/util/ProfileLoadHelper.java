package lambda.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import lambda.model.profiles.ProfileManager;
import lambda.model.profiles.ProfileModel;
import lambda.model.settings.SettingsModel;
import lambda.model.shop.BackgroundImageItemModel;
import lambda.model.shop.ElementUIContextFamily;
import lambda.model.shop.MusicItemModel;
import lambda.model.shop.ShopModel;
import lambda.model.statistics.StatisticModel;

/**
 * This class helps with loading and initialising a ProfileModel.
 * It loads the general information, the settings, the statistics 
 * and the shop state of a profile from the specific profile directory.
 * 
 * @author Robert Hochweiss
 */
public final class ProfileLoadHelper {

	private ProfileLoadHelper() {
	}
	
	/**
	 * Loads and initialize the ProfileModel and its specific parts with the given profile name.
	 * If no profile directory with the given name exists, it returns a default ProfileModel without initialized data.
	 * 
	 * @param name the name of the to be loaded ProfileModel
	 * @return the loaded and initialized ProfileModel
	 * @throws IOException if there is an error while reading the profile json files
	 */
    public static ProfileModel loadProfile(String name) {
    	// Before there was a ! missing (temporary solution?)
        if (!(Gdx.files.local(ProfileManager.PROFILE_FOLDER + "/" + name).exists())) {
            return new ProfileModel(name);
        }
        ProfileModel profile = new ProfileModel(name);
        FileHandle file = Gdx.files.local(ProfileManager.PROFILE_FOLDER + "/" + name + "/general_information.json");
        JsonReader reader = new JsonReader();
        JsonValue value = reader.parse(file);
        FileHandle settingsFile = Gdx.files.local(ProfileManager.PROFILE_FOLDER + "/" + name + "/settings.json");
        JsonValue settingsValue = reader.parse(settingsFile);
        FileHandle statisticFile = Gdx.files.local(ProfileManager.PROFILE_FOLDER + "/" + name + "/statistic.json");
        JsonValue statisticValue = reader.parse(statisticFile);
        FileHandle shopFile = Gdx.files.local(ProfileManager.PROFILE_FOLDER + "/" + name + "/shop_state.json");
        JsonValue shopValue = reader.parse(shopFile);
        
        profile.setAvatar(value.child().getString("avatar"));
        profile.setLanguage(value.child().getString("language"));
        profile.setCoins(value.child().getInt("coins"));
        profile.setLevelIndex(value.child().getInt("levelIndex"));
        loadIntoSettings(settingsValue.child(), profile.getSettings());
        loadIntoStatistic(statisticValue.child(), profile.getStatistics());
        loadIntoShop(shopValue.child(), profile.getShop());
        return profile;
    }
    
    private static void loadIntoSettings(JsonValue settingsJson, SettingsModel settings) {
    	settings.setMusicOn(settingsJson.getBoolean("musicOn"));
    	settings.setMusicVolume(settingsJson.getFloat("musicVolume"));
    	settings.setSoundVolume(settingsJson.getFloat("soundVolume"));
    }
    
    private static void loadIntoStatistic(JsonValue statisticJson, StatisticModel statistic) {
    	statistic.setLambsEnchanted(statisticJson.getInt("lambsEnchanted"));
    	statistic.setGemsEnchanted(statisticJson.getInt("gemsEnchanted"));
    	statistic.setLambsPlaced(statisticJson.getInt("lambsPlaced"));
    	statistic.setGemsPlaced(statisticJson.getInt("gemsPlaced"));
    	statistic.setLambsEnchantedPerLevel(statisticJson.getInt("lambsEnchantedPerLevel"));
    	statistic.setGemsEnchantedPerLevel(statisticJson.getInt("gemsEnchantedPerLevel"));
    	statistic.setLambsPlacedPerLevel(statisticJson.getInt("lambsPlacedPerLevel"));
    	statistic.setGemsPlacedPerLevel(statisticJson.getInt("gemsPlacedPerLevel"));
    	statistic.setLevelCompleted(statisticJson.getInt("levelCompleted"));
    	statistic.setHintsNotUsed(statisticJson.getInt("hintsNotUsed"));
    	statistic.setLevelTries(statisticJson.getInt("levelTries"));
    	statistic.setSuccessfulLevelTries(statisticJson.getInt("successfulLevelTries"));
    }

    private static void loadIntoShop(JsonValue shopJson, ShopModel shop) {
    	// Update this method if ShopItemModel gets an update and gets more precise regarding references and activation
    	
    	for (JsonValue entry = shopJson.get("musics").child(); entry != null; entry = entry.next) {
    		MusicItemModel music = new MusicItemModel(entry.getString("musicId"), entry.getInt("price"));
    		music.setPurchased(entry.getBoolean("purchased"));
    		if (music.getId().equals(shopJson.getString("activatedMusicId"))) {
    			// Todo: maybe activate MusicItemModel in another way, initializing of ShopItemModel is not so precise
    			shop.getMusic().setActivatedItem(music);
    		}
    		shop.getMusic().getItems().add(music);
    	}
    	
    	for (JsonValue entry = shopJson.get("images").child(); entry != null; entry = entry.next) {
    		BackgroundImageItemModel image = new BackgroundImageItemModel(entry.getString("imageId"), 
    																		entry.getInt("price"));
    		image.setPurchased(entry.getBoolean("purchased"));
    		//Same as above
    		if (image.getId().equals(shopJson.getString("activatedBKImageId"))) {
    			shop.getImages().setActivatedItem(image);
    		}
    		shop.getImages().getItems().add(image);
    	}
    	
    	for (JsonValue entry = shopJson.get("sprites").child(); entry != null; entry = entry.next) {
    		ElementUIContextFamily sprite = new ElementUIContextFamily(entry.getString("spriteId"), 
    																	entry.getInt("price"));
    		sprite.setPurchased(entry.getBoolean("purchased"));
    		//Same as above
    		if (sprite.getId().equals(shopJson.getString("activatedSpriteId"))) {
    			shop.getElementUIContextFamilies().setActivatedItem(sprite);
    		}
    		shop.getElementUIContextFamilies().getItems().add(sprite);
    	}
    }
}
