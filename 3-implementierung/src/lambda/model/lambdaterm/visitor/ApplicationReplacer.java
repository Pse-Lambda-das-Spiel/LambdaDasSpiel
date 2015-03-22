package lambda.model.lambdaterm.visitor;

import lambda.model.lambdaterm.LambdaAbstraction;
import lambda.model.lambdaterm.LambdaApplication;
import lambda.model.lambdaterm.LambdaRoot;
import lambda.model.lambdaterm.LambdaVariable;

/**
 * A visitor that replaces the children of a given application with the children
 * of the visited application. Does nothing when visiting a term that is not an
 * application.
 *
 * @author Florian Fervers
 */
public class ApplicationReplacer implements LambdaTermVisitor<Object> {
    /**
     * The application whose children are to be replaced.
     */
    private final LambdaApplication replaced;

    /**
     * Creates a new ApplicationReplacer.
     *
     * @param replaced the application whose children are to be replaced
     */
    public ApplicationReplacer(LambdaApplication replaced) {
        this.replaced = replaced;
    }

    /**
     * Visits the given lambda root.
     *
     * @param node the root to be visited
     */
    @Override
    public void visit(LambdaRoot node) {
    }

    /**
     * Visits the given lambda application and checks whether it is a valid
     * node. Then traverses to both child nodes.
     *
     * @param node the application to be visited
     */
    @Override
    public void visit(LambdaApplication node) {
        replaced.setLeft(node.getLeft());
        replaced.setRight(node.getRight());
    }

    /**
     * Visits the given lambda abstraction and checks whether it is a valid
     * node. Then traverses to the child node.
     *
     * @param node the abstraction to be visited
     */
    @Override
    public void visit(LambdaAbstraction node) {
    }

    /**
     * Visits the given lambda variable and checks whether it is a valid node.
     *
     * @param node the variable to be visited
     */
    @Override
    public void visit(LambdaVariable node) {
    }

    @Override
    public Object getResult() {
        return null;
    }
}
