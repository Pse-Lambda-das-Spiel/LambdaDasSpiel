package lambda.model.lambdaterm;

import java.awt.Color;

/**
 * Represents an observer that can observe a lambda term and be notified by changes.
 * 
 * @author Florian Fervers
 */
public interface LambdaTermObserver {
    /**
     * Is called when the given old term is replaced with the given new term. Either oldTerm or newTerm can be null, but never both at the same time.
     * 
     * @param oldTerm the old term to be replaced
     * @param newTerm the new replacing term
     */
    public void replaceTerm(LambdaTerm oldTerm, LambdaTerm newTerm);
    
    /**
     * Is called when the given term's color is changed.
     * 
     * @param term the modified term
     * @param color the new color
     */
    public void setColor(LambdaValue term, Color color);
}
