package lambda.viewcontroller.lambdaterm;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;

import java.util.IdentityHashMap;
import java.util.Map;
import static lambda.LambdaGame.DEBUG;

import lambda.model.lambdaterm.LambdaAbstraction;
import lambda.model.lambdaterm.LambdaRoot;
import lambda.model.lambdaterm.LambdaTerm;
import lambda.model.lambdaterm.LambdaTermObserver;
import lambda.model.lambdaterm.LambdaValue;
import lambda.model.lambdaterm.LambdaVariable;
import lambda.model.levels.LevelContext;
import lambda.viewcontroller.lambdaterm.visitor.ViewInsertionVisitor;
import lambda.viewcontroller.lambdaterm.visitor.ViewRemovalVisitor;

/**
 * Represents a libgdx viewcontroller object that displays a single lambda term
 * and handles input events that modify the term.
 *
 * @author Florian Fervers
 */
public final class LambdaTermViewController extends Group implements LambdaTermObserver {
    /**
     * Indicates whether this term can be modified by the user.
     */
    private final boolean editable;
    /**
     * Maps model nodes to viewcontroller nodes. Not all model nodes must have a
     * viewcontroller node.
     */
    private final Map<LambdaTerm, LambdaNodeViewController> nodeMap;
    /**
     * Contains all data of the current level.
     */
    private final LevelContext context;
    /**
     * The root of the viewController tree.
     */
    private LambdaNodeViewController root;
    /**
     * Handles the dragging and dropping of nodes.
     */
    private final DragAndDrop dragAndDrop;

    /**
     * Creates a new instance of LambdaTermViewController.
     *
     * @param root the term to be displayed by this viewcontroller
     * @param editable true if this viewconroller can be edited by the user,
     * false otherwise
     * @param context contains all data of the current level
     * @return the new LambdaTermViewController
     * @throws IllegalArgumentException if root is null or context is null
     */
    public static LambdaTermViewController build(LambdaRoot root, boolean editable, LevelContext context) {
        if (root == null) {
            throw new IllegalArgumentException("Lambda term cannot be null!");
        }
        if (context == null) {
            throw new IllegalArgumentException("Level context cannot be null!");
        }
        if (DEBUG) {
            System.out.println("Building VC for term \"" + root.toString() + "\"");
        }

        LambdaTermViewController result = new LambdaTermViewController(editable, context);

        // Observe lambda term model
        root.addObserver(result);

        // Root node viewcontroller
        result.root = new LambdaNodeViewController(root, null, result) {
            @Override
            public float getMinWidth() {
                return 0.0f;
            }
        };
        result.addNode(result.root);

        // Recursive insertion of children
        root.accept(new ViewInsertionVisitor(root.getChild(), result));

        return result;
    }

    /**
     * Creates a new instance of LambdaTermViewController. Only used by build()
     * since it is required to pass this as a reference to other functions.
     *
     * @param editable true if this viewconroller can be edited by the user,
     * false otherwise
     * @param context contains all data of the current level
     */
    private LambdaTermViewController(boolean editable, LevelContext context) {
        this.editable = editable;
        this.context = context;
        nodeMap = new IdentityHashMap<>();
        if (editable) {
            dragAndDrop = new DragAndDrop();
            dragAndDrop.setButton(0);
        } else {
            dragAndDrop = null;
        }
    }

    /**
     * Is called when the given old term is replaced with the given new term.
     * Either oldTerm or newTerm can be null, but never both at the same time.
     *
     * @param oldTerm the old term to be replaced
     * @param newTerm the new replacing term
     */
    @Override
    public void replaceTerm(LambdaTerm oldTerm, LambdaTerm newTerm) {
        if (oldTerm != null) {
            oldTerm.accept(new ViewRemovalVisitor(this));
        }
        if (newTerm != null) {
            newTerm.getParent().accept(new ViewInsertionVisitor(newTerm, this));
        }
    }

    /**
     * Called when an application is started.
     *
     * @param abstraction the applied abstraction
     * @param applicant the applicant
     */
    @Override
    public void applicationStarted(LambdaAbstraction abstraction, LambdaTerm applicant) {
        LambdaAbstractionViewController vc = ((LambdaAbstractionViewController) getNode(abstraction));

        // Start animation and block until it is complete
        synchronized (this) {
            vc.animate();
            while (!vc.isAnimationFinished()) {
                try {
                    wait();
                } catch (InterruptedException ex) {
                    // TODO 
                }
            }
        }
    }

