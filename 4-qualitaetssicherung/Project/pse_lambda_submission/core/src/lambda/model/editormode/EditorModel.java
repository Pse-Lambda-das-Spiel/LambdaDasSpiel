package lambda.model.editormode;

import lambda.Consumer;
import lambda.Observable;
import lambda.model.lambdaterm.LambdaRoot;
import lambda.model.lambdaterm.visitor.CopyVisitor;
import lambda.model.lambdaterm.visitor.strategy.BetaReductionVisitor;
import lambda.model.levels.LevelContext;
import lambda.model.levels.ReductionStrategy;
import lambda.model.reductionmode.ReductionModel;

/**
 * Contains data and logics of the editor mode. Will be observed by the editor
 * view controller.
 *
 * @author Florian Fervers
 */
public class EditorModel extends Observable<EditorModelObserver> {
    /**
     * Contains all data of the current level.
     */
    private LevelContext context;
    /**
     * Stores the currently selected reduction strategy.
     */
    private ReductionStrategy strategy;
    /**
     * Stores the current lambda term
     */
    private LambdaRoot term;

    /**
     * Creates a new instance of EditorModel.
     */
    public EditorModel() {
        context = null;
        strategy = null;
        term = null;
    }

    /**
     * Resets the model with the given values.
     *
     * @param context contains all data of the current level
     * @throws IllegalArgumentException if context is null
     */
    public void reset(LevelContext context) {
        if (context == null) {
            throw new IllegalArgumentException("Level context cannot be null!");
        }
        this.context = context;
        strategy = context.getLevelModel().getDefaultStrategy();
        if (context.getLevelModel().isStandardMode()) {
            term = (LambdaRoot) context.getLevelModel().getStart().accept(new CopyVisitor());
        } else {
            term = new LambdaRoot();
        }
        notify(new Consumer<EditorModelObserver>() {
            @Override
            public void accept(EditorModelObserver observer) {
                observer.termChanged(term);
            }
        });
    }

    /**
     * Returns a new visitor for the current reduction strategy.
     *
     * @return a new visitor for the current reduction strategy
     */
    public BetaReductionVisitor getReductionStrategy() {
        return strategy.toVisitor();
    }

    /**
     * Sets the currently selected reduction strategy.
     *
     * @param strategy the new selected reduction strategy
     * @throws IllegalArgumentException if strategy is null
     */
    public void setStrategy(final ReductionStrategy strategy) {
        if (strategy == null) {
            throw new IllegalArgumentException(
                    "Reduction strategy cannot be null!");
        }
        if (strategy != this.strategy) {
            this.strategy = strategy;
            notify(new Consumer<EditorModelObserver>() {
                @Override
                public void accept(EditorModelObserver observer) {
                    observer.strategyChanged(strategy);
                }
            });
        }
    }

    /**
     * Returns the current term.
     *
     * @return the current term
     */
    public LambdaRoot getTerm() {
        return term;
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
     * Resets the reduction model with the current state of this editor model.
     *
     * @param model the reduction model to be reset
     */
    public void update(ReductionModel model) {
        model.reset((LambdaRoot) term.accept(new CopyVisitor()),
                BetaReductionVisitor.fromReductionStrategy(strategy), context);
    }

    /**
     * Called when the hint was used in the observing EditorViewController.
     */
    public void hintIsUsed() {
        notify(new Consumer<EditorModelObserver>() {
            @Override
            public void accept(EditorModelObserver observer) {
                observer.hintUsed();
            }
        });
    }

    /**
     * Called when the level is started and the user can make input events.
     */
    public void levelIsStarted() {
        notify(new Consumer<EditorModelObserver>() {
            @Override
            public void accept(EditorModelObserver observer) {
                observer.levelStarted();
            }
        });
    }

    /**
     * Returns the reduction strategy
     *
     * @return the strategy
     */
    public ReductionStrategy getStrategy() {
        return strategy;
    }
}
