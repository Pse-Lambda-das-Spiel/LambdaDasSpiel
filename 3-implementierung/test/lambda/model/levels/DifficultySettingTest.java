package lambda.model.levels;

import org.junit.After;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;



/**
 * @author Kay Schmitteckert
 */
public class DifficultySettingTest {

    DifficultySetting difficulty;
    String musicString;
    String bgImageString;
    int difficultyNumber;

    @Before
    public void setUp() throws Exception {
        difficultyNumber = 1;
        musicString = "01.mp3";
        bgImageString = "01.jpg";
        difficulty = new DifficultySetting(difficultyNumber, musicString, bgImageString);

    }

    @Test
    public void testGetter() {
        
        assertTrue(difficulty.getBgImageString().equals(bgImageString));
        assertTrue(difficulty.getMusicString().equals(musicString));
        assertTrue(difficulty.getDifficulty() == difficultyNumber);

    }

    @After
    public void tearDown() throws Exception {
        difficulty = null;
        bgImageString = null;
        musicString = null;
        difficultyNumber = 0;
    }

}
