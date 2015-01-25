package lambda.model.shop;



/**
 *
 * Created by kay on 19.01.15.
 */
public class ElementUIContextFamily extends ShopItemModel {

    private SpriteModel paranthesisUIContext;
    private SpriteModel variableUIContext;
    private SpriteModel abstractionUIContext;

    public ElementUIContextFamily() {

    }

    /**
     * Returns the sprite of the paranthesis
     *
     * @return paranthesis
     */
    public SpriteModel getParanthesis() {
        return paranthesisUIContext;
    }

    /**
     * Returns the sprite of the variable
     *
     * @return variable
     */
    public SpriteModel getVariable() {
        return variableUIContext;
    }

    /**
     * Returns the sprite of the front of the application
     *
     * @return applicationFront
     */
    public SpriteModel getApplication() {
        return abstractionUIContext;
    }

}
