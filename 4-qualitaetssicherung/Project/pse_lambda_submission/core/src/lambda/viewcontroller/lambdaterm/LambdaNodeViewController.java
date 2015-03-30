package lambda.viewcontroller.lambdaterm;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.LinkedList;
import java.util.List;

import lambda.Consumer;
import static lambda.LambdaGame.DEBUG;
import lambda.model.lambdaterm.LambdaApplication;
import lambda.model.lambdaterm.LambdaTerm;
import lambda.model.lambdaterm.LambdaUtils;
import lambda.model.lambdaterm.visitor.FrontInserter;
import lambda.model.lambdaterm.visitor.ImplicitApplicationRemover;
import lambda.model.lambdaterm.visitor.SiblingInserter;
import static lambda.viewcontroller.lambdaterm.LambdaTermViewController.DEBUG_DRAG_AND_DROP;
import lambda.viewcontroller.lambdaterm.draganddrop.LambdaTermDragSource;

/**
 * Represents a single viewcontroller node in a LambdaTermViewController.
 *
 * @author Florian Fervers
 */
public abstract class LambdaNodeViewController extends Actor {
    /**
     * Max distance of two floats to still be considered equal.
     */
    public static final float EPSILON = 1e-3f;
    /**
     * The term that is displayed by this viewcontroller.
     */
    private final LambdaTerm linkedTerm;
    /**
     * The viewcontroller on which this node is being displayed.
     */
    private final LambdaTermViewController viewController;
    /**
     * The parent viewcontroller node. Can be null.
     */
    private final LambdaNodeViewController parent;
    /**
     * The viewcontroller child nodes.
     */
    private final List<LambdaNodeViewController> children;
    /**
     * Indicates whether this node can have children.
     */
    private final boolean canHaveChildren;
    /**
     * The animation that is shown when an applicant is removed during an
     * application.
     */
    private final Animation animation;
    /**
     * Indicates whether the animation for removing an applicant during an
     * application is currently being or has been run. Is set to true when the
     * animation starts.
     */
    private boolean animateVanishSmoke;
    /**
     * The time since the start of the animation or zero if the animation hasn't
     * started yet.
     */
    private float vanishSmokeStateTime;

    /**
     * Creates a new instance of LambdaNodeViewController.
     *
     * @param linkedTerm
     *            the term that is displayed by this viewcontroller
     * @param parent
     *            the parent viewcontroller node
     * @param viewController
     *            the viewcontroller on which this node is being
     * @param canHaveChildren
     *            true if this node can have children, false otherwise
     */
    public LambdaNodeViewController(LambdaTerm linkedTerm,
            LambdaNodeViewController parent,
            LambdaTermViewController viewController, boolean canHaveChildren) {
        if (linkedTerm == null) {
            throw new IllegalArgumentException("Linked term cannot be null!");
        }
        if (viewController == null) {
            throw new IllegalArgumentException(
                    "Lambda term viewcontroller cannot be null!");
        }
        this.linkedTerm = linkedTerm;
        this.parent = parent;
        this.viewController = viewController;
        this.canHaveChildren = canHaveChildren;
        animation = viewController.getContext().getCloudAnimation();
        animateVanishSmoke = false;
        vanishSmokeStateTime = 0.0f;
        children = new LinkedList<>();
        setHeight(viewController.getBlockSize());
        assert (viewController.getStage() != null);
        setStage(viewController.getStage());
    }

    /**
     * Returns whether this node is a root, i.e. whether the parent node is
     * null.
     *
     * @return true if this node is a root, false otherwise
     */
    public boolean isRoot() {
        return parent == null;
    }

    /**
     * Returns the term that is displayed by this viewcontroller.
     *
     * @return the term that is displayed by this viewcontroller
     */
    public LambdaTerm getLinkedTerm() {
        return linkedTerm;
    }

    /**
     * Returns the viewcontroller parent node if this node.
     *
     * @return the viewcontroller parent node if this node
     */
    public LambdaNodeViewController getParentNode() {
        return parent;
    }

    /**
     * Returns the viewcontroller that displays this node.
     *
     * @return the viewcontroller that displays this node
     */
    public LambdaTermViewController getViewController() {
        return viewController;
    }

    /**
     * Returns the child at the given index.
     *
     * @param index
     *            the child's index
     * @return the child at the given index
     */
    public LambdaNodeViewController getChild(int index) {
        return children.get(index);
    }

    /**
     * Returns the minimum width of this node view.
     *
     * @return the minimum width of this node view
     */
    public abstract float getMinWidth();

    /**
     * Returns the family height (i.e. height of this node and all children
     * combined).
     *
     * @return the family height
     */
    public float getFamilyHeight() {
        float result = 0.0f;
        for (LambdaNodeViewController child : children) {
            result = Math.max(child.getFamilyHeight(), result);
        }
        return result + viewController.getBlockSize();
    }

    /**
     * Starts the vanish animation.
     */
    public void animateVanish() {
        animateVanishSmoke = true;
    }

