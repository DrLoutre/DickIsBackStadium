package exceptions;

/**
 * AlreadyExistException is an exception that occurs when a key already exists
 * in a dictonary.
 */
public class AlreadyExistException extends RuntimeException{
     /**
     * @post Makes this be a new AlreadyExistException
     */
    public AlreadyExistException() {
        super();
    }
    
    /**
     * @post Makes this be a new AlreadyExistException with the given message
     * @param receivedMessage : The exception message
     */
    public AlreadyExistException(String receivedMessage) {
        super(receivedMessage);
    }
    
    /**
     * @post Makes this be a new AlreadyExistException with the given message and 
     *          exception
     * @param receivedMessage : The exception message
     * @param baseException : The base exception
     */
    public AlreadyExistException(String receivedMessage, Throwable baseException) {
        super(receivedMessage, baseException);
    }
}
