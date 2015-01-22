package test.lambda.model.levels;

import lambda.model.levels.ElementType;
import lambda.model.levels.LevelModel;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


/**
 * Created by kay on 21.01.15.
 */
public class LevelModelTest {

    LevelModel level;

    @Before
    public void setUp() throws Exception {
        level = new LevelModel();


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

    @After
    public void tearDown() throws Exception {
    }



}
