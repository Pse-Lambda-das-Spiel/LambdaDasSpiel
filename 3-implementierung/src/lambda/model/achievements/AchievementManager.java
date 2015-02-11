package lambda.model.achievements;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import lambda.model.levels.InvalidJsonException;
import lambda.model.profiles.ProfileManager;
import lambda.model.statistics.StatisticModel;
import lambda.viewcontroller.achievements.AchievementViewController;

/**
 * This class is used to manage all achievements. It contains a collection of all achievements in the game.
 * It can also initialize and check all achievement or add or remove a single achievement.
 * 
 * @author Robert Hochweiss
 */
public class AchievementManager {

	private static AchievementManager manager;
	private Map<String, AchievementModel> achievements;
	private List<String> achievementTypeList;

	private Map<String, Integer> achievementNumberPerType;
	
	private AchievementManager() {
		achievements = new HashMap<>();
	}
	
	/**
	 * Returns the only AchievementManager instance.
	 * 
	 * @return the only AchievementManager instance
	 */
	public static AchievementManager getManager() {
		if (manager == null) {
			manager = new AchievementManager();
		}
		return manager;
	}
	
	/**
	 * Returns the collection of all achievements.
	 * 
	 * @return the achievements
	 */
	public Map<String, AchievementModel> getAchievements() {
		return achievements;
	}
	
	/**
	 * Returns the list of all achievement types.
	 * 
	 * @return the achievementTypeList 
	 */
	public List<String> getAchievementTypeList() {
		return achievementTypeList;
	}

	/**
	 * Returns a Map of all achievement numbers per type, with the achievement type as key.
	 * 
	 * @return the achievementNumberPerType
	 */
	public Map<String, Integer> getAchievementNumberPerType() {
		return achievementNumberPerType;
	}
	
	/**
	 * Resets all achievements.
	 * 
	 * @param assets the AssetManager needed for loading the resources needed for initializing the achievements.
	 * @throws IllegalArgumentException if assets is null
	 */
	public void resetAchievements(AssetManager assets) {
		if (assets == null) {
			throw new IllegalArgumentException("assets cannot be null!");
		}
		Collection<AchievementModel> achievementCollection = achievements.values();
		StatisticModel statistic = ProfileManager.getManager().getCurrentProfile().getStatistics();
		for (AchievementModel achievement : achievementCollection) {
			statistic.addObserver(achievement);
			achievement.reset(assets);
		}
	}
	
	/**
	 * Checks all achievements with the current {@link StatisticModel}. Checks whether their requirements are met or not.
	 * Sets the locked-/unlocked-state of the achievements accordingly.
	 * 
	 */
	public void checkAllAchievements() {
		StatisticModel statistic = ProfileManager.getManager().getCurrentProfile().getStatistics();
		Collection<AchievementModel> achievementCollection = achievements.values();
		for (AchievementModel achievement : achievementCollection) {
			achievement.checkRequirements(statistic);
		}
	}
	
	/**
	 * Adds a new achievement to the collection of all achievements, if it is not already there.
	 * 
	 * @param achievement the to be added achievement
	 * @throws IllegalArgumentException if achievement is null
	 */
	public void addAchievement(AchievementModel achievement) {
		if (achievement == null) {
			throw new IllegalArgumentException("achievement cannot be null!");
		}
		if (!achievements.containsKey(achievement.getId())) {
			achievements.put(achievement.getId(), achievement);
		}	
	}
	
	/**
	 * Removes an achievement.
	 * 
	 * @param id the id of the to be removed achievement
	 */
	public void removeAchievement(String id) {
		achievements.remove(id);
	}
	
