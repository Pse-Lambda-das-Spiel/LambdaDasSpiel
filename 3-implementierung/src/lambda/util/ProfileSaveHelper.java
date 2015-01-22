package lambda.util;

import java.io.File;

import lambda.model.profiles.ProfileManager;
import lambda.model.profiles.ProfileModel;

public final class ProfileSaveHelper {

	private ProfileSaveHelper() {
	}
	
    public static void saveProfile(ProfileModel profile) {
        File a = new File(ProfileManager.PROFILE_FOLDER + "/" + profile.getName());
        if (!a.exists()) {
            a.mkdir();
        }
    }

}
