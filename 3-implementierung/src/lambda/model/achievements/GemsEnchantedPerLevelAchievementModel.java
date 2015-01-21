package lambda.model.achievements;

import lambda.AssetModel;
import lambda.model.statistics.StatisticModel;

/**
 * This class represents an achievement unlocked by enchanting a specific number of gems in a level.
 * 
 * @author Robert Hochweiss
 */
public class GemsEnchantedPerLevelAchievementModel extends PerLevelAchievementModel{

	private int reqGemsEnchantedPerLevel;
	
	/**
	 * Creates a new instance of this class.
	 * 
	 * @param reqGemsEnchantedPerlevel the number of enchanted gems in a level needed for unlocking the achievement
	 */
	public GemsEnchantedPerLevelAchievementModel(int reqGemsEnchantedPerLevel) {
		this.reqGemsEnchantedPerLevel = reqGemsEnchantedPerLevel;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void initialize() {
		setIconPathAchievementUnlocked("achievements/gems_enchanted_per_Level/unlocked/aul" + Integer.toString(getId()));
		setIconPathAchievementLocked("achievements/gems_enchanted_per_level/locked/al" + Integer.toString(getId()));
		setDescription(AssetModel.getAssets().getString("gemsEnchantedPerLevelAchievement_" + Integer.toString(getId())));
		setRequirementsDescription(AssetModel.getAssets().getString("reqGemsEnchantedPerLevelAchievement_" + Integer.toString(getId())));		
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
			if (statistic.getGemsEnchantedPerLevel() >= reqGemsEnchantedPerLevel) {
				setLocked(false);
			}
		} else {
			// To reset the achievements automatically if needed for example after a profile change
			if (statistic.getGemsEnchantedPerLevel() < reqGemsEnchantedPerLevel) {
				setLocked(true);
			}
		}
	}

}