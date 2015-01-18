package lambda.model.lambdaterm;

import java.awt.Color;
import java.util.Objects;
import lambda.model.lambdaterm.visitor.LambdaTermVisitor;

/**
 * Represents an abstraction in a lambda term tree.
 * 
 * @author Florian Fervers
 */
public class LambdaAbstraction extends LambdaValue {
    /**
     * The term inside this abstraction.
     */
    private LambdaTerm inside;
    
    /**
     * Creates a new lambda abstraction.
     * 
     * @param parent the parent node
     * @param color the color of the variable bound by this abstraction
     * @param locked true if this node can be modified by the user, false otherwise
     * @throws IllegalArgumentException if color is null
     */
    public LambdaAbstraction(LambdaTerm parent, Color color, boolean locked) {
        super(parent, color, locked);
    }

    /**
     * Returns the child node.
     * 
     * @return the child node
     */
    public LambdaTerm getInside() {
        return inside;
    }
    
    /**
     * Sets the child node and notifies all observers of the change.
     * 
     * @param inside the new child node
     */
    public void setInside(LambdaTerm inside) {
        notify((observer) -> observer.replaceTerm(this.inside, inside));
        this.inside = inside;
    }
    
    /**
     * Accepts the given visitor by letting it visit this lambda abstraction. Returns null if the visitor is null.
     * 
     * @param <T> the return type of the visit
     * @param visitor the visitor
     * @return the result of the visit
     */
    @Override
    public <T> T accept(LambdaTermVisitor<T> visitor) {
        if (visitor != null) {
            visitor.visit(this);
            return visitor.getResult();
        } else {
            return null;
        }
    }
    
    /**
     * Returns whether this object is equal to the given object.
     * 
     * @param object the other object
     * @return true if this object is equal to the given object, false otherwise
     */
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof LambdaAbstraction)) {
            return false;
        }
        LambdaAbstraction other = (LambdaAbstraction) object;
        return this.inside.equals(other.inside);
    }
    
    /**
     * Returns a hash code value for this object.
     * 
     * @return a hash code value for this object
     */
    @Override
    public int hashCode() {
        return 334 * getClass().hashCode() + 38 * Objects.hashCode(inside) + 458 * getColor().hashCode();
    }
}
