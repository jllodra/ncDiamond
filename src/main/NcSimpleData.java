/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import exceptions.StandardException;
import outputWriters.OutputDataWriterJSON;

/**
 *
 * @author josep
 */
public class NcSimpleData extends NcAbstractServlet {

    String variableName;

    @Override
    protected void gatherParameters() throws StandardException {
        super.gatherParameters();
        variableName = req.getParameter("var");
        if(null == variableName) {
            throw new StandardException("Please, a 'var' parameter is needed, example: "); // TODO: example
        }
    }
    
    @Override
    protected Renderer getJSONRenderer() {
        // Check if variable exist
        return new Renderer(){
            @Override
            public void render() {
                OutputDataWriterJSON outDataJSON = new OutputDataWriterJSON(ncFile, variableName, out);
                outDataJSON.outputStart();
                outDataJSON.outputAll();
                outDataJSON.outputSeparator();
                outDataJSON.outputEnd();
            }
        };
    }

    @Override
    protected Renderer getJSONPRenderer() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected Renderer getNcMLRenderer() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected Renderer getCDLRenderer() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected void debug() {
        throw new UnsupportedOperationException("Not supported yet.");
    } 

}