    /**
     * Returns whether the vanish animation is fininshed.
     *
     * @return true if the vanish animation is finished, false otherwise
     */
    public boolean isVanishAnimationFinished() {
        return vanishSmokeStateTime >= animation.getAnimationDuration();
    }

    /**
     * Draws the vanish animation over this node family if necessary.
     *
     * @param batch
     *            the batch on which the node will be drawn
     * @param alpha
     *            the parent's alpha
     */
    public void drawVanishAnimation(Batch batch, float alpha) {
        // Vanish animation
        synchronized (getViewController()) {
            if (animateVanishSmoke) {
                float familyHeight = getFamilyHeight();
                batch.draw(animation.getKeyFrame(vanishSmokeStateTime), getX(),
                        getY() - familyHeight + viewController.getBlockSize(), getWidth(),
                        familyHeight);
                vanishSmokeStateTime += Gdx.graphics.getDeltaTime();
                if (isVanishAnimationFinished()) {
                    animateVanishSmoke = false;
                    getViewController().notifyAll();
                }
            }
        }
    }

    /**
     * Inserts the given node as a child of this node left to the given right
     * sibling. Will insert child at the end of the children list if right
     * sibling is null.
     *
     * @param child
     *            the child to be inserted
     * @param rightSibling
     *            the right sibling
     * @throws IllegalArgumentException
     *             if child is null or rightSibling is null
     */
    public void insertChild(LambdaNodeViewController child,
            LambdaTerm rightSibling) {
        if (child == null) {
            throw new IllegalArgumentException(
                    "Child node viewcontroller cannot be null!");
        }
        if (rightSibling == null) {
            // Insert at the end of the children list
            children.add(child);
        } else {
            // Insert before right sibling
            for (int i = 0; i < children.size() + 1; i++) {
                if (i == children.size()
                        || children.get(i).getLinkedTerm() == rightSibling) {
                    children.add(i, child);
                    break;
                }
            }
        }

        viewController.addNode(child);
        child.updateWidth();
    }

    /**
     * Removes the given child node from this node.
     *
     * @param child
     *            the child to be removed
     * @throws IllegalArgumentException
     *             if child is null
     */
    public void removeChild(LambdaNodeViewController child) {
        if (child == null) {
            throw new IllegalArgumentException(
                    "Child node viewcontroller cannot be null!");
        }
        children.remove(child);

        viewController.removeNode(child);
        child.updateWidth();
    }

    /**
     * Calculates the width of the current node. Then traverses up the tree. If
     * the node is reached, updates positions and then drag&drop sources sand
     * targets.
     */
    public void updateWidth() {
        // Calculate own width
        float width = 0.0f;
        for (LambdaNodeViewController child : children) {
            width += child.getWidth();
        }
        width = Math.max(width, getMinWidth());
        setWidth(width);
        if (DEBUG) {
            System.out.println("        Updated size of "
                    + this.getLinkedTerm().getClass().getSimpleName() + " ("
                    + getLinkedTerm().toString() + ") to (" + getWidth() + ", "
                    + getHeight() + ")");
        }

        // Recurse
        if (!isRoot()) {
            parent.updateWidth();
        } else {
            // Update position downwards if root is reached, then update
            // drag&drop sources and targets
            getViewController().setSize(0.0f, 0.0f);
            updatePosition(0.0f, 0.0f);
            updateDragAndDrop();
        }
    }

    /**
     * Sets the position for the current node. Then traverses down the tree.
     *
     * @param x
     *            the new x-coordinate of this node
     * @param y
     *            the new y-coordinate of this node
     */
    public void updatePosition(float x, float y) {
        setPosition(x, y);
        getViewController().setSize(
                Math.max(getViewController().getX(), x + this.getWidth()),
                Math.max(getViewController().getY(), -y + this.getHeight()
                        + viewController.getBlockSize()));
        if (DEBUG) {
            System.out.println("        Updated position of "
                    + this.getLinkedTerm().getClass().getSimpleName() + " ("
                    + getLinkedTerm().toString() + ") to (" + getX() + ", "
                    + getY() + ")");
        }

        // Recurse
        if (!isRoot()) {
            y -= viewController.getBlockSize();
        }
        for (LambdaNodeViewController child : children) {
            child.updatePosition(x, y);
            x += child.getWidth();
        }
    }

