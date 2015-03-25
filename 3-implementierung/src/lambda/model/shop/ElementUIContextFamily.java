package lambda.model.shop;

import lambda.viewcontroller.level.AbstractionUIContext;
import lambda.viewcontroller.level.ParanthesisUIContext;
import lambda.viewcontroller.level.VariableUIContext;

/**
 * Represents a whole family of elements which are placed in the editor mode
 * 
 * @author Kay Schmitteckert
 */
public class ElementUIContextFamily extends ShopItemModel {

    private ParanthesisUIContext paranthesisUIContext;
    private VariableUIContext variableUIContext;
    private AbstractionUIContext abstractionUIContext;

    /**
     * Creates a new instance of this class
     * 
     * @param id
     *            the if of this item
     * @param price
     *            the price of this item
     */
    public ElementUIContextFamily(String id, int price, String filepath) {
        super(id, price, filepath);
        shopItemType = ShopModel.getShop().getElementUIContextFamilies();
    }

    /**
     * Returns the parenthesis element
     *
     * @return the context of the parenthesis
     */
    public ParanthesisUIContext getParenthesis() {
        return paranthesisUIContext;
    }

    /**
     * Returns the variable element
     *
     * @return the context of the variable
     */
    public VariableUIContext getVariable() {
        return variableUIContext;
    }

    /**
     * Returns the abstraction element
     *
     * @return the context of the abstraction
     */
    public AbstractionUIContext getAbstraction() {
        return abstractionUIContext;
    }

    /**
     * Sets the committed element as new parenthesis
     * 
     * @param the
     *            new parenthesis
     */
    public void setParanthesisUIContext(
            ParanthesisUIContext parenthesisUIContext) {
        this.paranthesisUIContext = parenthesisUIContext;
    }

    /**
     * Sets the committed element as new variable
     * 
     * @param the
     *            new variable
     */
    public void setVariableUIContext(VariableUIContext variableUIContext) {
        this.variableUIContext = variableUIContext;
    }

    /**
     * Sets the committed element as new abstraction
     * 
     * @param the
     *            new abstraction
     */
    public void setAbstractionUIContext(
            AbstractionUIContext abstractionUIContext) {
        this.abstractionUIContext = abstractionUIContext;
    }
}
