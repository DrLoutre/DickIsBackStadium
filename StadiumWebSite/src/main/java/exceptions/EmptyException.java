package exceptions;

/**
 * EmptyException is an Exception class aimed to signal an empty list
 */
public class EmptyException extends Exception {
    /**
     * @post Makes this be a new EmptyException
     */
    public EmptyException() {
        super();
    }
    
    /**
     * @post Makes this be a new EmptyException with the given message
     * @param receivedMessage : The exception message
     */
    public EmptyException(String receivedMessage) {
        super(receivedMessage);
    }
}
