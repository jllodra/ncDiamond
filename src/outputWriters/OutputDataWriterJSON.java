/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package outputWriters;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import ucar.ma2.Array;
import ucar.nc2.Variable;
import ucar.nc2.dataset.NetcdfDataset;

/**
 *
 * @author josep
 */
public class OutputDataWriterJSON extends OutputDataWriter {

    protected NetcdfDataset ncFile;
    protected String variableName;
    protected Array variableData;
    private Integer errors = 0;
    protected PrintWriter out;

    public OutputDataWriterJSON(NetcdfDataset ncFile, String variableName, PrintWriter out) {
        this.ncFile = ncFile;
        this.variableName = NetcdfDataset.escapeName(variableName);
        this.out = out;
    }

    @Override
    public void outputStart() {
        out.print("{");
    }

    @Override
    public void outputAll() {
        /** Variable Name **/
        out.print("\"variable_name\":\"");
        Variable v = ncFile.findVariable(variableName);
        if(null != v) {
            out.print(v.getFullNameEscaped());
        } else {
            out.print("Variable not found");
            errors++;
        }
        out.print("\"");
        
        /** Separator **/
        outputSeparator();
        
        /** Variable Data **/
        // Should have some sort of limit for safe memory usage...
        out.print("\"variable_data\":[");
        try {
            variableData = v.read();
        } catch (IOException ex) {
            Logger.getLogger(OutputDataWriterJSON.class.getName()).log(Level.SEVERE, null, ex);
        }
        variableData.resetLocalIterator();
        boolean first = true;
        while(variableData.hasNext()) {
            if(!first) out.print(","); else first = false;
            Object o = variableData.next();
            out.print(o);
        }
        
        out.print("]");
    }

    @Override
    public void outputSeparator() {
        out.println(",");
    }

    @Override
    public void outputEnd() {
        out.print("\"time_elapsed\":\"");
        out.print(this.getTotalTime());
        out.print("ms\",");
        out.print("\"memory_used\":\"");
        out.print(this.getTotalMemory());
        out.print(" bytes\",");
        out.print("\"number_of_errors\":");
        out.print(errors);
        out.print("}");
    }
}
