package lambda.model.lambdaterm.visitor;

import java.awt.Color;
import java.util.HashSet;
import java.util.Set;
import lambda.model.lambdaterm.InvalidLambdaTermException;
import lambda.model.lambdaterm.LambdaAbstraction;
import lambda.model.lambdaterm.LambdaApplication;
import lambda.model.lambdaterm.LambdaRoot;
import lambda.model.lambdaterm.LambdaVariable;

/**
 * Represents a visitor on a lambda term that collects all colors used in a term and returns them as a set. Can visit invalid terms.
 * 
 * @author Florian Fervers
 */
public class ColorCollectionVisitor implements LambdaTermVisitor<Set<Color>> {
    /**
     * The set of used colors.
     */
    private final Set<Color> result;
    
    /**
     * Creates a ColorCollectionVisitor.
     */
    public ColorCollectionVisitor() {
        result = new HashSet<>();
    }

    /**
     * Visits the given lambda root and traverses to the child node if possible.
     * 
     * @param node the root to be visited
     */
    @Override
    public void visit(LambdaRoot node) {
        if (node.getChild() != null) {
            node.getChild().accept(this);
        }
    }
    
    /**
     * Visits the given lambda application and traverses to both child nodes if possible.
     * 
     * @param node the application to be visited
     * @throws InvalidLambdaTermException if the visited term is invalid
     */
    @Override
    public void visit(LambdaApplication node) {
        if (node.getLeft() != null) {
            node.getLeft().accept(this);
        }
        if (node.getRight() != null) {
            node.getRight().accept(this);
        }
    }
    
    /**
     * Visits the given lambda abstraction and saves the color. Then traverses to the child node if possible.
     * 
     * @param node the abstraction to be visited
     */
    @Override
    public void visit(LambdaAbstraction node) {
        result.add(node.getColor());
        if (node.getInside() != null) {
            node.getInside().accept(this);
        }
    }
    
    /**
     * Visits the given lambda variable and adds the color.
     * 
     * @param node the variable to be visited
     * @throws InvalidLambdaTermException if the visited term is invalid
     */
    @Override
    public void visit(LambdaVariable node) {
        result.add(node.getColor());
    }
    
    /**
     * Returns the set of used colors in the visited term.
     * 
     * @return the set of used colors in the visited term
     */
    @Override
    public Set<Color> getResult() {
        return result;
    }
}
