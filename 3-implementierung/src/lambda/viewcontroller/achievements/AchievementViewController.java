package lambda.viewcontroller.achievements;

import lambda.model.achievements.AchievementModel;
import lambda.model.achievements.AchievementModelObserver;

import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;

/**
 * This class is responsible for displaying a single achievement.
 * 
 * @author Robert Hochweiss
 */
public class AchievementViewController extends ImageButton implements AchievementModelObserver {

	private AchievementModel achievement;
	
	/**
	 * Creates a new AchievementViewController with the given {@link AchievementModel}.
	 * 
	 * @param achievement the to be displayed {@link AchievementModel}
	 */
	public AchievementViewController(AchievementModel achievement) {
		// Since there is no default constructor in ImageButton
		super(null, null, null);
		this.achievement = achievement;
		achievement.addObserver(this);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void changedLockedState() {
		if (achievement.isLocked()) {
			setStyle(AchievementMenuViewController.getImageButtonStyle(achievement.getIconPathAchievementLocked()));
		} else {
			setStyle(AchievementMenuViewController.getImageButtonStyle(achievement.getIconPathAchievementUnlocked()));
		}
	}
	
	/**
	 * Initialize the image button.
	 */
	public void initializeButton() {
		setStyle(AchievementMenuViewController.getImageButtonStyle(achievement.getIconPathAchievementLocked()));
	}
	
	/**
	 * Returns the description of the achievement if the achievement is unlocked
	 * and the requirements if the achievement is locked.
	 * 
	 * @return the currently shown text.
	 */
	public String getText() {
		if (achievement.isLocked()) {
			return achievement.getRequirementsDescription();
		}
		return achievement.getDescription();
	}
}
