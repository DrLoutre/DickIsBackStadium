package exceptions;

/**
 * IntegrityException is an Exception class aimed to signal an integrity problem
 */
public class IntegrityException extends RuntimeException {
    /**
     * @post Makes this be a new IntegrityException
     */
    public IntegrityException() {
        super();
    }
    
    /**
     * @post Makes this be a new IntegrityException with the given message
     * @param receivedMessage : The exception message
     */
    public IntegrityException(String receivedMessage) {
        super(receivedMessage);
    }
    
    /**
     * @post Makes this be a new IntegrityException with the given message 
     *          and exception
     * @param receivedMessage : The exception message
     * @param baseException : The base exception
     */
    public IntegrityException(String receivedMessage, Throwable baseException) {
        super(receivedMessage, baseException);
    }
}
