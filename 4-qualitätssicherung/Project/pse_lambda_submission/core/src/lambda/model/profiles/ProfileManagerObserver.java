package lambda.model.profiles;

/**
 * An interface for an observer of an ProfileManager-object. An observed
 * ProfileManager-object calls the containing methods of its observers.
 * Implement this interface to keep a class informed on profile changes.
 * 
 * @author Kai Fieger
 */
public interface ProfileManagerObserver {

    /**
     * Gets called by an observed ProfileManager-object as the selected profile
     * (the current user profile) is changed.
     */
    void changedProfile();

    /**
     * Gets called by an observed ProfileManager-object if there is a change in
     * the list of all profilenames the ProfileManager currently manages (for
     * example after creating/renaming/deleting a profile).
     */
    void changedProfileList();

}
