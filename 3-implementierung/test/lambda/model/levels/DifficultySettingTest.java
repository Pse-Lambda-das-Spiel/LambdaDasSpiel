package lambda.model.levels;

import org.junit.After;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Kay Schmitteckert
 */
public class DifficultySettingTest {

    DifficultySetting difficultySetting;
    String musicString;
    String bgImageString;
    int difficultyNumber;

    @Before
    public void setUp() throws Exception {
        difficultyNumber = 1;
        musicString = "01.mp3";
        bgImageString = "01.jpg";
        difficultySetting = new DifficultySetting(difficultyNumber,
                musicString, bgImageString);

    }

    @Test
    public void testGetter() {

        assertTrue(difficultySetting.getBgImageString().equals(bgImageString));
        assertTrue(difficultySetting.getMusicString().equals(musicString));
        int i = difficultySetting.getDifficulty();
        String number = Integer.toString(i);
        assertTrue(number.equals("1"));

    }

    @After
    public void tearDown() throws Exception {
        difficultySetting = null;
        bgImageString = null;
        musicString = null;
        difficultyNumber = 0;
    }

}
