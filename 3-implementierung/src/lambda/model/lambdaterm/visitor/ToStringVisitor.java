package lambda.model.lambdaterm.visitor;

import lambda.model.lambdaterm.InvalidLambdaTermException;
import lambda.model.lambdaterm.LambdaAbstraction;
import lambda.model.lambdaterm.LambdaApplication;
import lambda.model.lambdaterm.LambdaRoot;
import lambda.model.lambdaterm.LambdaTerm;
import lambda.model.lambdaterm.LambdaVariable;

/**
 * Represents a visitor on a lambda term that converts a lambda term to a string. Can only visit valid lambda terms.
 * 
 * @author Florian Fervers
 */
public class ToStringVisitor extends ValidLambdaTermVisitor<String> {
    /**
     * Stores the resulting string.
     */
    private String result;
    /**
     * Indicates whether the currently visited node is the right child of an application. Possibly requires paranthesis.
     */
    private boolean isRightApplicationChild;
    /**
     * Indicates whether the currently visited node is the left child of an application. Possibly requires paranthesis.
     */
    private boolean isLeftApplicationChild;
    
    /**
     * Creates a new ToStringVisitor.
     */
    public ToStringVisitor() {
        super("Cannot convert an invalid lambda term to string!");
        isRightApplicationChild = false;
    }

    /**
     * Visits the given lambda root and saves the string representation as a result.
     * 
     * @param node the root to be visited
     * @throws InvalidLambdaTermException if the visited term is invalid
     */
    @Override
    public void visitValid(LambdaRoot node) {
        result = node.getChild().accept(this);
    }
    
    /**
     * Visits the given lambda application and saves the string representation as a result.
     * 
     * @param node the application to be visited
     * @throws InvalidLambdaTermException if the visited term is invalid
     */
    @Override
    public void visitValid(LambdaApplication node) {
        boolean paranthesis = isRightApplicationChild;
        isLeftApplicationChild = true;
        isRightApplicationChild = false;
        String left = node.getLeft().accept(this);
        isLeftApplicationChild = false;
        isRightApplicationChild = true;
        String right = node.getRight().accept(this);
        isLeftApplicationChild = false;
        isRightApplicationChild = false;
        result = (paranthesis ? "(" : "") + left + " " + right + (paranthesis ? ")" : "");
    }
    
    /**
     * Visits the given lambda abstraction and saves the string representation as a result.
     * 
     * @param node the abstraction to be visited
     * @throws InvalidLambdaTermException if the visited term is invalid
     */
    @Override
    public void visitValid(LambdaAbstraction node) {
        boolean paranthesis = isLeftApplicationChild || isRightApplicationChild;
        isLeftApplicationChild = false;
        isRightApplicationChild = false;
        result = (paranthesis ? "(" : "") + "/" + Character.toString((char) node.getColor().getRed()) + "." + node.getInside().accept(this) + (paranthesis ? ")" : "");
    }
    
    /**
     * Visits the given lambda variable and saves the string representation as a result.
     * 
     * @param node the variable to be visited
     * @throws InvalidLambdaTermException if the visited term is invalid
     */
    @Override
    public void visitValid(LambdaVariable node) {
        result = Character.toString((char) node.getColor().getRed());
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
