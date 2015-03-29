package lambda;

/**
 * Represents a supplier of results with no input arguments. There is no
 * requirement that a new or distinct result be returned each time the supplier
 * is invoked. Serves as replacement for java.util.function.Supplier from Java
 * 8.
 * 
 * @param <T>
 *            the type of results supplied by this supplier
 * @author Robert Hochweiss
 */
public interface Supplier<T> {

    /**
     * Gets a result.
     * 
     * @return the result
     */
    T get();
}
