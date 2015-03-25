package lambda.model.lambdaterm.visitor;

import lambda.model.lambdaterm.LambdaAbstraction;
import lambda.model.lambdaterm.LambdaApplication;
import lambda.model.lambdaterm.LambdaRoot;
import lambda.model.lambdaterm.LambdaTerm;
import lambda.model.lambdaterm.LambdaVariable;

/**
 * Represents a visitor on a lambda term that replaces this lambda term in its
 * parent with the given lambda term.
 *
 * @author Florian Fervers
 */
public class ReplaceTermVisitor implements LambdaTermVisitor {
    /**
     * The lambda term to be replaced.
     */
    private LambdaTerm replaced;
    /**
     * The replacing lambda term.
     */
    private final LambdaTerm replacing;

    /**
     * Creates a new ReplaceTermVisitor.
     *
     * @param replacing
     *            the replacing lambda term
     */
    public ReplaceTermVisitor(LambdaTerm replacing) {
        this.replaced = null;
        this.replacing = replacing;
    }

    /**
     * Visits the given lambda root. If there is a term to be replaced, replaces
     * it in this node.
     *
     * @param node
     *            the root to be visited
     */
    @Override
    public void visit(LambdaRoot node) {
        if (replaced != null) {
            assert (node.getChild() == replaced);
            node.setChild(replacing);
            replaced.setParent(null);
        }
    }

    /**
     * Visits the given lambda application. If there is a term to be replaced,
     * replaced it in this node. Saves this node as replaced node and traverses
     * to the parent node if possible.
     *
     * @param node
     *            the application to be visited
     */
    @Override
    public void visit(LambdaApplication node) {
        if (replaced != null) {
            if (node.getLeft() == replaced) {
                node.setLeft(replacing);
            } else {
                assert (node.getRight() == replaced);
                node.setRight(replacing);
            }
            replaced.setParent(null);
        } else if (node.getParent() != null) {
            replaced = node;
            node.getParent().accept(this);
        }
    }

    /**
     * Visits the given lambda abstraction. If there is a term to be replaced,
     * replaces it in this node. Saves this node as replaced node and traverses
     * to the parent node if possible.
     *
     * @param node
     *            the abstraction to be visited
     */
    @Override
    public void visit(LambdaAbstraction node) {
        if (replaced != null) {
            assert (node.getInside() == replaced);
            node.setInside(replacing);
            replaced.setParent(null);
        } else if (node.getParent() != null) {
            replaced = node;
            node.getParent().accept(this);
        }
    }

    /**
     * Visits the given lambda variable. Saves this node as replaced node and
     * traverses to the parent node if possible.
     *
     * @param node
     *            the variable to be visited
     */
    @Override
    public void visit(LambdaVariable node) {
        assert (replaced == null);
        if (node.getParent() != null) {
            replaced = node;
            node.getParent().accept(this);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getResult() {
        return null;
    }
}
