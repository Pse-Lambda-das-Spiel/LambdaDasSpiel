package lambda.model.achievements;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

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
		achievements = new HashMap<Integer, AchievementModel>();
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
	 */
	public void initializeAchievements() {
		Collection<AchievementModel> achievementCollection = achievements.values();
		for (AchievementModel a : achievementCollection) {
			a.initialize();
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
		for (AchievementModel a : achievementCollection) {
			a.checkRequirements(statistic);
		}
	}
	
	/**
	 * Adds a new achievement to the collection of all achievements.
	 * 
	 * @param achievement the to be added achievement
	 */
	public void addAchievement(AchievementModel achievement) {
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
