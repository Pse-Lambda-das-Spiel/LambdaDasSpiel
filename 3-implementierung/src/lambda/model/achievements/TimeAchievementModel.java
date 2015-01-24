package lambda.model.achievements;
import lambda.model.statistics.StatisticModel;

/**
 * This class represents an achievement unlocked by playing a specific amount of time.
 * 
 * @author Robert Hochweiss
 */
public class TimeAchievementModel extends AchievementModel {

	private int reqTimePlayed;
	
	/**
	 * Creates a new instance of this class.
	 * 
	 * @param reqTimePlayed the number of played time needed for unlocking the achievement
	 */
	public TimeAchievementModel(int reqTimePlayed) {
		this.reqTimePlayed = reqTimePlayed;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void initialize() {
		setIconPathAchievementUnlocked("achievements/time/unlocked/aul" + Integer.toString(getId()));
		setIconPathAchievementLocked("achievements/time/locked/al" + Integer.toString(getId()));
		//setDescription(AssetModel.getAssets().getString("timeAchievement_" + Integer.toString(getId())));
		//setRequirementsDescription(AssetModel.getAssets().getString("reqTimeAchievement_" + Integer.toString(getId())));		
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
			if (statistic.getTimePlayed() >= reqTimePlayed) {
				setLocked(false);
			}
		} else {
			// To reset the achievements automatically if needed for example after a profile change
			if (statistic.getTimePlayed() < reqTimePlayed) {
				setLocked(true);
			}
		}
	}

}
