package lambda.model.lambdaterm;

/**
 * An exception that is thrown when an operation is performed on an invalid
 * lambda term that can only performed on a valid lambda term.
 * 
 * @author Florian Fervers
 */
public class InvalidLambdaTermException extends RuntimeException {
    /**
     * Constructs a new LambdaTermInvalidException with the specified detail
     * message. The cause is not initialized, and may subsequently be
     * initialized by a call to {@link #initCause}.
     *
     * @param message
     *            the detail message. The detail message is saved for later
     *            retrieval by the {@link #getMessage()} method.
     */
    public InvalidLambdaTermException(String message) {
        super(message);
    }
}
