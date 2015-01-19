package lambda.model.achievements;

import lambda.AssetModel;
import lambda.model.statistics.StatisticModel;

/**
 * This class represents an achievement unlocked by enchanting a specific number of gems.
 * 
 * @author Robert Hochweiss
 */
public class GemsEnchantedAchievementModel extends AchievementModel {

	private int reqGemsEnchanted;
	
	/**
	 * Creates a new instance of this class.
	 * 
	 * @param reqGemsEnchanted the number of enchanted gems needed for unlocking the achievement
	 */
	public GemsEnchantedAchievementModel(int reqGemsEnchanted) {
		this.reqGemsEnchanted = reqGemsEnchanted;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void initialize() {
		setIconPathAchievementUnlocked("achievements/gems_enchanted/unlocked/aul" + Integer.toString(getId()));
		setIconPathAchievementLocked("achievements/gems_enchanted/locked/al" + Integer.toString(getId()));
		setDescription(AssetModel.getAssets().getString("gemsEnchantedAchievement_" + Integer.toString(getId())));
		setRequirementsDescription(AssetModel.getAssets().getString("reqGemsEnchantedAchievement_" + Integer.toString(getId())));		
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
			if (statistic.getGemsEnchanted() >= reqGemsEnchanted) {
				setLocked(false);
			}
		} else {
			// To resett the achievements automatically if needed for example after a profile change
			if (statistic.getGemsEnchanted() < reqGemsEnchanted) {
				setLocked(true);
			}
		}
	}

}
