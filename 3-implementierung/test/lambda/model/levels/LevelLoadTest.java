
package lambda.model.levels;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.backends.lwjgl.LwjglFiles;

import lambda.model.lambdaterm.LambdaAbstraction;
import lambda.model.lambdaterm.LambdaApplication;
import lambda.model.lambdaterm.LambdaRoot;
import lambda.model.lambdaterm.LambdaVariable;
import lambda.viewcontroller.level.TutorialMessage;

import org.junit.*;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


/**
 * This is test for the LevelLoadHelper and the LevelModelLoader.
 * 
 * @author Robert Hochweiss
 */
public class LevelLoadTest {
	
	private static LevelModel testLevel;
	private static AssetManager assets;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		Gdx.files = new LwjglFiles();
		assets = new AssetManager();
		LambdaRoot start = new LambdaRoot();
		LambdaRoot goal = new LambdaRoot();
		LambdaRoot hint = new LambdaRoot();
		List<TutorialMessageModel> tutorial = new ArrayList<>();
		List<ReductionStrategy> availableRedStrats = new ArrayList<>();
		List<ElementType> useableElements = new ArrayList<>();
		List<Color> availableColors = new ArrayList<>();
		availableColors.add(Color.PINK);
		availableColors.add(Color.RED);
		availableColors.add(Color.GREEN);
		availableColors.add(Color.CYAN);
		availableColors.add(Color.ORANGE);
		availableColors.add(Color.YELLOW);
		availableRedStrats.add(ReductionStrategy.NORMAL_ORDER);
		// Do you know any better way to initialize the lambdaterm constellations?
		tutorial.add(new TutorialMessageModel("tutorial_2_0", ""));
		tutorial.add(new TutorialMessageModel("tutorial_2_1", ""));
		tutorial.add(new TutorialMessageModel("tutorial_2_2", ""));
		tutorial.add(new TutorialMessageModel("tutorial_2_3", ""));
		tutorial.add(new TutorialMessageModel("tutorial_2_4", ""));
		
		// Initialize the test start constellation: (lx.x)y, lx is blue, x is white, y is white
		LambdaApplication startApplication = new LambdaApplication(start, true);
		LambdaAbstraction startAbstraction = new LambdaAbstraction(startApplication, new Color(Color.BLUE), false);
		startAbstraction.setInside(new LambdaVariable(startAbstraction, new Color(Color.BLUE), false));
		startApplication.setLeft(startAbstraction);
		startApplication.setRight(new LambdaVariable(startApplication, new Color(Color.WHITE), false));
		start.setChild(startApplication);
		// Initialize the test goal constellation: y, y is red
		goal.setChild(new LambdaVariable(goal, new Color(Color.RED), false));
		// Initialize the hint start constellation: (lx.x)y, lx is blue, x is blue, y is white
		LambdaApplication hintApplication = new LambdaApplication(hint, true);
		LambdaAbstraction hintAbstraction = new LambdaAbstraction(null, new Color(Color.BLUE), false);
		hintAbstraction.setInside(new LambdaVariable(hintAbstraction, new Color(Color.BLUE), false));
		hintApplication.setLeft(hintAbstraction);
		hintApplication.setRight(new LambdaVariable(hintApplication, new Color(Color.WHITE), false));
		hint.setChild(hintApplication);
		testLevel = new LevelModel(2, start, goal, hint, tutorial, availableRedStrats, useableElements, 1, 10, true,
					true, availableColors, ReductionStrategy.NORMAL_ORDER);
		
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		testLevel = null;
		assets.dispose();
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}
	
	/**
	 * Tests the loading of a sample level json file(in this case 03.json).
	 */
	@Test
	public void testLoadLevel() {
		LevelModel jsonLevel = LevelLoadHelper.loadLevel(Gdx.files.internal("data/levels/02.json"));
		assertEquals(testLevel.getId(), jsonLevel.getId());
		assertEquals(testLevel.getCoins(), jsonLevel.getCoins());
		assertEquals(testLevel.getDifficulty(), jsonLevel.getDifficulty());
		assertEquals(testLevel.isStandardMode(), jsonLevel.isStandardMode());
		assertEquals(testLevel.getStart(), jsonLevel.getStart());
		assertEquals(testLevel.getGoal(), jsonLevel.getGoal());
		assertEquals(testLevel.getHint(), jsonLevel.getHint());
		for (int i = 0; i < jsonLevel.getTutorial().size(); i++) {
			assertEquals(testLevel.getTutorial().get(i).getId(), jsonLevel.getTutorial().get(i).getId());
			assertEquals(testLevel.getTutorial().get(i).getImageName(), jsonLevel.getTutorial().get(i).getImageName());
		}
		assertEquals(testLevel.getAvailableColors(), jsonLevel.getAvailableColors());
		assertEquals(testLevel.getAvailableRedStrats(), jsonLevel.getAvailableRedStrats());
		assertEquals(testLevel.getUseableElements(), jsonLevel.getUseableElements());
	}

	@Test
	public void testLoadAllLevels() {
		int number = LevelLoadHelper.loadAllLevelPaths().length;
		assertEquals(25, number);
	}
	
	/**
	 * Tests the loading of a sample level json file while using the {@link LevelModelLoader}.
	 */
	@Test
	public void testLevelModelLoader() {
		assets.setLoader(LevelModel.class, new LevelModelLoader(new InternalFileHandleResolver()));
		assets.load("data/levels/02.json", LevelModel.class);
		while (!(assets.update())) {
		}
		LevelModel loadedLevel = assets.get("data/levels/02.json", LevelModel.class);
		assertEquals(testLevel.getId(), loadedLevel.getId());
		assertEquals(testLevel.getCoins(), loadedLevel.getCoins());
		assertEquals(testLevel.getDifficulty(), loadedLevel.getDifficulty());
		assertEquals(testLevel.isStandardMode(), loadedLevel.isStandardMode());
		assertEquals(testLevel.getStart(), loadedLevel.getStart());
		assertEquals(testLevel.getGoal(), loadedLevel.getGoal());
		assertEquals(testLevel.getHint(), loadedLevel.getHint());
		for (int i = 0; i < loadedLevel.getTutorial().size(); i++) {
			assertEquals(testLevel.getTutorial().get(i).getId(), loadedLevel.getTutorial().get(i).getId());
			assertEquals(testLevel.getTutorial().get(i).getImageName(), loadedLevel.getTutorial().get(i).getImageName());
		}
		assertEquals(testLevel.getAvailableColors(), loadedLevel.getAvailableColors());
		assertEquals(testLevel.getAvailableRedStrats(), loadedLevel.getAvailableRedStrats());
		assertEquals(testLevel.getUseableElements(), loadedLevel.getUseableElements());
	}

}
