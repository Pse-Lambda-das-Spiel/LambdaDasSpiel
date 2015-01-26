package lambda.model.achievements;

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
	public void initialize() {
		ProfileManager.getManager().getCurrentProfile().getStatistics().addObserver(this);
		setIconPathAchievementUnlocked("achievements/lambs_placed/unlocked/aul" + Integer.toString(getId()));
		setIconPathAchievementLocked("achievements/lambs_placed/locked/al" + Integer.toString(getId()));
		//setDescription(AssetModel.getAssets().getString("lambsPlacedAchievement_" + Integer.toString(getId())));
		//setRequirementsDescription(AssetModel.getAssets().getString("reqLambsPlacedAchievement_" + Integer.toString(getId())));	
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
		if (statistic.getLambsPlaced() >= reqLambsPlaced) {
			setLocked(false);
		}
	}

}
