package lambda.model.lambdaterm.visitor;

import lambda.model.lambdaterm.LambdaAbstraction;
import lambda.model.lambdaterm.LambdaApplication;
import lambda.model.lambdaterm.LambdaRoot;
import lambda.model.lambdaterm.LambdaTerm;
import lambda.model.lambdaterm.LambdaVariable;

/**
 * Represents a visitor on a lambda term that copies the visited lambda term. Can copy invalid lambda terms.
 * 
 * @author Florian Fervers
 */
public class CopyVisitor implements LambdaTermVisitor<LambdaTerm> {
    /**
     * Stores the resulting copy.
     */
    private LambdaTerm result;
    
    /**
     * Creates a new CopyVisitor.
     */
    public CopyVisitor() {
        result = null;
    }

    /**
     * Visits the given lambda root. Saves a new root as result and copies the child node into the new root by traversing down the tree.
     * 
     * @param node the root to be visited
     */
    @Override
    public void visit(LambdaRoot node) {
        LambdaRoot root = new LambdaRoot();
        
        LambdaTerm child = null;
        if (node.getChild() != null) {
            child = node.getChild().accept(this);
        }
        
        root.setChild(child);
        result = root;
    }
    
    /**
     * Visits the given lambda application. Saves a new application as result and copies the child nodes into the new application by traversing down the tree.
     * 
     * @param node the application to be visited
     */
    @Override
    public void visit(LambdaApplication node) {
        LambdaApplication application = new LambdaApplication(null, node.isLocked());
        
        LambdaTerm left = null;
        LambdaTerm right = null;
        if (node.getLeft() != null) {
            left = node.getLeft().accept(this);
        }
        if (node.getRight() != null) {
            right = node.getRight().accept(this);
        }
        
        application.setLeft(left);
        application.setRight(right);
        result = application;
    }
    
    /**
     * Visits the given lambda abstraction. Saves a new abstraction as result and copies the child node into the new abstraction by traversing down the tree.
     * 
     * @param node the abstraction to be visited
     */
    @Override
    public void visit(LambdaAbstraction node) {
        LambdaAbstraction abstraction = new LambdaAbstraction(null, node.getColor(), node.isLocked());
        
        LambdaTerm inside = null;
        if (node.getInside() != null) {
            inside = node.getInside().accept(this);
        }
        
        abstraction.setInside(inside);
        result = abstraction;
    }
    
    /**
     * Visits the given lambda variable saves a copy of the variable as result.
     * 
     * @param node the variable to be visited
     */
    @Override
    public void visit(LambdaVariable node) {
        result = new LambdaVariable(null, node.getColor(), node.isLocked());
    }
    
    /**
     * Returns the resulting copy.
     * 
     * @return the resulting copy
     */
    @Override
    public LambdaTerm getResult() {
        return result;
    }
}
