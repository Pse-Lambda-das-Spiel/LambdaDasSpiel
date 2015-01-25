package lambda.viewcontroller.lambdaterm;

import com.badlogic.gdx.graphics.g2d.Batch;
import java.awt.Color;
import lambda.model.lambdaterm.LambdaAbstraction;

/**
 * Represents a viewcontroller abstraction node in a LambdaTermViewController.
 * 
 * @author Florian Fervers
 */
public class LambdaAbstractionViewController extends LambdaValueViewController {
    /**
     * The color of the variable bound by this abstraction.
     */
    private Color color;

    /**
     * Creates a new instance of LambdaAbstractionViewController.
     * 
     * @param linkedTerm the abstraction displayed by this node
     * @param parent the parent viewcontroller node
     * @param viewController the viewcontroller on which this node will be displayed
     */
    public LambdaAbstractionViewController(LambdaAbstraction linkedTerm, LambdaNodeViewController parent, LambdaTermViewController viewController) {
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
        if (!(other instanceof LambdaAbstractionViewController)) {
            return false;
        }
        LambdaAbstractionViewController abstraction = (LambdaAbstractionViewController) other;
        return super.equals(abstraction) && abstraction.getColor().equals(this.getColor());
    }
}
