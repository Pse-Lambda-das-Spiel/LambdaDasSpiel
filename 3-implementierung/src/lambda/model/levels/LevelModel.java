package lambda.model.levels;

import java.util.List;
import lambda.model.lambdaterm.LambdaRoot;

/**
 * Represents a level
 *
 * @author Kay Schmitteckert
 */
public class LevelModel {

    private int ID;
    private int difficulty;
    private boolean standardMode;
    private LambdaRoot start;
    private LambdaRoot goal;
    private LambdaRoot hint;
    private List<TutorialMessage> tutorial;
    private List<ReductionStrategy> availableRedStrats;
    private List<ElementType> useableElements;

    /**
     * WIRD NOCH BEARBEITET, WENN DAS MIT DEN JSON GEKLÄRT IST.
     */
    public LevelModel() {

    }

    /**
     * Returns the ID of the level
     *
     * @return ID
     */
    public int getID() {
        return ID;
    }

    /**
     * Returns the difficulty of the level
     *
     * @return difficulty
     */
    public int getDifficulty() {
        return difficulty;
    }

    /**
     *  Returns if the level has to be solved in standardmode or reverse
     *
     * @return standardMode
     */
    public boolean isStandardMode() {
        return standardMode;
    }

    /**
     * Returns the lambda-set-up at the beginning of the level
     *
     * @return start
     */
    public LambdaRoot getStart() {
        return start;
    }

    /**
     * Returns the configuration which should be achieved at the end of the level
     *
     * @return goal
     */
    public LambdaRoot getGoal() {
        return goal;
    }

    /**
     * Returns a hint which can be used to solce the level
     *
     * @return hint
     */
    public LambdaRoot getHint() {
        return hint;
    }

    /**
     * Returns a list of tutorials which relate to the level
     *
     * @return tutorial
     */
    public List<TutorialMessage> getTutorial() {
        return tutorial;
    }

    /**
     * Returns a list of reduction-strategies which are allowed to solve the level
     *
     * @return availabeRedStrats
     */
    public List<ReductionStrategy> getAvailableRedStrats() {
        return availableRedStrats;
    }

    /**
     * Returns a list of elements which are allowed to create or modifiy the lambda-term
     *
     * @return useableElements
     */
    public List<ElementType> getUseableElements() {
        return useableElements;
    }


}
