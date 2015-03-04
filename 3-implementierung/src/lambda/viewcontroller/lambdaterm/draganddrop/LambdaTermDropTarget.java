package lambda.viewcontroller.lambdaterm.draganddrop;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Payload;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Source;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Target;

import lambda.Consumer;
import static lambda.LambdaGame.DEBUG;
import lambda.model.lambdaterm.LambdaTerm;
import static lambda.viewcontroller.lambdaterm.LambdaTermViewController.DEBUG_DRAG_AND_DROP;

/**
 * A class that represents a target for a drag&drop operation on a lambda term.
 *
 * @author Florian Fervers
 */
public class LambdaTermDropTarget extends Target {
    /**
     * The operation that inserts the payload into the original lambdaterm tree.
     */
    private final Consumer<LambdaTerm> insertOperation;

    /**
     * Creates a new drag&drop target for the given parameters.
     *
     * @param targetRectangle the target rectangle where a selection can be
     * dropped
     * @param insertOperation the operation that inserts the payload into the
     * original lambdaterm tree
     * @param actor the actor for the drop location
     */
    public LambdaTermDropTarget(Rectangle targetRectangle, Consumer<LambdaTerm> insertOperation, DropLocationActor actor) {
        super(actor);
        this.insertOperation = insertOperation;

        if (DEBUG_DRAG_AND_DROP) {
            System.out.println("        Added drop target at (" + targetRectangle.toString() + ")");
        }
    }

    /**
     * When the drop operation is finished.
     *
     * @param source the drag&drop source
     * @param payload the payload that was dragged
     * @param x the x-coordinate of the drop
     * @param y the y-coordinate of the drop
     * @param pointer the touch index
     */
    @Override
    public void drop(Source source, Payload payload, float x, float y, int pointer) {
        if (DEBUG) {
            System.out.println("Dropping term (" + ((LambdaTerm) payload.getObject()).toString() + ")");
        }

        // Insert payload into lambdaterm tree
        insertOperation.accept((LambdaTerm) payload.getObject());
    }

    /**
     * Called when a drag&drop selection is dragged over this target.
     *
     * @param source the drag&drop source
     * @param payload the payload of the drag&drop operation
     * @param x the current x-coordinate of the touch
     * @param y the current y-coordinate of the touch
     * @param pointer the touch index
     * @return true since the payload can be dropped here
     */
    @Override
    public boolean drag(Source source, Payload payload, float x, float y, int pointer) {
        ((DropLocationActor) getActor()).setHovered(true);
        return true;
    }

    /**
     * Called when the current drag&drop element leaves this target or a drop has occurred.
     *
     * @param source the source of the current drag&drop element
     * @param payload the payload of the drag&drop
     */
    @Override
    public void reset(Source source, Payload payload) {
        ((DropLocationActor) getActor()).setHovered(false);
    }
}
