package lambda.model.levels;

/**
 * This class represents the model of a {@link TutorialMessage} and holds all non graphical data.
 * 
 * @author Robert Hochweiss
 *
 */
public class TutorialMessageModel {

	private String id;
	private String imageName;
	
	/**
	 * Creates a new instance of this object.
	 * @param id the ID of the {@linkTutorialMessage}
	 * @param imageName the name of the shown image of this {@łink TutorialMessage}
	 */
	public TutorialMessageModel(String id, String imageName) {
		this.id = id;
		this.imageName = imageName;
	}
	
	/**
	 *  Sets the ID of this {@link TutorialMessage}
	 * 
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Returns the image name of this {@link TutorialMessage}
	 * 
	 * @return the imageName
	 */
	public String getImageName() {
		return imageName;
	}

	/**
	 *  Sets the ID of this {@link TutorialMessage}
	 * 
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Sets the image name of this {@link TutorialMessage}
	 * 
	 * @param imageName the imageName to set
	 */
	public void setImageName(String imageName) {
		this.imageName = imageName;
	}


}
