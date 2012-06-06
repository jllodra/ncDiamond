/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package outputWriters;

/**
 *
 * @author josep
 */
public abstract class OutputInfoWriter extends OutputWriter {

    protected abstract void outputFileInfo();
    
    protected abstract void outputGlobalAttributes();
    
    protected abstract void outputDimensions();
    
    protected abstract void outputVariables();
                
}
