/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package outputWriters;

/**
 *
 * @author josep
 */
public abstract class OutputDataWriter extends OutputWriter {
    
    // Simple outputs
    protected abstract void outputVariableName();
    protected abstract void outputVariableData();
    protected abstract void outputVariableUnits();
    
    // Elaborated outputs
    protected abstract void outputVariableAttributes();
    protected abstract void outputExtendedVariableData();
    
}
