package lambda.viewcontroller.lambdaterm.visitor;

import lambda.model.lambdaterm.LambdaAbstraction;
import lambda.model.lambdaterm.LambdaApplication;
import lambda.model.lambdaterm.LambdaRoot;
import lambda.model.lambdaterm.LambdaTerm;
import lambda.model.lambdaterm.LambdaVariable;
import lambda.model.lambdaterm.visitor.LambdaTermVisitor;
import lambda.viewcontroller.lambdaterm.LambdaAbstractionViewController;
import lambda.viewcontroller.lambdaterm.LambdaNodeViewController;
import lambda.viewcontroller.lambdaterm.LambdaParenthesisViewController;
import lambda.viewcontroller.lambdaterm.LambdaTermViewController;
import lambda.viewcontroller.lambdaterm.LambdaVariableViewController;

/**
 * Represents a visitor on a lambda term that creates a new instance of the specific subclass of LambdaNodeViewController for the visited node type and inserts it into the given parent. Traverses to children after the insertion. Helper class for ViewInsertionVisitor.
 * 
 * @author Florian Fervers
 */
public class NodeViewControllerCreator implements LambdaTermVisitor {
    /**
     * The created LambdaNodeViewController.
     */
    private LambdaTermViewController result;
    /**
     * The parent viewcontroller node.
     */
    private final LambdaNodeViewController parent;
    /**
     * The view into which the node will be inserted.
     */
    private final LambdaTermViewController viewController;
    /**
     * Indicates whether a parenthesis can be created if the visited node is an application.
     */
    private final boolean canCreateParenthesis;
    /**
     * The right sibling of the inserted element or null if no right sibling exists.
     */
    private final LambdaTerm rightSibling;
    
    /**
     * Creates a new instance of NodeViewControllerCreator.
     * 
     * @param parent the parent node viewcontroller
     * @param canCreateParenthesis indicates whether a parenthesis can be created if the visited node is an application
     * @param viewController the viewController that the node will be inserted into
     * @param rightSibling the right sibling of the inserted element
     * @throws IllegalArgumentException if parent is null or viewController is null
     */
    public NodeViewControllerCreator(LambdaNodeViewController parent, boolean canCreateParenthesis, LambdaTermViewController viewController, LambdaTerm rightSibling) {
        if (parent == null) {
            throw new IllegalArgumentException("Parent node viewcontroller cannot be null!");
        }
        if (viewController == null) {
            throw new IllegalArgumentException("ViewController cannot be null!");
        }
        this.parent = parent;
        this.viewController = viewController;
        this.canCreateParenthesis = canCreateParenthesis;
        this.rightSibling = rightSibling;
        result = null;
    }

    /**
     * Cannot happen since the viewcontroller for the root is created separately.
     * 
     * @param node the root to be visited
     */
    @Override
    public void visit(LambdaRoot node) {
        assert(false);
    }
    
    /**
     * Visits the given lambda application and inserts a new parenthesis under the given parent if possible. Then traverses to the child nodes.
     * 
     * @param node the application to be visited
     */
    @Override
    public void visit(LambdaApplication node) {
        if (canCreateParenthesis) {
            parent.insertChild(new LambdaParenthesisViewController(node, parent, viewController), rightSibling);
        }
        // Traverse
        if (node.getLeft() != null) {
            node.accept(new ViewInsertionVisitor(node.getLeft(), viewController));
        }
        if (node.getRight() != null) {
            node.accept(new ViewInsertionVisitor(node.getRight(), viewController));
        }
    }
    
    /**
     * Visits the given lambda abstraction and inserts a new abstraction under the given parent. Then traverses to the child node.
     * 
     * @param node the abstraction to be visited
     */
    @Override
    public void visit(LambdaAbstraction node) {
        parent.insertChild(new LambdaAbstractionViewController(node, parent, viewController), rightSibling);
        if (node.getInside() != null) {
            node.accept(new ViewInsertionVisitor(node.getInside(), viewController));
        }
    }
    
    /**
     * Visits the given lambda variable and inserts a new variable under the given parent.
     * 
     * @param node the variable to be visited
     */
    @Override
    public void visit(LambdaVariable node) {
        parent.insertChild(new LambdaVariableViewController(node, parent, viewController), rightSibling);
    }

	@Override
	public Object getResult() {
		return null;
	}
}
