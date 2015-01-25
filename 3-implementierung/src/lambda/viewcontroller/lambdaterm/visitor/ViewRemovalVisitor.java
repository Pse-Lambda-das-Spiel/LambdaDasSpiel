package lambda.viewcontroller.lambdaterm.visitor;

import lambda.model.lambdaterm.LambdaAbstraction;
import lambda.model.lambdaterm.LambdaApplication;
import lambda.model.lambdaterm.LambdaRoot;
import lambda.model.lambdaterm.LambdaVariable;
import lambda.model.lambdaterm.visitor.LambdaTermVisitor;
import lambda.viewcontroller.lambdaterm.LambdaNodeViewController;
import lambda.viewcontroller.lambdaterm.LambdaTermViewController;

/**
 * Represents a visitor on a lambda term that removes the node that displays it from its parent and viewcontroller.
 * 
 * @author Florian Fervers
 */
public class ViewRemovalVisitor implements LambdaTermVisitor {
    /**
     * The view controller that the node will be removed from.
     */
    private final LambdaTermViewController viewController;
    
    /**
     * Creates a new ViewRemovalVisitor.
     * 
     * @param viewController the viewController that the node will be inserted into
     * @throws IllegalArgumentException if viewController is null
     */
    public ViewRemovalVisitor(LambdaTermViewController viewController) {
        if (viewController == null) {
            throw new IllegalArgumentException("ViewController cannot be null!");
        }
        this.viewController = viewController;
    }

    /**
     * Cannot happen since the root is never removed.
     * 
     * @param node the root to be visited
     */
    @Override
    public void visit(LambdaRoot node) {
        assert(false);
    }
    
    /**
     * Visits the given lambda application, traverses to the children and then removes this node from its parent.
     * 
     * @param node the application to be visited
     */
    @Override
    public void visit(LambdaApplication node) {
        if (node.getLeft() != null) {
            node.getLeft().accept(this);
        }
        if (node.getRight() != null) {
            node.getRight().accept(this);
        }
        if (viewController.hasNode(node)) {
            LambdaNodeViewController actor = viewController.getNode(node);
            actor.getParentNode().removeChild(actor);
        }
    }
    
    /**
     * Visits the given lambda abstraction, traverses to the child and then removes this node from its parent.
     * 
     * @param node the abstraction to be visited
     */
    @Override
    public void visit(LambdaAbstraction node) {
        if (node.getInside() != null) {
            node.getInside().accept(this);
        }
        assert(viewController.hasNode(node)); // Abstractions always have an explicit display
        LambdaNodeViewController actor = viewController.getNode(node);
        actor.getParentNode().removeChild(actor);
    }
    
    /**
     * Visits the given lambda variable and removes this node from its parent.
     * 
     * @param node the variable to be visited
     */
    @Override
    public void visit(LambdaVariable node) {
        assert(viewController.hasNode(node)); // Variables always have an explicit display
        LambdaNodeViewController actor = viewController.getNode(node);
        actor.getParentNode().removeChild(actor);
    }
}