    /**
     * Updates the drag&drop source for this node and the drag&drop targets for
     * all spaces next to its children. Then traverses down the tree.
     */
    public void updateDragAndDrop() {
        if (getViewController().isEditable()) {
            if (this.isRoot()) {
                // Clear drag&drop once when at the root
                if (DEBUG_DRAG_AND_DROP) {
                    System.out.println("        Clearing drag&drop");
                }

                getViewController().getDragAndDrop().clearDragAndDrop();
            } else if (!getLinkedTerm().isLocked()) {
                // Add drag&drop source for all elements but the root
                assert (this.getStage() != null);
                getViewController().getDragAndDrop().addDragSource(
                        new LambdaTermDragSource(this, true, false));
            }

            // Add drop targets next to children
            if (canHaveChildren) {
                // 1. First target left of all children
                Rectangle target;
                if (children.isEmpty()) {
                    // Drop target in the center below this element
                    target = new Rectangle(this.getX() + this.getWidth() / 2.0f
                            - viewController.getGapWidth() / 2, this.getY() - viewController.getBlockSize(),
                            viewController.getGapWidth(), viewController.getBlockSize());
                    if (DEBUG_DRAG_AND_DROP) {
                        System.out.println("        Adding drop target below "
                                + getLinkedTerm().getClass().getSimpleName()
                                + " (" + getLinkedTerm().toString() + ")");
                    }
                    getViewController().getDragAndDrop().addDropTarget(
                            new Consumer<LambdaTerm>() {
                                @Override
                                public void accept(LambdaTerm term) {
                                    if (DEBUG) {
                                        System.out.println("Inserting "
                                                + term.getClass()
                                                        .getSimpleName()
                                                + " ("
                                                + term.toString()
                                                + ") as first and only child of "
                                                + getLinkedTerm().getClass()
                                                        .getSimpleName() + " ("
                                                + getLinkedTerm().toString()
                                                + ")");
                                    }
                                    getLinkedTerm().accept(
                                            new FrontInserter(term));
                                }
                            }, target);
                } else {
                    // Drop target left of first child
                    target = new Rectangle(children.get(0).getX(), children
                            .get(0).getY(), viewController.getGapWidth(), viewController.getBlockSize());
                    if (DEBUG_DRAG_AND_DROP) {
                        System.out
                                .println("        Adding drop target left of "
                                        + children.get(0).getLinkedTerm()
                                                .getClass().getSimpleName()
                                        + " ("
                                        + children.get(0).getLinkedTerm()
                                                .toString() + ")");
                    }

                    getViewController().getDragAndDrop().addDropTarget(
                            new Consumer<LambdaTerm>() {
                                @Override
                                public void accept(LambdaTerm term) {
                                    if (DEBUG) {
                                        System.out.println("Inserting "
                                                + term.getClass()
                                                        .getSimpleName()
                                                + " ("
                                                + term.toString()
                                                + ") as left sibling of "
                                                + children.get(0)
                                                        .getLinkedTerm()
                                                        .getClass()
                                                        .getSimpleName()
                                                + " ("
                                                + children.get(0)
                                                        .getLinkedTerm()
                                                        .toString() + ")");
                                    }
                                    children.get(0)
                                            .getLinkedTerm()
                                            .accept(new SiblingInserter(term,
                                                    true));
                                }
                            }, target);
                }

                // 2. Targets right of each child
                int index = 0;
                for (LambdaNodeViewController childVC : children) {
                    LambdaTerm childTerm = childVC.getLinkedTerm();
                    if (index > 0) {
                        // Sibling is the parent application
                        childTerm = childTerm.getParent();
                        assert (childTerm instanceof LambdaApplication);
                    }

                    if (DEBUG_DRAG_AND_DROP) {
                        System.out
                                .println("        Adding drop target right of "
                                        + childTerm.getClass().getSimpleName()
                                        + " (" + childTerm.toString() + ")");
                    }

                    final LambdaTerm siblingTerm = childTerm;
                    target = new Rectangle(childVC.getX()
                            + childVC.getWidth()
                            - (index == children.size() - 1 ? viewController.getGapWidth()
                                    : viewController.getGapWidth() / 2), childVC.getY(), viewController.getGapWidth(),
                            viewController.getBlockSize());
                    getViewController().getDragAndDrop().addDropTarget(
                            new Consumer<LambdaTerm>() {
                                @Override
                                public void accept(LambdaTerm term) {
                                    if (DEBUG) {
                                        System.out.println("Inserting "
                                                + term.getClass()
                                                        .getSimpleName()
                                                + " ("
                                                + term.toString()
                                                + ") as right sibling of "
                                                + siblingTerm.getClass()
                                                        .getSimpleName() + " ("
                                                + siblingTerm.toString() + ")");
                                    }
                                    siblingTerm.accept(new SiblingInserter(
                                            term, false));
                                    LambdaUtils
                                            .getRoot(getLinkedTerm())
                                            .accept(new ImplicitApplicationRemover());
                                }
                            }, target);
                    index++;
                }

                // Recurse
                for (LambdaNodeViewController child : children) {
                    child.updateDragAndDrop();
                }
            }
        }
    }

    /**
     * Returns a string representation of this object.
     *
     * @return a string representation of this object
     */
    @Override
    public String toString() {
        return linkedTerm.toString();
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
        if (!(other instanceof LambdaNodeViewController)) {
            return false;
        }
        LambdaNodeViewController node = (LambdaNodeViewController) other;
        if (node.children.size() != this.children.size()) {
            return false;
        }
        for (int i = 0; i < children.size(); i++) {
            if (!node.children.get(i).equals(this.children.get(i))) {
                return false;
            }
        }
        return true;
    }
}
