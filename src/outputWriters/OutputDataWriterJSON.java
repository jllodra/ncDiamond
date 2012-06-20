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
import ucar.ma2.DataType;
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
    protected List<DataType> variableType = new ArrayList<DataType>();
    protected List<String> variableWrapping = new ArrayList<String>();
    protected PrintWriter out;

    public OutputDataWriterJSON(NetcdfDataset ncFile, String variableNames, PrintWriter out) throws StandardException, IOException {
        this.ncFile = ncFile;
        this.out = out;
        for (String variableName : variableNames.split("\\|")) {
            Variable variable = ncFile.findVariable(variableName);
            if (null != variable) {
                this.variables.add(variable);
                this.variableData.add(variable.read());
                this.variableType.add(variable.getDataType());
                this.variableWrapping.add((variable.getDataType().isString()) ? "\"" : "");
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
        for (Iterator<Variable> i = this.variables.iterator(); i.hasNext();) {
            out.print("\"");
            out.print(i.next().getFullNameEscaped());
            out.print("\"");
            if (i.hasNext()) {
                out.print(",");
            }
        }
        out.print("]");
    }

    @Override
    public void outputVariableData() {
        // Should have some sort of memory usage limit, to be on the safe side...
        out.print("\"variable_data\":[");

        Boolean sameDimensions = true;
        String dimensions = null;
        for (int i = 0; i < this.variables.size() && sameDimensions; i++) {
            String newDimensions = this.variables.get(i).getDimensionsString();
            if (null != dimensions && !dimensions.equals(newDimensions)) {
                sameDimensions = false;
            }
            dimensions = newDimensions;
        }

        if (sameDimensions) {
            // Varies does share dimensions
            this.outputVariableDataSharingDimensions();
        } else {
            // Variables does not share dimensions           
            this.outputVariableDataNotSharingDimensions();
        }

        out.print("]");
    }

    private void outputVariableDataSharingDimensions() {
        for (int i = 0; i < this.variableData.get(0).getSize(); i++) {
            for (int j = 0; j < this.variableData.size(); j++) {
                if (j == 0) {
                    out.print("[");
                }
                Object o = (Object) this.variableData.get(j).getObject(i);
                out.print(this.variableWrapping.get(j));
                out.print((o.toString().equals("NaN")) ? "null" : o);
                out.print(this.variableWrapping.get(j));
                if (j < this.variableData.size() - 1) {
                    out.print(",");
                } else {
                    out.print("]");
                }
            }
            if (i < this.variableData.get(0).getSize() - 1) {
                out.print(",");
            }
        }
    }

    private void outputVariableDataNotSharingDimensions() {
        for (int i = 0; i < this.variables.size(); i++) {
            out.print("[");
            for (int j = 0; j < this.variableData.get(i).getSize(); j++) {
                Object o = (Object) this.variableData.get(i).getObject(j);
                out.print(this.variableWrapping.get(i));
                out.print((o.toString().equals("NaN")) ? "null" : o);
                out.print(this.variableWrapping.get(i));
                if (j < this.variableData.get(i).getSize() - 1) {
                    out.print(",");
                }
            }
            out.print("]");
            if (i < this.variables.size() - 1) {
                out.print(",");
            }
        }
    }

    @Override
    public void outputVariableUnits() {
        out.print("\"variable_units\":[");
        for (Iterator<Variable> i = this.variables.iterator(); i.hasNext();) {
            out.print("\"");
            out.print(i.next().getUnitsString());
            out.print("\"");
            if (i.hasNext()) {
                out.print(",");
            }
        }
        out.print("]");
    }

    @Override
    public void outputVariableAttributes() {
        out.print("\"attributes\":[");
        for (int i = 0; i < this.variables.size(); i++) {
            out.print("{");
            List<Attribute> variableAttributes = this.variables.get(i).getAttributes();
            for (int j = 0; j < variableAttributes.size(); j++) {
                Attribute a = variableAttributes.get(j);
                out.print("\"");
                out.print(a.getName());
                out.print("\":");
                if (a.isString()) {
                    out.print("\"");
                    out.print(a.getStringValue());
                    out.print("\"");
                } else {
                    String s =
                            String.valueOf(a.getNumericValue());
                    if ("NaN".equals(s)) {
                        s = "\"" + s + "\"";
                    }
                    out.print(s);
                }
                if (j < variableAttributes.size() - 1) {
                    out.print(",");
                }
                j++;
            }
            out.print("}");
            if (i < this.variables.size() - 1) {
                out.print(",");
            }
        }
        out.print("]");
    }

    @Override
    public void outputExtendedVariableData() {
        // Should have some sort of memory usage limit, to be on the safe side...
        // TO-DO
        out.print("\"Not implemented\":[0");
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
