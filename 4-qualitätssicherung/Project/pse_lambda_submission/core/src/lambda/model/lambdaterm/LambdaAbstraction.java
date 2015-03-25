package lambda.model.lambdaterm;

import java.util.Objects;

import com.badlogic.gdx.graphics.Color;

import lambda.Consumer;
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
     * @param rgbColor the color of the variable bound by this abstraction
     * @param locked true if this node can be modified by the user, false otherwise
     * @throws IllegalArgumentException if color is null
     */
    public LambdaAbstraction(LambdaTerm parent, Color rgbColor, boolean locked) {
        super(parent, rgbColor, locked);
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
     * @return true if the inside term has changed, false otherwise
     */
    public boolean setInside(final LambdaTerm inside) {
        final LambdaTerm oldInside = this.inside;
        this.inside = inside;
        if (inside != null) {
            inside.setParent(this);
        }
        if (oldInside != inside) {
            notify(new Consumer<LambdaTermObserver>(){
                @Override
                public void accept(LambdaTermObserver observer) {
                    observer.replaceTerm(oldInside, inside);
                }
            });
        }
        return oldInside != inside;
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
            return (T) visitor.getResult();
        } 
        return null;
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
        return getClass().hashCode() + Objects.hashCode(inside) + getColor().hashCode();
    }
}
