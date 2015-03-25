package lambda.model.levels;

import org.junit.After;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.badlogic.gdx.graphics.Color;

import java.util.LinkedList;


/**
 * @author Kay Schmitteckert
 */
public class LevelModelTest {

    LevelModel level;

    @Before
    public void setUp() throws Exception {
        level = new LevelModel(13, null, null, null,
                new LinkedList<TutorialMessageModel>(), new LinkedList<ReductionStrategy>(),
                new LinkedList<ElementType>(), 3, 13, 50, true, true, new LinkedList<Color>(),
                new LinkedList<Color>(), ReductionStrategy.NORMAL_ORDER);

    }

    @Test
    public void testUseableElements() {
        level.getUseableElements().add(0, ElementType.ABSTRACTION);
        assertTrue(level.getUseableElements().get(0) == ElementType.ABSTRACTION);
        level.getUseableElements().add(1, ElementType.PARENTHESIS);
        assertTrue(level.getUseableElements().get(1) == ElementType.PARENTHESIS);
        level.getUseableElements().add(2, ElementType.VARIABLE);
        assertTrue(level.getUseableElements().get(2) == ElementType.VARIABLE);

    }

    @Test
    public void testAvailableRedStrats() {
        level.getAvailableRedStrats().add(0, ReductionStrategy.APPLICATIVE_ORDER);
        assertTrue(level.getAvailableRedStrats().get(0) == ReductionStrategy.APPLICATIVE_ORDER);
        assertFalse(level.getAvailableRedStrats().get(0) != ReductionStrategy.APPLICATIVE_ORDER);
        level.getAvailableRedStrats().add(1, ReductionStrategy.CALL_BY_NAME);
        assertTrue(level.getAvailableRedStrats().get(1) == ReductionStrategy.CALL_BY_NAME);
        level.getAvailableRedStrats().add(2, ReductionStrategy.CALL_BY_VALUE);
        assertTrue(level.getAvailableRedStrats().get(2) == ReductionStrategy.CALL_BY_VALUE);
        level.getAvailableRedStrats().add(3, ReductionStrategy.NORMAL_ORDER);
        assertTrue(level.getAvailableRedStrats().get(3) == ReductionStrategy.NORMAL_ORDER);
    }
    
    @Test
    public void testOtherGetters() {
        assertTrue(level.getId() == 13);
        assertTrue(level.getDifficulty() == 3);
        assertTrue(level.isStandardMode() == true);
        assertNull(level.getStart());
        assertNull(level.getGoal());
        assertNull(level.getHint());
        assertTrue(level.getTutorial().isEmpty() == true);
        assertTrue(level.getCoins() == 13);
        assertTrue(level.getMaxReductionSteps() == 50);
        assertTrue(level.getDefaultStrategy() == ReductionStrategy.NORMAL_ORDER);
        assertTrue(level.isColorEquivalence() == true);
        assertTrue(level.getAvailableColors().isEmpty() == true);
        assertTrue(level.getLockedColors().isEmpty() == true);
    }

    @After
    public void tearDown() throws Exception {
        level = null;
    }

}
