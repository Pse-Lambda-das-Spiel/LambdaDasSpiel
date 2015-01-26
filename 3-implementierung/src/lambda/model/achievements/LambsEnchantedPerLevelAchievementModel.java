package lambda.model.achievements;

import lambda.model.profiles.ProfileManager;
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
	public void changedLambsEnchantedPerLevel() {
		if (isLocked()) {
			checkRequirements(ProfileManager.getManager().getCurrentProfile().getStatistics());
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void initialize() {
		ProfileManager.getManager().getCurrentProfile().getStatistics().addObserver(this);
		setIconPathAchievementUnlocked("achievements/lambs_enchanted_per_Level/unlocked/aul" + Integer.toString(getId()));
		setIconPathAchievementLocked("achievements/lambs_enchanted_per_level/locked/al" + Integer.toString(getId()));
		//setDescription(AssetModel.getAssets().getString("lambsEnchantedPerLevelAchievement_" + Integer.toString(getId())));
		//setRequirementsDescription(AssetModel.getAssets().getString("reqLambsEnchantedPerLevelAchievement_" + Integer.toString(getId())));	
		setLocked(true);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void checkRequirements(StatisticModel statistic) {
		if (statistic == null) {
			throw new IllegalArgumentException("statistic cannot be null!");
		}
		if (statistic.getLambsEnchantedPerLevel() >= reqLambsEnchantedPerLevel) {
			setLocked(false);
		}
	}

}
