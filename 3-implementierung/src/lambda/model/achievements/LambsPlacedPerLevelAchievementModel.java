package lambda.model.achievements;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.I18NBundle;

import lambda.model.profiles.ProfileManager;
import lambda.model.statistics.StatisticModel;

/**
 * This class represents an achievement unlocked by placing a specific number of lambs in a level.
 * 
 * @author Robert Hochweiss
 */
public class LambsPlacedPerLevelAchievementModel extends PerLevelAchievementModel {

	private int reqLambsPlacedPerLevel;
	
	/**
	 * Creates a new instance of this class.
	 * 
	 * @param reqLambsPlacedPerLevel the number of placed lambs in a level needed for unlocking the achievement
	 */
	public LambsPlacedPerLevelAchievementModel(int reqLambsPlacedPerLevel) {
		this.reqLambsPlacedPerLevel = reqLambsPlacedPerLevel;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void changedLambsPlacedPerLevel() {
		if (isLocked()) {
			checkRequirements(ProfileManager.getManager().getCurrentProfile().getStatistics());
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void initialize(AssetManager assets) {
		if (assets == null) {
			throw new IllegalArgumentException("assets cannot be null!");
		}
		I18NBundle bundle = assets.get(ProfileManager.getManager().getCurrentProfile().getLanguage(), I18NBundle.class);
		ProfileManager.getManager().getCurrentProfile().getStatistics().addObserver(this);
		setIconPathAchievementUnlocked("achievements/lambs_placed_per_Level/unlocked/aul" + Integer.toString(getId()));
		setIconPathAchievementLocked("achievements/lambs_placed_per_level/locked/al" + Integer.toString(getId()));
		setDescription(bundle.format("lambsPlacedPerLevelAchievement", reqLambsPlacedPerLevel));
		setRequirementsDescription("reqLambsPlacedPerLevelAchievement");
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
		if (statistic.getLambsPlacedPerLevel() >= reqLambsPlacedPerLevel) {
			setLocked(false);
		}
	}

}
