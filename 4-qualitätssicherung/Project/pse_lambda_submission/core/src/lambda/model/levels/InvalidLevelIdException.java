package lambda.model.levels;

/**
 * This Exception is thrown if you want to call a level with an invalid id
 * 
 * @author Kay Schmitteckert
 *
 */
public class InvalidLevelIdException extends RuntimeException {
    /**
     * Creates a new InvalidJsonException with an error message.
     * 
     * @param message
     *            The description of the error.
     */
    public InvalidLevelIdException(String message) {
        super(message);
    }
}
