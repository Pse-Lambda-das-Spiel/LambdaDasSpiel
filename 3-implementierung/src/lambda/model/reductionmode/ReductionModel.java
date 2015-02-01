package lambda.model.reductionmode;

import java.util.Stack;
import lambda.Observable;
import lambda.model.lambdaterm.LambdaRoot;
import lambda.model.lambdaterm.visitor.CopyVisitor;
import lambda.model.lambdaterm.visitor.IsAlphaEquivalentVisitor;
import lambda.model.lambdaterm.visitor.strategy.BetaReductionVisitor;
import lambda.model.levels.LevelContext;

/**
 * Contains data and logics of the reduction mode. Will be observed by the reduction view controller.
 * 
 * @author Florian Fervers
 */
public class ReductionModel extends Observable<ReductionModelObserver> {
    /**
     * Saves all previous lambda terms to be able to undo reduction steps.
     */
    private final Stack<LambdaRoot> history;
    /**
     * Indicated whether the automatic reduction is paused. When paused is true the model will automatically perform reduction steps until either a pause is requested, a minimal term is reached or the maximum number of reduction steps has been performed.
     */
    private boolean paused;
    /**
     * Indicates whether a pause of the automatic reduction has been requested.
     */
    private boolean pauseRequested;
    /**
     * The reduction strategy used for beta reductions.
     */
    private BetaReductionVisitor strategy;
    /**
     * Stores the current lambda term.
     */
    private LambdaRoot current;
    /**
     * Indicates whether the model is busy, i.e. whether a step is being performed at the moment.
     */
    private boolean busy;
    /**
     * Contains all data of the current level.
     */
    private LevelContext context;
    
    /**
     * Creates a new instance of ReductionModel.
     */
    public ReductionModel() {
        current = null;
        strategy = null;
        context = null;
        history = new Stack<>();
        paused = true;
        pauseRequested = false;
        busy = false;
    }
    
    /**
     * Resets the model with the given values.
     * 
     * @param term the term to be reduced
     * @param strategy the reduction strategy
     * @param context the current level context
     * @throws IllegalArgumentException if term is null, strategy is null or context is null
     * @throws IllegalStateException if the model is currently busy or not paused
     */
    public void reset(LambdaRoot term, BetaReductionVisitor strategy, LevelContext context) {
        if (term == null) {
            throw new IllegalArgumentException("Lambda term cannot be null!");
        }
        if (strategy == null) {
            throw new IllegalArgumentException("Strategy cannot be null!");
        }
        if (context == null) {
            throw new IllegalArgumentException("Level context cannot be null!");
        }
        if (!paused || busy || pauseRequested) {
            throw new IllegalStateException("Cannot start automatic reduction in the current model state!");
        }
        current = term;
        this.strategy = strategy;
        this.context = context;
        history.clear();
        paused = true;
        pauseRequested = false;
        busy = false;
    }
    
    /**
     * Starts the automatic reduction.
     * 
     * @throws IllegalStateException if the automatic reduction isn't paused, a step is currently being performed or a pause is requested
     */
    public void play() {
        if (!paused || busy || pauseRequested) {
            throw new IllegalStateException("Cannot start automatic reduction in the current model state!");
        }
        setPaused(false);
        step();
    }
    
    /**
     * Performs steps in a separate thread until a pause is requested, automatic reduction is paused or a minimal term is reached. Performs at least one reduction step.
     * 
     * @throws IllegalStateException if the model is busy or a pause is requested
     */
    public void step() {
        if (busy || pauseRequested) {
            throw new IllegalStateException("Cannot perform a reduction step in the current model state!");
        }
        setBusy(true);
        
        // Start performing steps in a separate thread
        new Thread() {
            @Override
            public void run() {
                do {
                    // Save current term in history
                    history.push((LambdaRoot) current.accept(new CopyVisitor()));
                    
                    // Perform beta reduction
                    strategy.reset();
                    current.accept(strategy);
                } while (!paused && !pauseRequested && strategy.hasReduced());
                setBusy(false);
                
                // Minimal term reached
                if (!strategy.hasReduced()) {
                    ReductionModel.this.notify((observer) -> observer.reductionFinished(
                            current.accept(new IsAlphaEquivalentVisitor(ReductionModel.this.context.getLevelModel().getGoal()))));
                }
                
                // Steps finished
                pauseRequested = false;
                setPaused(true);
            }
        }.start();
    }
    
    /**
     * Reverts the last step in a separate thread.
     * 
     * @throws IllegalStateException if the automatic reduction isn't paused, a step is currently being performed, a pause is requested or the history is empty
     */
    public void stepRevert() {
        if (!paused || busy || pauseRequested || history.isEmpty()) {
            throw new IllegalStateException("Cannot revert the last reduction step in the current model state!");
        }
        setBusy(true);
        
        // Reverts the last step in a separate thread
        new Thread() {
            @Override
            public void run() {
                current.setChild(history.pop().getChild());
                setBusy(false);
            }
        }.start();
        
        // TODO wait till animation is finished?
    }
    
    /**
     * Sets whether the automatic reduction is paused and informs all observers of the change.
     * 
     * @param paused true if the automatic reduction is paused, false otherwise
     */
    private void setPaused(boolean paused) {
        if (paused != this.paused) {
            this.paused = paused;
            notify((observer) -> observer.pauseChanged(paused));
        }
    }
    
    /**
     * Sets whether the reduction model is currently performing a step and informs all observers of the change.
     * 
     * @param busy true if the reduction model is currently performing a step, false otherwise
     */
    private void setBusy(boolean busy) {
        if (busy != this.busy) {
            this.busy = busy;
            notify((observer) -> observer.busyChanged(busy));
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
}
