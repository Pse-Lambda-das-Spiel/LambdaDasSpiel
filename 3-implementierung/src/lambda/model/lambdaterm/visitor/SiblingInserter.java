package lambda.model.lambdaterm.visitor;

import lambda.model.lambdaterm.LambdaAbstraction;
import lambda.model.lambdaterm.LambdaApplication;
import lambda.model.lambdaterm.LambdaRoot;
import lambda.model.lambdaterm.LambdaTerm;
import lambda.model.lambdaterm.LambdaVariable;

/**
 * A visitor on a lambdaterm that inserts an application between the visited
 * node and its parent and adds a sibling to that application.
 *
 * @author Florian Fervers
 */
public class SiblingInserter implements LambdaTermVisitor {
    /**
     * The sibling to be inserted.
     */
    private final LambdaTerm sibling;
    /**
     * True if the sibling is inserted as the left application child, false if
     * it is inserted as the right child.
     */
    private final boolean left;
    /**
     * The child node that is visited first. Traverses to the parent node and
     * then inserts the new application.
     */
    private LambdaTerm oldChild;

    /**
     * Creates a new instance of SiblingInserter.
     *
     * @param sibling the sibling to be inserted
     * @param left true if the sibling is inserted as the left application
     * child, false if it is inserted as the right child
     */
    public SiblingInserter(LambdaTerm sibling, boolean left) {
        this.sibling = sibling;
        this.left = left;
        oldChild = null;
    }

    /**
     * Visits the given lambda root.
     *
     * @param node the root to be visited
     */
    @Override
    public void visit(LambdaRoot node) {
        if (oldChild == null) {
            assert(false); // Roots don't have parents
        } else {
            node.setChild(buildApplication(node));
        }
    }

    /**
     * Visits the given lambda application.
     *
     * @param node the application to be visited
     */
    @Override
    public void visit(LambdaApplication node) {
        if (oldChild == null) {
            oldChild = node;
            node.getParent().accept(this);
        } else {
            if (node.getLeft() == oldChild) {
                node.setLeft(buildApplication(node));
            } else {
                assert(node.getRight() == oldChild);
                node.setRight(buildApplication(node));
            }
        }
    }

    /**
     * Visits the given lambda abstraction.
     *
     * @param node the abstraction to be visited
     */
    @Override
    public void visit(LambdaAbstraction node) {
        if (oldChild == null) {
            oldChild = node;
            node.getParent().accept(this);
        } else {
            node.setInside(buildApplication(node));
        }
    }

    /**
     * Visits the given lambda variable.
     *
     * @param node the variable to be visited
     */
    @Override
    public void visit(LambdaVariable node) {
        if (oldChild == null) {
            oldChild = node;
            node.getParent().accept(this);
        } else {
            assert(false); // Variables don't have children
        }
    }

    /**
     * Builds the inserted application for the given parent.
     *
     * @param parent the inserted application's parent
     * @return the built application
     */
    private LambdaApplication buildApplication(LambdaTerm parent) {
        assert(oldChild.getParent() == parent);
        LambdaApplication application = new LambdaApplication(null, sibling.isLocked());
        if (left) {
            application.setLeft(sibling);
            application.setRight(oldChild);
        } else {
            application.setRight(sibling);
            application.setLeft(oldChild);
        }
        application.setParent(parent);
        return application;
    }

    @Override
    public Object getResult() {
        return null;
    }
}
