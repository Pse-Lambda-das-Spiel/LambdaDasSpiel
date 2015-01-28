package lambda.viewcontroller.level;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * @author: Kay Schmitteckert
 */
public class ParanthesisUIContext extends ElementUIContext {

    private Texture tFront;
    private Animation aFront;
    private Texture tCenter;
    private Animation aCenter;
    private Texture tBack;
    private Animation aBack;

    public ParanthesisUIContext(Texture front, Texture center, Texture back) {
        tFront = front;
        tCenter = center;
        tBack = back;
    }

    /**
     * Returns an animation relating to the sprite
     *
     * @return animation relating to the sprite
     */
    public Animation getAFront() {
        return aFront;
    }

}
