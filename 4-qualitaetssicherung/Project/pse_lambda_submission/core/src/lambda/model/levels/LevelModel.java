package lambda.model.levels;

import java.util.List;
import com.badlogic.gdx.graphics.Color;

import lambda.model.lambdaterm.LambdaRoot;

/**
 * Represents a level.
 *
 * @author Kay Schmitteckert
 */
public class LevelModel {

    private int id;
    private LambdaRoot start;
    private LambdaRoot goal;
    private LambdaRoot hint;
    private List<TutorialMessageModel> tutorial;
    private List<ReductionStrategy> availableRedStrats;
    private List<ElementType> useableElements;
    private int difficulty;
    private int coins;
    private int maxReductionSteps;
    private boolean standardMode;
    private boolean colorEquivalence;
    private List<Color> availableColors;
    private List<Color> lockedColors;
    private ReductionStrategy defaultStrategy;

    /**
     * Creates a new instance of this class and initialize it with the given
     * data.
     *
     * @param id
     *            The id of this level
     * @param start
     *            the start board constellation of this level
     * @param goal
     *            the goal board constellation of this level
     * @param hint
     *            the hint to solve this level
     * @param tutorial
     *            the list of all associated Tutorial messages models
     * @param availableRedStrats
     *            the list of all available reduction strategies for this level
     * @param useableElements
     *            the useable elements of this level
     * @param difficulty
     *            the level difficulty
     * @param coins
     *            the coins you will get if you complete the level
     * @param maxReductionSteps
     *            the maximum number of allowed reductions steps in this level
     * @param standardMode
     *            indicates whether the level has to be solved in standard mode
     *            or reversed
     * @param colorEquivalence
     *            indicates whether color equivalence or alpha equivalence is
     *            checked
     * @param availableColors
     *            the available colors for the coloring of this level(not
     *            available for alpha conversion)
     * @param lockedColors
     *            the locked colors of this level (neither available for
     *            coloring dialog nor alpha conversion)
     * @param defaultStrategy
     *            the default reduction strategy of this level
     */
    public LevelModel(int id, LambdaRoot start, LambdaRoot goal,
            LambdaRoot hint, List<TutorialMessageModel> tutorial,
            List<ReductionStrategy> availableRedStrats,
            List<ElementType> useableElements, int difficulty, int coins,
            int maxReductionSteps, boolean standardMode,
            boolean colorEquivalence, List<Color> availableColors,
            List<Color> lockedColors, ReductionStrategy defaultStrategy) {
        this.id = id;
        this.start = start;
        this.goal = goal;
        this.hint = hint;
        this.tutorial = tutorial;
        this.availableRedStrats = availableRedStrats;
        this.useableElements = useableElements;
        this.difficulty = difficulty;
        this.coins = coins;
        this.maxReductionSteps = maxReductionSteps;
        this.standardMode = standardMode;
        this.colorEquivalence = colorEquivalence;
        this.availableColors = availableColors;
        this.lockedColors = lockedColors;
        this.defaultStrategy = defaultStrategy;
    }

    /**
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
     * Returns if the level has to be solved in standard mode or reverse
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
     * Returns the configuration which should be achieved at the end of the
     * level
     *
     * @return goal
     */
    public LambdaRoot getGoal() {
        return goal;
    }

    /**
     * Returns a hint which can be used to solve the level
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
    public List<TutorialMessageModel> getTutorial() {
        return tutorial;
    }

    /**
     * Returns a list of reduction-strategies which are allowed to solve the
     * level
     *
     * @return availabeRedStrats
     */
    public List<ReductionStrategy> getAvailableRedStrats() {
        return availableRedStrats;
    }

    /**
     * Returns a list of elements which are allowed to create or modifiy the
     * lambda-term
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
     * Returns the maximum number of allowed reduction steps in this level. This
     * maximum number is the limit for the reduction steps of the
     * ReductionViewcontroller for this level. If this limit is exceeded the
     * whole reduction process will be aborted and the level try will be counted
     * as not solved.
     * 
     * @return the maximum number of allowed reduction steps in this level
     */
    public int getMaxReductionSteps() {
        return maxReductionSteps;
    }

    /**
     * Returns the default strategy for the level.
     * 
     * @return the defaultStrategy
     */
    public ReductionStrategy getDefaultStrategy() {
        return defaultStrategy;
    }

    /**
     * Returns whether color equivalence is set or not. Color equivalence
     * determines whether the colors of the lambda terms should be considered or
     * not when comparing the user input to the goal of the level. If color
     * equivalence is false, alpha equivalence is automatically set.
     * 
     * @return the colorEquivalence
     */
    public boolean isColorEquivalence() {
        return colorEquivalence;
    }

    /**
     * Returns all colors available for the coloring dialog of this level. These
     * colors are not available as new colors for an alpha conversion in this
     * level.
     * 
     * @return the availableColors of this level
     */
    public List<Color> getAvailableColors() {
        return availableColors;
    }

    /**
     * Returns the list of colors which are neither available for the coloring
     * dialog of this level nor as new colors for an alpha conversion in this
     * level.
     * 
     * @return the lockedColors
     */
    public List<Color> getLockedColors() {
        return lockedColors;
    }
}