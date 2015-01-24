package lambda.model.achievements;

import lambda.model.statistics.StatisticModel;

/**
 * This class represents an achievement unlocked by placing a specific number of gems in a level.
 * 
 * @author Robert Hochweiss
 */
public class GemsPlacedPerLevelAchievementModel extends PerLevelAchievementModel{

	private int reqGemsPlacedPerLevel;
	
	/**
	 * Creates a new instance of this class.
	 * 
	 * @param reqGemsPlacedPerlevel the number of placed gems in a level needed for unlocking the achievement
	 */
	public GemsPlacedPerLevelAchievementModel(int reqGemsPlacedPerLevel) {
		this.reqGemsPlacedPerLevel = reqGemsPlacedPerLevel;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void initialize() {
		setIconPathAchievementUnlocked("achievements/gems_placed_per_Level/unlocked/aul" + Integer.toString(getId()));
		setIconPathAchievementLocked("achievements/gems_placed_per_level/locked/al" + Integer.toString(getId()));
		//setDescription(AssetModel.getAssets().getString("gemsPlacedPerLevelAchievement_" + Integer.toString(getId())));
		//setRequirementsDescription(AssetModel.getAssets().getString("reqGemPlacedPerLevelAchievement_" + Integer.toString(getId())));		
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
			if (statistic.getGemsPlacedPerLevel() >= reqGemsPlacedPerLevel) {
				setLocked(false);
			}
		} else {
			// To reset the achievements automatically if needed for example after a profile change
			if (statistic.getGemsPlacedPerLevel() < reqGemsPlacedPerLevel) {
				setLocked(true);
			}
		}
	}

}
