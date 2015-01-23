package lambda.util;

import com.badlogic.gdx.Gdx;

import lambda.model.profiles.ProfileManager;
import lambda.model.profiles.ProfileModel;

public final class ProfileLoadHelper {

	private ProfileLoadHelper() {
	}
	
    public static ProfileModel loadProfile(String name) {
        if (Gdx.files.local(ProfileManager.PROFILE_FOLDER + "/" + name).exists()) {
            return new ProfileModel(name);
        }
        // TODO Auto-generated method stub
        return null;
    }

}
