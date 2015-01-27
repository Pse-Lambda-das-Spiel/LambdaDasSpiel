package lambda.model.shop;


import com.badlogic.gdx.graphics.g2d.Sprite;
import lambda.viewcontroller.level.AbstractionUIContext;
import lambda.viewcontroller.level.ParanthesisUIContext;
import lambda.viewcontroller.level.VariableUIContext;

/**
 *
 * Created by kay on 19.01.15.
 */
public class ElementUIContextFamily extends ShopItemModel {

    private ParanthesisUIContext paranthesisUIContext;
    private VariableUIContext variableUIContext;
    private AbstractionUIContext abstractionUIContext;

    private String paranthesisPath;
    private String variablePath;
    private String abstractionPath;

    /**
     * 
     * @param id
     * @param price
     */
    public ElementUIContextFamily(String id, int price, String pPath, String vPath, String aPath) {
        super(id, price, null);
        paranthesisPath = pPath;
        variablePath = vPath;
        abstractionPath = aPath;
    }
    
    /**
     * Returns the sprite of the paranthesis
     *
     * @return paranthesis
     */
    public ParanthesisUIContext getParanthesis() {
        return paranthesisUIContext;
    }

    /**
     * Returns the sprite of the variable
     *
     * @return variable
     */
    public VariableUIContext getVariable() {
        return variableUIContext;
    }

    /**
     * Returns the sprite of the front of the application
     *
     * @return applicationFront
     */
    public AbstractionUIContext getApplication() {
        return abstractionUIContext;
    }

    public void setParanthesisUIContext(ParanthesisUIContext paranthesisUIContext) {
        this.paranthesisUIContext = paranthesisUIContext;
    }

    public void setVariableUIContext(VariableUIContext variableUIContext) {
        this.variableUIContext = variableUIContext;
    }

    public void setAbstractionUIContext(AbstractionUIContext abstractionUIContext) {
        this.abstractionUIContext = abstractionUIContext;
    }

    public String getParanthesisPath() {
        return paranthesisPath;
    }

    public String getVariablePath() {
        return variablePath;
    }

    public String getAbstractionPath() {
        return abstractionPath;
    }

}
