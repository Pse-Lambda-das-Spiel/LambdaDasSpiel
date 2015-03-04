package lambda.viewcontroller.lambdaterm;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import lambda.model.lambdaterm.LambdaAbstraction;

/**
 * Represents a viewcontroller abstraction node in a LambdaTermViewController.
 *
 * @author Florian Fervers
 */
public class LambdaAbstractionViewController extends LambdaValueViewController {
    /**
     * The texture of the first block of the lamb.
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
     * The texture mask of the first block of the lamb. Color in this part will
     * be adjusted.
     */
    private final TextureRegion frontMask;
    /**
     * The center texture mask of the lamb. Will be used multiple times. Color
     * in this part will be adjusted.
     */
    private final TextureRegion centerMask;
    /**
     * The back texture mask of the lamb. Color in this part will be adjusted.
     */
    private final TextureRegion backMask;
    /**
     * Indicates whether the spell is currently being or has been animated. Is
     * set to true when the animation starts.
     */
    private boolean animate;
    /**
     * The time since the start of the animation or zero if the animation hasn't
     * started yet.
     */
    private float stateTime;

    /**
     * Creates a new instance of LambdaAbstractionViewController.
     *
     * @param linkedTerm the abstraction displayed by this node
     * @param parent the parent viewcontroller node
     * @param viewController the viewcontroller on which this node will be
     * displayed
     */
    public LambdaAbstractionViewController(LambdaAbstraction linkedTerm, LambdaNodeViewController parent, LambdaTermViewController viewController) {
        super(linkedTerm, parent, viewController, true);
        front = viewController.getContext().getElementUIContextFamily().getAbstraction().getFront();
        center = viewController.getContext().getElementUIContextFamily().getAbstraction().getCenter();
        back = viewController.getContext().getElementUIContextFamily().getAbstraction().getBack();
        frontMask = viewController.getContext().getElementUIContextFamily().getAbstraction().getmFront();
        centerMask = viewController.getContext().getElementUIContextFamily().getAbstraction().getmCenter();
        backMask = viewController.getContext().getElementUIContextFamily().getAbstraction().getmBack();
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
        return 2 * BLOCK_WIDTH;
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
        Gdx.gl.glColorMask(true, true, true, false);
        batch.draw(back, getX(), getY(), BLOCK_WIDTH, BLOCK_HEIGHT);
        batch.setColor(getLambdaColor().r, getLambdaColor().g, getLambdaColor().b, 1.0f);
        batch.draw(backMask, getX(), getY(), BLOCK_WIDTH, BLOCK_HEIGHT);
        batch.setColor(1f, 1f, 1f, 1f);
        // Center
        float x;
        for (x = getX() + BLOCK_WIDTH; x < getWidth() - 1 * BLOCK_WIDTH - EPSILON; x += BLOCK_WIDTH) {
            batch.draw(center, x, getY(), BLOCK_WIDTH, BLOCK_HEIGHT);
            batch.setColor(getLambdaColor().r, getLambdaColor().g, getLambdaColor().b, 1.0f);
            batch.draw(centerMask, x, getY(), BLOCK_WIDTH, BLOCK_HEIGHT);
            batch.setColor(1f, 1f, 1f, 1f);
        }
        // Front
        batch.draw(front, x, getY(), BLOCK_WIDTH, BLOCK_HEIGHT);
        batch.setColor(getLambdaColor().r, getLambdaColor().g, getLambdaColor().b, 1.0f);
        batch.draw(frontMask, x, getY(), BLOCK_WIDTH, BLOCK_HEIGHT);
        batch.setColor(1f, 1f, 1f, 1f);

        //.getKeyFrame(stateTime) TODO
        synchronized (getViewController()) {
            if (animate) {
                stateTime += Gdx.graphics.getDeltaTime();
                if (isAnimationFinished()) {
                    animate = false;
                    getViewController().notifyAll();
                }
            }
        }
    }

    /**
     * Starts the spell animation.
     */
    public void animate() {
        animate = true;
    }

    /**
     * Returns whether the spell animation is fininshed.
     *
     * @return true if the spell animation is finished, false otherwise
     */
    public boolean isAnimationFinished() {
        return true;
        //return stateTime > front.getAnimationDuration(); TODO
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
