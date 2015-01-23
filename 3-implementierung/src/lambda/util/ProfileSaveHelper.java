package lambda.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import lambda.model.profiles.ProfileManager;
import lambda.model.profiles.ProfileModel;

public final class ProfileSaveHelper {

	private ProfileSaveHelper() {
	}
	
    public static void saveProfile(ProfileModel profile) {
        FileHandle a = Gdx.files.local(ProfileManager.PROFILE_FOLDER + "/" + profile.getName());
        if (!a.exists()) {
            a.mkdirs();
        }
    }

}
