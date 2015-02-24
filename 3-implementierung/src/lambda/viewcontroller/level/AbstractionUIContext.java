package lambda.viewcontroller.level;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;


/**
 * @author: Kay Schmitteckert
 */
public class AbstractionUIContext extends ElementUIContext implements ApplicationListener {
    
    private Texture tFront;
    private Texture tCenter;
    private Texture tBack;
    private Texture mFront;
    private Texture mCenter;
    private Texture mBack;

    public AbstractionUIContext(Texture front, Texture center, Texture back, 
            Texture mFront, Texture mCenter, Texture mBack) {
        tFront = front;
        tCenter = center;
        tBack = back;
        this.mFront = mFront;
        this.mCenter = mCenter;
        this.mBack = mBack;
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
    public Texture getmFront() {
        return mFront;
    }


    public void setmFront(Texture mFront) {
        this.mFront = mFront;
    }


    public Texture getmCenter() {
        return mCenter;
    }


    public void setmCenter(Texture mCenter) {
        this.mCenter = mCenter;
    }


    public Texture getmBack() {
        return mBack;
    }


    public void setmBack(Texture mBack) {
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
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

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
