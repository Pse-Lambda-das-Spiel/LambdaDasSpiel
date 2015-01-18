package lambda.model.lambdaterm.visitor;

import java.awt.Color;
import java.util.Stack;
import lambda.model.lambdaterm.LambdaAbstraction;
import lambda.model.lambdaterm.LambdaApplication;
import lambda.model.lambdaterm.LambdaRoot;
import lambda.model.lambdaterm.LambdaUtils;
import lambda.model.lambdaterm.LambdaVariable;

/**
 * Represents a visitor on a lambda term that checks if the term is a valid lambda term. A term is valid iff 
 *      1. the root of the tree is an instance of LambdaRoot,
 *      2. no children are null and
 *      3. no color is bound in an abstraction and in one of its descending abstractions at the same time.
 * 
 * @author Florian Fervers
 */
public class IsValidVisitor implements LambdaTermVisitor<Boolean> {
    /**
     * Stores whether the visitor has visited the root already.
     */
    private boolean hasVisitedRoot;
    /**
     * Stores whether the nodes that were visited so far are valid lambda nodes.
     */
    private boolean result;
    /**
     * Stores all bound colors till the currently visited node. Whenever an abstraction is visited the bound color is pushed on the stack and when the abstraction is left popped from the stack.
     */
    private final Stack<Color> mBoundColors;
    
    /**
     * Creates a new IsValidVisitor.
     */
    public IsValidVisitor() {
        hasVisitedRoot = false;
        result = true;
        mBoundColors = new Stack<>();
    }

   /**
     * Visits the given lambda root and checks whether it is a valid node. Then traverses to the child node.
     * 
     * @param node the root to be visited
     */
    @Override
    public void visit(LambdaRoot node) {
        if (result) {
            if (!hasVisitedRoot) {
                hasVisitedRoot = true;
                LambdaUtils.getRoot(node).accept(this);
            } else {
                result &= node.getParent() == null && node.getChild() != null;
                node.getChild().accept(this);
            }
        }
    }
    
    /**
     * Visits the given lambda application and checks whether it is a valid node. Then traverses to both child nodes.
     * 
     * @param node the application to be visited
     */
    @Override
    public void visit(LambdaApplication node) {
        if (result) {
            if (!hasVisitedRoot) {
                hasVisitedRoot = true;
                LambdaUtils.getRoot(node).accept(this);
            } else {
                result &= node.getParent() != null && node.getLeft() != null && node.getRight() != null;
                node.getLeft().accept(this);
                node.getRight().accept(this);
            }
        }
    }
    
    /**
     * Visits the given lambda abstraction and checks whether it is a valid node. Then traverses to the child node.
     * 
     * @param node the abstraction to be visited
     */
    @Override
    public void visit(LambdaAbstraction node) {
        if (result) {
            if (!hasVisitedRoot) {
                hasVisitedRoot = true;
                LambdaUtils.getRoot(node).accept(this);
            } else {
                result &= node.getParent() != null && node.getInside() != null && !mBoundColors.contains(node.getColor());
                mBoundColors.push(node.getColor());
                node.getInside().accept(this);
                mBoundColors.pop();
            }
        }
    }
    
    /**
     * Visits the given lambda variable and checks whether it is a valid node.
     * 
     * @param node the variable to be visited
     */
    @Override
    public void visit(LambdaVariable node) {
        if (result) {
            if (!hasVisitedRoot) {
                hasVisitedRoot = true;
                LambdaUtils.getRoot(node).accept(this);
            } else {
                result &= node.getParent() != null;
            }
        }
    }
    
    /**
     * Returns whether the visited lambda term is valid.
     * 
     * @return true if the visited lambda term is valid, false otherwise
     */
    @Override
    public Boolean getResult() {
        return result;
    }
}
