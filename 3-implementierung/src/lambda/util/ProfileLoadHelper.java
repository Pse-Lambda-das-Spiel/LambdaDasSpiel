package lambda.util;

import java.io.File;

import lambda.model.profiles.ProfileManager;
import lambda.model.profiles.ProfileModel;

public final class ProfileLoadHelper {

	private ProfileLoadHelper() {
	}
	
    public static ProfileModel loadProfile(String name) {
        File a = new File(ProfileManager.PROFILE_FOLDER + "/" + name);
        if (a.exists()) {
            return new ProfileModel(name);
        }
        // TODO Auto-generated method stub
        return null;
    }

}
