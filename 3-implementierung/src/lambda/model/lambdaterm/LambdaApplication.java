package lambda.model.lambdaterm;

import java.util.Objects;
import lambda.model.lambdaterm.visitor.LambdaTermVisitor;

/**
 * Represents an application in a lambda term tree.
 * 
 * @author Florian Fervers
 */
public class LambdaApplication extends LambdaTerm {
    /**
     * The left child node.
     */
    private LambdaTerm left;
    /**
     * The right child node.
     */
    private LambdaTerm right;
    
    /**
     * Creates a new lambda application.
     * 
     * @param parent the parent node
     * @param locked true if this node can be modified by the user, false otherwise
     */
    public LambdaApplication(LambdaTerm parent, boolean locked) {
        super(parent, locked);
    }
    
    /**
     * Returns the left child node.
     * 
     * @return the left child node
     */
    public LambdaTerm getLeft() {
        return left;
    }
    
    /**
     * Sets the left child node and notifies all observers of the change.
     * 
     * @param left the new left child node
     */
    public void setLeft(LambdaTerm left) {
        notify((observer) -> observer.replaceTerm(this.left, left));
        this.left = left;
        if (left != null) {
            left.setParent(this);
        }
    }
    
    /**
     * Returns the right child node.
     * 
     * @return the right child node
     */
    public LambdaTerm getRight() {
        return right;
    }
    
    /**
     * Sets the right child node and notifies all observers of the change.
     * 
     * @param right the new right child node
     */
    public void setRight(LambdaTerm right) {
        notify((observer) -> observer.replaceTerm(this.right, right));
        this.right = right;
        if (right != null) {
            right.setParent(this);
        }
    }

    /**
     * Accepts the given visitor by letting it visit this lambda application. Returns null if the visitor is null.
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
        if (!(object instanceof LambdaApplication)) {
            return false;
        }
        LambdaApplication other = (LambdaApplication) object;
        return this.left.equals(other.left) && this.right.equals(other.right);
    }
    
    /**
     * Returns a hash code value for this object.
     * 
     * @return a hash code value for this object
     */
    @Override
    public int hashCode() {
        return getClass().hashCode() + Objects.hashCode(left) + Objects.hashCode(right);
    }
}
