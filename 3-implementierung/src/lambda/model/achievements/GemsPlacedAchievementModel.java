package lambda.model.achievements;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.I18NBundle;

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
	public void initialize(AssetManager assets) {
		if (assets == null) {
			throw new IllegalArgumentException("assets cannot be null!");
		}
		I18NBundle bundle = assets.get(ProfileManager.getManager().getCurrentProfile().getLanguage(), I18NBundle.class);
		ProfileManager.getManager().getCurrentProfile().getStatistics().addObserver(this);
		setIconPathAchievementUnlocked("achievements/gems_placed/unlocked/aul" + Integer.toString(getId()));
		setIconPathAchievementLocked("achievements/gems_placed/locked/al" + Integer.toString(getId()));
		setDescription(bundle.format("gemsPlacedAchievement", reqGemsPlaced));
		setRequirementsDescription(bundle.format("reqGemsPlacedAchievement", reqGemsPlaced));
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
