package lambda.viewcontroller.level;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * This class represents the parenthesis (the lamb without magic stick) and
 * holds every asset which is required to show the parenthesis
 * 
 * @author: Kay Schmitteckert
 */
public class ParanthesisUIContext extends ElementUIContext {

    private TextureRegion tFront;
    private TextureRegion tCenter;
    private TextureRegion tBack;
    private TextureRegion mFront;
    private TextureRegion mCenter;
    private TextureRegion mBack;

    /**
     * Creates a new instance of this class
     *
     * @param front
     *            the front image of the parenthesis
     * @param center
     *            the center image of the parenthesis
     * @param back
     *            the back image of the parenthesis
     * @param mFront
     *            the front mask of the parenthesis
     * @param mCenter
     *            the center mask of the parenthesis
     * @param mBack
     *            the back mask of the parenthesis
     */
    public ParanthesisUIContext(TextureRegion front, TextureRegion center,
            TextureRegion back, TextureRegion mFront, TextureRegion mCenter,
            TextureRegion mBack) {
        tFront = front;
        tCenter = center;
        tBack = back;
        this.setmFront(mFront);
        this.setmCenter(mCenter);
        this.setmBack(mBack);
    }

    /**
     * Returns the front image
     * 
     * @return the front image
     */
    public TextureRegion getFront() {
        return tFront;
    }

    /**
     * Returns the center image
     * 
     * @return the center image
     */
    public TextureRegion getCenter() {
        return tCenter;
    }

    /**
     * Returns the back image
     * 
     * @return the back image
     */
    public TextureRegion getBack() {
        return tBack;
    }

    /**
     * Returns the mask of the front
     * 
     * @return the mask of the front
     */
    public TextureRegion getmFront() {
        return mFront;
    }

    /**
     * Sets the mask of the front
     * 
     * @param mFront
     *            mask of the front
     */
    public void setmFront(TextureRegion mFront) {
        this.mFront = mFront;
    }

    /**
     * Returns the mask of the center
     * 
     * @return the mask of the center
     */
    public TextureRegion getmCenter() {
        return mCenter;
    }

    /**
     * Sets the mask of the center
     * 
     * @return mCenter the mask of the center
     */
    public void setmCenter(TextureRegion mCenter) {
        this.mCenter = mCenter;
    }

    /**
     * Returns the mask of the back
     * 
     * @return the mask of the back
     */
    public TextureRegion getmBack() {
        return mBack;
    }

    /**
     * Sets the mask of the back
     * 
     * @param mBack
     *            the mask of the back
     */
    public void setmBack(TextureRegion mBack) {
        this.mBack = mBack;
    }
}
