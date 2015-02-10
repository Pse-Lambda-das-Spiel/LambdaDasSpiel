package lambda.model.achievements;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.I18NBundle;

import lambda.model.profiles.ProfileManager;
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
	public void changedGemsEnchanted() {
		if (isLocked()) {
			checkRequirements(ProfileManager.getManager().getCurrentProfile().getStatistics());
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void initialize() {
		setIconPathAchievementUnlocked("achievements/gems_enchanted/ul_" + getId());
		setIconPathAchievementLocked("achievements/gems_enchanted/l_" + getId());
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
		setDescription(bundle.format("gemsEnchantedAchievement", reqGemsEnchanted));
		setRequirementsDescription(bundle.format("reqGemsEnchantedAchievement", reqGemsEnchanted));
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
		if (statistic.getGemsEnchanted() >= reqGemsEnchanted) {
			setLocked(false);
		}
	}

}
