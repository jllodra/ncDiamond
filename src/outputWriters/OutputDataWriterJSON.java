/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package outputWriters;

import exceptions.StandardException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import ucar.ma2.Array;
import ucar.nc2.Attribute;
import ucar.nc2.Dimension;
import ucar.nc2.Variable;
import ucar.nc2.dataset.NetcdfDataset;

/**
 *
 * @author josep
 */
public class OutputDataWriterJSON extends OutputDataWriter {

    protected NetcdfDataset ncFile;
    protected String variableName;
    protected Variable variable;
    protected Array variableData;
    protected PrintWriter out;

    public OutputDataWriterJSON(NetcdfDataset ncFile, String variableName, PrintWriter out) throws StandardException, IOException {
        this.ncFile = ncFile;
        this.variableName = NetcdfDataset.escapeName(variableName);
        this.out = out;
        this.variable = ncFile.findVariable(variableName);
        if (null == this.variable) {
            throw new StandardException("Variable not found.");
        }
        variableData = variable.read();
    }

    @Override
    public void outputStart() {
        out.print("{");
    }

    @Override
    public void outputVariableName() {
        out.print("\"variable_name\":\"");
        out.print(variable.getFullNameEscaped());
        out.print("\"");
    }

    @Override
    public void outputVariableData() {
        // Should have some sort of limit for safe memory usage...
        String pre, post;
        pre = post = (variable.getDataType().isString()) ? "\"" : "";
        
        out.print("\"variable_data\":[");
        variableData.resetLocalIterator();
        boolean first = true;
        while (variableData.hasNext()) {
            if (!first) {
                out.print(",");
            } else {
                first = false;
            }
            Object o = variableData.next();
            out.print(pre);
            out.print(o);
            out.print(post);
        }
        out.print("]");
    }

    @Override
    public void outputVariableUnits() {
        out.print("\"variable_units\":\"");
        out.print(variable.getUnitsString());
        out.print("\"");
    }

    @Override
    public void outputVariableAttributes() {
        out.print("\"attributes\":{");
        List<Attribute> attributes = variable.getAttributes();
        int j = 0;
        int sizej = attributes.size();
        for (Attribute a : attributes) {
            out.print("\"");
            out.print(a.getName());
            out.print("\":");
            if (a.isString()) {
                out.print("\"");
                out.print(a.getStringValue());
                out.print("\"");
            } else {
                String s = String.valueOf(a.getNumericValue());
                if ("NaN".equals(s)) {
                    s = "\"" + s + "\"";
                }
                out.print(s);
            }
            if (j < sizej - 1) {
                out.print(",");
            }
            j++;
        }
        out.print("}");
    }

    @Override
    public void outputExtendedVariableData() {
        // Should have some sort of limit for safe memory usage...
        // Foreach rank (dimension) get dimension and iterate over variable
        
        List<Dimension> dimensions = variable.getDimensions();
        out.print("\"number_of_dimensions\":" + dimensions.size() + ",");
        
        out.print("\"variable_data\":[");
        boolean first = true;
        variableData.resetLocalIterator();
        while (variableData.hasNext()) {
            if (!first) {
                out.print(",");
            } else {
                first = false;
            }
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
        out.print(" bytes\"");
        out.print("}");
    }
}
