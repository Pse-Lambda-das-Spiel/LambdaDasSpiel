package lambda.model.lambdaterm.visitor;

import java.util.Map;
import java.util.HashMap;

import lambda.model.lambdaterm.LambdaAbstraction;
import lambda.model.lambdaterm.LambdaApplication;
import lambda.model.lambdaterm.LambdaRoot;
import lambda.model.lambdaterm.LambdaVariable;

/**
 * Represents a visitor on a lambda term that counts the occurrences of lambda
 * abstractions and lambda variables inside the term and returns them as a list
 * of integers.
 *
 * @author Robert Hochweiss
 */
public class LambdaValueCounterVisitor implements
        LambdaTermVisitor<Map<String, Integer>> {

    private Map<String, Integer> result;
    private int abstractionNumber;
    private int variableNumber;

    /**
     * Creates a new LambdaValueCounterVsitor.
     */
    public LambdaValueCounterVisitor() {
        result = new HashMap<>();
    }

    /**
     * Visits the given lambda root and traverses to the child node if possible.
     * Adds the number of occurrences of lambda abstractions and lambda
     * variables inside the given lambda root in result afterwards.
     *
     * @param node
     *            the root to be visited
     * @throws IllegalArgumentException
     *             if node is null
     */
    @Override
    public void visit(LambdaRoot node) {
        if (node == null) {
            throw new IllegalArgumentException("node cannot be null!");
        }
        if (node.getChild() != null) {
            node.getChild().accept(this);
        }
    }

    /**
     * Visits the given lambda application and traverses to both child nodes if
     * possible.
     *
     * @param node
     *            the application to be visited
     * @throws IllegalArgumentException
     *             if node is null
     */
    @Override
    public void visit(LambdaApplication node) {
        if (node == null) {
            throw new IllegalArgumentException("node cannot be null!");
        }
        if (node.getLeft() != null) {
            node.getLeft().accept(this);
        }
        if (node.getRight() != null) {
            node.getRight().accept(this);
        }
    }

    /**
     * Visits the given lambda abstraction and increments the number of lambda
     * abstraction occurrences. Then traverses to the child node if possible.
     *
     * @param node
     *            the abstraction to be visited
     * @throws IllegalArgumentException
     *             if node is null
     */
    @Override
    public void visit(LambdaAbstraction node) {
        if (node == null) {
            throw new IllegalArgumentException("node cannot be null!");
        }
        abstractionNumber++;
        if (node.getInside() != null) {
            node.getInside().accept(this);
        }
    }

    /**
     * Visits the given lambda variable and increments the number of lambda
     * variable occurrences.
     *
     * @param node
     *            the variable to be visited
     * @throws IllegalArgumentException
     *             if node is null
     */
    @Override
    public void visit(LambdaVariable node) {
        if (node == null) {
            throw new IllegalArgumentException("node cannot be null!");
        }
        variableNumber++;
    }

    /**
     * Returns the map with the number of occurrences of lambda abstractions and
     * values inside the visited lambda term.
     *
     * @return the map with the number of occurrences of lambda abstractions and
     *         values inside the visited lambda term
     */
    @Override
    public Map<String, Integer> getResult() {
        result.put("lambs", abstractionNumber);
        result.put("gems", variableNumber);
        return result;
    }
}
