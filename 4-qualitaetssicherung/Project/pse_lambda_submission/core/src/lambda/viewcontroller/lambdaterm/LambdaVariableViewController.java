package lambda.viewcontroller.lambdaterm;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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
     * Indicates whether the animation for replacing this variable is currently
     * being or has been run. Is set to true when the animation starts.
     */
    private boolean animateSmoke;
    /**
     * The time since the start of the animation or zero if the animation hasn't
     * started yet.
     */
    private float smokeStateTime;
    /**
     * The variable's texture.
     */
    private final TextureRegion texture;
    /**
     * The animation that is shown while an application is being performed.
     */
    private final Animation animation;

    /**
     * Creates a new instance of LambdaVariableViewController.
     *
     * @param linkedTerm
     *            the variable displayed by this node
     * @param parent
     *            the parent viewcontroller node
     * @param viewController
     *            the viewcontroller on which this node will be displayed
     */
    public LambdaVariableViewController(LambdaVariable linkedTerm,
            LambdaNodeViewController parent,
            LambdaTermViewController viewController) {
        super(linkedTerm, parent, viewController, false);
        texture = viewController.getContext().getElementUIContextFamily()
                .getVariable().getTexture();
        animation = viewController.getContext().getCloudAnimation();

        animateSmoke = false;
        smokeStateTime = 0.0f;
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
     * @param batch
     *            the batch on which the node will be drawn
     * @param alpha
     *            the parent's alpha
     */
    @Override
    public void draw(Batch batch, float alpha) {
        updateColorAnimation();

        // Texture
        batch.setColor(getCurrentColor());
        batch.draw(texture, getX(), getY(), BLOCK_WIDTH, BLOCK_HEIGHT);
        batch.setColor(1f, 1f, 1f, 1f);

        drawVanishAnimation(batch, alpha);

        // Smoke animation
        synchronized (getViewController()) {
            if (animateSmoke) {
                batch.draw(animation.getKeyFrame(smokeStateTime), getX(),
                        getY(), BLOCK_WIDTH, BLOCK_HEIGHT);
                smokeStateTime += Gdx.graphics.getDeltaTime();
                if (isSmokeAnimationFinished()) {
                    animateSmoke = false;
                    getViewController().notifyAll();
                }
            }
        }
    }

    /**
     * Starts the smoke animation.
     */
    public void animateSmoke() {
        animateSmoke = true;
    }

    /**
     * Returns whether the smoke animation is fininshed.
     *
     * @return true if the smoke animation is finished, false otherwise
     */
    public boolean isSmokeAnimationFinished() {
        return smokeStateTime >= animation.getAnimationDuration();
    }

    /**
     * Returns whether this and the other object are equal.
     *
     * @param other
     *            the other object
     * @return true if this and the other object are equal, false otherwise
     */
    @Override
    public boolean equals(Object other) {
        if (!(other instanceof LambdaVariableViewController)) {
            return false;
        }
        LambdaVariableViewController variable = (LambdaVariableViewController) other;
        return super.equals(variable)
                && variable.getCurrentColor().equals(this.getCurrentColor());
    }
}
