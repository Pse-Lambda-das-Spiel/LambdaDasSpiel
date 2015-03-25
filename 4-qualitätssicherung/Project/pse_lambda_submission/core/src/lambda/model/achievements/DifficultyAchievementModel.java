package lambda.model.achievements;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.I18NBundle;

import lambda.model.levels.LevelManager;
import lambda.model.profiles.ProfileManager;
import lambda.model.statistics.StatisticModel;

/**
 * This class represents an achievement unlocked by solving all levels of a specific difficulty.
 * 
 * @author Robert Hochweiss
 */
public class DifficultyAchievementModel extends AchievementModel {

	private int reqLevelPerDifficultySolved;
	
	/**
	 * Creates a new instance of this class.
	 * 
	 * @param difficultyId the identifier of the specific difficulty 
	 */
	public DifficultyAchievementModel(int difficultyId) {
		this.reqLevelPerDifficultySolved = difficultyId * LevelManager.LEVEL_PER_DIFFICULTY;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void changedLevelIndex() {
		if (isLocked()) {
			checkRequirements(ProfileManager.getManager().getCurrentProfile().getStatistics());
		}
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
		setDescription(bundle.format("difficultyAchievement",  
				reqLevelPerDifficultySolved / LevelManager.LEVEL_PER_DIFFICULTY));
		setRequirementsDescription(bundle.format("reqDifficultyAchievement",  
				reqLevelPerDifficultySolved / LevelManager.LEVEL_PER_DIFFICULTY));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void checkRequirements(StatisticModel statistic) {
		if (statistic == null) {
			throw new IllegalArgumentException("statistic cannot be null!");
		}
		if (ProfileManager.getManager().getCurrentProfile().getLevelIndex() > reqLevelPerDifficultySolved) {
			setLocked(false);
		} else {
			setLocked(true);
		}
	}

}
