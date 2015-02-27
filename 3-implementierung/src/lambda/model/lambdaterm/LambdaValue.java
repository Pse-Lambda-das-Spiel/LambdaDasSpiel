package lambda.model.lambdaterm;

import java.awt.Color;

import lambda.Consumer;

/**
 * Represents a value (abstraction or variable) with a color in a lambda term tree.
 * 
 * @author Florian Fervers
 */
public abstract class LambdaValue extends LambdaTerm {
    /**
     * The color of this value.
     */
    private Color color;
    
    /**
     * Creates a new lambda value. Used for setting parameters by subclasses.
     * 
     * @param parent the parent node
     * @param color the color of this value
     * @param locked true if this node can be modified by the user, false otherwise
     * @throws IllegalArgumentException if color is null
     */
    public LambdaValue(LambdaTerm parent, Color color, boolean locked) {
        super(parent, locked);
        if (color == null) {
            throw new IllegalArgumentException("Color cannot be null!");
        }
        this.color = color;
    }
    
    /**
     * Returns the color of this value.
     * 
     * @return the color of this value
     */
    public Color getColor() {
        return color;
    }
    
    /**
     * Sets the color of this value and notifies all observers of the change.
     * 
     * @param color the new color
     * @return true if the color has changed, false otherwise
     * @throws IllegalArgumentException if color is null
     */
    public boolean setColor(final Color color) {
        if (color == null) {
            throw new IllegalArgumentException("Color cannot be null!");
        }
        Color oldColor = this.color;
        this.color = color;
        if (oldColor != color) {
            notify(new Consumer<LambdaTermObserver>(){
                @Override
                public void accept(LambdaTermObserver observer) {
                    observer.setColor(LambdaValue.this, color);
                }
            });
        }
        return oldColor != color;
    }
    
    /**
     * Returns whether this lambda term is a value (abstraction or variable).
     * 
     * @return whether this lambda term is a value (abstraction or variable).
     */
    @Override
    public boolean isValue() {
        return true;
    }
}
