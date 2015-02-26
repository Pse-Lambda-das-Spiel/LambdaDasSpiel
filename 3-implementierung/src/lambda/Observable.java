package lambda;

import java.util.LinkedList;
import java.util.List;
import lambda.NotifyAction;

/**
 * Represents an object in the observer pattern that can be observed by multiple observers.
 * 
 * @param <Observer> the type of the observer
 * @author Florian Fervers
 */
public class Observable<Observer> {
    /**
     * The list of observers of this object.
     */
    private final List<Observer> observers;
    
    /**
     * Creates a new observable object without any observers.
     */
    public Observable() {
        observers = new LinkedList<>();
    }
    
    /**
     * Adds an observer to this observable if it isn't already observing this object.
     * 
     * @param observer the new observer
     * @throws IllegalArgumentException if observer is null
     */
    public void addObserver(Observer observer) {
        if (observer == null) {
            throw new IllegalArgumentException("Observer cannot be null!");
        }
        // Add if not already present
        boolean present = false;
        for (Observer observer2 : observers) {
            if (observer == observer2) {
                present = true;
                break;
            }
        }
        if (!present) {
            observers.add(observer);
        }
    }
    
    /**
     * Removes the given observer from this observable if it is currently observing this object.
     * 
     * @param observer the observer to be removed
     * @throws IllegalArgumentException if observer is null
     */
    public void removeObserver(Observer observer) {
        if (observer == null) {
            throw new IllegalArgumentException("Observer cannot be null!");
        }
        observers.remove(observer);
    }
    
    /**
     * Notifies all observers of a change by calling the given consumer on all observers.
     * 
     * @param message the function to call on all consumers
     * @throws IllegalArgumentException if message is null
     */
    public void notify(NotifyAction<Observer> message) {
        if (message == null) {
            throw new IllegalArgumentException("Message cannot be null!");
        }
        for (Observer observer : observers) {
            message.notify(observer);
        }
    }
}
