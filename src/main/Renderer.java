/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import exceptions.StandardException;
import java.io.IOException;

/**
 *
 * @author josep
 */
public interface Renderer {
    
    public void render() throws StandardException, IOException;
    
}
