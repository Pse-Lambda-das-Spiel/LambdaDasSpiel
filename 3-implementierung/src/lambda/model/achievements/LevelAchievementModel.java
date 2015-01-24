package lambda.model.achievements;

import lambda.model.statistics.StatisticModel;

/**
 * This class represents an achievement unlocked by completing a specific number of level.
 * 
 * @author Robert Hochweiss
 */
public class LevelAchievementModel extends AchievementModel {

	private int reqLevelCompleted;
	
	/**
	 * Creates a new instance of this class.
	 * 
	 * @param reqLevelCompleted the number of completed level needed for unlocking the achievement
	 */
	public LevelAchievementModel(int reqLevelCompleted) {
		this.reqLevelCompleted = reqLevelCompleted;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void initialize() {
		setIconPathAchievementUnlocked("achievements/level/unlocked/aul" + Integer.toString(getId()));
		setIconPathAchievementLocked("achievements/level/locked/al" + Integer.toString(getId()));
		//setDescription(AssetModel.getAssets().getString("levelAchievement_" + Integer.toString(getId())));
		//setRequirementsDescription(AssetModel.getAssets().getString("reqLevelAchievement_" + Integer.toString(getId())));		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void checkRequirements(StatisticModel statistic) {
		if (statistic == null) {
			throw new IllegalArgumentException("statistic cannot be null!");
		}
		if (isLocked()) {
			if (statistic.getLevelCompleted() >= reqLevelCompleted) {
				setLocked(false);
			}
		} else {
			// To reset the achievements automatically if needed for example after a profile change
			if (statistic.getLevelCompleted() < reqLevelCompleted) {
				setLocked(true);
			}
		}
	}

}
