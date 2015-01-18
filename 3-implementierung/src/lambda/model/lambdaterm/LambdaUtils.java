package lambda.model.lambdaterm;

import lambda.model.lambdaterm.visitor.IsValidVisitor;
import lambda.model.lambdaterm.visitor.RemoveTermVisitor;

/**
 * A utility class with helper methods for lambda terms.
 * 
 * @author Florian Fervers
 */
public final class LambdaUtils {
    /**
     * Returns the root of the given lambda term tree.
     * 
     * @param term the lambda term tree
     * @return the root of the given lambda term tree
     * @throws IllegalArgumentException if term is null
     */
    public static LambdaTerm getRoot(LambdaTerm term) {
        if (term == null) {
            throw new IllegalArgumentException("Lambda term cannot be null!");
        }
        return term.getParent() == null ? term : getRoot(term.getParent());
    }
    
    /**
     * Splits of a term by removing it from its parent and passing it to a new LambdaRoot as child.
     * 
     * @param term the term to be split off
     * @return the new LambdaRoot with the split off term as child
     */
    public static LambdaRoot split(LambdaTerm term) {
        if (!term.accept(new IsValidVisitor())) {
            throw new InvalidLambdaTermException("Can only split a valid lambda term!");
        }
        if (term.getParent() == null) {
            throw new InvalidLambdaTermException("Cannot split a root!");
        }
        term.accept(new RemoveTermVisitor());
        LambdaRoot result = new LambdaRoot();
        result.setChild(term);
        return result;
    }
    
    /**
     * Prevents the instantiation of this class.
     */
    private LambdaUtils() {
    }
}
