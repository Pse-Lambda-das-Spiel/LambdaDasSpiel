package lambda.model.profiles;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import lambda.Observable;

/**
 * The ProfileManager controls the games profiles. It contains a list of all
 * user profiles and can load and save them. It also creates, renames and
 * deletes profiles and selects the games current one.
 * 
 * @author Kai Fieger
 */
public class ProfileManager extends Observable<ProfileManagerObserver> {

    /**
     * The maximum number of allowed profiles.
     */
    public static final int MAX_NUMBER_OF_PROFILES = 6;
    public static final String PROFILE_FOLDER = "profiles";
    private static ProfileManager manager;
    private final ProfileEditModel profileEdit;
    private ProfileModel currentProfile;
    private List<ProfileModel> profiles;

    private ProfileManager() {
        profileEdit = new ProfileEditModel();
        currentProfile = null;
        profiles = loadProfiles(Gdx.files.local(PROFILE_FOLDER));
    }

    /**
     * Returns an (/the only) instance of ProfileManager.
     * 
     * @return The ProfileManager-instance.
     */
    public static ProfileManager getManager() {
        if (manager == null) {
            manager = new ProfileManager();
        }
        return manager;
    }

    /**
     * Returns the currently selected profile.
     * 
     * @return The currently selected profile.
     */
    public ProfileModel getCurrentProfile() {
        return currentProfile;
    }

    /**
     * Sets the currently selected profile. Afterwards it notifies its observers
     * by calling their {@link ProfileManagerObserver#changedProfile()
     * changedProfile()} method.
     * 
     * @param name
     *            The name of the new profile.
     * @return false if profile with the given name doesn't exist.
     */
    public boolean setCurrentProfile(String name) {
        if (name == null) {
            throw new IllegalArgumentException("name cannot be null");
        }
        for (ProfileModel profile : profiles) {
            if (profile.getName().equals(name)) {
                currentProfile = profile;
                notify(o -> o.changedProfile());
                return true;
            }
        }
        return false;
    }

    /**
     * Changes the name of the current profile by replacing it with the same
     * profile only with a different name. References on the current profile
     * should be updated. Afterwards it notifies its observers by calling their
     * {@link ProfileManagerObserver#changedProfileList() changedProfileList()}
     * method.
     * 
     * @param newName
     *            The name of the new profile.
     * @return false if newName was already taken by a different profile.
     */
    public boolean changeCurrentName(String newName) {
        if (newName == null) {
            throw new IllegalArgumentException("newName cannot be null");
        }
        String name = newName.trim();
        if (name.equals("")) {
            throw new IllegalArgumentException("newName cannot empty");
        }
        for (ProfileModel profile : profiles) {
            if (profile.getName().equals(name)) {
                return currentProfile == profile;
            }
        }
        ProfileModel newProfile = new ProfileModel(name, currentProfile);
        profiles.add(profiles.indexOf(currentProfile), newProfile);
        save(name);
        delete(currentProfile.getName());
        currentProfile = newProfile;
        notify(o -> o.changedProfileList());
        return true;
    }

    /**
     * Returns a list of all currently taken profile names.
     * 
     * @return name List of all profile names
     */
    public List<String> getNames() {
        List<String> names = new ArrayList<String>();
        for (ProfileModel profile : profiles) {
            names.add(profile.getName());
        }
        return names;
    }

    /**
     * Creates a new profile with an empty string as name. Afterwards it
     * notifies its observers by calling their
     * {@link ProfileManagerObserver#changedProfileList() changedProfileList()}
     * method.
     * 
     * @return new profile or null if {@link #MAX_NUMBER_OF_PROFILES} was
     *         already reached or a profile with an empty name already exists.
     */
    public ProfileModel createProfile() {
        if (profiles.size() < MAX_NUMBER_OF_PROFILES) {
            for (String name : getNames()) {
                if (name.equals("")) {
                    return null;
                }
            }
            ProfileModel newProfile = new ProfileModel("");
            profiles.add(newProfile);
            notify(o -> o.changedProfileList());
            return newProfile;
        }
        return null;
    }

    /**
     * Saves the profile with the given name in the games profile folder.
     * 
     * @param name
     *            The name of the profile.
     */
    public void save(String name) {
        if (name == null) {
            throw new IllegalArgumentException("name cannot be null");
        }
        for (ProfileModel profile : profiles) {
            if (profile.getName().equals(name)) {
                if (!profile.getName().equals("")) {
                    ProfileSaveHelper.saveProfile(profile);
                }
            }
        }
    }

    /**
     * Deletes the profile with the given name from the games profile folder and
     * the ProfileManager. Afterwards it notifies its observers by calling their
     * {@link ProfileManagerObserver#changedProfileList() changedProfileList()}
     * method.
     * 
     * @param name
     *            The name of the profile that should be deleted.
     */
    public void delete(String name) {
        if (name == null) {
            throw new IllegalArgumentException("name cannot be null");
        }
        ProfileModel deadProfile = null;
        for (ProfileModel profile : profiles) {
            if (profile.getName().equals(name)) {
                deadProfile = profile;
            }
        }
        if (deadProfile != null) {
            if (currentProfile == deadProfile) {
                currentProfile = null;
            }
            profiles.remove(deadProfile);
            if (!deadProfile.getName().equals("")) {
                Gdx.files.local(PROFILE_FOLDER + "/" + deadProfile.getName())
                        .deleteDirectory();
            }
            notify(o -> o.changedProfileList());
        }
    }

    /**
     * Returns the ProfileEditModel-object that is used for the editing language
     * and avatars of the managers profiles.
     * 
     * @return ProfileEditModel The model behind the language and avatar edit.
     */
    public ProfileEditModel getProfileEdit() {
        return profileEdit;
    }

    private List<ProfileModel> loadProfiles(FileHandle profileFolder) {
        List<ProfileModel> profiles = new LinkedList<ProfileModel>();
        if (!profileFolder.exists()) {
            profileFolder.mkdirs();
        } else {
            if (!profileFolder.isDirectory()) {
                throw new InvalidProfilesException(profileFolder.name()
                        + " isn't a directory");
            }
            for (FileHandle file : profileFolder.list()) {
                ProfileModel profile = ProfileLoadHelper.loadProfile(file
                        .name());
                if (!file.name().equals(profile.getName())) {
                    throw new InvalidProfilesException(
                            "a profile's name and it's save folder's name aren't the same");
                }
                profiles.add(profile);
            }
        }
        return profiles;
    }

}
