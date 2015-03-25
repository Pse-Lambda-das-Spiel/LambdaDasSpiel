package lambda.model.levels;

/**
 * This exception is thrown when a json file can be read and loaded but has invalid content.
 * 
 * @author Robert Hochweiss
 */
public class InvalidJsonException extends RuntimeException {

    /**
     * Creates a new InvalidJsonException with an error message.
     * 
     * @param message The description of the error.
     */
    public InvalidJsonException(String message) {
        super(message);
    }
    
}