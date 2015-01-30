package lambda.viewcontroller.level;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * This class represents a full tutorial including the area and vector
 *
 * @author Kay Schmitteckert
 */
public class TutorialMessage {

	String id;
	String message;
	Rectangle bound;
	Vector2 arrowStart;
	Vector2 arrowEnd;

	/**
	 * Creates a new TutorialMessage.
	 *
	 * @param id the id of the new TutorialMessage
	 */
	public TutorialMessage(String id, String message, Rectangle bound, Vector2 arrowStart, Vector2 arrowEnd) {
		this.id = id;
		this.message = message;
		this.bound = bound;
		this.arrowStart = arrowStart;
		this.arrowEnd = arrowEnd;
	}

	/**
	 * Returns the id of this TutorialMessage.
	 *
	 * @return the id the id of this TutorialMessage
	 */
	public String getId() {
		return id;
	}

	/**
	 * Returns the message of the tutorial
	 *
	 * @return the message of the tutorial
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Returns a Rectangle which represents the area in which the message/tutorial will be displayed
	 *
	 * @return Rectangle in which the tutorial will be displayed
	 */
	public Rectangle getBound() {
		return bound;
	}

	/**
	 * Returns the start of the vector
	 *
	 * @return the start of the vector
	 */
	public Vector2 getArrowStart() {
		return arrowStart;
	}

	/**
	 * Returns the end of the vector
	 *
	 * @return the end of the vector
	 */
	public Vector2 getArrowEnd() {
		return arrowEnd;
	}
}
