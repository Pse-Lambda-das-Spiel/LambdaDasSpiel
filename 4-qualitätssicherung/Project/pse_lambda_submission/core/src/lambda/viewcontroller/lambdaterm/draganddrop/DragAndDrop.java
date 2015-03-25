package lambda.viewcontroller.lambdaterm.draganddrop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import java.util.LinkedList;
import java.util.List;
import lambda.Consumer;
import lambda.model.lambdaterm.LambdaRoot;
import lambda.model.lambdaterm.LambdaTerm;
import lambda.model.lambdaterm.visitor.CopyVisitor;
import lambda.model.lambdaterm.visitor.ImplicitApplicationRemover;
import lambda.model.lambdaterm.visitor.ReplaceTermVisitor;
import lambda.viewcontroller.lambdaterm.LambdaTermViewController;

/**
 * Handles drag&drop events on a lambda term.
 *
 * @author Florian Fervers
 */
public class DragAndDrop extends InputAdapter {
    /**
     * Indicates whether debugging output is enabled.
     */
    public static final boolean DEBUG = false;
    /**
     * The minimum distance of dragging the touch to be recognized as a drag
     * operation.
     */
    public static final float MIN_DRAG_DISTANCE = 20.0f;
    /**
     * The node that is currently being dragged.
     */
    private LambdaTermViewController dragged;
    /**
     * The current list of drag&drop targets.
     */
    private final List<LambdaTermDropTarget> dropTargets;
    /**
     * The current list of drag&drop sources.
     */
    private final List<LambdaTermDragSource> dragSources;
    /**
     * The lambda term vc.
     */
    private final LambdaTermViewController vc;
    /**
     * The target that is currently being hovered or null if no target is being
     * hovered.
     */
    private LambdaTermDropTarget currentTarget;
    /**
     * The location that the last touch happened at. Set to null when the drag
     * starts.
     */
    private Vector2 touchDown;
    /**
     * The source from which the current drag&drop operation started.
     */
    private LambdaTermDragSource currentSource = null;

    /**
     * Creates a new drag&drop handler.
     *
     * @param vc the lambda term vc
     */
    public DragAndDrop(LambdaTermViewController vc) {
        this.dropTargets = new LinkedList<>();
        this.dragSources = new LinkedList<>();
        this.dragged = null;
        this.vc = vc;
        this.currentTarget = null;
    }

    /**
     * Called when a drag is started.
     *
     * @param source the drag source
     */
    public void dragStart(LambdaTermDragSource source) {
        LambdaTerm term;
        if (source.shouldSplit()) {
            source.getNode().getLinkedTerm().accept(new ReplaceTermVisitor(null));
            vc.getRoot().getLinkedTerm().accept(new ImplicitApplicationRemover());
            term = source.getNode().getLinkedTerm();
        } else {
            term = source.getNode().getLinkedTerm().accept(new CopyVisitor());
        }
        term.setLocked(false);

        // Drag actor is a new lambda term vc for the selected node
        LambdaRoot selection = new LambdaRoot();
        selection.setChild((LambdaTerm) term);
        dragged = LambdaTermViewController.build(selection, false, vc.getContext(), vc.getStage());
        dragged.addOffset(-dragged.getWidth() / 2.0f, -dragged.getHeight() / 2.0f);
        vc.getStage().addActor(dragged);

        for (LambdaTermDropTarget actor : dropTargets) {
            actor.setVisible(true);
        }

        if (DEBUG) {
            System.out.println("Dragging " + term.toString() + " from " + vc.getRoot().getLinkedTerm().toString());
        }
    }

    /**
     * Called when a drag is performed.
     *
     * @param target the currently hovered drag target or null if no target is
     * being hovered
     */
    public void drag(LambdaTermDropTarget target) {
        Vector2 pos = vc.getStage().screenToStageCoordinates(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
        dragged.setPosition(pos.x, pos.y);

        if (currentTarget != null) {
            currentTarget.setHovered(false);
        }
        if (target != null) {
            target.setHovered(true);
        }
        currentTarget = target;
    }

    /**
     * Called when a drag is stopped.
     */
    public void dragStop() {
        dragged.remove();
        for (LambdaTermDropTarget actor : dropTargets) {
            actor.setVisible(false);
        }

        if (currentTarget != null) {
            currentTarget.insert(dragged.getRoot().getChild(0).getLinkedTerm());
            currentTarget.setHovered(false);
            vc.getRoot().getLinkedTerm().accept(new ImplicitApplicationRemover());
        }

        if (DEBUG) {
            System.out.println("Dropped " + dragged.getRoot().getLinkedTerm().toString() + " resulting in " + vc.getRoot().getLinkedTerm().toString());
        }
    }

    /**
     * Adds a new drag&drop target.
     *
     * @param insertOperation the operation for inserting an element into the
     * term when it is dropped here
     * @param target the drop location
     */
    public void addDropTarget(Consumer<LambdaTerm> insertOperation, Rectangle target) {
        LambdaTermDropTarget actor = new LambdaTermDropTarget(target, vc, insertOperation);
        vc.addActor(actor);
        dropTargets.add(actor);
    }

    /**
     * Adds a new drag&drop source.
     *
     * @param source the new drag&drop source
     */
    public void addDragSource(final LambdaTermDragSource source) {
        dragSources.add(source);
    }

    /**
     * Clears all current drag&drop sources and targets (except permanent
     * sources, i.e. toolbar elements).
     */
    public void clearDragAndDrop() {
        for (LambdaTermDropTarget actor : dropTargets) {
            actor.remove();
        }
        dropTargets.clear();

        for (int i = 0; i < dragSources.size(); i++) {
            if (!dragSources.get(i).isPermanent()) {
                dragSources.remove(i);
                i--;
            }
        }
    }

    /**
     * Resets the touch state.
     */
    public void resetTouchState() {
        currentSource = null;
        touchDown = null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (pointer == 0 && button == 0) {
            // Find source under mouse pointer
            currentSource = null;
            touchDown = null;
            for (LambdaTermDragSource source : dragSources) {
                if (source.isOn(Gdx.input.getX(), Gdx.input.getY())) {
                    currentSource = source;
                    touchDown = new Vector2(screenX, screenY);
                    break;
                }
            }
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (pointer == 0 && button == 0) {
            if (currentSource != null && touchDown == null) {
                dragStop();
                currentSource = null;
                return true;
            } else {
                currentSource = null;
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (pointer == 0) {
            if (currentSource != null) {
                if (touchDown != null && new Vector2(screenX, screenY).dst(touchDown) >= MIN_DRAG_DISTANCE) {
                    // Touch down but drag not started yet
                    dragStart(currentSource);
                    touchDown = null;
                }
                if (touchDown == null) {
                    LambdaTermDropTarget t = null;
                    for (LambdaTermDropTarget target : dropTargets) {
                        if (target.isOn(Gdx.input.getX(), Gdx.input.getY())) {
                            t = target;
                            break;
                        }
                    }
                    drag(t);
                }
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}
