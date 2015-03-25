package lambda.model.lambdaterm.visitor.strategy;

import lambda.model.lambdaterm.visitor.*;

import lambda.model.lambdaterm.LambdaAbstraction;
import lambda.model.lambdaterm.LambdaApplication;
import lambda.model.lambdaterm.LambdaRoot;
import lambda.model.lambdaterm.LambdaVariable;

/**
 * Represents a visitor on a lambda term that counts the number of lambda nodes
 * used.
 *
 * @author Florian Fervers
 */
public class NodeCounter implements LambdaTermVisitor<Integer> {
    /**
     * The number of nodes found so far.
     */
    private int result;

    /**
     * Creates a new IsValidVisitor.
     */
    public NodeCounter() {
        result = 0;
    }

    /**
     * Visits the given lambda root, updates the counter and traverses
     * downwards.
     *
     * @param node the root to be visited
     */
    @Override
    public void visit(LambdaRoot node) {
        result++;
        if (node.getChild() != null) {
            node.getChild().accept(this);
        }
    }

    /**
     * Visits the given lambda application, updates the counter and traverses
     * downwards.
     *
     * @param node the application to be visited
     */
    @Override
    public void visit(LambdaApplication node) {
        result++;
        if (node.getLeft() != null) {
            node.getLeft().accept(this);
        }
        if (node.getRight() != null) {
            node.getRight().accept(this);
        }
    }

    /**
     * Visits the given lambda abstraction, updates the counter and traverses
     * downwards.
     *
     * @param node the abstraction to be visited
     */
    @Override
    public void visit(LambdaAbstraction node) {
        result++;
        if (node.getInside() != null) {
            node.getInside().accept(this);
        }
    }

    /**
     * Visits the given lambda variable, updates the counter and traverses
     * downwards.
     */
    @Override
    public void visit(LambdaVariable node) {
        result++;
    }

    /**
     * Returns the number of nodes found in the visited term.
     *
     * @return the number of nodes found in the visited term
     */
    @Override
    public Integer getResult() {
        return result;
    }
}
