package lambda.model.lambdaterm.visitor;

import lambda.model.lambdaterm.LambdaAbstraction;
import lambda.model.lambdaterm.LambdaApplication;
import lambda.model.lambdaterm.LambdaRoot;
import lambda.model.lambdaterm.LambdaTerm;
import lambda.model.lambdaterm.LambdaVariable;

/**
 * A visitor on a lambdaterm that inserts a given term as the first child of the visited term.
 * 
 * @author Florian Fervers
 */
public class FrontInserter implements LambdaTermVisitor {
    /**
     * The term to be inserted
     */
    private final LambdaTerm inserted;
    /**
     * Creates a new instance of SiblingInserter.
     * 
     * @param inserted the term to be inserted
     */
    public FrontInserter(LambdaTerm inserted) {
        this.inserted = inserted;
    }
    
    /**
     * Visits the given lambda root and inserts the node.
     * 
     * @param node the root to be visited
     */
    @Override
    public void visit(LambdaRoot node) {
        node.setChild(inserted);
    }
    
    /**
     * Visits the given lambda application and inserts the node as left child.
     * 
     * @param node the application to be visited
     */
    @Override
    public void visit(LambdaApplication node) {
        node.setLeft(inserted);
    }
    
    /**
     * Visits the given lambda abstraction and inserts the node.
     * 
     * @param node the abstraction to be visited
     */
    @Override
    public void visit(LambdaAbstraction node) {
        node.setInside(inserted);
    }
    
    /**
     * Visits the given lambda variable.
     * 
     * @param node the variable to be visited
     */
    @Override
    public void visit(LambdaVariable node) {
        assert(false);
    }
}
