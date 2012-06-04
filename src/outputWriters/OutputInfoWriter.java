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

    abstract void outputFileInfo();
    
    abstract void outputGlobalAttributes();
    
    abstract void outputDimensions();
    
    abstract void outputVariables();
                
}
