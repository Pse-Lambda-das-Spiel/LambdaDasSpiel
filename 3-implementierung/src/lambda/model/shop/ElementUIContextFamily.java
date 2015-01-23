package lambda.model.shop;



/**
 *
 * Created by kay on 19.01.15.
 */
public class ElementUIContextFamily extends ShopItemModel {

    private SpriteModel paranthesis;
    private SpriteModel variable;
    private SpriteModel applicationFront;
    private SpriteModel applicationBack;
    private SpriteModel applicationCentre;

    public ElementUIContextFamily() {

    }

    /**
     * Returns the sprite of the paranthesis
     *
     * @return paranthesis
     */
    public SpriteModel getParanthesis() {
        return paranthesis;
    }

    /**
     * Returns the sprite of the variable
     *
     * @return variable
     */
    public SpriteModel getVariable() {
        return variable;
    }

    /**
     * Returns the sprite of the front of the application
     *
     * @return applicationFront
     */
    public SpriteModel getApplicationFront() {
        return applicationFront;
    }

    /**
     * Returns the sprite of the back of the application
     *
     * @return applicationBack
     */
    public SpriteModel getApplicationBack() {
        return applicationBack;
    }

    /**
     * Returns the sprite of the centre of the application
     *
     * @return applicationCentre
     */
    public SpriteModel getApplicationCentre() {
        return applicationCentre;
    }


}
