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
	private boolean inEditorModel;

	/**
	 * Creates a new instance of this object.
	 * @param id the ID of the {@linkTutorialMessage}
	 * @param imageName the name of the shown image of this {@łink TutorialMessage}
	 */
	public TutorialMessageModel(String id, String imageName, Boolean inEditorMode) {
		this.id = id;
		this.imageName = imageName;
		this.inEditorModel = inEditorMode;
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
	 * Returns whether this tutorial should be in EditorViewController or not.
	 * 
	 * @return whether this tutorial should be in EditorViewController or not.
	 */
	public boolean isInEditorModel() {
		return inEditorModel;
	}

}
