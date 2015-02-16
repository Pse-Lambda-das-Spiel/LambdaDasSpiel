package lambda.viewcontroller.level;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import java.awt.Color;

/**
 * @author: Kay Schmitteckert
 */
public class AbstractionUIContext extends ElementUIContext implements ApplicationListener {
    
    private Texture tFront;
    /*
    private Animation aFront;
    private TextureRegion[] walkFrames;
    private SpriteBatch spriteBatch;
    private TextureRegion currentFrame;
    private float stateTime;
     */
    private Texture tCenter;
    private Texture tBack;

    public AbstractionUIContext(Texture front, Texture center, Texture back) {
        tFront = front;
        tCenter = center;
        tBack = back;
    }

    /**
     * Returns an animation relating to the sprite
     *
     * @return animation relating to the sprite
     */
    public Animation getAFront() { // TODO
        return null;
        //return aFront;
    }
    
    public Texture getCenter() { // TODO
        return tCenter;
    }
    
    public Texture getBack() { // TODO
        return tBack;
    }

    @Override
    public void create() {
        /*
        TextureRegion[][] tmp = TextureRegion.split(tFront, tFront.getWidth()/FRAME_COLS, tFront.getHeight()/FRAME_ROWS);              // #10
        walkFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
        int index = 0;
        for (int i = 0; i < FRAME_ROWS; i++) {
            for (int j = 0; j < FRAME_COLS; j++) {
                walkFrames[index++] = tmp[i][j];
            }
        }
        aFront = new Animation(0.025f, walkFrames);
        spriteBatch = new SpriteBatch();
        stateTime = 0f;
        */
    }

    @Override
    public void resize(int i, int i1) {

    }

    @Override
    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        /*
        stateTime += Gdx.graphics.getDeltaTime();
        currentFrame = aFront.getKeyFrame(stateTime, true);
        spriteBatch.begin();
        spriteBatch.draw(currentFrame, 50, 50);
        spriteBatch.end();
        */
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        //spriteBatch.dispose();
    }
}
