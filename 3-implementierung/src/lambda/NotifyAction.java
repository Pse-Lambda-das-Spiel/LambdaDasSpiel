package lambda;

/**
 * Represents a notify message to an observer.
 * 
 * @param <Observer> the type of the observer
 * @author Florian Fervers
 */
public interface NotifyAction<Observer> {
    /**
     * Notifies the given observer.
     * 
     * @param observer the observer to be notified
     */
    public abstract void notify(Observer observer);
}
