package lambda.model.lambdaterm.visitor;

import java.awt.Color;
import java.util.Stack;
import lambda.model.lambdaterm.InvalidLambdaTermException;
import lambda.model.lambdaterm.LambdaAbstraction;
import lambda.model.lambdaterm.LambdaApplication;
import lambda.model.lambdaterm.LambdaRoot;
import lambda.model.lambdaterm.LambdaTerm;
import lambda.model.lambdaterm.LambdaVariable;

/**
 * Represents a visitor on a lambda term that checks if it is equivalent with another lambda term. Can only check valid lambda terms.
 * 
 * @author Florian Fervers
 */
public class IsAlphaEquivalentVisitor extends ValidLambdaTermVisitor<Boolean> {
    /**
     * The other lambda term to compare the visited term with.
     */
    private LambdaTerm other;
    /**
     * Stores the result of the comparison.
     */
    private boolean result;
    /**
     * Stores the color translations of bound colors from the visited term to the other term. All elements are arrays of length 2. Everytime an abstraction is visited the color translation is pushed on the stack and popped when the abstraction if left.
     */
    private Stack<Color[]> colorMap;
    
    /**
     * Creates a new IsAlphaEquivalentVisitor.
     * 
     * @param other the other lambda term to compare the visited term with
     * @throws IllegalArgumentException if other is null
     */
    public IsAlphaEquivalentVisitor(LambdaTerm other) {
        super("Cannot perform an alpha equivalence check on an invalid lambda term!");
        if (other == null) {
            throw new IllegalArgumentException("Lambda term cannot be null!");
        }
        if (!other.accept(new IsValidVisitor())) {
            throw new InvalidLambdaTermException("Cannot perform an alpha equivalence check on an invalid lambda term!");
        }
        this.other = other;
        result = true;
        colorMap = new Stack<>();
    }

    /**
     * Visits the given lambda root and compares with other for alpha equivalence.
     * 
     * @param node the root to be visited
     * @throws InvalidLambdaTermException if the visited term is invalid
     */
    @Override
    public void visitValid(LambdaRoot node) {
        result &= other instanceof LambdaRoot;
        if (result) {
            other = ((LambdaRoot) other).getChild();
            node.getChild().accept(this);
        }
    }
    
    /**
     * Visits the given lambda application and compares with other for alpha equivalence.
     * 
     * @param node the application to be visited
     * @throws InvalidLambdaTermException if the visited term is invalid
     */
    @Override
    public void visitValid(LambdaApplication node) {
        result &= other instanceof LambdaApplication;
        if (result) {
            LambdaApplication otherApplication = ((LambdaApplication) other);
            other = otherApplication.getLeft();
            node.getLeft().accept(this);
            other = otherApplication.getRight();
            node.getRight().accept(this);
        }
    }
    
    /**
     * Visits the given lambda abstraction and compares with other for alpha equivalence.
     * 
     * @param node the abstraction to be visited
     * @throws InvalidLambdaTermException if the visited term is invalid
     */
    @Override
    public void visitValid(LambdaAbstraction node) {
        result &= other instanceof LambdaAbstraction;
        if (result) {
            // Push to stack: Translation from visited color to other term's color
            colorMap.push(new Color[]{node.getColor(), ((LambdaAbstraction) other).getColor()});
            
            // Traverse
            other = ((LambdaAbstraction) other).getInside();
            node.getInside().accept(this);
            
            // Pop bound color translation
            colorMap.pop();
        }
    }
    
    /**
     * Visits the given lambda variable and compares with other for alpha equivalence.
     * 
     * @param node the variable to be visited
     * @throws InvalidLambdaTermException if the visited term is invalid
     */
    @Override
    public void visitValid(LambdaVariable node) {
        result &= other instanceof LambdaVariable;
        if (result) {
            Color otherColor = ((LambdaVariable) other).getColor();
            boolean bound = false; // Indicates whether the visited variable is bound
            // Check all bound colors
            for (Color[] colorTranslation : colorMap) {
                assert(colorTranslation.length == 2);
                if (colorTranslation[0].equals(node.getColor())) {
                    bound = true;
                    // Color of visited variable is bound => color of other variable should be translated color
                    if (!colorTranslation[1].equals(otherColor)) {
                        result = false;
                        break;
                    }
                }
            }
            if (result && !bound && !node.getColor().equals(otherColor)) {
                // Color of visited variable is free => colors should be equal
                result = false;
            }
        }
    }
    
    /**
     * Return whether the visited and other terms are alpha equivalent.
     * 
     * @return true if the visited and other terms are alpha equivalent, false otherwise
     */
    @Override
    public Boolean getResult() {
        return result;
    }
}
