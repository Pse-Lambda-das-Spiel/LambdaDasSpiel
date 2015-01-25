package lambda.viewcontroller.level;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * @author: Kay Schmitteckert
 */
public class VariableUIContext extends ElementUIContext {

    private Sprite sprite;
    private Animation animation;

    public VariableUIContext() {

    }

    /**
     * Returns an animation relating to the sprite
     *
     * @return animation relating to the sprite
     */
    public Animation getAnimation() {
        return animation;
    }

    /**
     * Returns the sprite
     *
     * @return the sprite
     */
    public Sprite getSprite() {
        return sprite;
    }

}
