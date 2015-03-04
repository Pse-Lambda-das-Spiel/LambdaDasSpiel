package lambda.model.levels;

import java.util.List;

import com.badlogic.gdx.graphics.Color;

import lambda.model.lambdaterm.LambdaRoot;
import lambda.viewcontroller.level.TutorialMessage;

/**
 * Represents a level.
 *
 * @author Kay Schmitteckert
 */
public class LevelModel {

    private int id;
    private int difficulty;
    private int coins;
    private boolean standardMode;


	private boolean colorEquivalence; 
    private List<Color> availableColors;
    private LambdaRoot start;
    private LambdaRoot goal;
    private LambdaRoot hint;
    private List<TutorialMessageModel> tutorial;
    private List<ReductionStrategy> availableRedStrats;
    private List<ElementType> useableElements;
    private ReductionStrategy defaultStrategy;
    

    /**
     * Creates a new instance of this class and initialize it with the given data.
     *
     * @param id                 The Id of this level
     * @param start              the start board constellation of this level
     * @param goal               the goal board constellation of this level
     * @param hint               the hint to solve this level
     * @param tutorial           the list of all associated Tutorial messages models
     * @param availableRedStrats the list of all available reduction strategies for this level
     * @param useableElements    the usuable elements of this level
     * @param difficulty         the level difficulty
     * @param coins              the coins you will get if you complete the level
     * @param standardMode       indicates whether the level has to be solved in standard mode or reversed
     * @param colorEquivalence	 indicates whether color equivalence or alpha equivalenz is checked
     * @param availableColors 	 the available colors for the coloring of this level
     * @param defaultStrategy 	 the default reduction strategy of this level
     */
    public LevelModel(int id, LambdaRoot start,
                      LambdaRoot goal, LambdaRoot hint,
                      List<TutorialMessageModel> tutorial, List<ReductionStrategy> availableRedStrats,
                      List<ElementType> useableElements, int difficulty, int coins, boolean standardMode, 
                      boolean colorEquivalence, List<Color> availableColors, ReductionStrategy defaultStrategy) {
        this.id = id;
        this.start = start;
        this.goal = goal;
        this.hint = hint;
        this.tutorial = tutorial;
        this.availableRedStrats = availableRedStrats;
        this.useableElements = useableElements;
        this.difficulty = difficulty;
        this.standardMode = standardMode;
        this.colorEquivalence =colorEquivalence;
        this.availableColors = availableColors;
        this.defaultStrategy = defaultStrategy; 
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
    public List<TutorialMessageModel> getTutorial() {
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
      *  Returns the default strategy for the level.
      *  
      * @return the default strategy
      */
    public ReductionStrategy getDefaultStrategy() {
        return defaultStrategy;
    }
    
    /**
     * Returns whether color equivalence is set or not.
     * Color equivalence determines whether the colors of the lambda terms should be considered or not 
     * when comparing the user input to the goal of the level.
     * If color equivalence is false, alpha equivalence is automatically set.
     * 
	 * @return the colorEquivalence
	 */
	public boolean isColorEquivalence() {
		return colorEquivalence;
	}

	/**
	 * Returns all colors available for the coloring dialog of this level.
	 * 
	 * @return the lockedColors
	 */
	public List<Color> getAvailableColors() {
		return availableColors;
	}

}