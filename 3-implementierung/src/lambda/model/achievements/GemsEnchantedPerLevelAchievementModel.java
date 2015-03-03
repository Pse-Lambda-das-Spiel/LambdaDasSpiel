package lambda.model.achievements;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.I18NBundle;

import lambda.model.profiles.ProfileManager;
import lambda.model.statistics.StatisticModel;

/**
 * This class represents an achievement unlocked by enchanting a specific number of gems in a level.
 * 
 * @author Robert Hochweiss
 */
public class GemsEnchantedPerLevelAchievementModel extends PerLevelAchievementModel {

	private int reqGemsEnchantedPerLevel;
	
	/**
	 * Creates a new instance of this class.
	 * 
	 * @param reqGemsEnchantedPerLevel the number of enchanted gems in a level needed for unlocking the achievement
	 */
	public GemsEnchantedPerLevelAchievementModel(int reqGemsEnchantedPerLevel) {
		this.reqGemsEnchantedPerLevel = reqGemsEnchantedPerLevel;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void changedGemsEnchantedPerLevel() {
		if (isLocked()) {
			checkRequirements(ProfileManager.getManager().getCurrentProfile().getStatistics());
		}
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
		setDescription(bundle.format("gemsEnchantedPerLevelAchievement", reqGemsEnchantedPerLevel));
		setRequirementsDescription(bundle.format("reqGemsEnchantedPerLevelAchievement", reqGemsEnchantedPerLevel));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void checkRequirements(StatisticModel statistic) {
		if (statistic == null) {
			throw new IllegalArgumentException("statistic cannot be null!");
		}
		if (statistic.getGemsEnchantedPerLevel() >= reqGemsEnchantedPerLevel) {
			setLocked(false);
		} else {
			setLocked(true);
		}
	}

}
