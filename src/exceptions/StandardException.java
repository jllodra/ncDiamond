/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package exceptions;

/**
 *
 * @author josep
 */
public class StandardException extends Exception {
    
    public StandardException() {
        
    }
    
    public StandardException(String string) {
        super(string);
    }

    public StandardException(Throwable thrwbl) {
        super(thrwbl);
    }

    public StandardException(String string, Throwable thrwbl) {
        super(string, thrwbl);
    }
    
}
