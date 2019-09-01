/**
 * <h1>InvalidDataException - Comp2240A1</h1>
 *
 * Exception to be called when the data file is formatted incorrectly
 *
 * @author Cody Lewis (c3283349)
 * @version 1
 * @since 2019-08-17
 */

public class InvalidDataException extends Exception {
    static final long serialVersionUID = 1L;
    public InvalidDataException() {
        super("The format of the data file is invalid.");
    }
}
