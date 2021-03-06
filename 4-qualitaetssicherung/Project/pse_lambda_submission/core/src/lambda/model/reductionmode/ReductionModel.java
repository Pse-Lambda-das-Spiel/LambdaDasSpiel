package lambda.model.reductionmode;

import com.badlogic.gdx.graphics.Color;

import java.util.List;
import java.util.Stack;

import lambda.Consumer;
import lambda.Observable;
import lambda.model.lambdaterm.LambdaRoot;
import lambda.model.lambdaterm.LambdaTerm;
import lambda.model.lambdaterm.visitor.CopyVisitor;
import lambda.model.lambdaterm.visitor.IsAlphaEquivalentVisitor;
import lambda.model.lambdaterm.visitor.strategy.BetaReductionVisitor;
import lambda.model.lambdaterm.visitor.strategy.NodeCounter;
import lambda.model.levels.LevelContext;
import lambda.model.levels.LevelManager;
import lambda.model.profiles.ProfileManager;

/**
 * Contains data and logics of the reduction mode. Will be observed by the
 * reduction view controller.
 *
 * @author Florian Fervers
 */
public class ReductionModel extends Observable<ReductionModelObserver> {
    /**
     * Saves all previous lambda terms to be able to undo reduction steps.
     */
    private final Stack<LambdaRoot> history;
    /**
     * Indicates whether the automatic reduction is paused. When paused is true
     * the model will automatically perform reduction steps until either a pause
     * is requested, a minimal term is reached or the maximum number of
     * reduction steps has been performed.
     */
    private volatile boolean paused;
    /**
     * Indicates whether a pause of the automatic reduction has been requested.
     */
    private volatile boolean pauseRequested;
    /**
     * The reduction strategy used for beta reductions.
     */
    private BetaReductionVisitor strategy;
    /**
     * Stores the current lambda term.
     */
    private LambdaRoot current;
    /**
     * Indicates whether the model is busy, i.e. whether a step is being
     * performed at the moment.
     */
    private volatile boolean busy;
    /**
     * Contains all data of the current level.
     */
    private LevelContext context;
    /**
     * The goal of the level, used for comparison with the current lambda term after a complete beta reduction.
     */
    private LambdaRoot goal;
    /**
     * A volatile attribute for the reduction thread.
     */
    private volatile boolean running;

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
        running = true;
    }

    /**
     * The lambda term that is being reduced.
     *
     * @return the lambda term that is being reduced
     */
    public LambdaRoot getTerm() {
        return current;
    }

    /**
     * Resets the model with the given values.
     *
     * @param term the term to be reduced
     * @param strategy the reduction strategy
     * @param context the current level context
     * @throws IllegalArgumentException if term is null, strategy is null or
     * context is null
     * @throws IllegalStateException if the model is currently busy or not
     * paused
     */
    public void reset(LambdaRoot term, BetaReductionVisitor strategy,
            LevelContext context) {
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
            throw new IllegalStateException(
                    "Cannot start automatic reduction in the current model state!");
        }
        if (context.getLevelModel().isStandardMode()) {
            current = term;
            goal = (LambdaRoot)context.getLevelModel().getGoal().accept(new CopyVisitor());
        } else {
            current = (LambdaRoot) context.getLevelModel().getStart().accept(new CopyVisitor());
            goal = term;
        }
        notify(new Consumer<ReductionModelObserver>() {
            @Override
            public void accept(ReductionModelObserver observer) {
                observer.termChanged(current);
            }
        });
        this.strategy = strategy;
        this.context = context;
        history.clear();
        paused = true;
        pauseRequested = false;
        busy = false;
        running = true;

        List<Color> alphaConversionColors = LevelManager.getAllColors();
        alphaConversionColors.removeAll(context.getLevelModel()
                .getAvailableColors());
        alphaConversionColors.removeAll(context.getLevelModel()
                .getLockedColors());

        // white should not be among the replacement colors for an alpha
        // conversion
        alphaConversionColors.remove(Color.valueOf("ffffffff"));
        // this order is needed for the tutorial levels for the alpha conversion
        alphaConversionColors.remove(LevelManager.convertVariableToColor('m'));
        alphaConversionColors.remove(LevelManager.convertVariableToColor('k'));
        alphaConversionColors.add(0, LevelManager.convertVariableToColor('m'));
        alphaConversionColors.add(1, LevelManager.convertVariableToColor('k'));
        this.strategy.setAlphaConversionColors(alphaConversionColors);
    }

    /**
     * Returns the number of terms that are stored in the history.
     *
     * @return the number of terms that are stored in the history
     */
    public int getHistorySize() {
        return history.size();
    }

    /**
     * Toggles the automatic reduction.
     *
     * @throws IllegalStateException if a step is currently being performed or a
     * pause is requested
     */
    public void togglePlay() {
        if (busy && paused || pauseRequested) {
            throw new IllegalStateException(
                    "Cannot start automatic reduction in the current model state!");
        }

        if (paused) {
            // Play
            paused = false;
            step();
        } else {
            // Pause
            pauseRequested = true;
            notifyState();
        }
    }

    /**
     * Performs steps in a separate thread until a pause is requested, automatic
     * reduction is paused or a minimal term is reached. Performs at least one
     * reduction step.
     *
     * @throws IllegalStateException if the model is busy or a pause is
     * requested
     */
    public void step() {
        if (busy || pauseRequested) {
            throw new IllegalStateException(
                    "Cannot perform a reduction step in the current model state!");
        }
        busy = true;
        notifyState();

        // Start performing steps in a separate thread
        new Thread() {
            @Override
            public void run() {
                int nodeCount;
                do {
                    // Save current term in history
                    history.push((LambdaRoot) current.accept(new CopyVisitor()));
                    notifyState();

                    // Check limits
                    nodeCount = current.accept(new NodeCounter());

                    // Perform beta reduction
                    if (nodeCount <= LambdaTerm.MAX_NODES_PER_TERM) {
                        strategy.reset();
                        current.accept(strategy);
                    }
                } while (running && !paused && !pauseRequested && strategy.hasReduced()
                        && nodeCount <= LambdaTerm.MAX_NODES_PER_TERM
                        && history.size() <= context.getLevelModel().getMaxReductionSteps());
                if (running) {
                    busy = false;
                    notifyState();
                }
     
                if (running) {
                    // Minimal term reached
                    if (!strategy.hasReduced()) {
                        ReductionModel.this.notify(new Consumer<ReductionModelObserver>() {
                            @Override
                            public void accept(ReductionModelObserver observer) {
                                if (context.getLevelModel().getId() == 0) {
                                    observer.reductionFinished(true);
                                } else {
                                    observer.reductionFinished((current.equals(goal) && ReductionModel.this.context
                                            .getLevelModel().isColorEquivalence())
                                            || (current.accept(new IsAlphaEquivalentVisitor(goal)) && !ReductionModel.this.context
                                                    .getLevelModel().isColorEquivalence()));
                                }
                            }
                        });
                        // save progress in the level
                        ProfileManager pManager = ProfileManager.getManager();
                        pManager.save(pManager.getCurrentProfile().getName());
                    } else if (nodeCount > LambdaTerm.MAX_NODES_PER_TERM) {
                        ReductionModel.this.notify(new Consumer<ReductionModelObserver>() {
                            @Override
                            public void accept(ReductionModelObserver observer) {
                                observer.maxNodesReached();
                            }
                        });
                        // save progress in the level
                        ProfileManager pManager = ProfileManager.getManager();
                        pManager.save(pManager.getCurrentProfile().getName());
                    } else if (history.size() > context.getLevelModel().getMaxReductionSteps()) {
                        ReductionModel.this.notify(new Consumer<ReductionModelObserver>() {
                            @Override
                            public void accept(ReductionModelObserver observer) {
                                observer.maxStepsReached();
                            }
                        });
                        // save progress in the level
                        ProfileManager pManager = ProfileManager.getManager();
                        pManager.save(pManager.getCurrentProfile().getName());
                    }

                    // Steps finished
                    pauseRequested = false;
                    paused = true;
                    notifyState();
                }
            }
        }.start();
    }

    /**
     * Reverts the last step in a separate thread.
     *
     * @throws IllegalStateException if the automatic reduction isn't paused, a
     * step is currently being performed, a pause is requested or the history is
     * empty
     */
    public void stepRevert() {
        if (!paused || busy || pauseRequested || history.isEmpty()) {
            throw new IllegalStateException(
                    "Cannot revert the last reduction step in the current model state!");
        }

        // Reverts the last step in a separate thread
        busy = true;
        notifyState();
        current.setChild(history.pop().getChild());
        busy = false;
        notifyState();
    }

    /**
     * Returns the level context.
     *
     * @return the level context
     */
    public LevelContext getContext() {
        return context;
    }

    /**
     * Resets the state of this model to its start state.
     */
    public void resetState() {
        running = false;
        paused = true;
        pauseRequested = false;
        busy = false;
        notifyState();
    }
    
    /**
     * Notifies all observers that the played level is left.
     * 
     * @param canSave
     *            true if the level data can be saved, false otherwise
     */
    public void leaveLevel(final boolean canSave) {
        notify(new Consumer<ReductionModelObserver>() {
            @Override
            public void accept(ReductionModelObserver observer) {
                observer.levelLeft(canSave);
            }
        });
    }
    
    /**
     * Notifies all observers of a state change.
     */
    private void notifyState() {
        notify(new Consumer<ReductionModelObserver>() {
            @Override
            public void accept(ReductionModelObserver observer) {
                observer.stateChanged(busy, history.size(),
                        ReductionModel.this.paused, pauseRequested);
            }
        });
    }
}
