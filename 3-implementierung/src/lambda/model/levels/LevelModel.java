package lambda.model.levels;

import java.util.List;
import lambda.model.lambdaterm.LambdaRoot;
import lambda.viewcontroller.level.TutorialMessage;

/**
 * Represents a level
 *
 * @author Kay Schmitteckert
 */
public class LevelModel {

    private int id;
    private int difficulty;
    private int coins;
    private boolean standardMode;
    private LambdaRoot start;
    private LambdaRoot goal;
    private LambdaRoot hint;
    private List<TutorialMessage> tutorial;
    private List<ReductionStrategy> availableRedStrats;
    private List<ElementType> useableElements;

    /**
     * Creates a new instance of this class and initialize it with the given data.s
     *
     * @param id                 The Id of this level
     * @param start              the start board constellation of this level
     * @param goal               the goal board constellation of this level
     * @param hint               the hint to solve this level
     * @param tutorial           the list of all associated Tutorial messages
     * @param availableRedStrats the list of all available reduction strategies for this level
     * @param useableElements    the usuable elements of this level
     * @param difficulty         the level difficulty
     * @param coins              the coins you will get if you complete the level
     * @param standardMode       indicates whether the level has to be solved in standard mode or reversed
     */
    public LevelModel(int id, LambdaRoot start,
                      LambdaRoot goal, LambdaRoot hint,
                      List<TutorialMessage> tutorial, List<ReductionStrategy> availableRedStrats,
                      List<ElementType> useableElements, int difficulty, int coins, boolean standardMode) {
        this.id = id;
        this.start = start;
        this.goal = goal;
        this.hint = hint;
        this.tutorial = tutorial;
        this.availableRedStrats = availableRedStrats;
        this.useableElements = useableElements;
        this.difficulty = difficulty;
        this.standardMode = standardMode;
    }

    /**
     * /**
     * Returns the Id of the level
     *
     * @return id
     */
    public int getId() {
        return id;
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
     * Returns if the level has to be solved in standardmode or reverse
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

    /**
     * Returns the quantity of numbers you will get if you complete the level
     *
     * @return coins
     */
    public int getCoins() {
        return coins;
    }


    /**
     * TODOOOOO
     * @return
     */
    public ReductionStrategy getDefaultStrategy() {
        return null;
    }
}