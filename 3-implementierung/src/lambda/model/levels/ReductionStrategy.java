package lambda.model.levels;
/**
 * Enumeration of the different reduction-strategies
 *
 * @author Kay Schmitteckert
 */
public enum ReductionStrategy {

    /**
     * Represents the reduction strategy "normal order"
     */
    NORMAL_ORDER,
    /**
     * Represents the reduction strategy "applicative order"
     */
    APPLICATIVE_ORDER,
    /**
     * Represents the reduction strategy "call by name"
     */
    CALL_BY_NAME,
    /**
     * Represents the reduction strategy "call by value"
     */
    CALL_BY_VALUE
}
