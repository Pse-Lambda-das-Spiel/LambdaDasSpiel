package lambda.model.profiles;

/**
 * This exception is thrown when the game's profile save folder can't be read or
 * the profiles can be loaded, but their names are invalid.
 * 
 * @author Kai Fieger
 */
public class InvalidProfilesException extends RuntimeException {

    /**
     * Creates a new InvalidProfilesException with an errormessage.
     * 
     * @param message
     *            The description of the error.
     */
    public InvalidProfilesException(String message) {
        super(message);
    }

}
