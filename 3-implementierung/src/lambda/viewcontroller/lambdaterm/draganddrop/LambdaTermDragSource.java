package lambda.viewcontroller.lambdaterm.draganddrop;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Payload;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Source;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Target;
import static lambda.LambdaGame.DEBUG;
import lambda.model.lambdaterm.LambdaRoot;
import lambda.model.lambdaterm.LambdaTerm;
import lambda.model.lambdaterm.visitor.CopyVisitor;
import lambda.model.lambdaterm.visitor.RemoveTermVisitor;
import lambda.viewcontroller.lambdaterm.LambdaNodeViewController;
import lambda.viewcontroller.lambdaterm.LambdaTermViewController;
import static lambda.viewcontroller.lambdaterm.LambdaTermViewController.DEBUG_DRAG_AND_DROP;

/**
 * A class that represents a source for a drag&drop operation on a lambda term.
 *
 * @author Florian Fervers
 */
public class LambdaTermDragSource extends Source {
    /**
     * The node that can be dragged.
     */
    private final LambdaNodeViewController node;
    /**
     * Indicates if f the selected term should be split off from the original
     * tree.
     */
    private final boolean split;
    /**
     * The view controller on which the drag&drop is happening.
     */
    private final LambdaTermViewController viewController;

    /**
     * Creates a new drag&drop source for the given lambdaterm node.
     *
     * @param node the lambdaterm node
     * @param split true if the selected term should be split off from the
     * original tree, false otherwise
     * @param viewController the view controller on which the drag&drop is
     * happening
     */
    public LambdaTermDragSource(LambdaNodeViewController node, boolean split, LambdaTermViewController viewController) {
        super(node);
        this.node = node;
        this.split = split;
        this.viewController = viewController;

        if (DEBUG_DRAG_AND_DROP) {
            System.out.println("        Added drop source (" + node.getLinkedTerm().toString() + ") at (" + node.getX() + ", " + node.getY() + ")");
        }
    }

    /**
     * Called when the dragging operation starts.
     *
     * @param event the event that caused the drag operation
     * @param x the x-coordinate of the drag
     * @param y the y-coordinate of the drag
     * @param pointer the touch index
     * @return a payload containing the selected node
     */
    @Override
    public Payload dragStart(InputEvent event, float x, float y, int pointer) {
        if (DEBUG) {
            System.out.println("Start dragging term (" + node.getLinkedTerm().toString() + ")");
        }

        // Remove selected node from tree
        if (split) {
            node.getLinkedTerm().accept(new RemoveTermVisitor());
        }

        // Payload is selected node
        Payload payload = new Payload();
        payload.setObject(split ? node.getLinkedTerm() : node.getLinkedTerm().accept(new CopyVisitor()));

        // Drag actor is a new lambda term vc for the selected node
        LambdaRoot selection = new LambdaRoot();
        selection.setChild((LambdaTerm) payload.getObject());
        LambdaTermViewController dragActor = LambdaTermViewController.build(selection, false, node.getViewController().getContext(), node.getStage(), node.getViewController().isForcingParenthesis());
        dragActor.addOffset(dragActor.getX() - dragActor.getWidth() / 2.0f, dragActor.getY() + dragActor.getHeight() / 2.0f);
        payload.setDragActor(dragActor);

        // Display drop targets
        viewController.displayDropTargets(true);

        return payload;
    }

    /**
     * Called when the dragging operation ended.
     *
     * @param event the event that caused the drop operation
     * @param x the x-coordinate of the drop
     * @param y the y-coordinate of the drop
     * @param pointer the touch index
     * @param payload a payload containing the selected node
     * @param target the drop target
     */
    @Override
    public void dragStop(InputEvent event, float x, float y, int pointer, Payload payload, Target target) {
        viewController.displayDropTargets(false);
    }
}
