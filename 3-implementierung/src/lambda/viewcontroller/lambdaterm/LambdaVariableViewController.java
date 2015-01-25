package lambda.viewcontroller.lambdaterm;

import com.badlogic.gdx.graphics.g2d.Batch;
import java.awt.Color;
import lambda.model.lambdaterm.LambdaVariable;

/**
 * Represents a viewcontroller variable node in a LambdaTermViewController.
 * 
 * @author Florian Fervers
 */
public class LambdaVariableViewController extends LambdaValueViewController {
    /**
     * The color of this variable.
     */
    private Color color;

    /**
     * Creates a new instance of LambdaVariableViewController.
     * 
     * @param linkedTerm the variable displayed by this node
     * @param parent the parent viewcontroller node
     * @param viewController the viewcontroller on which this node will be displayed
     */
    public LambdaVariableViewController(LambdaVariable linkedTerm, LambdaNodeViewController parent, LambdaTermViewController viewController) {
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
        if (!(other instanceof LambdaVariableViewController)) {
            return false;
        }
        LambdaVariableViewController variable = (LambdaVariableViewController) other;
        return super.equals(variable) && variable.getColor().equals(this.getColor());
    }
}
