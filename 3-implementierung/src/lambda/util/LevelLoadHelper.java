package lambda.util;

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
import lambda.model.levels.ElementType;
import lambda.model.levels.LevelModel;
import lambda.model.levels.ReductionStrategy;
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
	 * @param id the id of the to be loaded level
	 * @return the LevelModel initialized with the level data from the json file
	 * @throws InvalidJsonException if the corresponding json file has invalid content
	 */
	public static LevelModel loadLevel(int id) {
		FileHandle file = Gdx.files.internal("data/levels/" + String.format("%02d", id) + ".json");
		JsonReader reader = new JsonReader();
		JsonValue jsonFile = reader.parse(file);
		JsonValue level = jsonFile.child();
		if (!(level.getInt("levelId") == id)) {
			throw new InvalidJsonException("The id of the json file does not match with its file name!");
		}
		JsonValue availableRedStrats = level.get("availableRedStrats");
		JsonValue useableElements = level.get("useableElements");
		JsonValue tutorial = level.get("tutorial");
		JsonValue constellations = level.get("constellations");
		JsonValue start = constellations.get("start");
		JsonValue goal = constellations.get("goal");
		JsonValue hint = constellations.get("hint");
		LevelModel levelModel = new LevelModel(id, convertJsonToConstellation(start), convertJsonToConstellation(goal), 
				convertJsonToConstellation(hint), convertJsonToTutorial(tutorial), 
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
		// It is not necessary for all level to have TutorialMessages
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
	
}
