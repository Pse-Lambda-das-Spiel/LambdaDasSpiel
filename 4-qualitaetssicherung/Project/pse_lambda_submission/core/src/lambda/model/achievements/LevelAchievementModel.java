package lambda.model.achievements;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.I18NBundle;

import lambda.model.profiles.ProfileManager;
import lambda.model.statistics.StatisticModel;

/**
 * This class represents an achievement unlocked by completing a specific number
 * of level.
 * 
 * @author Robert Hochweiss
 */
public class LevelAchievementModel extends AchievementModel {

    private int reqLevelCompleted;

    /**
     * Creates a new instance of this class.
     * 
     * @param reqLevelCompleted
     *            the number of completed level needed for unlocking the
     *            achievement
     */
    public LevelAchievementModel(int reqLevelCompleted) {
        this.reqLevelCompleted = reqLevelCompleted;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void changedLevelIndex() {
        if (isLocked()) {
            checkRequirements(ProfileManager.getManager().getCurrentProfile()
                    .getStatistics());
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
        I18NBundle bundle = assets.get(ProfileManager.getManager()
                .getCurrentProfile().getLanguage(), I18NBundle.class);
        setDescription(bundle.format("levelAchievement", reqLevelCompleted));
        setRequirementsDescription(bundle.format("reqLevelAchievement",
                reqLevelCompleted));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void checkRequirements(StatisticModel statistic) {
        if (statistic == null) {
            throw new IllegalArgumentException("statistic cannot be null!");
        }
        if (ProfileManager.getManager().getCurrentProfile().getLevelIndex() > reqLevelCompleted) {
            setLocked(false);
        } else {
            setLocked(true);
        }
    }

}
