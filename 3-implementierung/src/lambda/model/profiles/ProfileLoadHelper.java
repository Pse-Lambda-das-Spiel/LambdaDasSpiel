package lambda.model.profiles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import lambda.model.settings.SettingsModel;
import lambda.model.shop.BackgroundImageItemModel;
import lambda.model.shop.ElementUIContextFamily;
import lambda.model.shop.MusicItemModel;
import lambda.model.shop.ShopModel;
import lambda.model.statistics.StatisticModel;

/**
 * This class helps with loading and initialising a ProfileModel.
 * It loads the general information, the settings, the statistics 
 * and on explicit invocation the shop state of a profile from the specific profile directory.
 * 
 * @author Robert Hochweiss
 */
public final class ProfileLoadHelper {

	private ProfileLoadHelper() {
	}
	
	/**
	 * Loads and initialize the ProfileModel and its specific parts with the given profile name.
	 * If no profile directory with the given name exists, it returns null.
	 * 
	 * @param name the name of the to be loaded ProfileModel
	 * @return the loaded and initialized ProfileModel
	 * @throws IOException if there is an error while reading the profile json files
	 */
    public static ProfileModel loadProfile(String name) {
        if (!(Gdx.files.local(ProfileManager.PROFILE_FOLDER + "/" + name).exists())) {
            return null;
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
    	//statistic.setLevelTries(statisticJson.getInt("levelTries"));
    	//statistic.setSuccessfulLevelTries(statisticJson.getInt("successfulLevelTries"));
    }

    /**
     * Loads the shop state of the profile associated with the given name into the shop singleton.
     * 
     * @param name the name of the profile whose shop state should be loaded
     * throws IOException if there is an error while reading the shop state json files
     */
    public static void loadIntoShop(String name) {  
    	if (!(Gdx.files.local(ProfileManager.PROFILE_FOLDER + "/" + name).exists())) {
             return;
        }
    	ShopModel shop = ShopModel.getShop();
    	FileHandle shopFile = Gdx.files.local(ProfileManager.PROFILE_FOLDER + "/" + name + "/shop_state.json");
    	JsonReader reader = new JsonReader();
        JsonValue shopValue = reader.parse(shopFile);
        JsonValue shopJson = shopValue.child();
    	for (JsonValue entry = shopJson.get("Musics").child(); entry != null; entry = entry.next) {
    		MusicItemModel music = shop.getMusic().getItems().get(entry.getInt("MusicId"));
    		music.setPurchased(entry.getBoolean("purchased"));
    		if (music.getId().equals(shopJson.getString("activatedMusicId"))) {
    			shop.getMusic().setActivatedItem(music);
    		}
    		if (shopJson.getString("activatedMusicId").equals("")) {
    			shop.getMusic().setActivatedItem(null);
    		}
    	}
    	for (JsonValue entry = shopJson.get("BKImages").child(); entry != null; entry = entry.next) {
    		BackgroundImageItemModel bkImage = shop.getImages().getItems().get(entry.getInt("BKImageId"));
    		bkImage.setPurchased(entry.getBoolean("purchased"));
    		if (bkImage.getId().equals(shopJson.getString("activatedBKImageId"))) {
    			shop.getImages().setActivatedItem(bkImage);
    		}
    		if (shopJson.getString("activatedBKImageId").equals("")) {
    			shop.getImages().setActivatedItem(null);
    		}
    	}
    	for (JsonValue entry = shopJson.get("ElementUIContextFamilies").child(); entry != null; entry = entry.next) {
    		ElementUIContextFamily elementUIContextFamily = shop.getElementUIContextFamilies().getItems().
    				get(entry.getInt("ElementUIContextFamilyId"));
    		elementUIContextFamily.setPurchased(entry.getBoolean("purchased"));
    		if (elementUIContextFamily.getId().equals(shopJson.getString("activatedElementUIContextFamilyId"))) {
    			shop.getElementUIContextFamilies().setActivatedItem(elementUIContextFamily);
    		}
    		if (shopJson.getString("activatedElementUIContextFamilyId").equals("")) {
    			shop.getElementUIContextFamilies().setActivatedItem(null);
    		}
    	}
    }
}
