package lambda.model.lambdaterm.visitor.strategy;

import lambda.Consumer;
import lambda.model.lambdaterm.LambdaAbstraction;
import lambda.model.lambdaterm.LambdaApplication;
import lambda.model.lambdaterm.InvalidLambdaTermException;
import lambda.model.lambdaterm.LambdaTermObserver;
import lambda.model.lambdaterm.visitor.ApplicationVisitor;

/**
 * Represents a visitor on a lambda term that performs a beta reduction with
 * call-by-value strategy. Can only visit a valid lambda term.
 *
 * Strategy: 1. Applications: Right child reduced before left child 2.
 * Abstractions: Apply only arguments that are values, don't reduce inner term
 *
 * @author Florian Fervers
 */
public class ReductionStrategyCallByValue extends BetaReductionVisitor {
    /**
     * Creates a new instance of ReductionStrategyCallByValue.
     */
    public ReductionStrategyCallByValue() {
    }

    /**
     * Visits the given lambda application and performs a call-by-value
     * reduction if possible.
     *
     * @param node
     *            the application to be visited
     * @throws InvalidLambdaTermException
     *             if the visited term is invalid
     */
    @Override
    public void visitValid(LambdaApplication node) {
        if (!hasReduced) {
            // Continue if no reduction was performed so far
            // Right child first
            applicant = null;
            node.setRight(node.getRight().accept(this));

            if (!hasReduced) {
                // No reduction happened in right child => continue with left
                // child
                applicant = node.getRight();
                node.setLeft(node.getLeft().accept(this));
                if (node.getRight() == null) {
                    // Left child is abstraction and application is performed =>
                    // result is in left child node
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
     * Visits the given lambda abstraction and performs a call-by-value
     * reduction if possible.
     *
     * @param node
     *            the abstraction to be visited
     * @throws InvalidLambdaTermException
     *             if the visited term is invalid
     */
    @Override
    public void visitValid(final LambdaAbstraction node) {
        if (!hasReduced && applicant != null && applicant.isValue()) {
            // Perform application since argument is a value (abstraction or
            // variable)
            node.notify(new Consumer<LambdaTermObserver>() {
                @Override
                public void accept(LambdaTermObserver observer) {
                    observer.applicationStarted(node, applicant);
                }
            });
            result = node.getInside().accept(
                    new ApplicationVisitor(node.getColor(), applicant,
                            alphaConversionColors));
            applicant = null;
            hasReduced = true;
            // Don't reduce inside abstractions
        } else {
            result = node;
        }
    }
}
