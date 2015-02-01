package lambda.model.lambdaterm.visitor.strategy;

import lambda.model.lambdaterm.LambdaAbstraction;
import lambda.model.lambdaterm.LambdaApplication;
import lambda.model.lambdaterm.InvalidLambdaTermException;
import lambda.model.lambdaterm.visitor.ApplicationVisitor;

/**
 * Represents a visitor on a lambda term that performs a beta reduction with normal order strategy. Can only visit a valid lambda term.
 * 
 * Strategy:
 * 1. Applications: Left child reduced before right child
 * 2. Abstractions: Apply arguments before reducing inner term
 * 
 * @author Florian Fervers
 */
public class ReductionStrategyNormalOrder extends BetaReductionVisitor {
    /**
     * Creates a new instance of ReductionStrategyNormalOrder.
     */
    public ReductionStrategyNormalOrder() {
    }
    
    /**
     * Visits the given lambda application and performs a normal order reduction if possible.
     * 
     * @param node the application to be visited
     * @throws InvalidLambdaTermException if the visited term is invalid
     */
    @Override
    public void visitValid(LambdaApplication node) {
        if (!hasReduced) {
            // Continue if no reduction was performed so far
            // Left child first
            applicant = node.getRight();
            node.setLeft(node.getLeft().accept(this));
            
            // Right child if no reduction was performed
            applicant = null;
            if (node.getRight() == null) {
                // Left child is abstraction and application is performed. Application result is in left child node
                result = node.getLeft();
            } else {
                if (!hasReduced) {
                    // No reduction happened in left child => continue with right child
                    node.setRight(node.getRight().accept(this));
                }
                result = node;
            }
        } else {
            result = node;
        }
    }
    
    /**
     * Visits the given lambda abstraction and performs a normal order reduction if possible.
     * 
     * @param node the abstraction to be visited
     * @throws InvalidLambdaTermException if the visited term is invalid
     */
    @Override
    public void visitValid(LambdaAbstraction node) {
        if (!hasReduced) {
            if (applicant != null) {
                // Perform application
                node.notify(observer -> observer.applicationStarted(node, applicant));
                result = node.getInside().accept(new ApplicationVisitor(node.getColor(), applicant));
                applicant = null;
                hasReduced = true;
            } else {
                // Continue with inner term
                node.setInside(node.getInside().accept(this));
                result = node;
            }
        } else {
            result = node;
        }
    }
}
