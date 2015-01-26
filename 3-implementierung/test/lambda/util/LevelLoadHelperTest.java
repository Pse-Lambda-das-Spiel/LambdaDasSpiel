/**
 * 
 */
package lambda.util;

import static org.junit.Assert.*;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import lambda.model.lambdaterm.LambdaAbstraction;
import lambda.model.lambdaterm.LambdaApplication;
import lambda.model.lambdaterm.LambdaRoot;
import lambda.model.lambdaterm.LambdaValue;
import lambda.model.lambdaterm.LambdaVariable;
import lambda.model.levels.ElementType;
import lambda.model.levels.LevelModel;
import lambda.model.levels.ReductionStrategy;
import lambda.viewcontroller.level.TutorialMessage;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglFiles;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

// This test is only for the desktop at the moment, so move it to the desktop sub project.

/**
 * This is test for the LevelLoadHelper.
 * 
 * @author Robert Hochweiss
 */
public class LevelLoadHelperTest {
	
	private static LevelModel testLevel;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		Gdx.files = new LwjglFiles();
		LambdaRoot start = new LambdaRoot();
		LambdaRoot goal = new LambdaRoot();
		LambdaRoot hint = new LambdaRoot();
		List<TutorialMessage> tutorial = new ArrayList<>();
		List<ReductionStrategy> availableRedStrats = new ArrayList<>();
		List<ElementType> useableElements = new ArrayList<>();
		tutorial.add(new TutorialMessage("8"));
		availableRedStrats.add(ReductionStrategy.NORMAL_ORDER);
		useableElements.add(ElementType.VARIABLE);
		useableElements.add(ElementType.ABSTRACTION);
		// Do you know any better way to initialize the lambdaterm constellations?
		
		// Initialize the test start constellation: (lx.x)y, lx is blue, x is white, y is white
		LambdaApplication startApplication = new LambdaApplication(null, true);
		LambdaAbstraction startAbstraction = new LambdaAbstraction(null, new Color(0, 0, 255), false);
		startAbstraction.setInside(new LambdaVariable(null, new Color(255, 255, 255), false));
		startApplication.setLeft(startAbstraction);
		startApplication.setRight(new LambdaVariable(null, new Color(255, 255, 255), false));
		start.setChild(startApplication);
		// Initialize the test goal constellation: y, y is red
		goal.setChild(new LambdaVariable(null, new Color(255, 0, 0), false));
		// Initialize the hint start constellation: (lx.x)y, lx is blue, x is blue, y is white
		LambdaApplication hintApplication = new LambdaApplication(null, true);
		LambdaAbstraction hintAbstraction = new LambdaAbstraction(null, new Color(0, 0, 255), false);
		hintAbstraction.setInside(new LambdaVariable(null, new Color(0, 0, 255), false));
		hintApplication.setLeft(hintAbstraction);
		hintApplication.setRight(new LambdaVariable(null, new Color(255, 255, 255), false));
		hint.setChild(hintApplication);
		testLevel = new LevelModel(4, start, goal, hint, tutorial, availableRedStrats, useableElements, 1, 10, true);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}
	
	/**
	 * Tests the loading of sample level json file(in this case 04.json).
	 */
	@Test
	public void testLevelFile() {
		LevelModel jsonLevel = LevelLoadHelper.loadLevel(4);
		assertEquals(testLevel.getId(), jsonLevel.getId());
		assertEquals(testLevel.getCoins(), jsonLevel.getCoins());
		assertEquals(testLevel.getDifficulty(), jsonLevel.getDifficulty());
		assertEquals(testLevel.isStandardMode(), jsonLevel.isStandardMode());
		assertEquals(testLevel.getStart(), jsonLevel.getStart());
		assertEquals(testLevel.getGoal(), jsonLevel.getGoal());
		assertEquals(testLevel.getHint(), jsonLevel.getHint());
		for (int i = 0; i < jsonLevel.getTutorial().size(); i++) {
			assertEquals(testLevel.getTutorial().get(i).getId(), jsonLevel.getTutorial().get(i).getId());
		}
		assertEquals(testLevel.getAvailableRedStrats(), jsonLevel.getAvailableRedStrats());
		assertEquals(testLevel.getUseableElements(), jsonLevel.getUseableElements());
	}

	@Test
	public void testLoadAllLevels() {
		int number = LevelLoadHelper.loadAllLevels().size();
		assertEquals(5, number);
	}

}
