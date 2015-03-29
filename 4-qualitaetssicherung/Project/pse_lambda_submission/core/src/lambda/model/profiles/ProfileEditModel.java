package lambda.model.profiles;

import java.util.LinkedList;
import java.util.List;

import lambda.Consumer;
import lambda.Observable;

/**
 * Represents the logic behind the editing-screen of a profile's avatar and
 * language.
 * 
 * @author Kai Fieger
 */
public class ProfileEditModel extends Observable<ProfileEditObserver> {

    private static final int AVATAR_NUMBER = 10;
    private List<String> lang;
    private int selectedLang;
    private List<String> avatar;
    private int selectedAvatar;

    /**
     * Creates a new ProfileEditModel-object.
     */
    public ProfileEditModel() {
        lang = new LinkedList<String>();
        selectedLang = 0;
        avatar = new LinkedList<String>();
        selectedAvatar = 0;

        lang.add("data/i18n/StringBundle_de");
        lang.add("data/i18n/StringBundle_en");
        lang.add("data/i18n/StringBundle_fr");

        avatar = new LinkedList<String>();
        for (int i = 0; i < AVATAR_NUMBER; i++) {
            this.avatar.add("a" + i);
        }
    }

    /**
     * Sets the language selection to the given ID. Afterwards it notifies its
     * observers by calling their {@link ProfileEditObserver#changedLanguage()
     * changedLanguage()} method. Is set to the standard if lang is an invalid
     * ID.
     * 
     * @param lang
     *            The language ID.
     */
    public void setLang(String lang) {
        if (lang == null) {
            throw new IllegalArgumentException("lang cannot be null");
        }
        selectedLang = this.lang.indexOf(lang);
        if (selectedLang == -1) {
            selectedLang = 0;
        }
        notify(new Consumer<ProfileEditObserver>() {
            @Override
            public void accept(ProfileEditObserver observer) {
                observer.changedLanguage();
            }
        });
    }

    /**
     * Sets the language selection one step to the right. (cyclic) Afterwards it
     * notifies its observers by calling their
     * {@link ProfileEditObserver#changedLanguage() changedLanguage()} method.
     */
    public void nextLang() {
        if (++selectedLang == lang.size()) {
            selectedLang = 0;
        }
        notify(new Consumer<ProfileEditObserver>() {
            @Override
            public void accept(ProfileEditObserver observer) {
                observer.changedLanguage();
            }
        });
    }

    /**
     * Sets the language selection one step to the left. (cyclic) Afterwards it
     * notifies its observers by calling their
     * {@link ProfileEditObserver#changedLanguage() changedLanguage()} method.
     */
    public void previousLang() {
        if (--selectedLang < 0) {
            selectedLang = lang.size() - 1;
        }
        notify(new Consumer<ProfileEditObserver>() {
            @Override
            public void accept(ProfileEditObserver observer) {
                observer.changedLanguage();
            }
        });
    }

    /**
     * Returns the current language ID.
     * 
     * @return The current language ID.
     */
    public String getLang() {
        return lang.get(selectedLang);
    }

    /**
     * Returns the current flag ID.
     * 
     * @return The current flag ID.
     */
    public String getLangPic() {
        return lang.get(selectedLang).replace("StringBundle_", "flags/")
                .concat("Flag.jpg");
    }

    /**
     * Sets the avatar selection to the given ID. Afterwards it notifies its
     * observers by calling their {@link ProfileEditObserver#changedAvatar()
     * changedAvatar()} method. Is set to the standard if avatar is an invalid
     * ID.
     * 
     * @param avatar
     *            The avatar picture ID.
     */
    public void setAvatar(String avatar) {
        if (avatar == null) {
            throw new IllegalArgumentException("avatar cannot be null");
        }
        selectedAvatar = this.avatar.indexOf(avatar);
        if (selectedAvatar == -1) {
            selectedAvatar = 0;
        }
        notify(new Consumer<ProfileEditObserver>() {
            @Override
            public void accept(ProfileEditObserver observer) {
                observer.changedAvatar();
            }
        });
    }

    /**
     * Sets the avatar selection one step to the right. (cyclic) Afterwards it
     * notifies its observers by calling their
     * {@link ProfileEditObserver#changedAvatar() changedAvatar()} method.
     */
    public void nextAvatar() {
        if (++selectedAvatar == avatar.size()) {
            selectedAvatar = 0;
        }
        notify(new Consumer<ProfileEditObserver>() {
            @Override
            public void accept(ProfileEditObserver observer) {
                observer.changedAvatar();
            }
        });
    }

    /**
     * Sets the avatar selection one step to the left. (cyclic) Afterwards it
     * notifies its observers by calling their
     * {@link ProfileEditObserver#changedAvatar() changedAvatar()} method.
     */
    public void previousAvatar() {
        if (--selectedAvatar < 0) {
            selectedAvatar = avatar.size() - 1;
        }
        notify(new Consumer<ProfileEditObserver>() {
            @Override
            public void accept(ProfileEditObserver observer) {
                observer.changedAvatar();
            }
        });
    }

    /**
     * Returns the current avatar ID.
     * 
     * @return The current avatar ID.
     */
    public String getAvatar() {
        return avatar.get(selectedAvatar);
    }

}
