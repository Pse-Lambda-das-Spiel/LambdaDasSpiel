package lambda.model.editormode;

import lambda.model.lambdaterm.LambdaTerm;
import lambda.model.levels.ReductionStrategy;

/**
 * Interface that provides all messages that can be sent from the editor model
 * to its observers.
 *
 * @author Florian Fervers
 */
public interface EditorModelObserver {
    /**
     * Called when the model is reset with a new lamdba term.
     *
     * @param term
     *            the new lambda term.
     */
    public void termChanged(LambdaTerm term);

    /**
     * Called when the a new reduction strategy is selected.
     *
     * @param strategy
     *            the new strategy
     */
    public void strategyChanged(ReductionStrategy strategy);

    /**
     * Called when the level is started.
     * 
     * @param levelId
     *            the id of the started level
     */
    public void levelStarted(int levelId);

    /**
     * Called when the hint of the level is used.
     */
    public void hintUsed();
    
    /**
     * Called when the level is left.
     * 
     * @param canSave true if the level data can be saved, false otherwise
     */
    public void levelLeft(boolean canSave);
}
