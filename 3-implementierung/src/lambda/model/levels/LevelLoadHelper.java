package lambda.model.levels;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import lambda.model.lambdaterm.LambdaAbstraction;
import lambda.model.lambdaterm.LambdaApplication;
import lambda.model.lambdaterm.LambdaRoot;
import lambda.model.lambdaterm.LambdaTerm;
import lambda.model.lambdaterm.LambdaVariable;
import lambda.viewcontroller.level.TutorialMessage;

/**
 * This class helps with the loading process of level json files.
 * It encapsulates all functionality needed for accessing and loading level json files and initializing the levels.
 * It also encapsulates all functionality needed for accessing and loading difficulty setting files of the levels
 * and their components, the background image files and music files.
 * 
 * @author Robert Hochweiss
 */
public final class LevelLoadHelper {

	private LevelLoadHelper() {
	}
	
	/**
	 * Loads the specific level json file and initialize a LevelModel with the loaded data.
	 * 
	 * @param file the {@link FileHandle} of the to be loaded level
	 * @return the LevelModel initialized with the level data from the json file
	 * @throws InvalidJsonException if the corresponding json file has invalid content
	 * @throws IOException if there is an error while reading the level json file
	 */
	public static LevelModel loadLevel(FileHandle file) {
		JsonReader reader = new JsonReader();
		JsonValue jsonFile = reader.parse(file);
		JsonValue level = jsonFile.child();
		String levelId = String.format("%02d", level.getInt("levelId"));
		if (!(levelId.equals(file.nameWithoutExtension()))) {
			throw new InvalidJsonException("The id of the json file " + file.name() 
																		+  " does not match with its file name!");
		}
		JsonValue availableRedStrats = level.get("availableRedStrats");
		JsonValue useableElements = level.get("useableElements");
		JsonValue tutorial = level.get("tutorial");
		ReductionStrategy defaultStrategy = convertJsonToReductionStrategy(level.getString("defaultStrategy"));
		List<ReductionStrategy> redStratsList = new ArrayList<>();
		for (JsonValue entry = availableRedStrats.child(); entry != null; entry = entry.next) {
			redStratsList.add(convertJsonToReductionStrategy(entry.getString("reductionStrategy")));
		}
		List<Color> availableColors = new ArrayList<>();
		JsonValue availableColorsValue = level.get("availableColors");
		for (JsonValue entry = availableColorsValue.child(); entry != null; entry = entry.next) {
			availableColors.add(setColor(entry.getString("color")));
		}
		JsonValue constellations = level.get("constellations");
		JsonValue start = constellations.get("start");
		JsonValue goal = constellations.get("goal");
		JsonValue hint = constellations.get("hint");
		LevelModel levelModel = new LevelModel(level.getInt("levelId"), convertJsonToConstellation(start), 
				convertJsonToConstellation(goal), convertJsonToConstellation(hint), convertJsonToTutorial(tutorial), 
				redStratsList, convertJsonToUseableElements(useableElements), 
				level.getInt("difficulty"), level.getInt("coins"), level.getBoolean("standardMode"),
				level.getBoolean("colorEquivalence"), availableColors, defaultStrategy);
		return levelModel;
	}

	private static ReductionStrategy convertJsonToReductionStrategy(String reductionStrategy) {
		switch (reductionStrategy) {
		case "NORMAL_ORDER":
			return(ReductionStrategy.NORMAL_ORDER);
		case "APPLICATIVE_ORDER":
			return(ReductionStrategy.APPLICATIVE_ORDER);
		case "CALL_BY_NAME":
			return(ReductionStrategy.CALL_BY_NAME);
		case "CALL_BY_VALUE":
			return(ReductionStrategy.CALL_BY_VALUE);
		default:
			throw new InvalidJsonException("Invalid reduction strategy!");
		}
	}
	
