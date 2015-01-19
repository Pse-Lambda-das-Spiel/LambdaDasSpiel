package lambda.model.lambdaterm.visitor.strategy;

import lambda.model.lambdaterm.InvalidLambdaTermException;
import lambda.model.lambdaterm.LambdaAbstraction;
import lambda.model.lambdaterm.LambdaApplication;
import lambda.model.lambdaterm.LambdaRoot;
import lambda.model.lambdaterm.LambdaTerm;
import lambda.model.lambdaterm.LambdaVariable;
import lambda.model.lambdaterm.visitor.IsValidVisitor;
import lambda.model.lambdaterm.visitor.LambdaTermVisitor;

/**
 * Represents a visitor on a lambda term that performs a beta reduction. Can only visit a valid lambda term. Subclasses override visit methods with their reduction strategies functionality.
 * 
 * @author Florian Fervers
 */
public abstract class BetaReductionVisitor implements LambdaTermVisitor<LambdaTerm> {
    /**
     * Stores the resulting lambda term after the reduction.
     */
    protected LambdaTerm result;
    /**
     * Indicates whether this visitor has already performed an application.
     */
    protected boolean hasReduced;
    /**
     * Stores an applicant (i.e. right child of an application). When an abstraction is reached and an applicant is present the application is performed.
     */
    protected LambdaTerm applicant;
    /**
     * Stores whether the visitor has checked the visited term for validity.
     */
    private boolean hasCheckedValidity;
    
    /**
     * Creates a new beta reduction visitor.
     */
    public BetaReductionVisitor() {
        result = null;
        hasReduced = false;
        applicant = null;
        hasCheckedValidity = false;
    }

    /**
     * Visits the given lambda root and traverses to the child node.
     * 
     * @param node the root to be visited
     * @throws InvalidLambdaTermException if the visited term is invalid
     */
    @Override
    public void visit(LambdaRoot node) {
        checkValidity(node);
        node.setChild(node.getChild().accept(this));
        result = node;
    }
    
    /**
     * Visits the given lambda application and saves it as result.
     * 
     * @param node the application to be visited
     * @throws InvalidLambdaTermException if the visited term is invalid
     */
    @Override
    public void visit(LambdaApplication node) {
        checkValidity(node);
        result = node;
    }
    
    /**
     * Visits the given lambda abstraction and saves it as result.
     * 
     * @param node the abstraction to be visited
     * @throws InvalidLambdaTermException if the visited term is invalid
     */
    @Override
    public void visit(LambdaAbstraction node) {
        checkValidity(node);
        result = node;
    }
    
    /**
     * Visits the given lambda variable and saves it as result.
     * 
     * @param node the variable to be visited
     * @throws InvalidLambdaTermException if the visited term is invalid
     */
    @Override
    public void visit(LambdaVariable node) {
        checkValidity(node);
        result = node;
    }
    
    /**
     * Checks the given term for validity and throws an exception if the term is invalid.
     * 
     * @param term the term to be checked
     * @throws InvalidLambdaTermException if the term is invalid
     */
    protected void checkValidity(LambdaTerm term) {
        if (!hasCheckedValidity) {
            hasCheckedValidity = true;
            if (!term.accept(new IsValidVisitor())) {
                throw new InvalidLambdaTermException("Cannot perform a beta reduction on an invalid lambda term!");
            }
        }
    }
    
    /**
     * Returns whether this visitor has performed an application.
     * 
     * @return true if this visitor has performed an application, false otherwise
     */
    public boolean hasReduced() {
        return hasReduced;
    }
    
    /**
     * Returns the resulting lambda term after the reduction.
     * 
     * @return the resulting lambda term after the reduction
     */
    @Override
    public LambdaTerm getResult() {
        return result;
    }
}
