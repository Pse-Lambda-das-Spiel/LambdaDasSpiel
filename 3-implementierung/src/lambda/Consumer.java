package lambda;

/**
 * Represents an operation that accepts a single input argument and returns no result.
 * Serves as replacement for java.util.functions.Consumer from Java 8.
 * 
 * @param <T> the type of the input argument
 * @author Florian Fervers
 */
public interface Consumer<T> {
    /**
     * Performs this operation on the given argument.
     * 
     * @param t the input argument
     */
    public void accept(T t);
}
