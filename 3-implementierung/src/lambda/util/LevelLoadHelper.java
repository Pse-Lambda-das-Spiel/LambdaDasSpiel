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
import lambda.model.levels.TutorialMessage;

/**
 * This class helps with the loading process of level json-files.
 * It loads the level data from the json file and initialize the LevelModel with it.
 * 
 * @author Robert Hochweiss
 */
public final class LevelLoadHelper {

	private LevelLoadHelper() {
	}
	
	/**
	 * This method loads the specific level json file and fills a Levelmodel with it.
	 * 
	 * @param id the id of the to be loaded level
	 * @return the LevelModel initialized with the level data from the json file
	 */
	public static LevelModel loadLevel(int id) {
		
		
		FileHandle file = Gdx.files.internal("json/levels/" + String.format("%02d", id) + ".json");
		JsonReader reader = new JsonReader();
		JsonValue jsonFile = reader.parse(file);
		JsonValue level = jsonFile.child();
		JsonValue availableRedStrats = level.get("availableRedStrats");
		JsonValue useableElements = level.get("useableElements");
		JsonValue tutorial = level.get("tutorial");
		JsonValue constellations = level.get("constellations");
		JsonValue start = constellations.get("start");
		JsonValue goal = constellations.get("goal");
		JsonValue hint = constellations.get("hint");
		LevelModel levelModel = new LevelModel(id, convertJsonToConstellation(start), convertJsonToConstellation(goal), 
				convertJsonToConstellation(hint), convertJsonToTutorial(tutorial), convertJsonToAvailableRedStrats(availableRedStrats),
				convertJsonToUseableElements(useableElements), level.getInt("coins"), level.getInt("difficulty"), level.getBoolean("standardMode"));
		return levelModel;
	}

	private static List<ReductionStrategy> convertJsonToAvailableRedStrats(JsonValue value) {
		List<ReductionStrategy> strategyList = new ArrayList<>();
		String[] strategies = value.asStringArray();
		for (int i = 0; i < strategies.length; i++) {
			switch (strategies[i]) {
			case "NORMAL_ORDER":
				strategyList.add(ReductionStrategy.NORMAL_ORDER);
				break;
			case "APPLICATIVE_ORDER":
				strategyList.add(ReductionStrategy.APPLICATIVE_ORDER);
				break;
			case "CALL_BY_NAME":
				strategyList.add(ReductionStrategy.CALL_BY_NAME);
				break;
			case "CALL_BY_VALUE":
				strategyList.add(ReductionStrategy.CALL_BY_VALUE);
				break;
			default:
				break;
			}
		}
		return strategyList;
	}
	
	private static List<ElementType> convertJsonToUseableElements(JsonValue value) {
		List<ElementType> elementTypeList = new ArrayList<>();
		String[] elementTypes = value.asStringArray();
		for (int i = 0; i < elementTypes.length; i++) {
			switch (elementTypes[i]) {
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
				break;
			}
		}
		return elementTypeList;
	}
	
	private static List<TutorialMessage> convertJsonToTutorial(JsonValue value) {
		List<TutorialMessage> tutorialMessageList = new ArrayList<>();
		String[] tutorialMessages  = value.asStringArray();
		for (int i = 0; i < tutorialMessages.length; i++) {
			tutorialMessageList.add(new TutorialMessage(tutorialMessages[i]));
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
			break;
		}
		return nextNode;
	}
	
}
