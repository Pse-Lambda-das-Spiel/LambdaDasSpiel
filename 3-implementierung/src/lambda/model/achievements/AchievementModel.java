package lambda.model.achievements;

import com.badlogic.gdx.assets.AssetManager;

import lambda.Observable;
import lambda.Consumer;
import lambda.model.statistics.StatisticModel;
import lambda.model.statistics.StatisticModelObserver;

/**
 * This class represents an achievement.
 * 
 * @author Robert Hochweiss
 */
public abstract class AchievementModel extends Observable<AchievementModelObserver> implements StatisticModelObserver {
	
	private String id;
	private int index;
	private String description;
	private String requirementsDescription;
	private String iconPathAchievementUnlocked;
	private String iconPathAchievementLocked;
	private boolean locked;

	/**
	 * Creates a new instance of this class.
	 */
	public AchievementModel() {
		id = "";
		index = 0;
		description = "";
		requirementsDescription = "";
		iconPathAchievementUnlocked = "";
		iconPathAchievementLocked = "";
		locked = true;
	}
	
	/**
	 * Initialize the achievement.
	 * 
	 */
	public abstract void initialize();
	
	/**
	 * Resets the description and requirement description of the achievement.
	 * 
	 * @param assets the AssetManager needed for loading the resources needed for resetting the achievement.
	 * @throws IllegalArgumentException if assets is null
	 */
	public abstract void reset(AssetManager assets);
	
	/**
	 * Checks with the given StatisticModel whether the requirements of this achievement are met or not.
	 * Sets the locked-/unlocked-state of this achievement accordingly.
	 * 
	 * @param statistic the StatisticModel with whitch the check of the requirements is done
	 * @throws IllegalArgumentException if statistic is null
	 */
	public abstract void checkRequirements(StatisticModel statistic);
	
	/**
	 * Returns the id of the achievement.
	 * 
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Returns the index of the achievement.
	 * 
	 * @return the index
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * Returns the description of the unlocked achievement.
	 * 
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Returns the description for the requirements to unlock the achievement.
	 * 
	 * @return the requirementsDescription
	 */
	public String getRequirementsDescription() {
		return requirementsDescription;
	}

	/**
	 * Returns the path to the icon for the unlocked achievement.
	 * 
	 * @return the iconPathAchievementUnlocked
	 */
	public String getIconPathAchievementUnlocked() {
		return iconPathAchievementUnlocked;
	}

	/**
	 * Returns the path to the icon for the locked achievement.
	 * 
	 * @return the iconPathAchievementLocked
	 */
	public String getIconPathAchievementLocked() {
		return iconPathAchievementLocked;
	}
	
	/**
	 * Returns whether the achievement is locked or unlocked.
	 * 
	 * @return the locked
	 */
	public boolean isLocked() {
		return locked;
	}

	/**
	 * Sets the id of the achievement.
	 * 
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Sets the index of the achievement
	 * 
	 * @param index the index to set
	 */
	public void setIndex(int index) {
		this.index = index;
	}

	/**
	 * Sets the description of the unlocked achievement
	 * 
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Sets the description for the requirements to unlock the achievement.
	 * 
	 * @param requirementsDescription the requirementsDescription to set
	 */
	public void setRequirementsDescription(String requirementsDescription) {
		this.requirementsDescription = requirementsDescription;
	}

	/**
	 * Sets the path to icon for the unlocked achievement.
	 * 
	 * @param iconPathAchievementUnlocked the iconPathAchievementUnlocked to set
	 */
	public void setIconPathAchievementUnlocked(String iconPathAchievementUnlocked) {
		this.iconPathAchievementUnlocked = iconPathAchievementUnlocked;
	}

	/**
	 * Sets the path to the icon for the locked achievement.
	 * 
	 * @param iconPathAchievementLocked the iconPathAchievementLocked to set
	 */
	public void setIconPathAchievementLocked(String iconPathAchievementLocked) {
		this.iconPathAchievementLocked = iconPathAchievementLocked;
	}
	
	/**
	 * Sets the achievement as locked or unlocked.
	 * 
	 * @param locked the locked to set
	 */
	public void setLocked(boolean locked) {
		this.locked = locked;
		notify(new Consumer<AchievementModelObserver>(){
             @Override
             public void accept(AchievementModelObserver observer) {
                 observer.changedLockedState();
             }
         });
	}

	@Override
	public void changedLambsEnchanted() {		
	}

	@Override
	public void changedGemsEnchanted() {		
	}

	@Override
	public void changedLambsPlaced() {		
	}

	@Override
	public void changedGemsPlaced() {		
	}

	@Override
	public void changedLambsEnchantedPerLevel() {		
	}

	@Override
	public void changedGemsEnchantedPerLevel() {		
	}

	@Override
	public void changedLambsPlacedPerLevel() {		
	}

	@Override
	public void changedGemsPlacedPerLevel() {		
	}

	@Override
	public void changedLevelCompleted() {		
	}

	@Override
	public void changedHintsNotUsed() {		
	}

	@Override
	public void changedTimePlayed() {		
	}
	
}
