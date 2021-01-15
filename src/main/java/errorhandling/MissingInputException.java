package errorhandling;

/**
 *
 * @author claes
 */
public class MissingInputException extends Exception {
    public MissingInputException (String message) {
        super(message);
    }
}
