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
public class CloudAnimation extends ViewController implements ApplicationListener {

    private Texture texture;
    private Animation animation;
    private TextureRegion[] walkFrames;
    private SpriteBatch spriteBatch;
    private TextureRegion currentFrame;
    private float stateTime;

    private AssetManager assets;
    private final String cloudAtlas = "data/animation/cloud/cloud.atlas";

    public CloudAnimation(Texture texture) {
        this.texture = texture;
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
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float v) {

    }

    @Override
    public void resize(int i, int i1) {

    }

    @Override
    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        stateTime += Gdx.graphics.getDeltaTime();
        currentFrame = animation.getKeyFrame(stateTime, true);
        spriteBatch.begin();
        spriteBatch.draw(currentFrame, 50, 50);
        spriteBatch.end();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        spriteBatch.dispose();
    }

    @Override
    public void queueAssets(AssetManager manager) {
        assets.load(cloudAtlas, TextureAtlas.class);
    }

    @Override
    public void create(AssetManager manager) {
        int i = 1;
        while (assets.containsAsset("cloud" + i)) {
            walkFrames[i++] = assets.get("cloud" + i, TextureRegion.class);
        }
        animation = new Animation(0.025f, walkFrames);
        spriteBatch = new SpriteBatch();
        stateTime = 0f;

    }
}
