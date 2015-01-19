package lambda.model.achievements;

/**
 * An interface for an observer of an AchievementModel-object. An observed
 * AchievementModel-object calls the containing methods of its observers.
 * Implement this interface to keep a class informed on the changed locked-/unlocked-state of an AchievementModel.
 * 
 * @author Robert Hochweiss
 */
public interface AchievementModelObserver {
	
	/**
	 * Gets called by an observed AchievementModel-object if its locked-/unlocked-state has changed.
	 * The default implementation is empty.
	 */
	default public void changedLockedState() {
	}
}
