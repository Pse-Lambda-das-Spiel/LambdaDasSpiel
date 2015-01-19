package lambda.model.achievements;

import lambda.AssetModel;
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
	public void initialize() {
		setIconPathAchievementUnlocked("achievements/lambs_placed/unlocked/aul" + Integer.toString(getId()));
		setIconPathAchievementLocked("achievements/lambs_placed/locked/al" + Integer.toString(getId()));
		setDescription(AssetModel.getAssets().getString("lambsPlacedAchievement_" + Integer.toString(getId())));
		setRequirementsDescription(AssetModel.getAssets().getString("reqLambsPlacedAchievement_" + Integer.toString(getId())));		
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
			if (statistic.getLambsPlaced() >= reqLambsPlaced) {
				setLocked(false);
			}
		} else {
			// To reset the achievements automatically if needed for example after a profile change
			if (statistic.getLambsPlaced() < reqLambsPlaced) {
				setLocked(true);
			}
		}
	}

}
