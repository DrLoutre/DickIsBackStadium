package exceptions;

/**
 * EmptyException is an Exception class aimed to signal that an object or entity
 * have not been found
 */
public class NotFoundException extends Exception {
    /**
     * @post Makes this be a new NotFoundException
     */
    public NotFoundException() {
        super();
    }
    
    /**
     * @post Makes this be a new NotFoundException with the given message
     * @param receivedMessage : The exception message
     */
    public NotFoundException(String receivedMessage) {
        super(receivedMessage);
    }
    
    /**
     * @post Makes this be a new NotFoundException with the given message 
     *          and exception
     * @param receivedMessage : The exception message
     * @param baseException : The base exception
     */
    public NotFoundException(String receivedMessage, Throwable baseException) {
        super(receivedMessage, baseException);
    }
}
