package lambda.model.levels;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
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
 * It encapsulates all functionality needed for accessing and loading level json files and initialising the levels.
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
	 * @throws java.io.IOException if there is an error while reading the level json file
	 */
	public static LevelModel loadLevel(FileHandle file) {
		JsonReader reader = new JsonReader();
		JsonValue jsonFile = reader.parse(file);
		JsonValue level = jsonFile.child();
		String levelId = String.format("%02d", level.getInt("levelId"));
		if (!(levelId.equals(file.nameWithoutExtension()))) {
			throw new InvalidJsonException("The id of the json file " + file.name() 
																		+  "does not match with its file name!");
		}
		JsonValue availableRedStrats = level.get("availableRedStrats");
		JsonValue useableElements = level.get("useableElements");
		JsonValue tutorial = level.get("tutorial");
		JsonValue constellations = level.get("constellations");
		JsonValue start = constellations.get("start");
		JsonValue goal = constellations.get("goal");
		JsonValue hint = constellations.get("hint");
		LevelModel levelModel = new LevelModel(level.getInt("levelId"), convertJsonToConstellation(start), 
				convertJsonToConstellation(goal), convertJsonToConstellation(hint), convertJsonToTutorial(tutorial), 
				convertJsonToAvailableRedStrats(availableRedStrats), convertJsonToUseableElements(useableElements), 
				level.getInt("difficulty"), level.getInt("coins"), level.getBoolean("standardMode"));
		return levelModel;
	}

	private static List<ReductionStrategy> convertJsonToAvailableRedStrats(JsonValue value) {
		List<ReductionStrategy> reductionStrategyList = new ArrayList<>();
		for (JsonValue entry = value.child(); entry != null; entry = entry.next) {
			switch (entry.getString("reductionStrategy")) {
			case "NORMAL_ORDER":
				reductionStrategyList.add(ReductionStrategy.NORMAL_ORDER);
				break;
			case "APPLICATIVE_ORDER":
				reductionStrategyList.add(ReductionStrategy.APPLICATIVE_ORDER);
				break;
			case "CALL_BY_NAME":
				reductionStrategyList.add(ReductionStrategy.CALL_BY_NAME);
				break;
			case "CALL_BY_VALUE":
				reductionStrategyList.add(ReductionStrategy.CALL_BY_VALUE);
				break;
			default:
				throw new InvalidJsonException("Invalid reduction strategy!");
			}
		}
		return reductionStrategyList;
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
			tutorialMessageList.add(new TutorialMessage(entry.getString("tutorialId")));
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
		JsonValue color = value.get("color");
		Color rgbColor = new Color(color.getInt("r"), color.getInt("g"), color.getInt("b"));
		LambdaAbstraction abstraction = new LambdaAbstraction(parent, rgbColor, value.getBoolean("locked"));
		abstraction.setInside(selectNextNode(value.get("inside"), abstraction));
		return abstraction;
	}
	
	private static LambdaVariable convertJsonToVariable(JsonValue value, LambdaTerm parent) {
		JsonValue color = value.get("color");
		Color rgbColor = new Color(color.getInt("r"), color.getInt("g"), color.getInt("b"));
		LambdaVariable variable = new LambdaVariable(parent, rgbColor, value.getBoolean("locked"));
		return variable;
	}
	
	private static LambdaTerm selectNextNode(JsonValue nextNodeValue, LambdaTerm parent) {
		LambdaTerm nextNode = null;
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

	//Internal file listing does not work on the desktop
	/**
	 * Returns an array which contains the path to all level files
	 *
	 * @return a an which contains the path to all level files
	 */
	public static String[] loadAllLevelPaths() {
		FileHandle file = Gdx.files.internal("data/levels/numberOfLevels.json");
		JsonReader reader = new JsonReader();
		JsonValue jsonFile = reader.parse(file);
		int numberOfLevels = jsonFile.getInt("numberOfLevels");
		String[] levelFilePaths = new String[numberOfLevels];
		for (int i = 0; i < numberOfLevels; i++) {
			levelFilePaths[i] = "data/levels/" + String.format("%02d", i) + ".json";
		}
		return levelFilePaths;
	}

	/**
	 * Returns settings for a difficulty
	 *
	 * @param file the {@link FileHandle}  of the to be loaded difficulty settings
	 * @return settings for a difficulty
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
		JsonValue music = difficulty.get("music");
		JsonValue bgImage = difficulty.get("bgImage");
		DifficultySetting difficultySetting = new DifficultySetting(difficulty.getInt("difficultyId"), 
				difficulty.getString("music"), difficulty.getString("bgImage"));


		return difficultySetting;
	}

	//Internal file listing does not work on the desktop
	/**
	 * Returns the paths to all DifficultySetting files
	 *
	 * @return an array which contains the paths for all DifficultySettings
	 */
	public static String[] loadAllDifficultyPaths() {
		FileHandle file = Gdx.files.internal("data/difficulties/numberOfDifficulties.json");
		JsonReader reader = new JsonReader();
		JsonValue jsonFile = reader.parse(file);
		int numberOfDifficulties = jsonFile.getInt("numberOfDifficulties");
		String[] difficultySettingFilePaths = new String[numberOfDifficulties];
		for (int i = 0; i < numberOfDifficulties; i++) {
			difficultySettingFilePaths[i] = "data/difficulties/" + String.format("%02d", i) + ".json";
		}
		return difficultySettingFilePaths;
	}
	
}
