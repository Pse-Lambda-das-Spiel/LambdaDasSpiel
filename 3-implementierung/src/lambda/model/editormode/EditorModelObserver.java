package lambda.model.editormode;

import lambda.model.levels.ReductionStrategy;

/**
 * Interface that provides all messages that can be sent from the editor model to its observers.
 * 
 * @author Florian Fervers
 */
public interface EditorModelObserver {
    /**
     * Called when the a new reduction strategy is selected.
     * 
     * @param strategy the new strategy
     */
    public void strategyChanged(ReductionStrategy strategy);
}
