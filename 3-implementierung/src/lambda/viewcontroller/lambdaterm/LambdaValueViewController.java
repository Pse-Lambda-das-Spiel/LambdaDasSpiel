package lambda.viewcontroller.lambdaterm;

import com.badlogic.gdx.graphics.Color;

import lambda.model.lambdaterm.LambdaValue;

/**
 * Represents a viewcontroller value node (abstraction or variable) in a LambdaTermViewController.
 * 
 * @author Florian Fervers
 */
public abstract class LambdaValueViewController extends LambdaNodeViewController {
    /**
     * The color of this variable.
     */
    private Color color;

    /**
     * Creates a new instance of LambdaValueViewController.
     * 
     * @param linkedTerm the value displayed by this node
     * @param parent the parent viewcontroller node
     * @param viewController the viewcontroller on which this node will be displayed
     * @param canHaveChildren true if this node can have children, false otherwise
     */
    public LambdaValueViewController(LambdaValue linkedTerm, LambdaNodeViewController parent, LambdaTermViewController viewController, boolean canHaveChildren) {
        super(linkedTerm, parent, viewController, canHaveChildren);
        color = linkedTerm.getColor();
    }
    
    /**
     * Returns the color of this value.
     * 
     * @return the color of this value
     */
    public Color getLambdaColor() {
        return color;
    }
    
    /**
     * Sets the color of this value.
     * 
     * @param color the new color
     */
    public void setLambdaColor(Color color) {
        this.color = color;
    }
   
}
