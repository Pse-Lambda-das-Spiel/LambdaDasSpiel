package lambda.model.achievements;
import lambda.model.profiles.ProfileManager;
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
	public void changedTimePlayed() {
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
		setIconPathAchievementUnlocked("achievements/time/unlocked/aul" + Integer.toString(getId()));
		setIconPathAchievementLocked("achievements/time/locked/al" + Integer.toString(getId()));
		//setDescription(AssetModel.getAssets().getString("timeAchievement_" + Integer.toString(getId())));
		//setRequirementsDescription(AssetModel.getAssets().getString("reqTimeAchievement_" + Integer.toString(getId())));	
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
		if (statistic.getTimePlayed() >= reqTimePlayed) {
			setLocked(false);
		}
	}

}