	/**
	 * Loads all achievements and the achievement types and the number of achievements per type.
	 * Loads also the achievements into their specific {@link AchievementViewController}.
	 * 
	 * @param achievementVCList the list for the {@link AchievementViewController}
	 * @throws IllegalArgumentException if achievementVCList is null
	 */
	public void loadAchievements(List<AchievementViewController> achievementVCList) {
		if (achievementVCList == null) {
			throw new IllegalArgumentException("The achievementVCList cannot be null!");
		}
		achievementTypeList = new ArrayList<>();
		achievementNumberPerType = new HashMap<>();
		FileHandle file = Gdx.files.internal("data/achievements/achievementList.json");
		JsonReader reader = new JsonReader();
		JsonValue value = reader.parse(file);
		for (JsonValue entry = value.get("achievementTypes").child(); entry != null; entry = entry.next) {
			switch (entry.getString("achievementType")) {
			case "gemsEnchantedAchievement":
				achievementTypeList.add("gemsEnchantedAchievement");
				break;
			case "lambsEnchantedAchievement":
				achievementTypeList.add("lambsEnchantedAchievement");
				break;
			case "gemsPlacedAchievement":
				achievementTypeList.add("gemsPlacedAchievement");
				break;
			case "lambsPlacedAchievement":
				achievementTypeList.add("lambsPlacedAchievement");
				break;
			case "gemsEnchantedPerLevelAchievement":
				achievementTypeList.add("gemsEnchantedPerLevelAchievement");
				break;
			case "lambsEnchantedPerLevelAchievement":
				achievementTypeList.add("lambsEnchantedPerLevelAchievement");
				break;
			case "gemsPlacedPerLevelAchievement":
				achievementTypeList.add("gemsPlacedPerLevelAchievement");
				break;
			case "lambsPlacedPerLevelAchievement":
				achievementTypeList.add("lambsPlacedPerLevelAchievement");
				break;
			case "levelAchievement":
				achievementTypeList.add("levelAchievement");
				break;
			case "hintsAchievement":
				achievementTypeList.add("hintsAchievement");
				break;
			case "timeAchievement":
				achievementTypeList.add("timeAchievement");
				break;
			default:
				throw new InvalidJsonException("The achievement type is invalid!");
			}
			achievementNumberPerType.put(entry.getString("achievementType"), 
					entry.getInt("achievementNumberPerType"));
		}
		loadAchievementList(value.get("achievements"), achievementVCList);	
	}
	
	private void loadAchievementList(JsonValue value, List<AchievementViewController> achievementVCList) {
		for (JsonValue entry = value.child(); entry != null; entry = entry.next) {
			AchievementModel achievement;
			String str;
			switch (entry.getString("type")) {
			case "gea":
				achievement = new GemsEnchantedAchievementModel(entry.getInt("req"));
				str = "gea";
				break;
			case "lea":
				achievement = new LambsEnchantedAchievementModel(entry.getInt("req"));
				str = "lea";
				break;
			case "gpa":
				achievement = new GemsPlacedAchievementModel(entry.getInt("req"));
				str = "gpa";
				break;
			case "lpa":
				achievement = new LambsPlacedAchievementModel(entry.getInt("req"));
				str = "lpa";
				break;
			case "gepla":
				achievement = new GemsEnchantedPerLevelAchievementModel(entry.getInt("req"));
				str = "gepla";
				break;
			case "lepla":
				achievement = new LambsEnchantedPerLevelAchievementModel(entry.getInt("req"));
				str = "lepla";
				break;
			case "gppla":
				achievement = new GemsPlacedPerLevelAchievementModel(entry.getInt("req"));
				str = "gppla";
				break;
			case "lppla":
				achievement = new LambsPlacedPerLevelAchievementModel(entry.getInt("req"));
				str = "lppla";
				break;
			case "la":
				achievement = new LevelAchievementModel(entry.getInt("req"));
				str = "la";
				break;
			case "ha":
				achievement = new HintsAchievementModel(entry.getInt("req"));
				str = "ha";
				break;
			case "ta":
				achievement = new TimeAchievementModel(entry.getInt("req"));
				str = "ta";
				break;
			default:
				throw new InvalidJsonException("The achievement type in the achievement list is invalid!");
			}
			achievement.setIndex(entry.getInt("index"));
			achievement.setId(str + String.format("%02d", achievement.getIndex()));
			achievement.initialize();
			addAchievement(achievement);
			achievementVCList.add(new AchievementViewController(achievement));
		}
	}
	
}
