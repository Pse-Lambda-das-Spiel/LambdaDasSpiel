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
public class VariableUIContext extends ElementUIContext implements ApplicationListener {

    private Texture tVariable;
    /*
    private Animation aVariable;
    private TextureRegion[] walkFrames;
    private SpriteBatch spriteBatch;
    private TextureRegion currentFrame;
    private float stateTime;
    */

    public VariableUIContext(Texture variable) {
        tVariable = variable;
    }

    /**
     * Returns an animation relating to the sprite. TODO: smoke animation.
     *
     * @return animation relating to the sprite
     */
    public Animation getAVariable() {
        return null;
        //return aVariable;
    }
    
    public Texture getTexture() { // TODO
        return tVariable;
    }

    @Override
    public void create() {
        /*
        TextureRegion[][] tmp = TextureRegion.split(tVariable, tVariable.getWidth()/FRAME_COLS, tVariable.getHeight()/FRAME_ROWS);              // #10
        walkFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
        int index = 0;
        for (int i = 0; i < FRAME_ROWS; i++) {
            for (int j = 0; j < FRAME_COLS; j++) {
                walkFrames[index++] = tmp[i][j];
            }
        }
        aVariable = new Animation(0.025f, walkFrames);
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
        currentFrame = aVariable.getKeyFrame(stateTime, true);
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
