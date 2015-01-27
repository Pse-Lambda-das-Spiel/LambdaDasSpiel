package lambda.viewcontroller.level;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * @author: Kay Schmitteckert
 */
public class ParanthesisUIContext extends ElementUIContext {

    private Animation animation;

    public ParanthesisUIContext() {

    }

    /**
     * Returns an animation relating to the sprite
     *
     * @return animation relating to the sprite
     */
    public Animation getAnimation() {
        return animation;
    }

}
