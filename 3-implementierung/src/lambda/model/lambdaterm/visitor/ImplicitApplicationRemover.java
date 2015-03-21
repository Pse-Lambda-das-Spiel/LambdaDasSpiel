package lambda.model.lambdaterm.visitor;

import lambda.model.lambdaterm.LambdaAbstraction;
import lambda.model.lambdaterm.LambdaApplication;
import lambda.model.lambdaterm.LambdaRoot;
import lambda.model.lambdaterm.LambdaTerm;
import lambda.model.lambdaterm.LambdaVariable;

/**
 * Visits the given lambda term and removes all implicit redundant applications
 * inside its children.
 *
 * @author Florian Fervers
 */
public class ImplicitApplicationRemover implements LambdaTermVisitor<LambdaTerm> {
    /**
     * The result of the visit.
     */
    private LambdaTerm result;

    /**
     * Creates a new ImplicitApplicationRemover.
     */
    public ImplicitApplicationRemover() {
        result = null;
    }

    /**
     * Visits the given lambda root.
     *
     * @param node the root to be visited
     */
    @Override
    public void visit(LambdaRoot node) {
        if (node.getChild() != null) {
            node.setChild(node.getChild().accept(this));
        }
        result = node;
    }

    /**
     * Visits the given lambda application, traverses to both children and then
     * saves the correct result without implicit applications.
     *
     * @param node the application to be visited
     */
    @Override
    public void visit(LambdaApplication node) {
        if (node.getLeft() != null) {
            node.setLeft(node.getLeft().accept(this));
        }
        if (node.getRight() != null) {
            node.setRight(node.getRight().accept(this));
        }
        result = node;
        if (!node.isExplicit()) {
            if (node.getLeft() == null) {
                result = node.getRight();
            } else if (node.getRight() == null) {
                result = node.getLeft();
            }
        }
    }

    /**
     * Visits the given lambda abstraction and traverses to its child.
     *
     * @param node the abstraction to be visited
     */
    @Override
    public void visit(LambdaAbstraction node) {
        if (node.getInside() != null) {
            node.setInside(node.getInside().accept(this));
        }
        result = node;
    }

    /**
     * Visits the given lambda variable.
     *
     * @param node the variable to be visited
     */
    @Override
    public void visit(LambdaVariable node) {
        result = node;
    }

    /**
     * Returns the visited term without redundant applications.
     *
     * @return the visited term without redundant applications
     */
    @Override
    public LambdaTerm getResult() {
        return result;
    }
}
