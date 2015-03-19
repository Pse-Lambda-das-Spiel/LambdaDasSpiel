package lambda;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton.ImageTextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**
 * 
 * @author Kay Schmitteckert, Robert Hochweiss
 *
 */

public class StyleManager {
    
    private static StyleManager styleManager;
    private static Skin skin;
    
    /**
     * 
     */
    private StyleManager() {
        
    }
    
    /**
     * 
     * @return
     */
    public static StyleManager getStyleManager() {
        if (styleManager == null) {
            styleManager = new StyleManager();
        }
        return styleManager;
    }
    
    /**
     * 
     * @param assets
     */
    public void queueAssets(AssetManager assets) {
        
    }
    
    /**
     * 
     * @param icon
     * @return
     */
    public static TextButtonStyle getTextButtonStyle(String icon) {
        TextButtonStyle style = new TextButtonStyle(skin.get(TextButtonStyle.class));
        style.up = skin.getDrawable(icon);
        style.down = skin.getDrawable(icon);
        return style;
    }
    
    /**
     * 
     * @param icon
     * @return
     */
    public static ImageButtonStyle getImageButtonStyle(String icon) {
        ImageButtonStyle style = new ImageButtonStyle(skin.get(ImageButtonStyle.class));
        style.imageUp = skin.getDrawable(icon);
        return style;
    }
    
    /**
     * 
     * @param icon
     * @return
     */
    public static ImageTextButtonStyle getImageTextButtonStyle(String icon) {
        ImageTextButtonStyle style = new ImageTextButtonStyle(skin.get(ImageTextButtonStyle.class));
        style.imageUp = skin.getDrawable(icon);
        return style;
    }
}
