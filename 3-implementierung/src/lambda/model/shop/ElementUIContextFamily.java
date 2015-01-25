package lambda.model.shop;


import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 *
 * Created by kay on 19.01.15.
 */
public class ElementUIContextFamily extends ShopItemModel {

    private Sprite paranthesisUIContext;
    private Sprite variableUIContext;
    private Sprite abstractionUIContext;

    public ElementUIContextFamily() {

    }

    /**
     * Returns the sprite of the paranthesis
     *
     * @return paranthesis
     */
    public Sprite getParanthesis() {
        return paranthesisUIContext;
    }

    /**
     * Returns the sprite of the variable
     *
     * @return variable
     */
    public Sprite getVariable() {
        return variableUIContext;
    }

    /**
     * Returns the sprite of the front of the application
     *
     * @return applicationFront
     */
    public Sprite getApplication() {
        return abstractionUIContext;
    }

}
