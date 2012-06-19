/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package outputWriters;

import java.io.IOException;

/**
 *
 * @author josep
 */
public abstract class OutputDataWriter extends OutputWriter {
    
    // Simple outputs
    protected abstract void outputVariableNames();
    protected abstract void outputVariableData();
    protected abstract void outputVariableUnits();
    
    // Elaborated outputs
    protected abstract void outputVariableAttributes();
    protected abstract void outputExtendedVariableData() throws IOException;
    
}
