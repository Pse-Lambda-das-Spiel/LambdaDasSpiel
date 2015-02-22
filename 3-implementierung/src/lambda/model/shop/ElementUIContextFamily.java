package lambda.model.shop;

import lambda.viewcontroller.level.AbstractionUIContext;
import lambda.viewcontroller.level.ParanthesisUIContext;
import lambda.viewcontroller.level.VariableUIContext;

/**
 * Created by kay on 19.01.15.
 * 
 * @author Kay Schmitteckert
 */
public class ElementUIContextFamily extends ShopItemModel {

    private ParanthesisUIContext paranthesisUIContext;
    private VariableUIContext variableUIContext;
    private AbstractionUIContext abstractionUIContext;

    /**
     * 
     * @param id
     * @param price
     */
    public ElementUIContextFamily(String id, int price, ParanthesisUIContext paranthesis, VariableUIContext variable,
                                  AbstractionUIContext abstraction) {
        super(id, price, null);
        shopItemType = ShopModel.getShop().getElementUIContextFamilies();
        paranthesisUIContext = paranthesis;
        variableUIContext = variable;
        abstractionUIContext = abstraction;
    }
    
    /**
     * Returns the sprite of the paranthesis
     *
     * @return paranthesis
     */
    public ParanthesisUIContext getParenthesis() {
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
    public AbstractionUIContext getAbstraction() {
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
}
