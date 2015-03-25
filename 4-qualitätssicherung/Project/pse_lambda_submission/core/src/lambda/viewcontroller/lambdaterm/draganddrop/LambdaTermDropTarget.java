package lambda.viewcontroller.lambdaterm.draganddrop;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import lambda.Consumer;
import lambda.model.lambdaterm.LambdaTerm;
import lambda.viewcontroller.lambdaterm.LambdaTermViewController;

/**
 * An actor that displays the target drop location for a drag&drop operation on
 * a lambdaterm.
 *
 * @author Florian Fervers
 */
public class LambdaTermDropTarget extends Actor {
    /**
     * The color that the actor will display when a dragged element is over it.
     */
    public static final Color HOVER_COLOR = Color.GREEN; // TODO: better color
    /**
     * The texture of the glow effect of drag&drop targets.
     */
    private final TextureRegion glowTexture;
    /**
     * Indicates whether this actor should be highlighted (i.e. when a drag&drop
     * element is hovered over this actor).
     */
    private boolean hovered;
    /**
     * The operation that inserts the dropped node at this location.
     */
    private final Consumer<LambdaTerm> insertOperation;

    /**
     * Creates a new drop location actor in the given rectangle.
     *
     * @param rectangle the target drop rectangle
     * @param viewController the view controller on which the drag&drop is
     * @param insertOperation the operation for inserting an element into the
     * term when it is dropped here happening
     */
    public LambdaTermDropTarget(Rectangle rectangle, LambdaTermViewController viewController, Consumer<LambdaTerm> insertOperation) {
        this.setBounds(rectangle.getX(), rectangle.getY(), rectangle.getWidth(), rectangle.getHeight());
        this.insertOperation = insertOperation;
        glowTexture = viewController.getContext().getGlow();
        hovered = false;
        setVisible(false);
    }

    /**
     * Sets whether a drag&drop element is currently being hovered over this
     * actor.
     *
     * @param hovered true if a drag&drop element is currently being hovered
     * over this actor, false otherwise
     */
    public void setHovered(boolean hovered) {
        this.hovered = hovered;
    }

    /**
     * Performs the insert operation with the given lambda term.
     *
     * @param term the lambda term to be inserted
     */
    public void insert(LambdaTerm term) {
        insertOperation.accept(term);
    }

    /**
     * Draws this node.
     *
     * @param batch the batch on which the node will be drawn
     * @param alpha the parent's alpha
     */
    @Override
    public void draw(Batch batch, float alpha) {
        if (hovered) {
            batch.setColor(HOVER_COLOR);
        }
        batch.draw(glowTexture, getX(), getY(), getWidth(), getHeight());
        batch.setColor(1f, 1f, 1f, 1f);
    }

    /**
     * Returns whether the given point is on this actor.
     *
     * @param x the x-coordinate of the point
     * @param y the y-coordinate of the point
     * @return true if the given point is on this actor, false otherwise
     */
    public boolean isOn(float x, float y) {
        Vector2 pos = screenToLocalCoordinates(new Vector2(x, y));
        return pos.x >= 0.0f && pos.x < getWidth() && pos.y >= 0.0f && pos.y < getHeight();
    }
}
