package lambda.model.lambdaterm;

import java.util.Objects;
import lambda.model.lambdaterm.visitor.IsValidVisitor;
import lambda.model.lambdaterm.visitor.LambdaTermVisitor;
import lambda.model.lambdaterm.visitor.ToStringVisitor;

/**
 * Represents a root object in a lambda term tree.
 * 
 * @author Florian Fervers
 */
public class LambdaRoot extends LambdaTerm {
    /**
     * The only child of this root.
     */
    private LambdaTerm child;
    
    /**
     * Creates a new lambda root.
     */
    public LambdaRoot() {
        super(null, true);
    }
    
    /**
     * Return this root's child.
     * 
     * @return this root's child
     */
    public LambdaTerm getChild() {
        return child;
    }
    
    /**
     * Sets the child node and notifies all observers of the change.
     * 
     * @param child the new child node
     */
    public void setChild(LambdaTerm child) {
        LambdaTerm oldChild = this.child;
        this.child = child;
        if (child != null) {
            child.setParent(this);
        }
        notify((observer) -> observer.replaceTerm(oldChild, child));
    }

    /**
     * Accepts the given visitor by letting it visit this lambda root. Returns null if the visitor is null.
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
        if (!(object instanceof LambdaRoot)) {
            return false;
        }
        LambdaRoot other = (LambdaRoot) object;
        return this.child.equals(other.child);
    }
    
    /**
     * Returns a hash code value for this object.
     * 
     * @return a hash code value for this object
     */
    @Override
    public int hashCode() {
        return getClass().hashCode() + Objects.hashCode(child);
    }
    
    /**
     * Returns a string representation of this object.
     * 
     * @return a string representation of this object.
     */
    @Override
    public String toString() {
        if (this.accept(new IsValidVisitor())) {
            return this.accept(new ToStringVisitor());
        } else {
            return "Invalid";
        }
    }
}
