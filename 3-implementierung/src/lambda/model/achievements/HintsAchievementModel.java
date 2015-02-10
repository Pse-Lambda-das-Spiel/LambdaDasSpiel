package lambda.model.achievements;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.I18NBundle;

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
		setIconPathAchievementUnlocked("achievements/hints/ul_" + getId());
		setIconPathAchievementLocked("achievements/hints/l_" + getId());
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
		setDescription(bundle.format("hintsAchievement", reqHintsNotUsed));
		setRequirementsDescription(bundle.format("reqHintsAchievement", reqHintsNotUsed));	
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
