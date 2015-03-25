package lambda.viewcontroller.lambdaterm.draganddrop;

import com.badlogic.gdx.math.Vector2;
import lambda.viewcontroller.lambdaterm.LambdaNodeViewController;

/**
 * Represents a drag&drop source in a lambda term.
 *
 * @author Florian Fervers
 */
public class LambdaTermDragSource {
    /**
     * The drag source node.
     */
    private final LambdaNodeViewController node;
    /**
     * Indicates whether the dragged node should be removed from its parent when
     * dragged.
     */
    private final boolean split;
    /**
     * Indicates whether this source should not be removed when the term is
     * updated.
     */
    private final boolean permanent;

    /**
     * Creates a new drag source.
     *
     * @param node the source node
     * @param split true if the dragged node should be removed from its parent
     * when dragged, false otherwise
     * @param permanent true if this source should not be removed when the term
     * is updated, false otherwise
     */
    public LambdaTermDragSource(LambdaNodeViewController node, boolean split, boolean permanent) {
        this.node = node;
        this.split = split;
        this.permanent = permanent;
    }

    /**
     * Returns the source node.
     *
     * @return the source node
     */
    public LambdaNodeViewController getNode() {
        return node;
    }

    /**
     * Returns whether the dragged node should be removed from its parent when
     * dragged.
     *
     * @return true if the dragged node should be removed from its parent when
     * dragged, false otherwise
     */
    public boolean shouldSplit() {
        return split;
    }

    /**
     * Returns whether this source should not be removed when the term is
     * updated.
     *
     * @return true if this source should not be removed when the term is
     * updated, false otherwise
     */
    public boolean isPermanent() {
        return permanent;
    }

    /**
     * Returns whether the given point is on this source.
     *
     * @param x the x-coordinate of the point
     * @param y the y-coordinate of the point
     * @return true if the given point is on this actor, false otherwise
     */
    public boolean isOn(float x, float y) {
        Vector2 pos = node.screenToLocalCoordinates(new Vector2(x, y));
        return pos.x >= 0.0f && pos.x < node.getWidth() && pos.y >= 0.0f && pos.y < node.getHeight();
    }
}
