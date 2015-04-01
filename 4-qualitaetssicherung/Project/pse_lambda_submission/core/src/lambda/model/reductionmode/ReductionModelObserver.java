package lambda.model.reductionmode;

import lambda.model.lambdaterm.LambdaTerm;

/**
 * Interface that provides all messages that can be sent from the reduction
 * model to its observers.
 *
 * @author Florian Fervers
 */
public interface ReductionModelObserver {
    /**
     * Called when the model is reset with a new lamdba term.
     *
     * @param term
     *            the new lambda term.
     */
    public void termChanged(LambdaTerm term);

    /**
     * Called when the model state changes.
     *
     * @param busy
     *            indicates whether the model is currently performing a
     *            reduction step
     * @param historySize
     *            the history size (>= 0)
     * @param paused
     *            indicates whether the automatic reduction is currently paused
     * @param pauseRequested
     *            indicates whether a pause of the automatic reduction is
     *            requested
     */
    public void stateChanged(boolean busy, int historySize, boolean paused,
            boolean pauseRequested);

    /**
     * Called when the reduction reached a minimal term.
     *
     * @param levelComplete
     *            true if the final term is alpha equivalent to the level's
     *            target term, false otherwise
     */
    public void reductionFinished(boolean levelComplete);
    
    /**
     * Called when the reduction reached the maximum number of nodes per term.
     */
    public void maxNodesReached();
    
    /**
     * Called when the reduction reached the maximum number of reduction steps.
     */
    public void maxStepsReached();
    
    /**
     * Called when the level is left.
     * 
     * @param canSave true if the level data can be saved, false otherwise
     */
    public void levelLeft(boolean canSave);
}
