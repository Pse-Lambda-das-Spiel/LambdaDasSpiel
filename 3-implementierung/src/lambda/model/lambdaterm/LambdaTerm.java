package lambda.model.lambdaterm;

import java.util.function.Consumer;
import lambda.Observable;
import lambda.model.lambdaterm.visitor.LambdaTermVisitor;

/**
 * Represents a lambda term/ node in a lambda term tree.
 * 
 * @author Florian Fervers
 */
public abstract class LambdaTerm extends Observable<LambdaTermObserver> {
    /**
     * The parent node.
     */
    private LambdaTerm parent;
    /**
     * Indicates whether this node can be modified by the user.
     */
    private boolean locked;
    
    /**
     * Creates a new instance of the LambdaTerm class. Used for setting parameters by subclasses.
     * 
     * @param parent the parent node
     * @param locked true if this node can be modified by the user, false otherwise
     */
    public LambdaTerm(LambdaTerm parent, boolean locked) {
        this.parent = parent;
        this.locked = locked;
    }
    
    /**
     * Returns whether this lambda term is a value (abstraction or variable).
     * 
     * @return true if this lambda term is a value (abstraction or variable), false otherwise
     */
    public boolean isValue() {
        return false;
    }
    
    /**
     * Returns the parent node.
     * 
     * @return the parent node
     */
    public LambdaTerm getParent() {
        return parent;
    }
    
    /**
     * Sets the parent node.
     * 
     * @param parent the new parent node
     */
    public void setParent(LambdaTerm parent) {
        this.parent = parent;
    }
    
    /**
     * Returns whether this node can be modified by the user.
     * 
     * @return whether this node can be modified by the user
     */
    public boolean isLocked() {
        return locked;
    }
    
    /**
     * Sets whether this node can be modified by the user.
     * 
     * @param locked true if this node can be modified by the user, false otherwise
     */
    public void setLocked(boolean locked) {
        this.locked = locked;
    }
    
    /**
     * Accepts the given visitor by letting it visit this lambda term.
     * 
     * @param <T> the return type of the visit
     * @param visitor the visitor
     * @return the result of the visit
     */
    public abstract <T> T accept(LambdaTermVisitor<T> visitor);
    
    /**
     * Notifies all observers of the given message and then routes the message to the parent.
     * 
     * @param message the function to call on all consumers
     * @throws IllegalArgumentException if message is null
     */
    @Override
    public void notify(Consumer<LambdaTermObserver> message) {
        super.notify(message);
        if (parent != null) {
            parent.notify(message);
        }
    }
    
    /**
     * Returns whether this object is equal to the given object.
     * 
     * @param object the other object
     * @return true if this object is equal to the given object, false otherwise
     */
    @Override
    public abstract boolean equals(Object object);

    /**
     * Returns a hash code value for this object.
     * 
     * @return a hash code value for this object
     */
    @Override
    public abstract int hashCode();
}