    /**
     * Called when the given variable is replaced by the given term during a
     * beta reduction.
     *
     * @param variable the removed applicant
     * @param replacing the replacing term
     */
    @Override
    public void variableReplaced(LambdaVariable variable, LambdaTerm replacing) {
        LambdaVariableViewController vc = ((LambdaVariableViewController) getNode(variable));

        // Start smoke animation and block until it is complete
        synchronized (this) {
            vc.animate();
            while (!vc.isAnimationFinished()) {
                try {
                    wait();
                } catch (InterruptedException ex) {
                    // TODO 
                }
            }
        }
    }

    /**
     * Is called when the given term's color is changed.
     *
     * @param term the modified term
     * @param color the new color
     */
    @Override
    public void setColor(LambdaValue term, Color color) {
        // TODO animation color change
        ((LambdaValueViewController) getNode(term)).setLambdaColor(color);
    }

    /**
     * Returns whether this term can be modified by the user.
     *
     * @return true if this term can be modified by the user, false otherwise
     */
    public boolean isEditable() {
        return editable;
    }

    /**
     * Returns the current level context.
     *
     * @return the current level context
     */
    public LevelContext getContext() {
        return context;
    }

    /**
     * Returns the drag&drop manager.
     *
     * @return the drag&drop manager
     */
    public DragAndDrop getDragAndDrop() {
        return dragAndDrop;
    }

    /**
     * Returns the root of the viewcontroller tree.
     *
     * @return the root of the viewcontroller tree
     */
    public LambdaNodeViewController getRoot() {
        return root;
    }

    /**
     * Adds the given node to this viewcontroller.
     *
     * @param node the node to be added
     * @throws IllegalArgumentException if node is null
     */
    public void addNode(LambdaNodeViewController node) {
        if (node == null) {
            throw new IllegalArgumentException("Viewcontroller node cannot be null!");
        }

        if (DEBUG) {
            System.out.println("    Adding " + node.getLinkedTerm().getClass().getSimpleName() + " (" + node.getLinkedTerm().toString() + ") to parent " + (node.getParentNode() != null ? node.getParentNode().getLinkedTerm().getClass().getSimpleName() : "unknown") + " (" + (node.getParentNode() != null ? node.getParentNode().getLinkedTerm().toString() : "null") + ")");
        }

        nodeMap.put(node.getLinkedTerm(), node);
        addActor(node);
    }

    /**
     * Removes the given node from this viewcontroller.
     *
     * @param node the node to be removed
     * @throws IllegalArgumentException if node is null
     */
    public void removeNode(LambdaNodeViewController node) {
        if (node == null) {
            throw new IllegalArgumentException("Viewcontroller node cannot be null!");
        }

        if (DEBUG) {
            System.out.println("    Removing " + node.getLinkedTerm().getClass().getSimpleName() + " (" + node.getLinkedTerm().toString() + ") from parent " + (node.getParentNode() != null ? node.getParentNode().getLinkedTerm().getClass().getSimpleName() : "unknown") + " (" + (node.getParentNode() != null ? node.getParentNode().getLinkedTerm().toString() : "null") + ")");
        }

        nodeMap.remove(node.getLinkedTerm());
        removeActor(node);
    }

    /**
     * Returns the node that displays the given term or null if no node displays
     * that term.
     *
     * @param term the term that displays the returned node
     * @return the node that displays the given term or null if no node displays
     * that term
     * @throws IllegalArgumentException if term is null
     */
    public LambdaNodeViewController getNode(LambdaTerm term) {
        if (term == null) {
            throw new IllegalArgumentException("Lambda term cannot be null!");
        }
        return nodeMap.get(term);
    }

    /**
     * Returns whether the given term is explicitly displayed by a
     * viewcontroller node.
     *
     * @param term the term that is/ is not displayed
     * @return true if the given term is explicitly displayed by a
     * viewcontroller node, false otherwise
     * @throws IllegalArgumentException if term is null
     */
    public boolean hasNode(LambdaTerm term) {
        if (term == null) {
            throw new IllegalArgumentException("Lambda term cannot be null!");
        }
        return nodeMap.containsKey(term);
    }

    /**
     * Returns whether this and the other object are equal.
     *
     * @param other the other object
     * @return true if this and the other object are equal, false otherwise
     */
    @Override
    public boolean equals(Object other) {
        if (!(other instanceof LambdaTermViewController)) {
            return false;
        }
        LambdaTermViewController vc = (LambdaTermViewController) other;
        return this.editable == vc.editable && this.root.equals(vc.root);
    }

    @Override
    public void alphaConverted(LambdaValue term, Color color) {
    }
}
