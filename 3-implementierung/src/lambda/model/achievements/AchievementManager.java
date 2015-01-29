package lambda.model.achievements;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.assets.AssetManager;

import lambda.model.profiles.ProfileManager;
import lambda.model.statistics.StatisticModel;

/**
 * This class is used to manage all achievements. It contains a collection of all achievements in the game.
 * It can also initialize and check all achievement or add or remove a single achievement.
 * 
 * @author Robert Hochweiss
 */
public class AchievementManager {

	private static AchievementManager manager;
	private Map<Integer, AchievementModel> achievements;
	
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
	public Map<Integer, AchievementModel> getAchievements() {
		return achievements;
	}
	
	/**
	 * Initialize all achievements.
	 * 
	 * @param assets the AssetManager needed for loading the resources needed for initializing the achievements.
	 * @throws IllegalArgumentException if assets is null
	 */
	public void initializeAchievements(AssetManager assets) {
		if (assets == null) {
			throw new IllegalArgumentException("assets cannot be null!");
		}
		Collection<AchievementModel> achievementCollection = achievements.values();
		for (AchievementModel achievement : achievementCollection) {
			achievement.initialize(assets);
		}
	}
	
	/**
	 * Checks all achievements with the given StatisticModel. Checks whether their requirements are met or not.
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
	public void removeAchievement(int id) {
		achievements.remove(id);
	}
	
}
