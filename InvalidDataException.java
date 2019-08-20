public class InvalidDataException extends Exception {
    static final long serialVersionUID = 1L;
    public InvalidDataException() {
        super("The format of the data file is invalid.");
    }
}
