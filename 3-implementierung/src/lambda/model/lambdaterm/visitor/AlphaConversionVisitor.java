package lambda.model.lambdaterm.visitor;

import java.awt.Color;
import lambda.model.lambdaterm.InvalidLambdaTermException;
import lambda.model.lambdaterm.LambdaAbstraction;
import lambda.model.lambdaterm.LambdaApplication;
import lambda.model.lambdaterm.LambdaRoot;
import lambda.model.lambdaterm.LambdaTerm;
import lambda.model.lambdaterm.LambdaVariable;

/**
 * Represents a visitor on a lambda term that performs an alpha conversion. Can only visit a valid lambda term.
 * 
 * @author Florian Fervers
 */
public class AlphaConversionVisitor implements LambdaTermVisitor {
    /**
     * The old color to be replaced.
     */
    private final Color oldColor;
    /**
     * The new replacing color;
     */
    private final Color newColor;
    /**
     * Stores whether the visitor has checked the visited term for validity.
     */
    private boolean hasCheckedValidity;
    
    /**
     * Creates a new alpha conversion visitor.
     * 
     * @param oldColor the old color to be replaced
     * @param newColor the new replacing color
     * @throws IllegalArgumentException if oldColor is null or newColor is null
     */
    public AlphaConversionVisitor(Color oldColor, Color newColor) {
        if (oldColor == null || newColor == null) {
            throw new IllegalArgumentException("Colors cannot be null!");
        }
        this.oldColor = oldColor;
        this.newColor = newColor;
        hasCheckedValidity = false;
    }

    /**
     * Visits the given lambda root and traverses to the child node.
     * 
     * @param node the root to be visited
     * @throws InvalidLambdaTermException if the visited term is invalid
     */
    @Override
    public void visit(LambdaRoot node) {
        checkValidity(node);
        node.getChild().accept(this);
    }
    
    /**
     * Visits the given lambda application and traverses to both child nodes.
     * 
     * @param node the application to be visited
     * @throws InvalidLambdaTermException if the visited term is invalid
     */
    @Override
    public void visit(LambdaApplication node) {
        checkValidity(node);
        node.getLeft().accept(this);
        node.getRight().accept(this);
    }
    
    /**
     * Visits the given lambda abstraction and replaces the color if possible. Then traverses to the child node.
     * 
     * @param node the abstraction to be visited
     * @throws InvalidLambdaTermException if the visited term is invalid
     */
    @Override
    public void visit(LambdaAbstraction node) {
        checkValidity(node);
        if (node.getColor().equals(oldColor)) {
            node.setColor(newColor);
        }
        node.getInside().accept(this);
    }
    
    /**
     * Visits the given lambda variable and replaces the color if possible.
     * 
     * @param node the variable to be visited
     * @throws InvalidLambdaTermException if the visited term is invalid
     */
    @Override
    public void visit(LambdaVariable node) {
        checkValidity(node);
        if (node.getColor().equals(oldColor)) {
            node.setColor(newColor);
        }
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
                throw new InvalidLambdaTermException("Cannot perform an alpha conversion on an invalid lambda term!");
            }
        }
    }
}
