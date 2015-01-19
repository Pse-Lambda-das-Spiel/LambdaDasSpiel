package lambda.model.profiles;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import lambda.Observable;
import lambda.util.ProfileLoadHelper;
import lambda.util.ProfileSaveHelper;

/**
 * The ProfileManager controls the games profiles. It contains a list of all user profiles
 * and can load and save them. It also creates, renames and deletes profiles and selects the games
 * current one.
 * 
 * @author Kai Fieger
 */
public class ProfileManager extends Observable<ProfileManagerObserver> {
    
    /**
     * The maximum number of allowed profiles.
     */
    public static final int MAX_NUMBER_OF_PROFILES = 6;
    private static final String PROFILE_FOLDER = "./profiles";
    private static ProfileManager manager;
    private final ProfileEditModel profileEdit;
    private ProfileModel currentProfile;
    private List<ProfileModel> profiles;

    private ProfileManager() {
        profileEdit = new ProfileEditModel();
        currentProfile = null;
        profiles = loadProfiles(new File(PROFILE_FOLDER));
    }

    /**
     * Returns an (/the only) instance of ProfileManager.
     * 
     * @return manager
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
     * @return currentProfile
     */
    public ProfileModel getCurrentProfile() {
        return currentProfile;
    }

    /**
     * Sets the currently selected profile.
     * Afterwards it notifies its observers by calling their
     * {@link ProfileManagerObserver#changedProfile() changedProfile()} method.
     * 
     * @param name of the new profile 
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
     * Changes the name of the current profile by replacing it with the same profile only with a different name. 
     * References on the current profile should be updated.
     * Afterwards it notifies its observers by calling their
     * {@link ProfileManagerObserver#changedProfileList() changedProfileList()} method.
     * 
     * @param newName of the profile
     * @return false if newName was already taken by a different profile
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
        save(name);
        profiles.add(profiles.indexOf(currentProfile), newProfile);
        delete(currentProfile.getName());
        currentProfile = newProfile;
        notify(o -> o.changedProfileList());
        return true;
    }

    /**
     * Returns an array of all currently taken profilenames.
     * 
     * @return name array
     */
    public String[] getNames() {
        String[] names = new String[profiles.size()];
        int i = 0;
        for (ProfileModel profile : profiles) {
            names[i++] = profile.getName();
        }
        return names;
    }

    /**
     * Creates a new profile with an empty string as name.
     * Afterwards it notifies its observers by calling their
     * {@link ProfileManagerObserver#changedProfileList() changedProfileList()} method.
     * 
     * @return new profile or null if {@link #MAX_NUMBER_OF_PROFILES} was already reached 
     *         or a profile with an empty name already exists.
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
            return newProfile;
        }
        return null;
    }

    /**
     * Saves the profile with the given name in the games profile folder.
     * 
     * @param name of the profile
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
     * Deletes the profile with the given name from the games profile folder and the ProfileManager.
     * Afterwards it notifies its observers by calling their
     * {@link ProfileManagerObserver#changedProfileList() changedProfileList()} method.
     * 
     * @param name
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
                deleteFolder(new File(PROFILE_FOLDER + "/" + deadProfile.getName()));
            }
            notify(o -> o.changedProfileList());
        }
    }

    /**
     * Returns the ProfileEditModel-object that is used for the editing 
     * (other things than name) of the managers profiles.
     * 
     * @return ProfileEditModel
     */
    public ProfileEditModel getProfileEdit() {
        return profileEdit;
    }

    private List<ProfileModel> loadProfiles(File profileFolder) {
        List<ProfileModel> profiles = new LinkedList<ProfileModel>();
        for (File file : profileFolder.listFiles()) {
            profiles.add(ProfileLoadHelper.loadProfile(file.getName()));
        }
        return profiles;
    }

    private void deleteFolder(File folder) {
        for (File file : folder.listFiles()) {
            if (file.isDirectory()) {
                deleteFolder(folder);
            }
            file.delete();
        }
        folder.delete();
    }

}
