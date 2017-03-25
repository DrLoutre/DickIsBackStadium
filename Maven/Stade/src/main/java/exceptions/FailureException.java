/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exceptions;

/**
 * FailureException is an Exception class aimed to signal an Exception that 
 * should never be thrown
 * 
 * @author Dwade
 */
public class FailureException extends RuntimeException {

    /**
     * @post Makes this be a new FailureException
     */
    public FailureException() {
        super();
    }
    
    /**
     * @post Makes this be a new FailureException with the given message
     * @param receivedMessage : The exception message
     */
    public FailureException(String receivedMessage) {
        super(receivedMessage);
    }
    
    /**
     * @post Makes this be a new FailureException with the given message and 
     *          exception
     * @param receivedMessage : The exception message
     * @param baseException : The base exception
     */
    public FailureException(String receivedMessage, Throwable baseException) {
        super(receivedMessage, baseException);
    }
    
}