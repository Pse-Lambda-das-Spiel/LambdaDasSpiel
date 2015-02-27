package lambda.model.profiles;

/**
 * An interface for an observer of an ProfileModel-object. An observed
 * ProfileModel-object calls the containing methods of its observers. Implement
 * this interface to keep a class informed on changes of a ProfileModel-object.
 * 
 * @author Kai Fieger
 */
public interface ProfileModelObserver {

    /*
     * not needed? default public void changedAvatar() {
     * 
     * }
     */

    /**
     * Gets called by an observed ProfileModel-object as the level-progress is
     * changed. The default implementation is empty.
     */
    public void changedLevelIndex();

    /**
     * Gets called by an observed ProfileModel-object as the number of owned
     * coins is changed. The default implementation is empty.
     */
    public void changedCoins();

}
