package lambda.model.achievements;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.I18NBundle;

import lambda.model.profiles.ProfileManager;
import lambda.model.statistics.StatisticModel;

/**
 * This class represents an achievement unlocked by playing a specific amount of
 * time.
 * 
 * @author Robert Hochweiss
 */
public class TimeAchievementModel extends AchievementModel {

    private int reqTimePlayed;

    /**
     * Creates a new instance of this class.
     * 
     * @param reqTimePlayed
     *            the number of played time needed for unlocking the achievement
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
        setDescription(bundle.format("timeAchievement", reqTimePlayed));
        setRequirementsDescription(bundle.format("reqTimeAchievement",
                reqTimePlayed));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void checkRequirements(StatisticModel statistic) {
        if (statistic == null) {
            throw new IllegalArgumentException("statistic cannot be null!");
        }
        long timePlayed = statistic.getTimePlayed();
        timePlayed /= 60;
        if (timePlayed >= reqTimePlayed) {
            setLocked(false);
        } else {
            setLocked(true);
        }
    }

}
