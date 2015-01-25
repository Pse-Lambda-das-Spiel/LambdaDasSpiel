package lambda.viewcontroller.level;

/**
 * Created by kay on 19.01.15.
 */
public class TutorialMessage {
	
	String id;
	
	/**
	 * Returns the id of this TutorialMessage.
	 * 
	 * @return the id the id of this TutorialMessage
	 */
	public String getId() {
		return id;
	}

	/**
	 * Creates a new TutorialMessage.
	 * 
	 * @param id the id of the new TutorialMessage
	 */
	public TutorialMessage(String id) {
		this.id = id;
	}
}
