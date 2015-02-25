package lambda.viewcontroller.level;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

/**
 * @author: Kay Schmitteckert
 */
public class AnimationEffect implements ApplicationListener {

    private Animation animation;
    private SpriteBatch spriteBatch;
    private TextureAtlas atlas;
    private float stateTime;
    private int x;
    private int y;

    /**
     * Constructor of this class
     * 
     * @param atlas TextureAtlas which contains every single frame of the animation
     */
    public AnimationEffect(TextureAtlas atlas, int positionX, int positionY) {
        this.atlas = atlas;
        x = positionX;
        y = positionY;
    }

    @Override
    public void create() {
        spriteBatch = new SpriteBatch();
        animation = new Animation(1/10f, atlas.getRegions());
    }


    @Override
    public void resize(int i, int i1) {
    }

    @Override
    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        spriteBatch.begin();
        stateTime += Gdx.graphics.getDeltaTime();
        spriteBatch.draw(animation.getKeyFrame(stateTime, true), x, y);
        spriteBatch.end();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        spriteBatch.dispose();
    }
}
