package lambda.viewcontroller.lambdaterm.draganddrop;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Payload;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Source;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Target;

import lambda.Consumer;
import lambda.model.lambdaterm.LambdaTerm;

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
     * @param targetRectangle the target rectangle where a selection can be dropped
     * @param insertOperation the operation that inserts the payload into the original lambdaterm tree
     */
    public LambdaTermDropTarget(Rectangle targetRectangle, Consumer<LambdaTerm> insertOperation) {
        super(new DropLocationActor(targetRectangle));
        this.insertOperation = insertOperation;
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
        return true;
    }
}
