package lambda.viewcontroller.lambdaterm;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;

import java.util.LinkedList;
import java.util.List;

import lambda.Consumer;
import static lambda.LambdaGame.DEBUG;
import lambda.model.lambdaterm.LambdaTerm;
import lambda.model.lambdaterm.visitor.FrontInserter;
import lambda.model.lambdaterm.visitor.SiblingInserter;
import lambda.viewcontroller.lambdaterm.draganddrop.LambdaTermDragSource;
import lambda.viewcontroller.lambdaterm.draganddrop.LambdaTermDropTarget;

/**
 * Represents a single viewcontroller node in a LambdaTermViewController.
 *
 * @author Florian Fervers
 */
public abstract class LambdaNodeViewController extends Actor {
    /**
     * Max distance of two floats to be still considered equal.
     */
    public static final float EPSILON = 1e-6f;
    /**
     * The width of one displayed block. Variables consist of one block,
     * parenthesis and abstractions of at least three.
     */
    public static final float BLOCK_WIDTH = 100.0f;
    /**
     * The height of one displayed block.
     */
    public static final float BLOCK_HEIGHT = 100.0f;
    /**
     * The size of the gap between two nodes.
     */
    public static final float GAP_SIZE = 10.0f;
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
     * Creates a new instance of LambdaNodeViewController.
     *
     * @param linkedTerm the term that is displayed by this viewcontroller
     * @param parent the parent viewcontroller node
     * @param viewController the viewcontroller on which this node is being
     * displayed
     */
    public LambdaNodeViewController(LambdaTerm linkedTerm, LambdaNodeViewController parent, LambdaTermViewController viewController) {
        if (linkedTerm == null) {
            throw new IllegalArgumentException("Linked term cannot be null!");
        }
        if (viewController == null) {
            throw new IllegalArgumentException("Lambda term viewcontroller cannot be null!");
        }
        this.linkedTerm = linkedTerm;
        this.parent = parent;
        this.viewController = viewController;
        children = new LinkedList<>();
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
     * @param index the child's index
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
     * Inserts the given node as a child of this node left to the given right
     * sibling. Will insert child at the end of the children list if right
     * sibling is null.
     *
     * @param child the child to be inserted
     * @param rightSibling the right sibling
     * @throws IllegalArgumentException if child is null or rightSibling is null
     */
    public void insertChild(LambdaNodeViewController child, LambdaTerm rightSibling) {
        if (child == null) {
            throw new IllegalArgumentException("Child node viewcontroller cannot be null!");
        }
        if (rightSibling == null) {
            // Insert at the end of the children list
            children.add(child);
        } else {
            // Insert before right sibling
            for (int i = 0; i < children.size() + 1; i++) {
                if (i == children.size() || children.get(i).getLinkedTerm() == rightSibling) {
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
     * @param child the child to be removed
     * @throws IllegalArgumentException if child is null
     */
    public void removeChild(LambdaNodeViewController child) {
        if (child == null) {
            throw new IllegalArgumentException("Child node viewcontroller cannot be null!");
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
    private void updateWidth() {
        // Calculate own width
        float width = 0.0f;
        for (LambdaNodeViewController child : children) {
            width += child.getWidth();
        }
        width = Math.max(width, getMinWidth());
        setWidth(width);
        if (DEBUG) {
            System.out.println("        Updated width of (" + getLinkedTerm().toString() + ") to " + width);
        }

        // Recurse
        if (!isRoot()) {
            parent.updateWidth();
        } else {
            // Update position downwards if root is reached, then update drag&drop sources and targets
            updatePosition(0.0f, 0.0f);
            if (viewController.isEditable()) {
                viewController.getDragAndDrop().clear();
                updateDragAndDrop();
            }
        }
    }

    /**
     * Sets the position for the current node. Then traverses down the tree.
     *
     * @param x the new x-coordinate of this node
     * @param y the new y-coordinate of this node
     */
    private void updatePosition(float x, float y) {
        setPosition(x, y);
        if (DEBUG) {
            System.out.println("        Updated position of (" + getLinkedTerm().toString() + ") to (" + x + ", " + y + ")");
        }

        // Recurse
        y += BLOCK_HEIGHT;
        for (LambdaNodeViewController child : children) {
            child.updatePosition(x, y);
            x += child.getWidth();
        }
    }

    /**
     * Updates the drag&drop source for this node and the drag&drop targets for
     * all spaces next to its children. Then traverses down the tree.
     */
    private void updateDragAndDrop() {
        if (getViewController().isEditable()) {
            // Add drag&drop source and targets
            if (!this.isRoot()) {
                DragAndDrop dragAndDrop = viewController.getDragAndDrop();
                dragAndDrop.addSource(new LambdaTermDragSource(this, true));

                // First target left of all children
                Rectangle target = new Rectangle(this.getX() - GAP_SIZE / 2, this.getY(), GAP_SIZE, BLOCK_HEIGHT);
                //dragAndDrop.addTarget(new LambdaTermDropTarget(target, term -> getLinkedTerm().accept(new FrontInserter(term))));
                dragAndDrop.addTarget(new LambdaTermDropTarget(target,
                        new Consumer<LambdaTerm>() {
                            @Override
                            public void accept(LambdaTerm term) {
                                LambdaNodeViewController.this.getLinkedTerm().accept(new FrontInserter(term));
                            }
                        }));

                // Targets right of each child
                for (LambdaNodeViewController childVC : children) {
                    final LambdaTerm childTerm = childVC.getLinkedTerm();
                    target = new Rectangle(childVC.getX() + BLOCK_WIDTH - GAP_SIZE / 2, childVC.getY(), GAP_SIZE, BLOCK_HEIGHT);
                    //dragAndDrop.addTarget(new LambdaTermDropTarget(target, term -> childTerm.accept(new SiblingInserter(term, false))));
                    dragAndDrop.addTarget(new LambdaTermDropTarget(target,
                            new Consumer<LambdaTerm>() {
                                @Override
                                public void accept(LambdaTerm term) {
                                    childTerm.accept(new SiblingInserter(term, false));
                                }
                            }));
                }
            }

            // Recurse
            for (LambdaNodeViewController child : children) {
                child.updateDragAndDrop();
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
     * @param other the other object
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
