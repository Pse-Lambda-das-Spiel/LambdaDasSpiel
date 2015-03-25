package lambda.model.statistics;

/**
 * An interface for an observer of an StatisticModel object. An observed
 * StatisticModel object calls the containing methods of its observers.
 * Implement this interface to keep a class informed on changes of a
 * StatisticModel object.
 * 
 * @author Robert Hochweiss
 */
public interface StatisticModelObserver {

    /**
     * Gets called by an observed StatisticModel object as the total number of
     * enchanted lambs in all levels is changed. The default implementation is
     * empty.
     */
    public void changedLambsEnchanted();

    /**
     * Gets called by an observed StatisticModel object as the total number of
     * enchanted gems in all level is changed. The default implementation is
     * empty.
     */
    public void changedGemsEnchanted();

    /**
     * Gets called by an observed StatisticModel object as the total number of
     * placed lambs in all level is changed. The default implementation is
     * empty.
     */
    public void changedLambsPlaced();

    /**
     * Gets called by an observed StatisticModel object as the total number of
     * placed gems in all level is changed. The default implementation is empty.
     */
    public void changedGemsPlaced();

    /**
     * Gets called by an observed StatisticModel object as the highest number of
     * enchanted lambs in a single level is changed. The default implementation
     * is empty.
     */
    public void changedLambsEnchantedPerLevel();

    /**
     * Gets called by an observed StatisticModel object as the highest number of
     * enchanted gems in a single level is changed. The default implementation
     * is empty.
     */
    public void changedGemsEnchantedPerLevel();

    /**
     * Gets called by an observed StatisticModel object as the highest number of
     * placed lambs in a single level is changed. The default implementation is
     * empty.
     */
    public void changedLambsPlacedPerLevel();

    /**
     * Gets called by an observed StatisticModel object as the highest number of
     * placed gems in a single level is changed. The default implementation is
     * empty.
     */
    public void changedGemsPlacedPerLevel();

    /**
     * Gets called by an observed StatisticModel object as the number of not
     * used hints (hint not used to solve level, counts only once) is changed.
     * The default implementation is empty.
     */
    public void changedHintsNotUsed();

    /**
     * Gets called by an observed StatisticModel object as the played game time
     * is changed. The default implementation is empty.
     */
    public void changedTimePlayed();

    /**
     * Gets called by an observed StatisticModel object as number of level tries
     * is changed. The default implementation is empty.
     */
    public void changedLevelTries();

    /**
     * Gets called by an observed StatisticModel object as number of successful
     * level tries is changed. The default implementation is empty.
     */
    public void changedSuccessfulLevelTries();

}
