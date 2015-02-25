package lambda.viewcontroller.lambdaterm;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import lambda.model.lambdaterm.LambdaVariable;
import static lambda.viewcontroller.lambdaterm.LambdaNodeViewController.BLOCK_HEIGHT;
import static lambda.viewcontroller.lambdaterm.LambdaNodeViewController.BLOCK_WIDTH;

/**
 * Represents a viewcontroller variable node in a LambdaTermViewController.
 * 
 * @author Florian Fervers
 */
public class LambdaVariableViewController extends LambdaValueViewController {
    /**
     * Indicates whether the animation for replacing this variable is currently being or has been run. Is set to true when the animation starts.
     */
    private boolean animate;
    /**
     * The time since the start of the animation or zero if the animation hasn't started yet.
     */
    private float stateTime;
    /**
     * The variable's texture.
     */
    private final Texture texture;

    /**
     * Creates a new instance of LambdaVariableViewController.
     * 
     * @param linkedTerm the variable displayed by this node
     * @param parent the parent viewcontroller node
     * @param viewController the viewcontroller on which this node will be displayed
     */
    public LambdaVariableViewController(LambdaVariable linkedTerm, LambdaNodeViewController parent, LambdaTermViewController viewController) {
        super(linkedTerm, parent, viewController);
        texture = viewController.getContext().getElementUIContextFamily().getVariable().getTexture();
        
        animate = false;
        stateTime = 0.0f;
    }

    /**
     * Returns the minimum width of this node view.
     * 
     * @return the minimum width of this node view
     */
    @Override
    public float getMinWidth() {
        return BLOCK_WIDTH;
    }
    
    /**
     * Draws this node.
     * 
     * @param batch the batch on which the node will be drawn
     * @param alpha the parent's alpha
     */
    @Override
    public void draw(Batch batch, float alpha) {
        // Texture
        batch.draw(texture, getX(), getY(), BLOCK_WIDTH, BLOCK_HEIGHT);
        
        // Smoke animation TODO: scale over applicant
        synchronized (getViewController()) {
            if (animate) {
                //batch.draw(animation.getKeyFrame(stateTime), getX(), getY(), BLOCK_WIDTH, BLOCK_HEIGHT); TODO
                stateTime += Gdx.graphics.getDeltaTime();
                if (isAnimationFinished()) {
                    animate = false;
                    getViewController().notifyAll();
                }
            }
        }
    }
    
    /**
     * Starts the smoke animation.
     */
    public void animate() {
        animate = true;
    }
    
    /**
     * Returns whether the smoke animation is fininshed.
     * 
     * @return true if the smoke animation is finished, false otherwise
     */
    public boolean isAnimationFinished() {
        return true;
        // return stateTime > animation.getAnimationDuration(); TODO
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
