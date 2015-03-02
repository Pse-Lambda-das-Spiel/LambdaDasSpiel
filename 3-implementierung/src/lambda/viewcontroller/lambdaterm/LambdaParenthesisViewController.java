package lambda.viewcontroller.lambdaterm;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import lambda.model.lambdaterm.LambdaApplication;

/**
 * Represents a viewcontroller parenthesis node in a LambdaTermViewController.
 * 
 * @author Florian Fervers
 */
public class LambdaParenthesisViewController extends LambdaNodeViewController {
    /**
     * The front texture of the lamb.
     */
    private final TextureRegion front;
    /**
     * The center texture of the lamb. Will be used multiple times.
     */
    private final TextureRegion center;
    /**
     * The back texture of the lamb.
     */
    private final TextureRegion back;
    
    /**
     * Creates a new instance of LambdaParenthesisViewController.
     * 
     * @param linkedTerm the first application under the parenthesis displayed by this node
     * @param parent the parent viewcontroller node
     * @param viewController the viewcontroller on which this node will be displayed
     */
    public LambdaParenthesisViewController(LambdaApplication linkedTerm, LambdaNodeViewController parent, LambdaTermViewController viewController) {
        super(linkedTerm, parent, viewController);
        
        front = viewController.getContext().getElementUIContextFamily().getParenthesis().getFront();
        center = viewController.getContext().getElementUIContextFamily().getParenthesis().getCenter();
        back = viewController.getContext().getElementUIContextFamily().getParenthesis().getBack();
    }

    /**
     * Returns the minimum width of this node view.
     * 
     * @return the minimum width of this node view
     */
    @Override
    public float getMinWidth() {
        return 3 * BLOCK_WIDTH;
    }
    
    /**
     * Draws this node.
     * 
     * @param batch the batch on which the node will be drawn
     * @param alpha the parent's alpha
     */
    @Override
    public void draw(Batch batch, float alpha) {
        // Back
        batch.draw(back, getX(), getY(), BLOCK_WIDTH, BLOCK_HEIGHT);
        // Center
        float x;
        for (x = getX() + BLOCK_WIDTH; x <= getWidth() - BLOCK_WIDTH + EPSILON; x += BLOCK_WIDTH) {
            batch.draw(center, x, getY(), BLOCK_WIDTH, BLOCK_HEIGHT);
        }
        // Front
        batch.draw(front, x, getY(), BLOCK_WIDTH, BLOCK_HEIGHT);
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
