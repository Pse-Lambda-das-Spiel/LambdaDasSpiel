package lambda.viewcontroller.level;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import lambda.viewcontroller.ViewController;

/**
 * @author: Kay Schmitteckert
 */
public class MagicAnimation implements ApplicationListener {

    private Animation animation;
    private SpriteBatch spriteBatch;
    private TextureAtlas atlas;
    private float stateTime;

    private AssetManager assets;
    private final String magicAtlas = "data/animation/magic/Magic_Animation.atlas";

    public MagicAnimation() {
    }

    /**
     * Returns an animation relating to the sprite
     *
     * @return animation relating to the sprite
     */
    public Animation getAVariable() {
        return animation;
    }

    @Override
    public void create() {
        spriteBatch = new SpriteBatch();
        atlas = new TextureAtlas(Gdx.files.internal(magicAtlas));
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
        spriteBatch.draw(animation.getKeyFrame(stateTime, true), 300, 300);
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
        atlas.dispose();
    }
}
