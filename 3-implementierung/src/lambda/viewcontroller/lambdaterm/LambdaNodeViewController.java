package lambda.viewcontroller.lambdaterm;

import com.badlogic.gdx.scenes.scene2d.Actor;
import java.util.LinkedList;
import java.util.List;
import lambda.model.lambdaterm.LambdaTerm;

/**
 * Represents a single viewcontroller node in a LambdaTermViewController.
 * 
 * @author Florian Fervers
 */
public abstract class LambdaNodeViewController extends Actor {
    /**
     * The term that is displayed by this viewcontroller.
     */
    private final LambdaTerm linkedTerm;
    
    /**
     * The viewcontroller on which this node is being displayed.
     */
    private final LambdaTermViewController viewController;
    
    /**
     * The parent viewcontroller node. Can be null.
     */
    private final LambdaNodeViewController parent;
    
    /**
     * The viewcontroller child nodes.
     */
    private final List<LambdaNodeViewController> children;
    
    /**
     * Creates a new instance of LambdaNodeViewController.
     * 
     * @param linkedTerm the term that is displayed by this viewcontroller
     * @param parent the parent viewcontroller node
     * @param viewController the viewcontroller on which this node is being displayed
     */
    public LambdaNodeViewController(LambdaTerm linkedTerm, LambdaNodeViewController parent, LambdaTermViewController viewController) {
        if (linkedTerm == null) {
            throw new IllegalArgumentException("Linked term cannot be null!");
        }
        if (viewController == null) {
            throw new IllegalArgumentException("Lambda term viewcontroller cannot be null!");
        }
        this.linkedTerm = linkedTerm;
        this.parent = parent;
        this.viewController = viewController;
        children = new LinkedList<>();
        
        if (viewController.isEditable()) {
            
        }
    }
    
    /**
     * Returns whether this node is a root, i.e. whether the parent node is null.
     * 
     * @return true if this node is a root, false otherwise
     */
    public boolean isRoot() {
        return parent == null;
    }
    
    /**
     * Returns the term that is displayed by this viewcontroller.
     * 
     * @return the term that is displayed by this viewcontroller
     */
    public LambdaTerm getLinkedTerm() {
        return linkedTerm;
    }
    
    /**
     * Returns the minimum width of this node view.
     * 
     * @return the minimum width of this node view
     */
    public abstract float getMinWidth();
}