	private static List<ElementType> convertJsonToUseableElements(JsonValue value) {
		List<ElementType> elementTypeList = new ArrayList<>();
		for (JsonValue entry = value.child(); entry != null; entry = entry.next) {
			switch (entry.getString("elementType")) {
			case "VARIABLE":
				elementTypeList.add(ElementType.VARIABLE);
				break;
			case "ABSTRACTION":
				elementTypeList.add((ElementType.ABSTRACTION));
				break;
			case "PARANTHESIS":
				elementTypeList.add((ElementType.PARANTHESIS));
				break;
			default:
				throw new InvalidJsonException("Invalid useable element type!");
			}
		}
		return elementTypeList;
	}
	
	private static List<TutorialMessage> convertJsonToTutorial(JsonValue value) {
		List<TutorialMessage> tutorialMessageList = new ArrayList<>();
		// It is not necessary for all levels to have TutorialMessages
		if (value.size == 0) {
			return tutorialMessageList;
		}
		for (JsonValue entry = value.child(); entry != null; entry = entry.next) {
			if (entry.getString("tutorialId").equals("")) {
				throw new InvalidJsonException("A tutorial id must not be empty!");
			}
			tutorialMessageList.add(new TutorialMessage(entry.getString("tutorialId"), null, null, null, null)); // TODO
		}
		return tutorialMessageList;
	}
	
	private static LambdaRoot convertJsonToConstellation(JsonValue constellation) {
		LambdaRoot root = new LambdaRoot();
		root.setChild(selectNextNode(constellation.child(), root));
		return root;
	}
	
	private static LambdaApplication convertJsonToApplication(JsonValue value, LambdaTerm parent) {
		LambdaApplication application = new LambdaApplication(parent, value.getBoolean("locked"));
		application.setLeft(selectNextNode(value.get("left"), application));
		application.setRight(selectNextNode(value.get("right"), application));
		return application;
	}
	
	private static LambdaAbstraction convertJsonToAbstraction(JsonValue value, LambdaTerm parent) {
		LambdaAbstraction abstraction = new LambdaAbstraction(parent, setColor(value.getString("color")),
				value.getBoolean("locked"));
		abstraction.setInside(selectNextNode(value.get("inside"), abstraction));
		return abstraction;
	}
	
	private static LambdaVariable convertJsonToVariable(JsonValue value, LambdaTerm parent) {
		LambdaVariable variable = new LambdaVariable(parent, setColor(value.getString("color")), 
				value.getBoolean("locked"));
		return variable;
	}
	
	private static LambdaTerm selectNextNode(JsonValue nextNodeValue, LambdaTerm parent) {
		LambdaTerm nextNode = null;
		if (nextNodeValue.isNull()) {
			return nextNode;
		}
		switch (nextNodeValue.getString("type")) {
		case "application":
			nextNode =  convertJsonToApplication(nextNodeValue, parent);
			break;
		case "abstraction":
			 nextNode = convertJsonToAbstraction(nextNodeValue, parent);
			break;
		case "variable":
			nextNode = convertJsonToVariable(nextNodeValue, parent);
			break;
		default:
			throw new InvalidJsonException("The LambdaTerm must be an application, an abstraction or a variable!");
		}
		return nextNode;
	}
	
	
	private static Color setColor(String color) {
		switch(color) {
		case "blue":
			return Color.BLUE;
		case "red":
			return Color.RED;
		case "yellow":
			return Color.YELLOW;
		case "green":
			return Color.GREEN;
		case "orange":
			return Color.ORANGE;
		case "cyan":
			return Color.CYAN;
		case "pink":
			return Color.PINK;
		case "purple": 
			return Color.PURPLE;
		case "olive":
			return Color.OLIVE;
		case "white":
			return Color.WHITE;
		default:
			throw new InvalidJsonException("Invalid color!");
		}
	}

