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
    private String baseLang = "data/i18n/StringBundle_de";
    private String baseAvatar = "a0";

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
     * Makes sure setLang() doesn't accept null as argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetLangNull() {
        edit.setLang(null);
    }

    /**
     * Tries to set a language that doesn't exist. setLang() should than set
     * back to the standard language.
     */
    @Test
    public void testWrongLang() {
        edit.nextLang();
        assertNotEquals(baseLang, edit.getLang());
        edit.setLang("noLang");
        assertEquals(baseLang, edit.getLang());
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
        assertEquals(
                edit.getLang().replace("StringBundle_", "flags/")
                        .concat("Flag.jpg"), edit.getLangPic());
        assertTrue(calledChangedLanguage);
        calledChangedLanguage = false;
        edit.previousLang();
        assertEquals(start, edit.getLang());
        assertEquals(start.replace("StringBundle_", "flags/")
                .concat("Flag.jpg"), edit.getLangPic());
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
        edit.setLang(baseLang);
        assertEquals(baseLang, edit.getLang());
        idsRight.add(edit.getLang());
        edit.nextLang();
        assertNotEquals(baseLang, edit.getLang());
        while (!idsRight.contains(edit.getLang())) {
            idsRight.add(edit.getLang());
            edit.nextLang();
        }

        List<String> idsLeft = new ArrayList<String>();
        edit.setLang(baseLang);
        assertEquals(baseLang, edit.getLang());
        idsLeft.add(edit.getLang());
        edit.previousLang();
        assertNotEquals(baseLang, edit.getLang());
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
     * Makes sure setAvatar() doesn't accept null as argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetAvatarNull() {
        edit.setAvatar(null);
    }

    /**
     * Tries to set an avatar that doesn't exist. setAvatar() should than set
     * back to the standard avatar.
     */
    @Test
    public void testWrongAvatar() {
        edit.nextAvatar();
        assertNotEquals(baseAvatar, edit.getAvatar());
        edit.setAvatar("noAvatar");
        assertEquals(baseAvatar, edit.getAvatar());
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
        edit.setAvatar(baseAvatar);
        assertEquals(baseAvatar, edit.getAvatar());
        idsRight.add(edit.getAvatar());
        edit.nextAvatar();
        assertNotEquals(baseAvatar, edit.getAvatar());
        while (!idsRight.contains(edit.getAvatar())) {
            idsRight.add(edit.getAvatar());
            edit.nextAvatar();
        }

        List<String> idsLeft = new ArrayList<String>();
        edit.setAvatar(baseAvatar);
        assertEquals(baseAvatar, edit.getAvatar());
        idsLeft.add(edit.getAvatar());
        edit.previousAvatar();
        assertNotEquals(baseAvatar, edit.getAvatar());
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
