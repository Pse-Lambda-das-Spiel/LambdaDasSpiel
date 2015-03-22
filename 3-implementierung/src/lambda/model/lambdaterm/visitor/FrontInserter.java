package lambda.model.lambdaterm.visitor;

import lambda.model.lambdaterm.LambdaAbstraction;
import lambda.model.lambdaterm.LambdaApplication;
import lambda.model.lambdaterm.LambdaRoot;
import lambda.model.lambdaterm.LambdaTerm;
import lambda.model.lambdaterm.LambdaUtils;
import lambda.model.lambdaterm.LambdaVariable;
import lambda.model.lambdaterm.visitor.strategy.NodeCounter;

/**
 * A visitor on a lambdaterm that inserts a given term as the first child of the
 * visited term.
 *
 * @author Florian Fervers
 */
public class FrontInserter implements LambdaTermVisitor<Boolean> {
    /**
     * The term to be inserted
     */
    private final LambdaTerm inserted;
    /**
     * Indicates whether the lamdba term limits (i.e. maximum number of nodes)
     * have been checked.
     */
    private boolean hasCheckedTermLimits;
    /**
     * Indicates whether the term could be inserted.
     */
    private boolean result;

    /**
     * Creates a new instance of FrontInserter.
     *
     * @param inserted the term to be inserted
     */
    public FrontInserter(LambdaTerm inserted) {
        this.inserted = inserted;
        hasCheckedTermLimits = false;
        result = true;
    }

    /**
     * Visits the given lambda root and inserts the node.
     *
     * @param node the root to be visited
     */
    @Override
    public void visit(LambdaRoot node) {
        if (!checkTermLimits(node)) {
            return;
        }
        if (node.getChild() == null) {
            node.setChild(inserted);
        } else {
            node.setChild(buildApplication(node, node.getChild()));
        }
    }

    /**
     * Visits the given lambda application and inserts the node as left child.
     *
     * @param node the application to be visited
     */
    @Override
    public void visit(LambdaApplication node) {
        if (!checkTermLimits(node)) {
            return;
        }
        if (node.getLeft() == null) {
            // Insert left
            node.setLeft(inserted);
        } else if (node.getRight() == null) {
            // Push left to right, then insert left
            node.setRight(node.getLeft());
            node.setLeft(inserted);
        } else {
            // Insert left with new application
            node.setLeft(buildApplication(node, node.getLeft()));
        }
    }

    /**
     * Visits the given lambda abstraction and inserts the node.
     *
     * @param node the abstraction to be visited
     */
    @Override
    public void visit(LambdaAbstraction node) {
        if (!checkTermLimits(node)) {
            return;
        }
        if (node.getInside() == null) {
            node.setInside(inserted);
        } else {
            node.setInside(buildApplication(node, node.getInside()));
        }
    }

    /**
     * Visits the given lambda variable.
     *
     * @param node the variable to be visited
     */
    @Override
    public void visit(LambdaVariable node) {
        assert (false);
    }

    /**
     * Builds the inserted application for the given parent and sibling.
     *
     * @param parent the inserted application's parent
     * @param sibling the inserted element's new sibling
     * @return the built application
     */
    private LambdaApplication buildApplication(LambdaTerm parent, LambdaTerm sibling) {
        LambdaApplication application = new LambdaApplication(null, sibling.isLocked() || inserted.isLocked(), false);
        application.setLeft(inserted);
        application.setRight(sibling);
        application.setParent(parent);
        return application;
    }

    @Override
    public Boolean getResult() {
        return result;
    }

    /**
     * Checks whether the term can be inserted into the given term.
     *
     * @param visited the visited term
     * @return true if the insertion is valid, false otherwise
     */
    private boolean checkTermLimits(LambdaTerm visited) {
        if (!hasCheckedTermLimits) {
            hasCheckedTermLimits = true;
            result = LambdaUtils.getRoot(visited).accept(new NodeCounter()) + inserted.accept(new NodeCounter()) <= LambdaTerm.MAX_NODES_PER_TERM;
        }
        return result;
    }
}
