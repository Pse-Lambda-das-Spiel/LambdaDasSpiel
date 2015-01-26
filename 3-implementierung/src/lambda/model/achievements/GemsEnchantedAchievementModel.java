package lambda.model.achievements;

import lambda.model.profiles.ProfileManager;
import lambda.model.statistics.StatisticModel;

/**
 * This class represents an achievement unlocked by enchanting a specific number of gems.
 * 
 * @author Robert Hochweiss
 */
public class GemsEnchantedAchievementModel extends AchievementModel {

	private int reqGemsEnchanted;
	
	/**
	 * Creates a new instance of this class.
	 * 
	 * @param reqGemsEnchanted the number of enchanted gems needed for unlocking the achievement
	 */
	public GemsEnchantedAchievementModel(int reqGemsEnchanted) {
		this.reqGemsEnchanted = reqGemsEnchanted;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void changedGemsEnchanted() {
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
		setIconPathAchievementUnlocked("data/achievements/gems_enchanted/unlocked/aul" + Integer.toString(getId()));
		setIconPathAchievementLocked("data/achievements/gems_enchanted/locked/al" + Integer.toString(getId()));
//		setDescription(...I18N Bundle.format("gemsEnchantedAchievement", reqGemsEnchanted);
//		setRequirementsDescription(.I18N Bundle.format("reqGemsEnchantedAchievement", reqGemsEnchanted)));	
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
		if (statistic.getGemsEnchanted() >= reqGemsEnchanted) {
			setLocked(false);
		}
	}

}
