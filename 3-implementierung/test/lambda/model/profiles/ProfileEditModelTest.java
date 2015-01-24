package lambda.model.profiles;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests a ProfileEditModel
 * 
 * @author Kai Fieger
 */
public class ProfileEditModelTest implements ProfileEditObserver {

    private boolean calledChangedLanguage;
    private boolean calledChangedAvatar;
    private ProfileEditModel edit;

    @Before
    public void setUp() throws Exception {
        calledChangedLanguage = false;
        calledChangedAvatar = false;
        edit = new ProfileEditModel();
        edit.addObserver(this);
    }

    @After
    public void tearDown() throws Exception {
        edit.removeObserver(this);
    }

    /**
     * Tests nextLang and previousLang with their getters (getLang and
     * getLangPic), while making sure the correct ProfileEditObserver-methods
     * are called.
     */
    @Test
    public void testLanguageNextPrev() {
        String start = edit.getLang();
        edit.nextLang();
        assertNotEquals(start, edit.getLang());
        assertEquals(edit.getLang() + "Flag", edit.getLangPic());
        assertTrue(calledChangedLanguage);
        calledChangedLanguage = false;
        edit.previousLang();
        assertEquals(start, edit.getLang());
        assertEquals(start + "Flag", edit.getLangPic());
        assertTrue(calledChangedLanguage);
    }

    /**
     * Tests setLang and if nextLang and prevousLang can make a full circles.
     * Afterwards it compares both acquired lists of IDs to make sure they
     * contain the same IDs.
     */
    @Test
    public void testLanguageCycle() {
        List<String> idsRight = new ArrayList<String>();
        edit.setLang("de");
        assertEquals("de", edit.getLang());
        idsRight.add(edit.getLang());
        edit.nextLang();
        assertNotEquals("de", edit.getLang());
        while (!idsRight.contains(edit.getLang())) {
            idsRight.add(edit.getLang());
            edit.nextLang();
        }

        List<String> idsLeft = new ArrayList<String>();
        edit.setLang("de");
        assertEquals("de", edit.getLang());
        idsLeft.add(edit.getLang());
        edit.previousLang();
        assertNotEquals("de", edit.getLang());
        while (!idsLeft.contains(edit.getLang())) {
            idsLeft.add(edit.getLang());
            edit.previousLang();
        }

        assertEquals(idsRight.size(), idsLeft.size());
        assertEquals(idsRight.get(0), idsLeft.get(0));

        for (int i = 1; i < idsRight.size(); i++) {
            assertEquals(idsRight.get(i), idsLeft.get(idsLeft.size() - i));
        }
    }

    /**
     * Tests nextAvatar and previousAvatar and their getter getAvatar, while
     * making sure the correct ProfileEditObserver-methods are called.
     */
    @Test
    public void testAvatarNextPrev() {
        String start = edit.getAvatar();
        edit.nextAvatar();
        assertNotEquals(start, edit.getAvatar());
        assertTrue(calledChangedAvatar);
        calledChangedAvatar = false;
        edit.previousAvatar();
        assertEquals(start, edit.getAvatar());
        assertTrue(calledChangedAvatar);
    }

    /**
     * Tests setAvatar and if nextAvatar and previousAvatar can make a full
     * circles. Afterwards it compares both aquired lists of IDs to make sure
     * they contain the same IDs.
     */
    @Test
    public void testAvatarCycle() {
        List<String> idsRight = new ArrayList<String>();
        edit.setAvatar("a0");
        assertEquals("a0", edit.getAvatar());
        idsRight.add(edit.getAvatar());
        edit.nextAvatar();
        assertNotEquals("a0", edit.getAvatar());
        while (!idsRight.contains(edit.getAvatar())) {
            idsRight.add(edit.getAvatar());
            edit.nextAvatar();
        }

        List<String> idsLeft = new ArrayList<String>();
        edit.setAvatar("a0");
        assertEquals("a0", edit.getAvatar());
        idsLeft.add(edit.getAvatar());
        edit.previousAvatar();
        assertNotEquals("a0", edit.getAvatar());
        while (!idsLeft.contains(edit.getAvatar())) {
            idsLeft.add(edit.getAvatar());
            edit.previousAvatar();
        }

        assertEquals(idsRight.size(), idsLeft.size());
        assertEquals(idsRight.get(0), idsLeft.get(0));

        for (int i = 1; i < idsRight.size(); i++) {
            assertEquals(idsRight.get(i), idsLeft.get(idsLeft.size() - i));
        }
    }

    @Override
    public void changedLanguage() {
        calledChangedLanguage = true;
    }

    @Override
    public void changedAvatar() {
        calledChangedAvatar = true;
    }

}
