package lambda.viewcontroller.level;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * @author: Kay Schmitteckert
 */
public class VariableUIContext extends ElementUIContext implements
        ApplicationListener {

    private TextureRegion tVariable;
    private TextureRegion mVariable;

    public VariableUIContext(TextureRegion variable, TextureRegion mask) {
        tVariable = variable;
        mVariable = mask;
    }

    public TextureRegion getTexture() {
        return tVariable;
    }

    public TextureRegion getMask() {
        return mVariable;
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
        // spriteBatch.dispose();
    }
}
