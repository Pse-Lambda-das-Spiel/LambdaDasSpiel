package lambda.viewcontroller.lambdaterm;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;

import java.util.IdentityHashMap;
import java.util.Map;

import lambda.model.lambdaterm.LambdaAbstraction;
import lambda.model.lambdaterm.LambdaRoot;
import lambda.model.lambdaterm.LambdaTerm;
import lambda.model.lambdaterm.LambdaTermObserver;
import lambda.model.lambdaterm.LambdaValue;
import lambda.model.lambdaterm.LambdaVariable;
import lambda.model.levels.LevelContext;
import lambda.viewcontroller.lambdaterm.draganddrop.DragAndDrop;
import lambda.viewcontroller.lambdaterm.visitor.ViewInsertionVisitor;
import lambda.viewcontroller.lambdaterm.visitor.ViewRemovalVisitor;

/**
 * Represents a libgdx viewcontroller object that displays a single lambda term
 * and handles input events that modify the term.
 *
 * @author Florian Fervers
 */
public final class LambdaTermViewController extends Group implements
        LambdaTermObserver {
    /**
     * The size of a display block relative to the size of the stage.
     */
    public static final float BLOCK_SIZE_PROPORTION = 0.1389f;
    /**
     * The size of a drag&drop gap relative to a display block.
     */
    public static final float GAP_SIZE_PROPORTION = 0.5f;
    /**
     * Indicates whether debugging output is enabled.
     */
    public static final boolean DEBUG = false;
    /**
     * Indicates whether drag&drop debugging output on the console is enabled.
     */
    public static final boolean DEBUG_DRAG_AND_DROP = false;
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
     * Handles drag&drop events on sources and targets.
     */
    private final DragAndDrop dragAndDrop;
    /**
     * The last node that has been alpha converted or null if no node has been
     * alpha converted yet.
     */
    private LambdaValueViewController lastAlphaConvertedNode;

    private AssetManager assets;

    /**
     * Creates a new instance of LambdaTermViewController.
     *
     * @param root the term to be displayed by this viewcontroller
     * @param editable true if this viewconroller can be edited by the user,
     * false otherwise
     * @param context contains all data of the current level
     * @param stage used for drag&drop
     * @return the new LambdaTermViewController
     * @throws IllegalArgumentException if root is null or context is null
     */
    public static LambdaTermViewController build(LambdaRoot root,
            boolean editable, LevelContext context, Stage stage) {
        if (root == null) {
            throw new IllegalArgumentException("Lambda term cannot be null!");
        }
        if (context == null) {
            throw new IllegalArgumentException("Level context cannot be null!");
        }
        if (DEBUG) {
            System.out.println("Building " + (editable ? "" : "non-")
                    + "editable VC for term \"" + root.toString() + "\"");
        }

        LambdaTermViewController result = new LambdaTermViewController(
                editable, context);
        result.setStage(stage);
        // Observe lambda term model
        root.addObserver(result);

        // Root node viewcontroller
        result.root = new LambdaNodeViewController(root, null, result, true) {
            @Override
            public float getMinWidth() {
                return 0.0f;
            }
        };
        result.addNode(result.root);

        // Recursive insertion of children
        root.accept(new ViewInsertionVisitor(root.getChild(), result));
        result.getRoot().updateWidth();

        return result;
    }

    public void setAssets(AssetManager assets) {
        this.assets = assets;
    }

    public AssetManager getAssets() {
        return assets;
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
        lastAlphaConvertedNode = null;
        nodeMap = new IdentityHashMap<>();
        if (editable) {
            dragAndDrop = new DragAndDrop(this);
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
        if (DEBUG) {
            System.out.println("    Term: Replacing "
                    + (oldTerm == null ? "null" : oldTerm.getClass()
                    .getSimpleName()
                    + " ("
                    + oldTerm.toString()
                    + ") under parent "
                    + oldTerm.getParent().getClass().getSimpleName()
                    + "(" + oldTerm.getParent().toString() + ")")
                    + " with "
                    + (newTerm == null ? "null" : newTerm.getClass()
                    .getSimpleName()
                    + " ("
                    + newTerm.toString()
                    + ") under parent "
                    + newTerm.getParent().getClass().getSimpleName()
                    + " (" + newTerm.getParent().toString() + ")"));
        }
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
    public void applicationStarted(LambdaAbstraction abstraction,
            LambdaTerm applicant) {
        LambdaAbstractionViewController vc = ((LambdaAbstractionViewController) getNode(abstraction));

        // Start animation and block until it is complete
        synchronized (this) {
            vc.animateMagic();
            while (!vc.isMagicAnimationFinished()) {
                try {
                    wait();
                } catch (InterruptedException ex) {
                    // TODO
                }
            }
        }
    }

    /**
     * Called before the given applicant is removed during an application.
     *
     * @param applicant the removed applicant
     */
    @Override
    public void removingApplicant(LambdaTerm applicant) {
        LambdaNodeViewController vc = ((LambdaNodeViewController) getNode(applicant));
        vc.setZIndex(Integer.MAX_VALUE);

        // Start animation and block until it is complete
        synchronized (this) {
            vc.animateVanish();
            while (!vc.isVanishAnimationFinished()) {
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
            vc.animateSmoke();
            while (!vc.isSmokeAnimationFinished()) {
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
        LambdaValueViewController vc = ((LambdaValueViewController) getNode(term));

        vc.setTargetColor(color, true);
    }

    /**
     * Called when the given values color is changed during an alpha conversion.
     *
     * @param term the modified term
     * @param color the new color
     */
    @Override
    public void alphaConverted(LambdaValue term, Color color) {
        lastAlphaConvertedNode = ((LambdaValueViewController) getNode(term));
    }

    /**
     * Called when a single alpha conversion is finished.
     */
    @Override
    public void alphaConversionFinished() {
        if (lastAlphaConvertedNode != null) {
            // Block until animation is complete
            synchronized (this) {
                while (!lastAlphaConvertedNode.isColorAnimationFinished()) {
                    try {
                        wait();
                    } catch (InterruptedException ex) {
                        // TODO
                    }
                }
            }
        }
    }

    /**
     * Adds an offset to all nodes in this tree.
     *
     * @param x x-coordinate of the offset
     * @param y y-coordinate of the offset
     */
    public void addOffset(float x, float y) {
        for (Actor child : this.getChildren()) {
            child.moveBy(x, y);
        }
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
            throw new IllegalArgumentException(
                    "Viewcontroller node cannot be null!");
        }

        if (DEBUG) {
            System.out.println("    Adding "
                    + node.getLinkedTerm().getClass().getSimpleName()
                    + " ("
                    + node.getLinkedTerm().toString()
                    + ") to parent "
                    + (node.getParentNode() != null ? node.getParentNode()
                    .getLinkedTerm().getClass().getSimpleName()
                    : "unknown")
                    + " ("
                    + (node.getParentNode() != null ? node.getParentNode()
                    .getLinkedTerm().toString() : "null") + ")");
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
            throw new IllegalArgumentException(
                    "Viewcontroller node cannot be null!");
        }

        if (DEBUG) {
            System.out.println("    Removing "
                    + node.getLinkedTerm().getClass().getSimpleName()
                    + " ("
                    + node.getLinkedTerm().toString()
                    + ") from parent "
                    + (node.getParentNode() != null ? node.getParentNode()
                    .getLinkedTerm().getClass().getSimpleName()
                    : "unknown")
                    + " ("
                    + (node.getParentNode() != null ? node.getParentNode()
                    .getLinkedTerm().toString() : "null") + ")");
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
     * Returns the drag&drop handler.
     *
     * @return the drag&drop handler
     */
    public DragAndDrop getDragAndDrop() {
        return dragAndDrop;
    }

    /**
     * Returns the width = height of a single display block.
     *
     * @return the width = height of a single display block
     */
    public float getBlockSize() {
        return Math.min(getStage().getHeight(), getStage().getWidth()) * BLOCK_SIZE_PROPORTION;
    }

    /**
     * Returns the width of a drag&drop gap.
     *
     * @return the the width of a drag&drop gap
     */
    public float getGapWidth() {
        return getBlockSize() * GAP_SIZE_PROPORTION;
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
}
