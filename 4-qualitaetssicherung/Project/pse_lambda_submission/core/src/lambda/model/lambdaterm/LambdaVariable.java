package lambda.model.lambdaterm;

import lambda.model.lambdaterm.visitor.LambdaTermVisitor;

import com.badlogic.gdx.graphics.Color;

/**
 * Represents a variable in a lambda term tree.
 *
 * @author Florian Fervers
 */
public class LambdaVariable extends LambdaValue {
    /**
     * Creates a new lambda variable.
     *
     * @param parent
     *            the parent node
     * @param rgbColor
     *            the color of this variable
     * @param locked
     *            true if this node can be modified by the user, false otherwise
     * @throws IllegalArgumentException
     *             if color is null
     */
    public LambdaVariable(LambdaTerm parent, Color rgbColor, boolean locked) {
        super(parent, rgbColor, locked);
    }

    /**
     * Accepts the given visitor by letting it visit this lambda variable.
     * Returns null if the visitor is null.
     *
     * @param <T>
     *            the return type of the visit
     * @param visitor
     *            the visitor
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
     * @param object
     *            the other object
     * @return true if this object is equal to the given object, false otherwise
     */
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof LambdaVariable)) {
            return false;
        }
        LambdaVariable other = (LambdaVariable) object;
        return this.getColor().equals(other.getColor());
    }

    /**
     * Returns a hash code value for this object.
     *
     * @return a hash code value for this object
     */
    @Override
    public int hashCode() {
        return getClass().hashCode() + getColor().hashCode();
    }

}