	/**
	 * Returns an array which contains the path to all level files
	 *
	 * @return a an which contains the path to all level files
	 */
	public static String[] loadAllLevelPaths() {
		FileHandle file = Gdx.files.internal("data/levels/numberOfLevels.json");
		JsonReader reader = new JsonReader();
		JsonValue jsonFile = reader.parse(file);
		// numberOfLevel is only the number of the regular levels, the sandbox (level 0) is special
		int numberOfLevels = jsonFile.getInt("numberOfLevels");
		String[] levelFilePaths = new String[numberOfLevels + 1];
		for (int i = 0; i <= numberOfLevels; i++) {
			levelFilePaths[i] = "data/levels/" + String.format("%02d", i) + ".json";
		}
		return levelFilePaths;
	}

	/**
	 * Returns settings for a difficulty
	 *
	 * @param file the {@link FileHandle}  of the to be loaded difficulty settings
	 * @return settings for a difficulty
	 * @throws InvalidJsonException if the corresponding json file has invalid content
	 * @throws IOException if there is an error while reading the level json file
	 */
	public static DifficultySetting loadDifficulty(FileHandle file) {
		JsonReader reader = new JsonReader();
		JsonValue jsonFile = reader.parse(file);
		JsonValue difficulty = jsonFile.child();
		String difficultyId = String.format("%02d", difficulty.getInt("difficultyId"));
		if (!(difficultyId.equals(file.nameWithoutExtension()))) {
			throw new InvalidJsonException("The id of the json file " + file.name() 
					+ "does not match with its file name!");
		}
		DifficultySetting difficultySetting = new DifficultySetting(difficulty.getInt("difficultyId"),
				"data/levels/music/" + difficulty.getString("music"),
				"data/levels/images/" + difficulty.getString("bgImage"));
		return difficultySetting;
	}

	/**
	 * Returns the paths to all DifficultySetting files
	 *
	 * @return an array which contains the paths for all DifficultySettings
	 * @throws IOException if there is an error while reading the level json file
	 */
	public static String[] loadAllDifficultyPaths() {
		FileHandle file = Gdx.files.internal("data/difficulties/numberOfDifficulties.json");
		JsonReader reader = new JsonReader();
		JsonValue jsonFile = reader.parse(file);
		int numberOfDifficulties = jsonFile.getInt("numberOfDifficulties");
		String[] difficultySettingFilePaths = new String[numberOfDifficulties];
		for (int i = 0; i < numberOfDifficulties; i++) {
			difficultySettingFilePaths[i] = "data/difficulties/" + String.format("%02d", i + 1) + ".json";
		}
		return difficultySettingFilePaths;
	}
	
	/**
	 * Returns the paths to all difficulty background image files.
	 *
	 * @return an array which contains the paths for all difficulty background image files;
	 * @throws IOException if there is an error while reading the level json file
	 */
	public static String[] loadAllDifficultyBGImageFilePaths() {
		FileHandle file = Gdx.files.internal("data/levels/images/numberOfImages.json");
		JsonReader reader = new JsonReader();
		JsonValue jsonFile = reader.parse(file);
		int numberOfImages = jsonFile.getInt("numberOfImages");
		String[] difficultyBGImageFilePaths = new String[numberOfImages];
		for (int i = 0; i < numberOfImages; i++) {
			difficultyBGImageFilePaths[i] = "data/levels/images/" + String.format("%02d", i + 1) + ".png";
		}
		return difficultyBGImageFilePaths;
	}
	
	/**
	 * Returns the paths to all difficulty music files.
	 *
	 * @return an array which contains the paths for all difficulty music files;
	 * @throws IOException if there is an error while reading the level json file
	 */
	public static String[] loadAllDifficultyMusicFilePaths() {
		FileHandle file = Gdx.files.internal("data/levels/music/numberOfMusic.json");
		JsonReader reader = new JsonReader();
		JsonValue jsonFile = reader.parse(file);
		int numberOfMusic = jsonFile.getInt("numberOfMusic");
		String[] difficultyMusicFilePaths = new String[numberOfMusic];
		for (int i = 0; i < numberOfMusic; i++) {
			difficultyMusicFilePaths[i] = "data/levels/music/" + String.format("%02d", i + 1) + ".mp3";
		}
		return difficultyMusicFilePaths;
	}
	
}
