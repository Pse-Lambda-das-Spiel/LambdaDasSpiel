package lambda.model.lambdaterm.visitor;

import lambda.model.lambdaterm.LambdaAbstraction;
import lambda.model.lambdaterm.LambdaApplication;
import lambda.model.lambdaterm.LambdaRoot;
import lambda.model.lambdaterm.LambdaVariable;
import lambda.model.levels.LevelManager;

/**
 * Represents a visitor on a lambda term that converts a lambda term to a
 * string.
 *
 * @author Florian Fervers
 */
public class ToStringVisitor implements LambdaTermVisitor<String> {
    /**
     * Stores the resulting string.
     */
    private String result;
    /**
     * Indicates whether the currently visited node is the right child of an
     * application. Possibly requires paranthesis.
     */
    private boolean isRightApplicationChild;
    /**
     * Indicates whether the currently visited node is the left child of an
     * application. Possibly requires paranthesis.
     */
    private boolean isLeftApplicationChild;

    /**
     * Creates a new ToStringVisitor.
     */
    public ToStringVisitor() {
        isRightApplicationChild = false;
    }

    /**
     * Visits the given lambda root and saves the string representation as a
     * result.
     *
     * @param node the root to be visited
     */
    @Override
    public void visit(LambdaRoot node) {
        if (node.getChild() != null) {
            result = node.getChild().accept(this);
        } else {
            result = "Empty root";
        }
    }

    /**
     * Visits the given lambda application and saves the string representation
     * as a result.
     *
     * @param node the application to be visited
     */
    @Override
    public void visit(LambdaApplication node) {
        boolean paranthesis = isRightApplicationChild;

        String left;
        if (node.getLeft() != null) {
            isLeftApplicationChild = true;
            isRightApplicationChild = false;
            left = node.getLeft().accept(this);
        } else {
            left = "null";
        }

        String right;
        if (node.getRight() != null) {
            isLeftApplicationChild = false;
            isRightApplicationChild = true;
            right = node.getRight().accept(this);
        } else {
            right = "null";
        }

        isLeftApplicationChild = false;
        isRightApplicationChild = false;
        result = (paranthesis ? "(" : "") + left + " " + right + (paranthesis ? ")" : "");
    }

    /**
     * Visits the given lambda abstraction and saves the string representation
     * as a result.
     *
     * @param node the abstraction to be visited
     */
    @Override
    public void visit(LambdaAbstraction node) {
        boolean paranthesis = isLeftApplicationChild || isRightApplicationChild;
        isLeftApplicationChild = false;
        isRightApplicationChild = false;

        String inside;
        if (node.getInside() != null) {
            inside = node.getInside().accept(this);
        } else {
            inside = "null";
        }

        result = (paranthesis ? "(" : "") + "/" + Character.toString(LevelManager.convertColorToVariable(node.getColor())) + "." + inside + (paranthesis ? ")" : "");
    }

    /**
     * Visits the given lambda variable and saves the string representation as a
     * result.
     *
     * @param node the variable to be visited
     */
    @Override
    public void visit(LambdaVariable node) {
        result = Character.toString(LevelManager.convertColorToVariable(node.getColor()));
    }

    /**
     * Returns the string representation of the visited lambda term.
     *
     * @return the string representation of the visited lambda term
     */
    @Override
    public String getResult() {
        return result;
    }
}
