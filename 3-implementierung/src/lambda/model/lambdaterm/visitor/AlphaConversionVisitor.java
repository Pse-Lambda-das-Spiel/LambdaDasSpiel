package lambda.model.lambdaterm.visitor;

import com.badlogic.gdx.graphics.Color;

import lambda.Consumer;
import lambda.model.lambdaterm.InvalidLambdaTermException;
import lambda.model.lambdaterm.LambdaAbstraction;
import lambda.model.lambdaterm.LambdaApplication;
import lambda.model.lambdaterm.LambdaRoot;
import lambda.model.lambdaterm.LambdaTermObserver;
import lambda.model.lambdaterm.LambdaVariable;

/**
 * Represents a visitor on a lambda term that performs an alpha conversion. Can only visit a valid lambda term.
 * 
 * @author Florian Fervers
 */
public class AlphaConversionVisitor extends ValidLambdaTermVisitor {
    /**
     * The old color to be replaced.
     */
    private final Color oldColor;
    /**
     * The new replacing color;
     */
    private final Color newColor;
    /** 
     * Indicates whether the old color is bound by an abstraction between the first visited node and the current node.
     */
    private boolean colorBound;
    
    /**
     * Creates a new alpha conversion visitor.
     * 
     * @param oldColor the old color to be replaced
     * @param newColor the new replacing color
     * @throws IllegalArgumentException if oldColor is null or newColor is null
     */
    public AlphaConversionVisitor(Color oldColor, Color newColor) {
        super("Cannot perform an alpha conversion on an invalid term!");
        if (oldColor == null || newColor == null) {
            throw new IllegalArgumentException("Colors cannot be null!");
        }
        this.oldColor = oldColor;
        this.newColor = newColor;
        colorBound = false;
    }

    /**
     * Visits the given lambda root and traverses to the child node.
     * 
     * @param node the root to be visited
     * @throws InvalidLambdaTermException if the visited term is invalid
     */
    @Override
    public void visitValid(LambdaRoot node) {
        node.getChild().accept(this);
    }
    
    /**
     * Visits the given lambda application and traverses to both child nodes.
     * 
     * @param node the application to be visited
     * @throws InvalidLambdaTermException if the visited term is invalid
     */
    @Override
    public void visitValid(LambdaApplication node) {
        node.getLeft().accept(this);
        node.getRight().accept(this);
    }
    
    /**
     * Visits the given lambda abstraction and replaces the color if necessary. Then traverses to the child node.
     * 
     * @param node the abstraction to be visited
     * @throws InvalidLambdaTermException if the visited term is invalid
     */
    @Override
    public void visitValid(final LambdaAbstraction node) {
        boolean bindsOldColor = node.getColor().equals(oldColor);
        if (bindsOldColor) {
            assert(!colorBound); // Checked in validity test
            colorBound = true;
            if (node.setColor(newColor)) {
                node.notify(new Consumer<LambdaTermObserver>(){
                    @Override
                    public void accept(LambdaTermObserver observer) {
                        observer.alphaConverted(node, newColor);
                    }
                });
            }
        }
        node.getInside().accept(this);
        if (bindsOldColor) {
            colorBound = false;
        }
    }
    
    /**
     * Visits the given lambda variable and replaces the color if necessary.
     * 
     * @param node the variable to be visited
     * @throws InvalidLambdaTermException if the visited term is invalid
     */
    @Override
    public void visitValid(final LambdaVariable node) {
        if (node.getColor().equals(oldColor) && colorBound) {
            if (node.setColor(newColor)) {
                node.notify(new Consumer<LambdaTermObserver>(){
                    @Override
                    public void accept(LambdaTermObserver observer) {
                        observer.alphaConverted(node, newColor);
                    }
                });
            }
        }
    }
    
	@Override
	public Object getResult() {
		return null;
	}
}
