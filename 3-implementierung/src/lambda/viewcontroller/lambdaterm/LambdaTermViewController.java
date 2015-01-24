package lambda.viewcontroller.lambdaterm;

import com.badlogic.gdx.scenes.scene2d.Group;
import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import lambda.model.lambdaterm.LambdaRoot;
import lambda.model.lambdaterm.LambdaTerm;
import lambda.model.lambdaterm.LambdaTermObserver;
import lambda.model.lambdaterm.LambdaValue;
import lambda.model.levels.LevelContext;

/**
 * Represents a libgdx viewcontroller object that displays a single lambda term and handles input events that modify the term.
 * 
 * @author Florian Fervers
 */
public final class LambdaTermViewController extends Group implements LambdaTermObserver {
    /**
     * Indicates whether this term can be modified by the user.
     */
    private final boolean editable;
    
    /**
     * Maps model nodes to viewcontroller nodes. Not all model nodes must have a viewcontroller node.
     */
    private final Map<LambdaTerm, LambdaNodeViewController> nodeViewMap;
    
    /**
     * Contains all data of the current level.
     */
    private final LevelContext context;
    
    public static LambdaTermViewController build(LambdaRoot term, boolean editable, LevelContext context) {
        LambdaTermViewController result = new LambdaTermViewController(term, editable, context);
        
        // Observe lambda term model
        term.addObserver(result);
        
        // Root node view
        result.addNodeView(new LambdaNodeViewController(term, null, result) {
            @Override
            public float getMinWidth() {
                return 0.0f;
            }
        });
        return result;
    }
    
    private LambdaTermViewController(LambdaRoot term, boolean editable, LevelContext context) {
        this.editable = editable;
        this.context = context;
        nodeViewMap = new HashMap<>();
    }
    
    /**
     * Is called when the given old term is replaced with the given new term. Either oldTerm or newTerm can be null, but never both at the same time.
     * 
     * @param oldTerm the old term to be replaced
     * @param newTerm the new replacing term
     */
    @Override
    public void replaceTerm(LambdaTerm oldTerm, LambdaTerm newTerm) {
        
    }
    
    /**
     * Is called when the given term's color is changed.
     * 
     * @param term the modified term
     * @param color the new color
     */
    @Override
    public void setColor(LambdaValue term, Color color) {
        
    }
    
    /**
     * Returns whether this term can be modified by the user.
     * 
     * @return true if this term can be modified by the user, false otherwise
     */
    public boolean isEditable() {
        return editable;
    }
    
    protected void addNodeView(LambdaNodeViewController node) {
        nodeViewMap.put(node.getLinkedTerm(), node);
        addActor(node);
        // node.addListener TODO
    }
}
