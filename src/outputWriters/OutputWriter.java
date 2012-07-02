/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package outputWriters;

/**
 *
 * @author josep
 */
public abstract class OutputWriter {
    
    private long millis;
    private long memory;
    
    public OutputWriter() {
        // Should gc first
        millis = System.currentTimeMillis();
        memory = Runtime.getRuntime().freeMemory();
    }
    
    protected long getTotalTime() {
        return System.currentTimeMillis() - millis;
    }
    
    protected long getTotalMemory() {
        return memory - Runtime.getRuntime().freeMemory();
    }
    
    abstract void outputStart();

    abstract void outputSeparator();
    
    abstract void outputEnd();
    
}
