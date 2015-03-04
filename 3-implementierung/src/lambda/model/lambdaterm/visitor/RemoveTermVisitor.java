package lambda.model.lambdaterm.visitor;

import lambda.model.lambdaterm.LambdaAbstraction;
import lambda.model.lambdaterm.LambdaApplication;
import lambda.model.lambdaterm.LambdaRoot;
import lambda.model.lambdaterm.LambdaTerm;
import lambda.model.lambdaterm.LambdaVariable;

/**
 * Represents a visitor on a lambda term that removes this lambda term from its parent if it is not a root. Can remove terms in invalid lambda terms.
 * 
 * @author Florian Fervers
 */
public class RemoveTermVisitor implements LambdaTermVisitor {
    /**
     * The lambda term to be removed from its parent. When a node is visited saves the node in this variable and traverses to the parent node to remove it.
     */
    private LambdaTerm remove;
    
    /**
     * Creates a new RemoveTermVisitor.
     */
    public RemoveTermVisitor() {
        remove = null;
    }

    /**
     * Visits the given lambda root. If there is a term to be removed, removes it from this node.
     * 
     * @param node the root to be visited
     */
    @Override
    public void visit(LambdaRoot node) {
        if (remove != null) {
            assert(node.getChild() == remove);
            node.setChild(null);
            remove.setParent(null);
        }
    }
    
    /**
     * Visits the given lambda application. If there is a term to be removed, removes it from this node. Saves this node as remove node and traverses to the parent node if possible.
     * 
     * @param node the application to be visited
     */
    @Override
    public void visit(LambdaApplication node) {
        if (remove != null) {
            if (node.getLeft() == remove) {
                node.setLeft(null);
            } else {
                assert(node.getRight() == remove);
                node.setRight(null);
            }
            remove.setParent(null);
        } else if (node.getParent() != null) {
            remove = node;
            node.getParent().accept(this);
        }
    }
    
    /**
     * Visits the given lambda abstraction. If there is a term to be removed, removes it from this node. Saves this node as remove node and traverses to the parent node if possible.
     * 
     * @param node the abstraction to be visited
     */
    @Override
    public void visit(LambdaAbstraction node) {
        if (remove != null) {
            assert(node.getInside() == remove);
            node.setInside(null);
            remove.setParent(null);
        } else if (node.getParent() != null) {
            remove = node;
            node.getParent().accept(this);
        }
    }
    
    /**
     * Visits the given lambda variable. Saves this node as remove node and traverses to the parent node if possible.
     * 
     * @param node the variable to be visited
     */
    @Override
    public void visit(LambdaVariable node) {
        assert(remove == null);
        if (node.getParent() != null) {
            remove = node;
            node.getParent().accept(this);
        }
    }

	@Override
	public Object getResult() {
		return null;
	}
}
