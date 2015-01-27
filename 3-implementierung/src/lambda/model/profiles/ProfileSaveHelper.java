package lambda.model.profiles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter.OutputType;

import lambda.model.settings.SettingsModel;
import lambda.model.shop.BackgroundImageItemModel;
import lambda.model.shop.ElementUIContextFamily;
import lambda.model.shop.MusicItemModel;
import lambda.model.shop.ShopModel;
import lambda.model.statistics.StatisticModel;

/**
 * This class helps with the saving of profiles into json files.
 * It saves the general information, settings, statistics and the shop state of a profile in the profile specific 
 * directory.
 * 
 * @author Robert Hochweiss
 */
public final class ProfileSaveHelper {

	private ProfileSaveHelper() {
	}
	
	/**
	 * Saves a ProfileModel and its specific parts.
	 * 
	 * @param profile the to be saved ProfileModel
	 */
    public static void saveProfile(ProfileModel profile) {
        FileHandle a = Gdx.files.local(ProfileManager.PROFILE_FOLDER + "/" + profile.getName());
        if (!a.exists()) {
            a.mkdirs();
        }
        saveGeneralInfos(profile);
        saveSettings(profile.getName(), profile.getSettings());
        saveStatistic(profile.getName(), profile.getStatistics());
        saveShopState(profile.getName(), profile.getShop());
    }

    private static void saveGeneralInfos(ProfileModel profile) {
    	Json profileJson = new Json();
        profileJson.setSerializer(ProfileModel.class,
				new Json.Serializer<ProfileModel>() {
					@Override
					public void write(Json json, ProfileModel profileModel, Class knownType) {
						// create json structure
						json.writeObjectStart();
						json.writeObjectStart("profile_general_information");
						json.writeValue("name", profileModel.getName());
						json.writeValue("avatar", profileModel.getAvatar());
						json.writeValue("language", profileModel.getLanguage());
						json.writeValue("levelIndex", profileModel.getLevelIndex());
						json.writeValue("coins", profileModel.getCoins());
						json.writeObjectEnd();
						json.writeObjectEnd();
					} 
					// not necessary for writing
					@Override
					public ProfileModel read(Json json, JsonValue jsonData,
							Class type) {
						return null;
					}
				});
		profileJson.setOutputType(OutputType.json);
		FileHandle generalInfos = Gdx.files.local(ProfileManager.PROFILE_FOLDER 
														+ "/" + profile.getName() + "/general_information.json");
		generalInfos.writeString(profileJson.prettyPrint(profile), false);
    }
    
    private static void saveSettings(String profileName, SettingsModel settings) {
    	Json settingsJson = new Json();
        settingsJson.setSerializer(SettingsModel.class,
				new Json.Serializer<SettingsModel>() {
					@Override
					public void write(Json json, SettingsModel settingsModel, Class knownType) {
						// create json structure
						json.writeObjectStart();
						json.writeObjectStart("profile_settings");
						json.writeValue("musicOn", settingsModel.isMusicOn());
						json.writeValue("musicVolume", settingsModel.getMusicVolume());
						json.writeValue("soundVolume", settingsModel.getSoundVolume());
						json.writeObjectEnd();
						json.writeObjectEnd();
					} 
					@Override
					public SettingsModel read(Json json, JsonValue jsonData,
							Class type) {
						return null;
					}
				});
		settingsJson.setOutputType(OutputType.json);
		FileHandle settingsFile = Gdx.files.local(ProfileManager.PROFILE_FOLDER 
														+ "/" + profileName + "/settings.json");
		settingsFile.writeString(settingsJson.prettyPrint(settings), false);
    }
    
