package lambda.viewcontroller.level;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.graphics.Texture;

/**
 * @author: Kay Schmitteckert
 */
public class ParanthesisUIContext extends ElementUIContext  implements ApplicationListener { // TODO: ParEnthesis name

    /**
     * Number of columns in the animation-sheet
     */
    private static final int FRAME_COLS = 6;
    /**
     * Number of rows in the animation-sheet
     */
    private static final int FRAME_ROWS = 5;


    private Texture tFront;
    private Texture tCenter;
    private Texture tBack;

    public ParanthesisUIContext(Texture front, Texture center, Texture back) {
        tFront = front;
        tCenter = center;
        tBack = back;
    }

    
    public Texture getFront() { // TODO
        return tFront;
    }
    
    public Texture getCenter() { // TODO
        return tCenter;
    }
    
    public Texture getBack() { // TODO
        return tBack;
    }

    @Override
    public void create() {

    }

    @Override
    public void resize(int i, int i1) {

    }

    @Override
    public void render() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
    }
}
