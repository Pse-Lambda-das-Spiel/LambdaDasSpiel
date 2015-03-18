package lambda.model.levels;

import org.junit.After;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.badlogic.gdx.graphics.Color;

import java.util.LinkedList;


/**
 * Created by kay on 21.01.15.
 */
public class LevelModelTest {

    LevelModel level;

    @Before
    public void setUp() throws Exception {
        level = new LevelModel(13, null, null, null,
                new LinkedList<TutorialMessageModel>(), new LinkedList<ReductionStrategy>(),
                new LinkedList<ElementType>(), 3, 13, true, true, new LinkedList<Color>(),
                new LinkedList<Color>(), ReductionStrategy.NORMAL_ORDER);

    }

    @Test
    public void testUseableElements() {
        level.getUseableElements().add(0, ElementType.ABSTRACTION);
        assertTrue(level.getUseableElements().get(0) == ElementType.ABSTRACTION);
        level.getUseableElements().add(1, ElementType.PARANTHESIS);
        assertTrue(level.getUseableElements().get(1) == ElementType.PARANTHESIS);
        level.getUseableElements().add(2, ElementType.VARIABLE);
        assertTrue(level.getUseableElements().get(2) == ElementType.VARIABLE);

    }

    @Test
    public void testAvailableRedStrats() {
        level.getAvailableRedStrats().add(0, ReductionStrategy.APPLICATIVE_ORDER);
        assertTrue(level.getAvailableRedStrats().get(0) == ReductionStrategy.APPLICATIVE_ORDER);
        level.getAvailableRedStrats().add(1, ReductionStrategy.CALL_BY_NAME);
        assertTrue(level.getAvailableRedStrats().get(1) == ReductionStrategy.CALL_BY_NAME);
        level.getAvailableRedStrats().add(2, ReductionStrategy.CALL_BY_VALUE);
        assertTrue(level.getAvailableRedStrats().get(2) == ReductionStrategy.CALL_BY_VALUE);
        level.getAvailableRedStrats().add(3, ReductionStrategy.NORMAL_ORDER);
        assertTrue(level.getAvailableRedStrats().get(3) == ReductionStrategy.NORMAL_ORDER);
    }

    @After
    public void tearDown() throws Exception {
    }

}