    private static void saveStatistic(String profileName, StatisticModel statistic) {
    	Json statisticJson = new Json();
        statisticJson.setSerializer(StatisticModel.class,
				new Json.Serializer<StatisticModel>() {
					@Override
					public void write(Json json, StatisticModel statisticModel, Class knownType) {
						// create json structure
						json.writeObjectStart();
						json.writeObjectStart("profile_statistic");
						json.writeValue("lambsEnchanted", statisticModel.getLambsEnchanted());
						json.writeValue("gemsEnchanted", statisticModel.getGemsEnchanted());
						json.writeValue("lambsPlaced", statisticModel.getLambsPlaced());
						json.writeValue("gemsPlaced", statisticModel.getGemsPlaced());
						json.writeValue("lambsEnchantedPerLevel", statisticModel.getLambsEnchantedPerLevel());
						json.writeValue("gemsEnchantedPerLevel", statisticModel.getGemsEnchantedPerLevel());
						json.writeValue("lambsPlacedPerLevel", statisticModel.getLambsPlacedPerLevel());
						json.writeValue("gemsPlacedPerLevel", statisticModel.getGemsPlacedPerLevel());
						json.writeValue("levelCompleted", statisticModel.getLevelCompleted());
						json.writeValue("hintsNotUsed", statisticModel.getHintsNotUsed());
						json.writeValue("levelTries", statisticModel.getLevelTries());
						json.writeValue("successfulLevelTries", statisticModel.getSuccessfulLevelTries());
						json.writeObjectEnd();
						json.writeObjectEnd();
					} 
					@Override
					public StatisticModel read(Json json, JsonValue jsonData, Class type) {
						return null;
					}
				});
		statisticJson.setOutputType(OutputType.json);
		FileHandle statisticFile = Gdx.files.local(ProfileManager.PROFILE_FOLDER 
														+ "/" + profileName + "/statistic.json");
		statisticFile.writeString(statisticJson.prettyPrint(statistic), false);
    }
    
    private static void saveShopState(String profileName, ShopModel shop) {
    	Json shopJson = new Json();
        shopJson.setSerializer(ShopModel.class,
				new Json.Serializer<ShopModel>() {
					@Override
					public void write(Json json, ShopModel shopModel,
							Class knownType) {
						// create json structure
						json.writeObjectStart();
						json.writeObjectStart("profile_shop_state");
						// Since the activated item can be null
						if (shopModel.getElementUIContextFamilies().getActivatedItem() != null) {
							json.writeValue("activatedSpriteId", shopModel.getElementUIContextFamilies().
																			getActivatedItem().getId());
						} else {
							json.writeValue("activatedSpriteId", "");
						}
						if (shopModel.getMusic().getActivatedItem() != null) {
							json.writeValue("activatedMusicId", shopModel.getMusic().getActivatedItem().getId());
						} else {
							json.writeValue("activatedMusicId", "");
						}
						if (shopModel.getImages().getActivatedItem() != null) {
							json.writeValue("activatedBKImageId", shopModel.getImages().getActivatedItem().getId());
						} else {
							json.writeValue("activatedBKImageId", "");
						}
						json.writeArrayStart("musics");
						for (MusicItemModel music : shopModel.getMusic().getItems()) {
							json.writeObjectStart();
							json.writeValue("musicId", music.getId());
							json.writeValue("price", music.getPrice());
							json.writeValue("purchased", music.isPurchased());
							json.writeObjectEnd();
						}
						json.writeArrayEnd();
						json.writeArrayStart("images");
						for (BackgroundImageItemModel image : shopModel.getImages().getItems()) {
							json.writeObjectStart();
							json.writeValue("imageId", image.getId());
							json.writeValue("price", image.getPrice());
							json.writeValue("purchased", image.isPurchased());
							json.writeObjectEnd();
						}
						json.writeArrayEnd();
						json.writeArrayStart("sprites");
						for (ElementUIContextFamily sprite : shopModel.getElementUIContextFamilies().getItems()) {
							json.writeObjectStart();
							json.writeValue("spriteId", sprite.getId());
							json.writeValue("price", sprite.getPrice());
							json.writeValue("purchased", sprite.isPurchased());
							json.writeObjectEnd();
						}
						json.writeArrayEnd();
						json.writeObjectEnd();
						json.writeObjectEnd();
					} 
					@Override
					public ShopModel read(Json json, JsonValue jsonData, Class type) {
						return null;
					}
				});
		shopJson.setOutputType(OutputType.json);
		FileHandle shopFile = Gdx.files.local(ProfileManager.PROFILE_FOLDER 
														+ "/" + profileName + "/shop_state.json");
		shopFile.writeString(shopJson.prettyPrint(shop), false);
    }
}
