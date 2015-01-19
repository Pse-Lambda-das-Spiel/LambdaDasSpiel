package lambda.model.lambdaterm.visitor;

import java.awt.Color;
import java.util.Set;
import lambda.model.lambdaterm.InvalidLambdaTermException;
import lambda.model.lambdaterm.LambdaAbstraction;
import lambda.model.lambdaterm.LambdaApplication;
import lambda.model.lambdaterm.LambdaRoot;
import lambda.model.lambdaterm.LambdaTerm;
import lambda.model.lambdaterm.LambdaVariable;

/**
 * Represents a visitor on a lambda term that performs an application. Can only visit valid terms.
 * 
 * @author Florian Fervers
 */
public class ApplicationVisitor implements LambdaTermVisitor<LambdaTerm> {
    /**
     * The color of the variables that are replaced in the application.
     */
    private final Color color;
    /**
     * The applicant of the application.
     */
    private final LambdaTerm applicant;
    /**
     * Stores the result of the application.
     */
    private LambdaTerm result;
    /**
     * Stores whether the visitor has checked the visited term for validity and if an alpha conversion is necessary before the application.
     */
    private boolean hasCheckedValidityAndAlphaConversion;
    
    /**
     * Creates a new application visitor.
     * 
     * @param color the color of the variables to be replaced
     * @param applicant the applicant that will replace variables
     * @throws IllegalArgumentException if color is null or applicant is null
     */
    public ApplicationVisitor(Color color, LambdaTerm applicant) {
        if (color == null) {
            throw new IllegalArgumentException("Color cannot be null!");
        }
        if (applicant == null) {
            throw new IllegalArgumentException("Applicant cannot be null!");
        }
        this.color = color;
        this.applicant = applicant;
        result = null;
        hasCheckedValidityAndAlphaConversion = false;
    }

    /**
     * Cannot happen since the visited node is always descendant of an abstraction.
     * 
     * @param node the root to be visited
     */
    @Override
    public void visit(LambdaRoot node) {
        assert(false);
    }
    
    /**
     * Visits the given lambda application and traverses to both child nodes. Saves the application as result.
     * 
     * @param node the application to be visited
     * @throws InvalidLambdaTermException if the visited term is invalid
     */
    @Override
    public void visit(LambdaApplication node) {
        checkValidityAndAlphaConversion(node);
        node.setLeft(node.getLeft().accept(this));
        node.setRight(node.getRight().accept(this));
        result = node;
    }
    
    /**
     * Visits the given lambda abstraction and and traverses to the child node. Saves the abstraction as result.
     * 
     * @param node the abstraction to be visited
     * @throws InvalidLambdaTermException if the visited term is invalid
     */
    @Override
    public void visit(LambdaAbstraction node) {
        checkValidityAndAlphaConversion(node);
        node.setInside(node.getInside().accept(this));
        result = node;
    }
    
    /**
     * Visits the given lambda variable. Saves a copy of the applicant as result if the variable's color is equal to the saved color. Saves the variable as result otherwise.
     * 
     * @param node the variable to be visited
     * @throws InvalidLambdaTermException if the visited term is invalid
     */
    @Override
    public void visit(LambdaVariable node) {
        checkValidityAndAlphaConversion(node);
        result = node.getColor().equals(color) ? applicant.accept(new CopyVisitor()) : node;
    }
    
    /**
     * Checks the given term for validity and throws an exception if the term is invalid. Also checks if an alpha conversion is necessary and performs it if necessary. Removes the applicant from its parent in order for the application node to know that it was reduced.
     * 
     * @param term the term to be checked
     * @throws InvalidLambdaTermException if the term is invalid
     */
    private void checkValidityAndAlphaConversion(LambdaTerm term) {
        if (!hasCheckedValidityAndAlphaConversion) {
            hasCheckedValidityAndAlphaConversion = true;
            
            // Check term validity
            if (!term.accept(new IsValidVisitor())) {
                throw new InvalidLambdaTermException("Cannot perform an application on an invalid lambda term!");
            }
            
            // Perform alpha conversion if necessary
            // Find intersection of bound variables in left term and free variables in right term
            Set<Color> collidingColors = term.accept(new ColorCollectionVisitor(ColorCollectionVisitor.Type.BOUND));
            collidingColors.retainAll(applicant.accept(new ColorCollectionVisitor(ColorCollectionVisitor.Type.FREE)));
            collidingColors.remove(this.color);
            for (Color collision : collidingColors) {
                // TODO: generate new color
                Color replacingColor = new Color(0, 0, 0);
                for (char v = 'a'; v <= 'z'; v++) {
                    replacingColor = new Color(v, v, v);
                    if (!applicant.getParent().accept(new IsColorBoundVisitor(color)) && !term.accept(new ColorCollectionVisitor(ColorCollectionVisitor.Type.ALL)).contains(replacingColor)  && !applicant.accept(new ColorCollectionVisitor(ColorCollectionVisitor.Type.ALL)).contains(replacingColor)) {
                        break;
                    }
                }
                
                term.accept(new AlphaConversionVisitor(collision, replacingColor));
            }
            
            // Remove applicant to tell application that it was reduced
            applicant.accept(new RemoveTermVisitor());
        }
    }
    
    /**
     * Returns the resulting lambda term after the application.
     * 
     * @return the resulting lambda term after the application
     */
    @Override
    public LambdaTerm getResult() {
        return result;
    }
}
