
package lambda.model.levels;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.I18NBundle;

import lambda.GdxTestRunner;
//import com.badlogic.gdx.backends.lwjgl.LwjglFiles;
import lambda.model.lambdaterm.LambdaAbstraction;
import lambda.model.lambdaterm.LambdaApplication;
import lambda.model.lambdaterm.LambdaRoot;
import lambda.model.lambdaterm.LambdaVariable;
import lambda.viewcontroller.level.TutorialMessage;

import org.junit.*;
import org.junit.runner.RunWith;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

// This test is only for the desktop at the moment, so move it to the desktop sub project.

/**
 * This is test for the LevelLoadHelper and the LevelModelLoader.
 * 
 * @author Robert Hochweiss
 */
@RunWith(GdxTestRunner.class)
public class LevelLoadTest {
	
	private static LevelModel testLevel;
	private static AssetManager assets;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		//Gdx.files = new LwjglFiles();
		assets = new AssetManager();
		LambdaRoot start = new LambdaRoot();
		LambdaRoot goal = new LambdaRoot();
		LambdaRoot hint = new LambdaRoot();
		List<TutorialMessage> tutorial = new ArrayList<>();
		List<ReductionStrategy> availableRedStrats = new ArrayList<>();
		List<ElementType> useableElements = new ArrayList<>();
		// tutorial.add(new TutorialMessage("8", null, null, null, null)); // TODO
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
		testLevel = new LevelModel(3, start, goal, hint, tutorial, availableRedStrats, useableElements, 1, 10, true);
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
	
	@Test
	public void simpleTest() {
		assets.load("data/i18n/StringBundle_de", I18NBundle.class);
		assets.finishLoading();
		I18NBundle testBundle = assets.get("data/i18n/StringBundle_de", I18NBundle.class);
		System.out.println(testBundle.format("gemsEnchantedAchievement", 4));
		assertTrue(true);
	}
	
	/**
	 * Tests the loading of a sample level json file(in this case 03.json).
	 */
	@Test
	public void testLoadLevel() {
		LevelModel jsonLevel = LevelLoadHelper.loadLevel(Gdx.files.internal("data/levels/03.json"));
		assertEquals(testLevel.getId(), jsonLevel.getId());
		assertEquals(testLevel.getCoins(), jsonLevel.getCoins());
		assertEquals(testLevel.getDifficulty(), jsonLevel.getDifficulty());
		assertEquals(testLevel.isStandardMode(), jsonLevel.isStandardMode());
		assertEquals(testLevel.getStart(), jsonLevel.getStart());
		assertEquals(testLevel.getGoal(), jsonLevel.getGoal());
		assertEquals(testLevel.getHint(), jsonLevel.getHint());
		for (int i = 0; i < jsonLevel.getTutorial().size(); i++) {
			//assertEquals(testLevel.getTutorial().get(i).getId(), jsonLevel.getTutorial().get(i).getId());
		}
		assertEquals(testLevel.getAvailableRedStrats(), jsonLevel.getAvailableRedStrats());
		assertEquals(testLevel.getUseableElements(), jsonLevel.getUseableElements());
	}

	@Test
	public void testLoadAllLevels() {
		int number = LevelLoadHelper.loadAllLevelPaths().length;
		assertEquals(5, number);
	}
	
	/**
	 * Tests the loading of a sample level json file while using the {@link LevelModelLoader}.
	 */
	@Test
	public void testLevelModelLoader() {
		assets.setLoader(LevelModel.class, new LevelModelLoader(new InternalFileHandleResolver()));
		assets.load("data/levels/03.json", LevelModel.class);
		while (!(assets.update())) {
			System.out.println("Wait until its loaded...");
		}
		LevelModel loadedLevel = assets.get("data/levels/03.json", LevelModel.class);
		assertEquals(testLevel.getId(), loadedLevel.getId());
		assertEquals(testLevel.getCoins(), loadedLevel.getCoins());
		assertEquals(testLevel.getDifficulty(), loadedLevel.getDifficulty());
		assertEquals(testLevel.isStandardMode(), loadedLevel.isStandardMode());
		assertEquals(testLevel.getStart(), loadedLevel.getStart());
		assertEquals(testLevel.getGoal(), loadedLevel.getGoal());
		assertEquals(testLevel.getHint(), loadedLevel.getHint());
		for (int i = 0; i < loadedLevel.getTutorial().size(); i++) {
			//assertEquals(testLevel.getTutorial().get(i).getId(), loadedLevel.getTutorial().get(i).getId());
		}
		assertEquals(testLevel.getAvailableRedStrats(), loadedLevel.getAvailableRedStrats());
		assertEquals(testLevel.getUseableElements(), loadedLevel.getUseableElements());
	}

}
