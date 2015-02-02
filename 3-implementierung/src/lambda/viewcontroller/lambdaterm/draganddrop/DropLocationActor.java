package lambda.viewcontroller.lambdaterm.draganddrop;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * An actor that displays the target drop location for a drag&drop operation on a lambdaterm.
 * 
 * @author Florian Fervers
 */
public class DropLocationActor extends Actor {
    /**
     * Creates a new drop location actor in the given rectangle.
     * 
     * @param rectangle the target drop rectangle
     */
    public DropLocationActor(Rectangle rectangle) {
        this.setBounds(rectangle.getX(), rectangle.getY(), rectangle.getWidth(), rectangle.getHeight());
    }
    
    // TODO render
}
