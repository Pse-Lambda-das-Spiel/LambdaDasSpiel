package lambda.model.editormode;

import lambda.Observable;
import lambda.model.lambdaterm.LambdaRoot;
import lambda.model.lambdaterm.visitor.CopyVisitor;
import lambda.model.lambdaterm.visitor.strategy.BetaReductionVisitor;
import lambda.model.levels.LevelContext;
import lambda.model.levels.ReductionStrategy;
import lambda.model.reductionmode.ReductionModel;

/**
 * Contains data and logics of the editor mode. Will be observed by the editor view controller.
 * 
 * @author Florian Fervers
 */
public class EditorModel extends Observable<EditorModelObserver> {
    /**
     * Contains all data of the current level.
     */
    private final LevelContext context;
    /**
     * Stores the currently selected reduction strategy.
     */
    private ReductionStrategy strategy;
    /**
     * Stores the current lambda term
     */
    private final LambdaRoot term;
    
    /**
     * Creates a new instance of EditorModel.
     * 
     * @param context contains all data of the current level
     * @throws IllegalArgumentException if context is null
     */
    public EditorModel(LevelContext context) {
        if (context == null) {
            throw new IllegalArgumentException("Level context cannot be null!");
        }
        this.context = context;
        strategy = context.getLevelModel().getDefaultStrategy();
        term = (LambdaRoot) context.getLevelModel().getStart().accept(new CopyVisitor());
    }
    
    /**
     * Sets the currently selected reduction strategy.
     * 
     * @param strategy the new selected reduction strategy
     * @throws IllegalArgumentException if strategy is null
     */
    public void setStrategy(ReductionStrategy strategy) {
        if (strategy == null) {
            throw new IllegalArgumentException("Reduction strategy cannot be null!");
        }
        if (strategy != this.strategy) {
            this.strategy = strategy;
            notify((observer) -> observer.strategyChanged(strategy));
        }
    }
    
    /**
     * Returns the level context.
     * 
     * @return the level context
     */
    public LevelContext getLevelContext() {
        return context;
    }
    
    /**
     * Creates a reduction model for the current state of the editor model.
     * 
     * @return a reduction model for the current state of the editor model
     */
    public ReductionModel createReductionModel() {
        return new ReductionModel((LambdaRoot) term.accept(new CopyVisitor()), BetaReductionVisitor.fromReductionStrategy(strategy), context);
    }
}
