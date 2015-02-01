package lambda.model.lambdaterm.visitor.strategy;

import lambda.model.lambdaterm.LambdaAbstraction;
import lambda.model.lambdaterm.LambdaApplication;
import lambda.model.lambdaterm.InvalidLambdaTermException;
import lambda.model.lambdaterm.LambdaTerm;
import lambda.model.lambdaterm.visitor.ApplicationVisitor;

/**
 * Represents a visitor on a lambda term that performs a beta reduction with applicative order strategy. Can only visit a valid lambda term.
 * 
 * Strategy:
 * 1. Applications: Right child reduced before left child
 * 2. Abstractions: Inner term reduced before applying arguments
 * 
 * @author Florian Fervers
 */
public class ReductionStrategyApplicativeOrder extends BetaReductionVisitor {
    /**
     * Creates a new instance of ReductionStrategyApplicativeOrder.
     */
    public ReductionStrategyApplicativeOrder() {
    }
    
    /**
     * Visits the given lambda application and performs an applicative order reduction if possible.
     * 
     * @param node the application to be visited
     * @throws InvalidLambdaTermException if the visited term is invalid
     */
    @Override
    public void visitValid(LambdaApplication node) {
        if (!hasReduced) {
            // Continue if no reduction was performed so far
            // Right child first
            applicant = null;
            node.setRight(node.getRight().accept(this));
            
            if (!hasReduced) {
                // No reduction happened in right child => continue with left child
                applicant = node.getRight();
                node.setLeft(node.getLeft().accept(this));
                if (node.getRight() == null) {
                    // Left child is abstraction and application is performed => result is in left child node
                    result = node.getLeft();
                } else {
                    // Application was not performed => result is visited node
                    result = node;
                }
            } else {
                result = node;
            }
            applicant = null;
        } else {
            result = node;
        }
    }
    
    /**
     * Visits the given lambda abstraction and performs an applicative order reduction if possible.
     * 
     * @param node the abstraction to be visited
     * @throws InvalidLambdaTermException if the visited term is invalid
     */
    @Override
    public void visitValid(LambdaAbstraction node) {
        if (!hasReduced) {
            // Traverse down the tree
            LambdaTerm myApplicant = applicant; // Save applicant since it can be changed down the tree
            node.setInside(node.getInside().accept(this));
            applicant = null;
            
            if (!hasReduced && myApplicant != null) {
                // No reduction performed in inner term and applicant is given => perform application
                node.notify(observer -> observer.applicationStarted(node, applicant));
                result = node.getInside().accept(new ApplicationVisitor(node.getColor(), myApplicant));
                hasReduced = true;
            } else {
                // Has performed reduction in the inner term or no applicant is given
                result = node;
            }
            
        } else {
            result = node;
        }
    }
}
