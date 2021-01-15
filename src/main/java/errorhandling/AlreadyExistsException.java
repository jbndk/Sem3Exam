package errorhandling;

/**
 *
 * @author claes
 */
public class AlreadyExistsException extends Exception {
    
    public AlreadyExistsException(String message) {
        super(message);
    }

    public AlreadyExistsException() {
        super("Requested item already exists");
    }
    
}
