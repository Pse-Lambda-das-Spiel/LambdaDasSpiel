package lambda.model.achievements;

import lambda.model.profiles.ProfileManager;
import lambda.model.statistics.StatisticModel;

/**
 * This class represents an achievement unlocked by solving a specific number of levels without using the hint.
 * 
 * @author Robert Hochweiss
 */
public class HintsAchievementModel extends AchievementModel {

	private int reqHintsNotUsed;
	
	/**
	 * Creates a new instance of this class.
	 * 
	 * @param reqHintsNotUsed the number of solved level without used hint needed for unlocking the achievement
	 */
	public HintsAchievementModel(int reqHintsNotUsed) {
		this.reqHintsNotUsed = reqHintsNotUsed;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void changedHintsNotUsed() {
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
		setIconPathAchievementUnlocked("achievements/hints/unlocked/aul" + Integer.toString(getId()));
		setIconPathAchievementLocked("achievements/hints/locked/al" + Integer.toString(getId()));
		//setDescription(AssetModel.getAssets().getString("hintsAchievement_" + Integer.toString(getId())));
		//setRequirementsDescription(AssetModel.getAssets().getString("reqHintsAchievement_" + Integer.toString(getId())));		
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
		if (statistic.getHintsNotUsed() >= reqHintsNotUsed) {
			setLocked(false);
		}
	}

}
