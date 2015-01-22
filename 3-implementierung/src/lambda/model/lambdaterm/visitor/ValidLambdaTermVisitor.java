package lambda.model.lambdaterm.visitor;

import lambda.model.lambdaterm.InvalidLambdaTermException;
import lambda.model.lambdaterm.LambdaAbstraction;
import lambda.model.lambdaterm.LambdaApplication;
import lambda.model.lambdaterm.LambdaRoot;
import lambda.model.lambdaterm.LambdaTerm;
import lambda.model.lambdaterm.LambdaVariable;

/**
 * Represents a visitor on a lambda term tree that checks if the visited tree is a valid term before visiting it.
 * 
 * @author Florian Fervers
 * @param <T> the return type of the visit
 */
public abstract class ValidLambdaTermVisitor<T> implements LambdaTermVisitor<T> {
    /**
     * The message of the exception that will be thrown if the visited term isn't valid.
     */
    private final String exceptionMessage;
    /**
     * Stores whether the visitor has checked the visited term for validity.
     */
    private boolean hasCheckedValidity;
    
    /**
     * Creates a new instance of ValidLambdaTermVisitor.
     * 
     * @param exceptionMessage the message of the exception that will be thrown if the visited term isn't valid
     */
    public ValidLambdaTermVisitor(String exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
        hasCheckedValidity = false;
    }

    /**
     * Visits the given lambda root, checks the term for validity and then calls visitValid.
     * 
     * @param node the root to be visited
     */
    @Override
    public final void visit(LambdaRoot node) {
        checkValidity(node);
        visitValid(node);
    }
    
    /**
     * Visits the given lambda application, checks the term for validity and then calls visitValid.
     * 
     * @param node the application to be visited
     */
    @Override
    public final void visit(LambdaApplication node) {
        checkValidity(node);
        visitValid(node);
    }
    
    /**
     * Visits the given lambda abstraction, checks the term for validity and then calls visitValid.
     * 
     * @param node the abstraction to be visited
     */
    @Override
    public final void visit(LambdaAbstraction node) {
        checkValidity(node);
        visitValid(node);
    }
    
    /**
     * Visits the given lambda variable, checks the term for validity and then calls visitValid.
     * 
     * @param node the variable to be visited
     */
    @Override
    public final void visit(LambdaVariable node) {
        checkValidity(node);
        visitValid(node);
    }
    
    /**
     * Visits the given valid lambda application.
     * 
     * @param node the application to be visited
     */
    public abstract void visitValid(LambdaRoot node);
    
    /**
     * Visits the given valid lambda application.
     * 
     * @param node the application to be visited
     */
    public abstract void visitValid(LambdaApplication node);
    
    /**
     * Visits the given valid lambda abstraction.
     * 
     * @param node the abstraction to be visited
     */
    public abstract void visitValid(LambdaAbstraction node);
    
    /**
     * Visits the given valid lambda variable.
     * 
     * @param node the variable to be visited
     */
    public abstract void visitValid(LambdaVariable node);
    
    /**
     * Checks the given term for validity and throws an exception if the term is invalid.
     * 
     * @param term the term to be checked
     * @throws InvalidLambdaTermException if the term is invalid
     */
    private void checkValidity(LambdaTerm term) {
        if (!hasCheckedValidity) {
            hasCheckedValidity = true;
            if (!term.accept(new IsValidVisitor())) {
                throw new InvalidLambdaTermException(exceptionMessage);
            }
        }
    }
}
