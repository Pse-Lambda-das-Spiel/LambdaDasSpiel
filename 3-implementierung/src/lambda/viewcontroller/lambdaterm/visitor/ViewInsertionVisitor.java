package lambda.viewcontroller.lambdaterm.visitor;

import lambda.model.lambdaterm.LambdaAbstraction;
import lambda.model.lambdaterm.LambdaApplication;
import lambda.model.lambdaterm.LambdaRoot;
import lambda.model.lambdaterm.LambdaTerm;
import lambda.model.lambdaterm.LambdaVariable;
import lambda.model.lambdaterm.visitor.LambdaTermVisitor;
import lambda.viewcontroller.lambdaterm.LambdaNodeViewController;
import lambda.viewcontroller.lambdaterm.LambdaTermViewController;

/**
 * Represents a visitor on a lambda term that inserts it recursively into a LambdaTermViewController. The first visited node must be the inserted element's parent. Finds parameters for the insertion and lets NodeViewControllerCreator insert the element and traverse to children.
 * 
 * @author Florian Fervers
 */
public class ViewInsertionVisitor implements LambdaTermVisitor {
    /**
     * Contains the node to be inserted into the view controller.
     */
    private final LambdaTerm inserted;
    /**
     * The view controller that the node will be inserted into.
     */
    private final LambdaTermViewController viewController;
    /**
     * The right sibling of the inserted element if it is the descendant of an application. Null if there is no right sibling.
     */
    private LambdaTerm rightSibling;
    /**
     * The element that was visited last by this visitor. Initialized with value of inserted.
     */
    private LambdaTerm lastVisited;
    /**
     * Indicates whether the inserted element is the right child of an application.
     */
    private boolean isRightApplicationChild;
    
    /**
     * Creates a new ViewInsertionVisitor.
     * 
     * @param inserted the inserted node
     * @param viewController the viewController that the node will be inserted into
     * @throws IllegalArgumentException if inserted is null or viewController is null
     */
    public ViewInsertionVisitor(LambdaTerm inserted, LambdaTermViewController viewController) {
        if (inserted == null) {
            throw new IllegalArgumentException("Inserted LambdaTerm cannot be null!");
        }
        if (viewController == null) {
            throw new IllegalArgumentException("ViewController cannot be null!");
        }
        this.inserted = inserted;
        this.viewController = viewController;
        rightSibling = null;
        lastVisited = inserted;
        isRightApplicationChild = false;
    }

    /**
     * Visits the given lambda root and inserts a new node under the node that displays the given root.
     * 
     * @param node the root to be visited
     */
    @Override
    public void visit(LambdaRoot node) {
        insertChild(viewController.getNode(node));
    }
    
    /**
     * Visits the given lambda application and find the next parent that is displayed by a node viewcontroller to insert the element there.
     * 
     * @param node the application to be visited
     */
    @Override
    public void visit(LambdaApplication node) {
        // Check for right sibling
        if (rightSibling == null && lastVisited == node.getLeft()) {
            rightSibling = node.getRight();
        }
        // Check if paranthesis are necessary around inserted element
        if (inserted == node.getRight()) {
            isRightApplicationChild = true;
        }
        
        if (viewController.hasNode(node)) {
            // Visited application is displayed by a parenthesis node => insert child here
            insertChild(viewController.getNode(node));
        } else {
            // Implicit parenthesis => traverse to parent
            lastVisited = node;
            node.getParent().accept(this);
        }
    }
    
    /**
     * Visits the given lambda abstraction and inserts a new node under the node that displays the given abstraction.
     * 
     * @param node the abstraction to be visited
     */
    @Override
    public void visit(LambdaAbstraction node) {
        insertChild(viewController.getNode(node));
    }
    
    /**
     * Visits the given lambda variable. Cannot happen since variables don't have children.
     * 
     * @param node the variable to be visited
     */
    @Override
    public void visit(LambdaVariable node) {
        assert(false);
    }
    
    /**
     * Inserts the given child under the given parent. Then recurses to the node's children.
     * 
     * @param parent the parent node
     */
    private void insertChild(LambdaNodeViewController parent) {
        assert(parent != null);
        inserted.accept(new NodeViewControllerCreator(parent, isRightApplicationChild, viewController, rightSibling));
    }
}
