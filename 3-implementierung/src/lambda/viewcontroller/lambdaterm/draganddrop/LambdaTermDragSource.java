package lambda.viewcontroller.lambdaterm.draganddrop;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Payload;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Source;
import lambda.model.lambdaterm.LambdaRoot;
import lambda.model.lambdaterm.LambdaTerm;
import lambda.model.lambdaterm.visitor.CopyVisitor;
import lambda.model.lambdaterm.visitor.RemoveTermVisitor;
import lambda.viewcontroller.lambdaterm.LambdaNodeViewController;
import lambda.viewcontroller.lambdaterm.LambdaTermViewController;

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
     * Indicates if f the selected term should be split off from the original tree.
     */
    private final boolean split;
    
    /**
     * Creates a new drag&drop source for the given lambdaterm node.
     * 
     * @param node the lambdaterm node
     * @param split true if the selected term should be split off from the original tree, false otherwise
     */
    public LambdaTermDragSource(LambdaNodeViewController node, boolean split) {
        super(node);
        this.node = node;
        this.split = split;
    }

    /**
     * Starts the dragging operation.
     * 
     * @param event the event that caused the drag&drop operation
     * @param x the x-coordinate of the drag
     * @param y the y-coordinate of the drag
     * @param pointer the touch index
     * @return a payload containing the selected node
     */
    @Override
    public Payload dragStart(InputEvent event, float x, float y, int pointer) {
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
        payload.setDragActor(LambdaTermViewController.build(selection, false, node.getViewController().getContext()));
        
        return payload;
    }
}
