package lambda.viewcontroller.lambdaterm;

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
     * The front mask of the lamb. Will be colored white.
     */
    private final TextureRegion frontMask;
    /**
     * The center mask of the lamb. Will be used multiple times. Will be colored
     * white.
     */
    private final TextureRegion centerMask;
    /**
     * The back mask of the lamb. Will be colored white.
     */
    private final TextureRegion backMask;

    /**
     * Creates a new instance of LambdaParenthesisViewController.
     *
     * @param linkedTerm
     *            the first application under the parenthesis displayed by this
     *            node
     * @param parent
     *            the parent viewcontroller node
     * @param viewController
     *            the viewcontroller on which this node will be displayed
     */
    public LambdaParenthesisViewController(LambdaApplication linkedTerm,
            LambdaNodeViewController parent,
            LambdaTermViewController viewController) {
        super(linkedTerm, parent, viewController, true);

        front = viewController.getContext().getElementUIContextFamily()
                .getParenthesis().getFront();
        center = viewController.getContext().getElementUIContextFamily()
                .getParenthesis().getCenter();
        back = viewController.getContext().getElementUIContextFamily()
                .getParenthesis().getBack();

        frontMask = viewController.getContext().getElementUIContextFamily()
                .getParenthesis().getmFront();
        centerMask = viewController.getContext().getElementUIContextFamily()
                .getParenthesis().getmCenter();
        backMask = viewController.getContext().getElementUIContextFamily()
                .getParenthesis().getmBack();
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
     * @param batch
     *            the batch on which the node will be drawn
     * @param alpha
     *            the parent's alpha
     */
    @Override
    public void draw(Batch batch, float alpha) {
        // Color is white
        // Back
        batch.draw(back, getX(), getY(), getViewController().getBlockSize(), getViewController().getBlockSize());
        batch.draw(backMask, getX(), getY(), getViewController().getBlockSize(), getViewController().getBlockSize());
        // Center
        float x;
        for (x = getX() + getViewController().getBlockSize(); x < getX() + getWidth() - getViewController().getBlockSize()
                - EPSILON; x += getViewController().getBlockSize()) {
            batch.draw(center, x, getY(), getViewController().getBlockSize(), getViewController().getBlockSize());
            batch.draw(centerMask, x, getY(), getViewController().getBlockSize(), getViewController().getBlockSize());
        }
        // Front
        batch.draw(front, x, getY(), getViewController().getBlockSize(), getViewController().getBlockSize());
        batch.draw(frontMask, x, getY(), getViewController().getBlockSize(), getViewController().getBlockSize());

        drawVanishAnimation(batch, alpha);
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
        return super.equals(other)
                && other instanceof LambdaParenthesisViewController;
    }
}