package lambda.model.achievements;

import lambda.model.statistics.StatisticModel;

/**
 * This class represents an achievement unlocked by enchanting a specific number of lambs.
 * 
 * @author Robert Hochweiss
 */
public class LambsEnchantedAchievementModel extends AchievementModel {

	private int reqLambsEnchanted;
	
	/**
	 * Creates a new instance of this class.
	 * 
	 * @param reqLambsEnchanted the number of enchanted lambs needed for unlocking the achievement
	 */
	public LambsEnchantedAchievementModel(int reqLambsEnchanted) {
		this.reqLambsEnchanted = reqLambsEnchanted;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void initialize() {
		setIconPathAchievementUnlocked("achievements/lambs_enchanted/unlocked/aul" + Integer.toString(getId()));
		setIconPathAchievementLocked("achievements/lambs_enchanted/locked/al" + Integer.toString(getId()));
		//setDescription(AssetModel.getAssets().getString("lambsEnchantedAchievement_" + Integer.toString(getId())));
		//setRequirementsDescription(AssetModel.getAssets().getString("reqLambsEnchantedAchievement_" + Integer.toString(getId())));		
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
			if (statistic.getLambsEnchanted() >= reqLambsEnchanted) {
				setLocked(false);
			}
		} else {
			// To reset the achievements automatically if needed for example after a profile change
			if (statistic.getLambsEnchanted() < reqLambsEnchanted) {
				setLocked(true);
			}
		}
	}

}
