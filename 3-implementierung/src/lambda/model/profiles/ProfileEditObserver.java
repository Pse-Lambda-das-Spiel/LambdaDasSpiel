package lambda.model.profiles;

/**
 * An interface for an observer of an ProfileEditModel-object. An observed
 * ProfileEditModel-object calls the containing methods of its observers.
 * Implement this interface to keep a class informed on changes during the
 * editing of profiles.
 * 
 * @author Kai Fieger
 */
public interface ProfileEditObserver {

    /**
     * Gets called by an observed ProfileEditModel-object as the selected
     * language is changed. The default implementation is empty.
     */
    default public void changedLanguage() {

    }

    /**
     * Gets called by an observed ProfileEditModel-object as the selected avatar
     * is changed. The default implementation is empty.
     */
    default public void changedAvatar() {

    }

}
