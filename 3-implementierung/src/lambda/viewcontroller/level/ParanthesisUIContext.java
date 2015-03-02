package lambda.viewcontroller.level;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * @author: Kay Schmitteckert
 */
public class ParanthesisUIContext extends ElementUIContext  implements ApplicationListener {
    
    private TextureRegion tFront;
    private TextureRegion tCenter;
    private TextureRegion tBack;
    private TextureRegion mFront;
    private TextureRegion mCenter;
    private TextureRegion mBack;

    public ParanthesisUIContext(TextureRegion front, TextureRegion center, TextureRegion back, 
            TextureRegion mFront, TextureRegion mCenter, TextureRegion mBack) {
        tFront = front;
        tCenter = center;
        tBack = back;
        this.setmFront(mFront);
        this.setmCenter(mCenter);
        this.setmBack(mBack);
    }

    
    public TextureRegion getFront() { // TODO
        return tFront;
    }
    
    public TextureRegion getCenter() { // TODO
        return tCenter;
    }
    
    public TextureRegion getBack() { // TODO
        return tBack;
    }

    public TextureRegion getmFront() {
        return mFront;
    }


    public void setmFront(TextureRegion mFront) {
        this.mFront = mFront;
    }


    public TextureRegion getmCenter() {
        return mCenter;
    }


    public void setmCenter(TextureRegion mCenter) {
        this.mCenter = mCenter;
    }


    public TextureRegion getmBack() {
        return mBack;
    }


    public void setmBack(TextureRegion mBack) {
        this.mBack = mBack;
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
