package lambda.model.achievements;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.I18NBundle;

import lambda.model.profiles.ProfileManager;
import lambda.model.statistics.StatisticModel;

/**
 * This class represents an achievement unlocked by placing a specific number of gems in a level.
 * 
 * @author Robert Hochweiss
 */
public class GemsPlacedPerLevelAchievementModel extends PerLevelAchievementModel {

	private int reqGemsPlacedPerLevel;
	
	/**
	 * Creates a new instance of this class.
	 * 
	 * @param reqGemsPlacedPerLevel the number of placed gems in a level needed for unlocking the achievement
	 */
	public GemsPlacedPerLevelAchievementModel(int reqGemsPlacedPerLevel) {
		this.reqGemsPlacedPerLevel = reqGemsPlacedPerLevel;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void changedGemsPlacedPerLevel() {
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
		setDescription(bundle.format("gemsPlacedPerLevelAchievement", reqGemsPlacedPerLevel));
		setRequirementsDescription(bundle.format("reqGemsPlacedPerLevelAchievement", reqGemsPlacedPerLevel));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void checkRequirements(StatisticModel statistic) {
		if (statistic == null) {
			throw new IllegalArgumentException("statistic cannot be null!");
		}
		if (statistic.getGemsPlaced() >= reqGemsPlacedPerLevel) {
			setLocked(false);
		} else {
			setLocked(true);
		}
	}

}
