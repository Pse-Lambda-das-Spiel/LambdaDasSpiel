package lambda;

import com.badlogic.gdx.assets.AssetManager;

public class AssetModel extends AssetManager{

	private static AssetModel assets;
	
	private AssetModel() {
	}
	
	public static AssetModel getAssets() {
		if (assets == null) {
			assets = new AssetModel();
		}
		return assets;
	}
	
	public String getString(String id) {
		//TODO load the string with the specific id from the current I18N-bundle
		return "";
	}
}
