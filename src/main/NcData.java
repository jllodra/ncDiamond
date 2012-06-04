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
public class NcData extends NcAbstractServlet {

    @Override
    protected void render() throws StandardException, IOException {
        out.print("NcData servlet.");
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected void debug() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
