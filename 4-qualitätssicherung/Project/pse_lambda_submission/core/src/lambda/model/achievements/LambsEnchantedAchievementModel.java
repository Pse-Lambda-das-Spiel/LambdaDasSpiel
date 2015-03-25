package lambda.model.achievements;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.I18NBundle;

import lambda.model.profiles.ProfileManager;
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
	public void changedLambsEnchanted() {
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
		setDescription(bundle.format("lambsEnchantedAchievement", reqLambsEnchanted));
		setRequirementsDescription(bundle.format("reqLambsEnchantedAchievement", reqLambsEnchanted));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void checkRequirements(StatisticModel statistic) {
		if (statistic == null) {
			throw new IllegalArgumentException("statistic cannot be null!");
		}
		if (statistic.getLambsEnchanted() >= reqLambsEnchanted) {
			setLocked(false);
		} else {
			setLocked(true);
		}
	}

}
