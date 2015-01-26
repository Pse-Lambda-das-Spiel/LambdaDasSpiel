package lambda.model.achievements;

import lambda.model.profiles.ProfileManager;
import lambda.model.statistics.StatisticModel;

/**
 * This class represents an achievement unlocked by placing a specific number of gems.
 * 
 * @author Robert Hochweiss
 */
public class GemsPlacedAchievementModel extends AchievementModel {

	private int reqGemsPlaced;
	
	/**
	 * Creates a new instance of this class.
	 * 
	 * @param reqGemsPlaced the number of placed gems needed for unlocking the achievement
	 */
	public GemsPlacedAchievementModel(int reqGemsPlaced) {
		this.reqGemsPlaced = reqGemsPlaced;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void changedGemsPlaced() {
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
		setIconPathAchievementUnlocked("achievements/gems_placed/unlocked/aul" + Integer.toString(getId()));
		setIconPathAchievementLocked("achievements/gems_placed/locked/al" + Integer.toString(getId()));
		//setDescription(AssetModel.getAssets().getString("gemsPlacedAchievement_" + Integer.toString(getId())));
		//setRequirementsDescription(AssetModel.getAssets().getString("reqGemsPlacedAchievement_" + Integer.toString(getId())));	
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
		if (statistic.getGemsPlaced() >= reqGemsPlaced) {
			setLocked(false);
		}
	}

}
