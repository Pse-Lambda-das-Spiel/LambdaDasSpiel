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
     * Indicates which colors should be collected by this visitor.
     */
    public enum Type {
        /**
         * Collect all colors.
         */
        ALL,
        /**
         * Collect only colors of bound variables.
         */
        BOUND,
        /**
         * Collect only colors of free variables.
         */
        FREE
    }
    /**
     * The set of used colors.
     */
    private final Set<Color> result;
    /**
     * Indicates which colors should be collected by this visitor.
     */
    private final Type type;
    
    /**
     * Creates a ColorCollectionVisitor.
     * 
     * @param type indicates which colors should be collected by this visitor.
     */
    public ColorCollectionVisitor(Type type) {
        result = new HashSet<>();
        this.type = type;
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
     * Visits the given lambda abstraction and saves the color if necessary. Then traverses to the child node if possible.
     * 
     * @param node the abstraction to be visited
     */
    @Override
    public void visit(LambdaAbstraction node) {
        if (type == Type.ALL || type == Type.BOUND) {
            result.add(node.getColor());
        }
        if (node.getInside() != null) {
            node.getInside().accept(this);
        }
    }
    
    /**
     * Visits the given lambda variable and saves the color if necessary.
     * 
     * @param node the variable to be visited
     * @throws InvalidLambdaTermException if the visited term is invalid
     */
    @Override
    public void visit(LambdaVariable node) {
        boolean bound = node.accept(new IsColorBoundVisitor(node.getColor()));
        if (type == Type.ALL || (type == Type.BOUND && bound) || (type == Type.FREE && !bound)) {
            result.add(node.getColor());
        }
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
