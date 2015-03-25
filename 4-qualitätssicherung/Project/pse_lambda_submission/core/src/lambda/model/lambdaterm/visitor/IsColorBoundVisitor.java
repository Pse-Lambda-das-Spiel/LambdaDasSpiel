package lambda.model.lambdaterm.visitor;

import com.badlogic.gdx.graphics.Color;

import lambda.model.lambdaterm.InvalidLambdaTermException;
import lambda.model.lambdaterm.LambdaAbstraction;
import lambda.model.lambdaterm.LambdaApplication;
import lambda.model.lambdaterm.LambdaRoot;
import lambda.model.lambdaterm.LambdaVariable;

/**
 * Represents a visitor on a lambda term that checks whether a color is bound in the term. Can only visit a valid lambda term.
 * 
 * @author Florian Fervers
 */
public class IsColorBoundVisitor extends ValidLambdaTermVisitor<Boolean> {
    /**
     * The color to be checked.
     */
    private final Color color;
    /**
     * Stores whether the given color is bound in the visited term.
     */
    private boolean result;
    
    /**
     * Creates a new IsColorBoundVisitor.
     * 
     * @param color the color to be checked
     * @throws IllegalArgumentException if oldColor is null or newColor is null
     */
    public IsColorBoundVisitor(Color color) {
        super("Cannot check if a color is bound in an invalid lambda term!");
        if (color == null) {
            throw new IllegalArgumentException("Color cannot be null!");
        }
        this.color = color;
        result = false;
    }

    /**
     * Visits the given lambda root and does nothing.
     * 
     * @param node the root to be visited
     */
    @Override
    public void visitValid(LambdaRoot node) {
    }
    
    /**
     * Visits the given lambda application and traverses to the parent node.
     * 
     * @param node the application to be visited
     */
    @Override
    public void visitValid(LambdaApplication node) {
        if (!result) {
            node.getParent().accept(this);
        }
    }
    
    /**
     * Visits the given lambda abstraction and checks if the color is bound here. Then traverses to the parent node.
     * 
     * @param node the abstraction to be visited
     * @throws InvalidLambdaTermException if the visited abstraction binds the color that is being checked
     */
    @Override
    public void visitValid(LambdaAbstraction node) {
        // assert(!node.getColor().equals(color)); // Checked in validity
        if (!result) {
            if (node.getColor().equals(color)) {
                result = true;
            } else {
                node.getParent().accept(this);
            }
        }
    }
    
    /**
     * Visits the given lambda variable and traverses to the parent node .
     * 
     * @param node the variable to be visited
     * @throws InvalidLambdaTermException if the visited term is invalid
     */
    @Override
    public void visitValid(LambdaVariable node) {
        if (!result) {
            node.getParent().accept(this);
        }
    }
    
    /**
     * Returns whether the given color is bound in the visited term.
     * 
     * @return true if the given color is bound in the visited term, false otherwise
     */
    @Override
    public Boolean getResult() {
        return result;
    }
}
