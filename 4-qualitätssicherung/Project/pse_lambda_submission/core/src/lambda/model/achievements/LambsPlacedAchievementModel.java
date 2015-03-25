package lambda.model.achievements;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.I18NBundle;

import lambda.model.profiles.ProfileManager;
import lambda.model.statistics.StatisticModel;

/**
 * This class represents an achievement unlocked by placing a specific number of lambs.
 * 
 * @author Robert Hochweiss
 */
public class LambsPlacedAchievementModel extends AchievementModel {

	private int reqLambsPlaced;
	
	/**
	 * Creates a new instance of this class.
	 * 
	 * @param reqLambsPlaced the number of placed lambs needed for unlocking the achievement
	 */
	public LambsPlacedAchievementModel(int reqLambsPlaced) {
		this.reqLambsPlaced = reqLambsPlaced;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void changedLambsPlaced() {
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
		setDescription(bundle.format("lambsPlacedAchievement", reqLambsPlaced));
		setRequirementsDescription(bundle.format("reqLambsPlacedAchievement", reqLambsPlaced));		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void checkRequirements(StatisticModel statistic) {
		if (statistic == null) {
			throw new IllegalArgumentException("statistic cannot be null!");
		}
		if (statistic.getLambsPlaced() >= reqLambsPlaced) {
			setLocked(false);
		} else {
			setLocked(true);
		}
	}
	
}
