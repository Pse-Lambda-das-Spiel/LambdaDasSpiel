package lambda.model.achievements;

import lambda.AssetModel;
import lambda.model.statistics.StatisticModel;

/**
 * This class represents an achievement unlocked by enchanting a specific number of lambs in a level.
 * 
 * @author Robert Hochweiss
 */
public class LambsEnchantedPerLevelAchievementModel extends PerLevelAchievementModel{

	private int reqLambsEnchantedPerLevel;
	
	/**
	 * Creates a new instance of this class.
	 * 
	 * @param reqGemsEnchantedPerlevel the number of enchanted lambs in a level needed for unlocking the achievement
	 */
	public LambsEnchantedPerLevelAchievementModel(int reqLambsEnchantedPerLevel) {
		this.reqLambsEnchantedPerLevel = reqLambsEnchantedPerLevel;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void initialize() {
		setIconPathAchievementUnlocked("achievements/lambs_enchanted_per_Level/unlocked/aul" + Integer.toString(getId()));
		setIconPathAchievementLocked("achievements/lambs_enchanted_per_level/locked/al" + Integer.toString(getId()));
		setDescription(AssetModel.getAssets().getString("lambsEnchantedPerLevelAchievement_" + Integer.toString(getId())));
		setRequirementsDescription(AssetModel.getAssets().getString("reqLambsEnchantedPerLevelAchievement_" + Integer.toString(getId())));		
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
			if (statistic.getLambsEnchantedPerLevel() >= reqLambsEnchantedPerLevel) {
				setLocked(false);
			}
		} else {
			// To reset the achievements automatically if needed for example after a profile change
			if (statistic.getLambsEnchantedPerLevel() < reqLambsEnchantedPerLevel) {
				setLocked(true);
			}
		}
	}

}
