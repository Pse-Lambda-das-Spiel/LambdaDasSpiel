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
public class ToStringVisitor implements LambdaTermVisitor<String> {
    /**
     * Stores whether the visitor has checked the visited term for validity.
     */
    private boolean hasCheckedValidity;
    /**
     * Stores the resulting string.
     */
    private String result;
    /**
     * Indicates whether the currently visited node is a right child of an application. Possibly requires paranthesis.
     */
    private boolean isRightApplicationChild;
    
    /**
     * Creates a new ToStringVisitor.
     */
    public ToStringVisitor() {
        hasCheckedValidity = false;
        isRightApplicationChild = false;
    }

    /**
     * Visits the given lambda root and saves the string representation as a result.
     * 
     * @param node the root to be visited
     * @throws InvalidLambdaTermException if the visited term is invalid
     */
    @Override
    public void visit(LambdaRoot node) {
        checkValidity(node);
        result = node.getChild().accept(this);
    }
    
    /**
     * Visits the given lambda application and saves the string representation as a result.
     * 
     * @param node the application to be visited
     * @throws InvalidLambdaTermException if the visited term is invalid
     */
    @Override
    public void visit(LambdaApplication node) {
        checkValidity(node);
        boolean paranthesis = isRightApplicationChild;
        isRightApplicationChild = false;
        String left = node.getLeft().accept(this);
        isRightApplicationChild = true;
        String right = node.getRight().accept(this);
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
    public void visit(LambdaAbstraction node) {
        checkValidity(node);
        result = "/" + ((char) node.getColor().getRed()) + "." + node.getInside().accept(this);
    }
    
    /**
     * Visits the given lambda variable and saves the string representation as a result.
     * 
     * @param node the variable to be visited
     * @throws InvalidLambdaTermException if the visited term is invalid
     */
    @Override
    public void visit(LambdaVariable node) {
        checkValidity(node);
        result = "" + ((char) node.getColor().getRed());
    }
    
    /**
     * Checks the given term for validity and throws an exception if the term is invalid.
     * 
     * @param term the term to be checked
     * @throws InvalidLambdaTermException if the term is invalid
     */
    private void checkValidity(LambdaTerm term) {
        if (!hasCheckedValidity) {
            hasCheckedValidity = true;
            if (!term.accept(new IsValidVisitor())) {
                throw new InvalidLambdaTermException("Cannot convert an invalid lambda term to string!");
            }
        }
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
