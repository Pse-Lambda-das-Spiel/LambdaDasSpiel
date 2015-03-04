package lambda.viewcontroller.lambdaterm.draganddrop;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import lambda.viewcontroller.lambdaterm.LambdaTermViewController;

/**
 * An actor that displays the target drop location for a drag&drop operation on
 * a lambdaterm.
 *
 * @author Florian Fervers
 */
public class DropLocationActor extends Actor {
    /**
     * The color that the actor will display when a dragged element is over it.
     */
    public static final Color HOVER_COLOR = Color.GREEN;
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
     * Creates a new drop location actor in the given rectangle.
     *
     * @param rectangle the target drop rectangle
     * @param viewController the view controller on which the drag&drop is
     * happening
     */
    public DropLocationActor(Rectangle rectangle, LambdaTermViewController viewController) {
        this.setBounds(rectangle.getX(), rectangle.getY(), rectangle.getWidth(), rectangle.getHeight());
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
}
