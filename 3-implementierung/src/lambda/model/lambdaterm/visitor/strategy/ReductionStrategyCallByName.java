package lambda.model.lambdaterm.visitor.strategy;

import lambda.Consumer;
import lambda.model.lambdaterm.LambdaAbstraction;
import lambda.model.lambdaterm.LambdaApplication;
import lambda.model.lambdaterm.InvalidLambdaTermException;
import lambda.model.lambdaterm.LambdaTermObserver;
import lambda.model.lambdaterm.visitor.ApplicationVisitor;

/**
 * Represents a visitor on a lambda term that performs a beta reduction with call-by-name strategy. Can only visit a valid lambda term.
 * 
 * Strategy:
 * 1. Applications: Left child reduced before right child
 * 2. Abstractions: Apply argument, don't reduce inner term
 * 
 * @author Florian Fervers
 */
public class ReductionStrategyCallByName extends BetaReductionVisitor {
    /**
     * Creates a new instance of ReductionStrategyCallByName.
     */
    public ReductionStrategyCallByName() {
    }
    
    /**
     * Visits the given lambda application and performs a call-by-name reduction if possible.
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
     * Visits the given lambda abstraction and performs a call-by-name reduction if possible.
     * 
     * @param node the abstraction to be visited
     * @throws InvalidLambdaTermException if the visited term is invalid
     */
    @Override
    public void visitValid(final LambdaAbstraction node) {
        if (!hasReduced && applicant != null) {
            // Perform application
            node.notify(new Consumer<LambdaTermObserver>(){
                @Override
                public void accept(LambdaTermObserver observer) {
                	observer.applicationStarted(node, applicant);
                }
            });
            result = node.getInside().accept(new ApplicationVisitor(node.getColor(), applicant));
            applicant = null;
            hasReduced = true;
            // Don't reduce inside abstractions
        } else {
            result = node;
        }
    }
}
