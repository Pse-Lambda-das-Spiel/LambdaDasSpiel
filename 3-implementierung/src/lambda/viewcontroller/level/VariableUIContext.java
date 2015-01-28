package lambda.viewcontroller.level;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * @author: Kay Schmitteckert
 */
public class VariableUIContext extends ElementUIContext {

    private Texture tVariable;
    private Animation aVariable;

    public VariableUIContext(Texture variable) {
        tVariable = variable;
    }

    /**
     * Returns an animation relating to the sprite
     *
     * @return animation relating to the sprite
     */
    public Animation getaVariable() {
        return aVariable;
    }

}
