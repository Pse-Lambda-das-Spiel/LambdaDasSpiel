package lambda.viewcontroller.lambdaterm;

import com.badlogic.gdx.graphics.g2d.Batch;
import lambda.model.lambdaterm.LambdaApplication;

/**
 * Represents a viewcontroller parenthesis node in a LambdaTermViewController.
 * 
 * @author Florian Fervers
 */
public class LambdaParenthesisViewController extends LambdaNodeViewController {
    /**
     * Creates a new instance of LambdaParenthesisViewController.
     * 
     * @param linkedTerm the first application under the parenthesis displayed by this node
     * @param parent the parent viewcontroller node
     * @param viewController the viewcontroller on which this node will be displayed
     */
    public LambdaParenthesisViewController(LambdaApplication linkedTerm, LambdaNodeViewController parent, LambdaTermViewController viewController) {
        super(linkedTerm, parent, viewController);
    }

    /**
     * Returns the minimum width of this node view.
     * 
     * @return the minimum width of this node view
     */
    @Override
    public float getMinWidth() {
        // TODO
        return 100.0f;
    }
    
    /**
     * Draws this node.
     * 
     * @param batch the batch on which the node will be drawn
     * @param alpha the parent's alpha
     */
    @Override
    public void draw(Batch batch, float alpha) {
        // TODO
    }
    
    /**
     * Returns whether this and the other object are equal.
     * 
     * @param other the other object
     * @return true if this and the other object are equal, false otherwise
     */
    @Override
    public boolean equals(Object other) {
        return super.equals(other) && other instanceof LambdaParenthesisViewController;
    }
}
