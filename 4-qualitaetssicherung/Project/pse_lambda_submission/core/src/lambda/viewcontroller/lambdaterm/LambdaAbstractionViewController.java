package lambda.viewcontroller.lambdaterm;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
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
     * The texture of the first block of the abstraction lamb.
     */
    private final TextureRegion abstractionFront;
    /**
     * The center texture of the abstraction lamb. Will be used multiple times.
     */
    private final TextureRegion abstractionCenter;
    /**
     * The back texture of the abstraction lamb.
     */
    private final TextureRegion abstractionBack;
    /**
     * The texture mask of the first block of the abstraction lamb. Color in this part will
     * be adjusted.
     */
    private final TextureRegion abstractionFrontMask;
    /**
     * The center texture mask of the abstraction lamb. Will be used multiple times. Color
     * in this part will be adjusted.
     */
    private final TextureRegion abstractionCenterMask;
    /**
     * The back texture mask of the abstraction lamb. Color in this part will be adjusted.
     */
    private final TextureRegion abstractionBackMask;
    /**
     * The texture of the first block of the parenthesis lamb.
     */
    private final TextureRegion parenthesisFront;
    /**
     * The center texture of the parenthesis lamb. Will be used multiple times.
     */
    private final TextureRegion parenthesisCenter;
    /**
     * The back texture of the parenthesis lamb.
     */
    private final TextureRegion parenthesisBack;
    /**
     * The texture mask of the first block of the lamb.
     */
    private final TextureRegion parenthesisFrontMask;
    /**
     * The center texture mask of the parenthesis lamb. Will be used multiple times.
     */
    private final TextureRegion parenthesisCenterMask;
    /**
     * The back texture mask of the parenthesis lamb.
     * */
    private final TextureRegion parenthesisBackMask;
    /**
     * The animation that is shown before an application is performed.
     */
    private final Animation animation;
    /**
     * Indicates whether the spell is currently being or has been animated. Is
     * set to true when the animation starts.
     */
    private boolean animate;
    /**
     * The time since the start of the animation or zero if the animation hasn't
     * started yet.
     */
    private float magicStateTime;

    /**
     * Creates a new instance of LambdaAbstractionViewController.
     *
     * @param linkedTerm the abstraction displayed by this node
     * @param parent the parent viewcontroller node
     * @param viewController the viewcontroller on which this node will be
     * displayed
     */
    public LambdaAbstractionViewController(LambdaAbstraction linkedTerm,
            LambdaNodeViewController parent,
            LambdaTermViewController viewController) {
        super(linkedTerm, parent, viewController, true);
        abstractionFront = viewController.getContext().getElementUIContextFamily()
                .getAbstraction().getFront();
        abstractionCenter = viewController.getContext().getElementUIContextFamily()
                .getAbstraction().getCenter();
        abstractionBack = viewController.getContext().getElementUIContextFamily()
                .getAbstraction().getBack();
        abstractionFrontMask = viewController.getContext().getElementUIContextFamily()
                .getAbstraction().getmFront();
        abstractionCenterMask = viewController.getContext().getElementUIContextFamily()
                .getAbstraction().getmCenter();
        abstractionBackMask = viewController.getContext().getElementUIContextFamily()
                .getAbstraction().getmBack();
        parenthesisFront = viewController.getContext().getElementUIContextFamily()
                .getParenthesis().getFront();
        parenthesisCenter = viewController.getContext().getElementUIContextFamily()
                .getParenthesis().getCenter();
        parenthesisBack = viewController.getContext().getElementUIContextFamily()
                .getParenthesis().getBack();
        parenthesisFrontMask = viewController.getContext().getElementUIContextFamily()
                .getParenthesis().getmFront();
        parenthesisCenterMask = viewController.getContext().getElementUIContextFamily()
                .getParenthesis().getmCenter();
        parenthesisBackMask = viewController.getContext().getElementUIContextFamily()
                .getParenthesis().getmBack();
        animation = viewController.getContext().getMagicAnimation();
        animate = false;
        magicStateTime = 0.0f;
    }

    /**
     * Returns the minimum width of this node view.
     *
     * @return the minimum width of this node view
     */
    @Override
    public float getMinWidth() {
        return 2 * getViewController().getBlockSize();
    }

    /**
     * Draws this node.
     *
     * @param batch the batch on which the node will be drawn
     * @param alpha the parent's alpha
     */
    @Override
    public void draw(Batch batch, float alpha) {
        updateColorAnimation();

        float t = Math.max(0.0f, Math.min(1.0f, magicStateTime / (float) animation.getAnimationDuration()));

        float x = 0.0f;
        if (t < 1.0f) {
            // Back
            batch.draw(abstractionBack, getX(), getY(), getViewController().getBlockSize(), getViewController().getBlockSize());
            batch.setColor(getCurrentColor().r, getCurrentColor().g,
                    getCurrentColor().b, 1.0f - t);
            batch.draw(abstractionBackMask, getX(), getY(), getViewController().getBlockSize(), getViewController().getBlockSize());
            batch.setColor(1f, 1f, 1f, 1.0f - t);
            // Center

            for (x = getX() + getViewController().getBlockSize(); x < getX() + getWidth() - getViewController().getBlockSize()
                    - EPSILON; x += getViewController().getBlockSize()) {
                batch.draw(abstractionCenter, x, getY(), getViewController().getBlockSize(), getViewController().getBlockSize());
                batch.setColor(getCurrentColor().r, getCurrentColor().g,
                        getCurrentColor().b, 1.0f - t);
                batch.draw(abstractionCenterMask, x, getY(), getViewController().getBlockSize(), getViewController().getBlockSize());
                batch.setColor(1f, 1f, 1f, 1.0f - t);
            }
            // Front
            batch.draw(abstractionFront, x, getY(), getViewController().getBlockSize(), getViewController().getBlockSize());
            batch.setColor(getCurrentColor().r, getCurrentColor().g,
                    getCurrentColor().b, 1.0f - t);
            batch.draw(abstractionFrontMask, x, getY(), getViewController().getBlockSize(), getViewController().getBlockSize());
            batch.setColor(1f, 1f, 1f, 1.0f - t);
        }
        if (t > 0.0f) {
            batch.setColor(1f, 1f, 1f, t);
            // Back
            batch.draw(parenthesisBack, getX(), getY(), getViewController().getBlockSize(), getViewController().getBlockSize());
            batch.draw(parenthesisBackMask, getX(), getY(), getViewController().getBlockSize(), getViewController().getBlockSize());
            // Center
            for (x = getX() + getViewController().getBlockSize(); x < getX() + getWidth() - getViewController().getBlockSize()
                    - EPSILON; x += getViewController().getBlockSize()) {
                batch.draw(parenthesisCenter, x, getY(), getViewController().getBlockSize(), getViewController().getBlockSize());
                batch.draw(parenthesisCenterMask, x, getY(), getViewController().getBlockSize(), getViewController().getBlockSize());
            }
            // Front
            batch.draw(parenthesisFront, x, getY(), getViewController().getBlockSize(), getViewController().getBlockSize());
            batch.draw(parenthesisFrontMask, x, getY(), getViewController().getBlockSize(), getViewController().getBlockSize());
        }
        batch.setColor(1f, 1f, 1f, 1f);

        drawVanishAnimation(batch, alpha);

        // Animation
        synchronized (getViewController()) {
            if (animate) {
                batch.draw(animation.getKeyFrame(magicStateTime), x, getY(),
                        getViewController().getBlockSize(), getViewController().getBlockSize());
                magicStateTime += Gdx.graphics.getDeltaTime();
                if (isMagicAnimationFinished()) {
                    animate = false;
                    getViewController().notifyAll();
                }
            }
        }
    }

    /**
     * Starts the spell animation.
     */
    public void animateMagic() {
        animate = true;
    }

    /**
     * Returns whether the spell animation is fininshed.
     *
     * @return true if the spell animation is finished, false otherwise
     */
    public boolean isMagicAnimationFinished() {
        return magicStateTime >= animation.getAnimationDuration();
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
        return super.equals(abstraction)
                && abstraction.getCurrentColor().equals(this.getCurrentColor());
    }
}
