package lambda.viewcontroller.level;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * This class represents the variable (the gem) and holds every asset which is
 * required to show the abstraction
 * 
 * @author: Kay Schmitteckert
 */
public class VariableUIContext extends ElementUIContext {

    private TextureRegion tVariable;
    private TextureRegion mVariable;

    /**
     * Creates a new instance of this class
     * 
     * @param variable
     *            the gem image
     * @param mask
     *            the mask of the gem
     */
    public VariableUIContext(TextureRegion variable, TextureRegion mask) {
        tVariable = variable;
        mVariable = mask;
    }

    /**
     * Returns the texture of the gem
     * 
     * @return the texture of the gem
     */
    public TextureRegion getTexture() {
        return tVariable;
    }

    /**
     * Returns the mask of the gem
     * 
     * @return the mask of the gem
     */
    public TextureRegion getMask() {
        return mVariable;
    }
}
