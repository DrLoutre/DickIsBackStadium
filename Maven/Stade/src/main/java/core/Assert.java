/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import exceptions.FailureException;
import java.util.LinkedList;
import java.util.List;

/**
 * Assert is an util class to check assertions
 */
public class Assert {
    
    /**
     * This class cannot be instantiated
     */
    private Assert() {
        
    }
    
    /**
     * @param o : The object that will be checked
     * @post Throws a FailureException if o is null 
     */
    public static void notNull(Object o) {
        if(o==null) {
            throw new FailureException(String.format(
                    "Assertion broken. %s should never be null", o));
        }
    }
    
    /**
     * @param b : The boolean that will be checked
     * @post Throws a FailureException if b is false
     */
    public static void isTrue(boolean b) {
        if(!b) {
            throw new FailureException(
                    "Assertion broken. Should never be false");
        }
    }
    
    /**
     * @param a : The first object to verify
     * @param b : The second object to verify
     * @post Throws a FailureException if a is not equals to b 
     */
    public static void equals(Object a, Object b) {
        if(a!=null && !a.equals(b)) {
            throw new FailureException(
                    "Assertion broken. %s and %s shoud be equals");
        }
    }
    
    /**
     * @pre o is not null
     * @param o : The iterable object that will be checked
     * @post Throws a FailureException if an object in o is null 
     */
    public static void noNullElements(Iterable o) {
        notNull(o);
        for (Object object : o) {
            notNull(object);
        }
    }
    
    /**
     * @pre o is not null
     * @param o : The iterable object that will be checked
     * @post Throws a FailureException if an object in o is null 
     */
    public static void noDuplicates(Iterable o) {
        notNull(o);
        List<Object> knownObjects = new LinkedList();
        
        for (Object object : o) {
            notNull(object);
            if(knownObjects.contains(object)){
                throw new FailureException(
                    "Assertion broken. This iterable should not contains"+
                            " duplicates");
            } else {
                knownObjects.add(object);
            }
        }
    }
}
