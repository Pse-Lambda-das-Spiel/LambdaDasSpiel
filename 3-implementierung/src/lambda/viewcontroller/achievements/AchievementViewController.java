package lambda.viewcontroller.achievements;

import lambda.LambdaGame;
import lambda.model.achievements.AchievementModel;
import lambda.model.achievements.AchievementModelObserver;

import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;

/**
 * This class is responsible for displaying a single achievement.
 * The class is a stack which contains all possible image buttons for an achievement:
 * the on for the locked and the one for the unlocked state.
 * It is also responsible for updating the visibility of an image button according to the state of the achievement.
 * 
 * @author Robert Hochweiss
 */
public class AchievementViewController extends Stack implements AchievementModelObserver {

	private AchievementModel achievement;
	private ImageButton achievementLockedButton;
	private ImageButton achievementUnlockedButton;
	
	/**
	 * Creates a new AchievementViewController with the given {@link AchievementModel}.
	 * 
	 * @param achievement the to be displayed {@link AchievementModel}
	 */
	public AchievementViewController(AchievementModel achievement) {
		this.achievement = achievement;
		achievement.addObserver(this);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void changedLockedState() {
		achievementLockedButton.setVisible(achievement.isLocked());
		achievementUnlockedButton.setVisible(!(achievement.isLocked()));
	}
	
	/**
	 * Fills the stack with the image buttons and set their initial state.
	 */
	public void fillStack() {
		achievementLockedButton = new ImageButton(AchievementMenuViewController.
				getImageButtonStyle(achievement.getIconPathAchievementLocked()));
		achievementUnlockedButton = new ImageButton(AchievementMenuViewController.
				getImageButtonStyle(achievement.getIconPathAchievementUnlocked()));
		add(achievementLockedButton);
		add(achievementUnlockedButton);
		achievementLockedButton.setVisible(true);
		achievementUnlockedButton.setVisible(false);
	}
	
	/**
	 * Returns the currently shown image button.
	 * 
	 * @return the currently shown image button.
	 */
	public ImageButton getShownButton() {
		if (achievementLockedButton.isVisible()) {
			return achievementLockedButton;
		}
		return achievementUnlockedButton;
	}
	
	/**
	 * Returns the currently shown text.
	 * Returns the description of the achievement if achievement is unlocked
	 * and the requirements if achievement is locked.
	 * 
	 * @return the currently shown text.
	 */
	public String getShownText() {
		if (achievementLockedButton.isVisible()) {
			return achievement.getRequirementsDescription();
		}
		return achievement.getDescription();
	}
}
