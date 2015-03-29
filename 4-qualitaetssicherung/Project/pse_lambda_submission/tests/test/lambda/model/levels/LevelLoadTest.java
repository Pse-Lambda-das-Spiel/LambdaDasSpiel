
package lambda.model.levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Color;
import com.libgdxtesting.GdxTestRunner;

import lambda.model.lambdaterm.LambdaAbstraction;
import lambda.model.lambdaterm.LambdaApplication;
import lambda.model.lambdaterm.LambdaRoot;
import lambda.model.lambdaterm.LambdaVariable;

import org.junit.*;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

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
		assets = new AssetManager();
		LevelManager.getLevelManager();
		LambdaRoot start = new LambdaRoot();
		LambdaRoot goal = new LambdaRoot();
		LambdaRoot hint = new LambdaRoot();
		List<TutorialMessageModel> tutorial = new ArrayList<>();
		List<ReductionStrategy> availableRedStrats = new ArrayList<>();
		List<ElementType> useableElements = new ArrayList<>();
		List<Color> availableColors = new ArrayList<>();
		// pink
		availableColors.add(Color.valueOf("ffc0cbff"));
		// red
		availableColors.add(Color.valueOf("ff0000ff"));
		// green
		availableColors.add(Color.valueOf("00ff00ff"));
		// cyan
		availableColors.add(Color.valueOf("00ffffff"));
		// orange
		availableColors.add(Color.valueOf("ff8000ff"));
		// yellow
		availableColors.add(Color.valueOf("ffff00ff"));
		List<Color> lockedColors = new ArrayList<>();
		// blue
		lockedColors.add(Color.valueOf("0000ffff"));
		availableRedStrats.add(ReductionStrategy.NORMAL_ORDER);
		tutorial.add(new TutorialMessageModel("tutorial_2_0", "tutorials/lamb_with_gem", true));
		tutorial.add(new TutorialMessageModel("tutorial_2_1", "tutorials/lamb_enchants_gem", true));
		tutorial.add(new TutorialMessageModel("tutorial_2_2", "tutorials/cloudanimation", true));
		tutorial.add(new TutorialMessageModel("tutorial_2_3", "tutorials/lamb_without_magicwand", true));
		tutorial.add(new TutorialMessageModel("tutorial_2_4", "tutorials/reminder_goal", true));

		// Initialize the test start constellation: (lx.x)y, lx is blue, x is white, y is white
		LambdaApplication startApplication = new LambdaApplication(start, true, false);
		LambdaAbstraction startAbstraction = new LambdaAbstraction(startApplication, Color.valueOf("0000ffff"), false);
		startAbstraction.setInside(new LambdaVariable(startAbstraction, Color.valueOf("0000ffff"), false));
		startApplication.setLeft(startAbstraction);
		startApplication.setRight(new LambdaVariable(startApplication, Color.valueOf("ffffffff"), false));
		start.setChild(startApplication);
		// Initialize the test goal constellation: y, y is red
		goal.setChild(new LambdaVariable(goal, Color.valueOf("ff0000ff"), false));
		// Initialize the hint start constellation: (lx.x)y, lx is blue, x is blue, y is white
		LambdaApplication hintApplication = new LambdaApplication(hint, true, false);
		LambdaAbstraction hintAbstraction = new LambdaAbstraction(null, Color.valueOf("0000ffff"), false);
		hintAbstraction.setInside(new LambdaVariable(hintAbstraction, Color.valueOf("0000ffff"), false));
		hintApplication.setLeft(hintAbstraction);
		hintApplication.setRight(new LambdaVariable(hintApplication, Color.valueOf("ffffffff"), false));
		hint.setChild(hintApplication);
		testLevel = new LevelModel(2, start, goal, hint, tutorial, availableRedStrats, useableElements, 1, 10, 50,
				true, true, availableColors, lockedColors, ReductionStrategy.NORMAL_ORDER);

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
	 * Tests the loading of a sample level json file(in this case 02.json).
	 */
	@Test
	public void testLoadLevel() {
		LevelModel jsonLevel = LevelLoadHelper.loadLevel(Gdx.files.internal("data/levels/02.json"));
		assertEquals(testLevel.getId(), jsonLevel.getId());
		assertEquals(testLevel.getCoins(), jsonLevel.getCoins());
		assertEquals(testLevel.getMaxReductionSteps(), jsonLevel.getMaxReductionSteps());
		assertEquals(testLevel.getDifficulty(), jsonLevel.getDifficulty());
		assertEquals(testLevel.isStandardMode(), jsonLevel.isStandardMode());
		assertEquals(testLevel.getStart(), jsonLevel.getStart());
		assertEquals(testLevel.getGoal(), jsonLevel.getGoal());
		assertEquals(testLevel.getHint(), jsonLevel.getHint());
		for (int i = 0; i < jsonLevel.getTutorial().size(); i++) {
			assertEquals(testLevel.getTutorial().get(i).getId(), jsonLevel.getTutorial().get(i).getId());
			assertEquals(testLevel.getTutorial().get(i).getImageName(), jsonLevel.getTutorial().get(i).getImageName());
			assertEquals(testLevel.getTutorial().get(i).isInEditorModel(), jsonLevel.getTutorial().get(i)
					.isInEditorModel());
		}
		assertEquals(testLevel.getAvailableColors(), jsonLevel.getAvailableColors());
		assertEquals(testLevel.getLockedColors(), jsonLevel.getLockedColors());
		assertEquals(testLevel.getAvailableRedStrats(), jsonLevel.getAvailableRedStrats());
		assertEquals(testLevel.getUseableElements(), jsonLevel.getUseableElements());
		assertEquals(testLevel.getDefaultStrategy(), jsonLevel.getDefaultStrategy());
	}

	/**
	 * Tests the loading of the number of all normal level json files.
	 */
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
		assertEquals(testLevel.getMaxReductionSteps(), loadedLevel.getMaxReductionSteps());
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
		assertEquals(testLevel.getLockedColors(), loadedLevel.getLockedColors());
		assertEquals(testLevel.getAvailableRedStrats(), loadedLevel.getAvailableRedStrats());
		assertEquals(testLevel.getUseableElements(), loadedLevel.getUseableElements());
		assertEquals(testLevel.getDefaultStrategy(), loadedLevel.getDefaultStrategy());
	}

}
