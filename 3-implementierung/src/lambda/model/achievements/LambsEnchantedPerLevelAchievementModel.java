package lambda.model.achievements;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.I18NBundle;

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
	 * @param reqLambsEnchantedPerLevel the number of enchanted lambs in a level needed for unlocking the achievement
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
		setIconPathAchievementUnlocked("achievements/lambs_enchanted_per_level/ul_" + getId());
		setIconPathAchievementLocked("achievements/lambs_enchanted_per_level/l_" +getId());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void reset(AssetManager assets) {
		if (assets == null) {
			throw new IllegalArgumentException("assets cannot be null!");
		}
		I18NBundle bundle = assets.get(ProfileManager.getManager().getCurrentProfile().getLanguage(), I18NBundle.class);
		setDescription(bundle.format("lambsEnchantedPerLevelAchievement", reqLambsEnchantedPerLevel));
		setRequirementsDescription(bundle.format("reqLambsEnchantedPerLevelAchievement", reqLambsEnchantedPerLevel));
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
		} else {
			setLocked(true);
		}
	}
	
}
