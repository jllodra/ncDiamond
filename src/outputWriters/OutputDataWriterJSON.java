/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package outputWriters;

import exceptions.StandardException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import ucar.ma2.Array;
import ucar.nc2.Attribute;
import ucar.nc2.Variable;
import ucar.nc2.dataset.NetcdfDataset;

/**
 *
 * @author josep
 */
public class OutputDataWriterJSON extends OutputDataWriter {

    protected NetcdfDataset ncFile;
    //protected String variableNames;
    protected List<Variable> variables = new ArrayList<Variable>();
    protected List<Array> variableData = new ArrayList<Array>();
    protected PrintWriter out;

    public OutputDataWriterJSON(NetcdfDataset ncFile, String variableNames, PrintWriter out) throws StandardException, IOException {
        this.ncFile = ncFile;
        this.out = out;
        for(String variableName : variableNames.split("\\|")) {
            Variable variable = ncFile.findVariable(variableName);
            if(null != variable) {
                this.variables.add(variable);
                this.variableData.add(variable.read());
            } else {
                throw new StandardException("Variable not found: " + variableName);                
            }
        }
    }

    @Override
    public void outputStart() {
        out.print("{");
    }

    @Override
    public void outputVariableNames() {
        out.print("\"variable_names\":[");
        for(Iterator<Variable> i = this.variables.iterator(); i.hasNext();) {
            out.print("\"");
            out.print(i.next().getFullNameEscaped());
            out.print("\"");
            if(i.hasNext()) {
                out.print(",");
            }
        }
        out.print("]");
    }

    @Override
    public void outputVariableData() {
        // Should have some sort of memory usage limit, to be on the safe side...
        // TODO: Please, take care of DataType :)
        out.print("\"variable_data\":[");

        Boolean sameDimensions = true;
        String dimensions = null;
        for(int i = 0; i < this.variables.size() && sameDimensions; i++) {
            String newDimensions = this.variables.get(i).getDimensionsString();
            if(null != dimensions && !dimensions.equals(newDimensions)) {
                sameDimensions = false;
            }
            dimensions = newDimensions;
        }
        
        if(!sameDimensions) {
            out.print("\"Not same dimensions\""); // TODO: decide strategy            
        } else {
            for(int i = 0; i < this.variableData.get(0).getSize(); i++) {
                for(int j = 0; j < this.variableData.size(); j++) {
                    if(j == 0) {
                        out.print("[");
                    }
                    Object o = (Object) this.variableData.get(j).getObject(i);
                    out.print((o.toString().equals("NaN")) ? "null" : o);
                    if(j < this.variableData.size() - 1) {
                        out.print(",");
                    } else {
                        out.print("]");
                    }
                }
                if(i < this.variableData.get(0).getSize() - 1) {
                    out.print(",");
                }
            }
        }
                
        out.print("]");
    }

    @Override
    public void outputVariableUnits() {
        out.print("\"variable_units\":[");
        for(Iterator<Variable> i = this.variables.iterator(); i.hasNext();) {
            out.print("\"");
            out.print(i.next().getUnitsString());
            out.print("\"");
            if(i.hasNext()) {
                out.print(",");
            }
        }
        out.print("]");
    }

    @Override
    public void outputVariableAttributes() {
        out.print("\"attributes\":{");
/*        List<Attribute> attributes = variable.getAttributes();
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
        }*/
        out.print("}");
    }

    @Override
    public void outputExtendedVariableData() {
        // Should have some sort of memory usage limit, to be on the safe side...
        // TO-DO
        out.print("[");
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
