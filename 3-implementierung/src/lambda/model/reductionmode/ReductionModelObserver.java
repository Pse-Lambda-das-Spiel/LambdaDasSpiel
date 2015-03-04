package lambda.model.reductionmode;

/**
 * Interface that provides all messages that can be sent from the reduction model to its observers.
 * 
 * @author Florian Fervers
 */
public interface ReductionModelObserver {
    /**
     * Called when the paused state is changed. As long as the reduction model is not paused it will automatically perform reductions until reaching a minimal term.
     * 
     * @param paused the new paused state
     */
    public void pauseChanged(boolean paused);
    
    /**
     * Called when the busy state is changed. The reduction model is busy as long as a reduction step is being performed.
     * 
     * @param busy the new busy state
     */
    public void busyChanged(boolean busy);
    
    /**
     * Called when the reduction reached a minimal term or the maximum number of reduction steps.
     * 
     * @param levelComplete true if the final term is alpha equivalent to the level's target term, false otherwise
     */
    public void reductionFinished(boolean levelComplete);
    
    /**
     * Called when the history stack size has changed.
     * 
     * @param newSize the new size of the histoy stack
     */
    public void historySizeChanged(int newSize);
}
