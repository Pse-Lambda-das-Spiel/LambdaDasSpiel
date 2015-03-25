package lambda.model.lambdaterm.visitor;

import lambda.model.lambdaterm.LambdaAbstraction;
import lambda.model.lambdaterm.LambdaApplication;
import lambda.model.lambdaterm.LambdaRoot;
import lambda.model.lambdaterm.LambdaVariable;

/**
 * Represents a visitor that can visit a lambda term tree.
 * 
 * @param <T>
 *            the return type of the visit
 * @author Florian Fervers
 */
public interface LambdaTermVisitor<T> {
    /**
     * Visits the given lambda root.
     * 
     * @param node
     *            the root to be visited
     */
    public void visit(LambdaRoot node);

    /**
     * Visits the given lambda application.
     * 
     * @param node
     *            the application to be visited
     */
    public void visit(LambdaApplication node);

    /**
     * Visits the given lambda abstraction.
     * 
     * @param node
     *            the abstraction to be visited
     */
    public void visit(LambdaAbstraction node);

    /**
     * Visits the given lambda variable.
     * 
     * @param node
     *            the variable to be visited
     */
    public void visit(LambdaVariable node);

    /**
     * Returns the result of the visit.
     * 
     * @return the result of the visit
     */
    public T getResult();
}
