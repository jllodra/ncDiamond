/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package outputWriters;

import java.io.PrintWriter;
import java.util.List;
import ucar.ma2.Range;
import ucar.nc2.Attribute;
import ucar.nc2.Dimension;
import ucar.nc2.Variable;
import ucar.nc2.dataset.NetcdfDataset;

/**
 *
 * @author josep
 */
public class OutputInfoWriterJSON extends OutputInfoWriter {

    protected NetcdfDataset ncFile;
    protected PrintWriter out;
    
    public OutputInfoWriterJSON(NetcdfDataset ncFile, PrintWriter out) {
        this.ncFile = ncFile;
        this.out = out;
    }

    @Override
    public void outputStart() {
        out.print("{");
    }

    @Override
    public void outputFileInfo() {
        out.print("\"source\":\"");
        out.print(this.ncFile.getFileTypeDescription());
        out.print("\",");
        out.print("\"version\":\"");
        out.print(this.ncFile.getFileTypeVersion());
        out.print("\",");
        out.print("\"uri\":\"");
        out.print(this.ncFile.getLocation());
        out.print("\"");
    }

    @Override
    public void outputGlobalAttributes() {
        int i;
        int size;

        out.print("\"global_attributes\":{\n");
        List<Attribute> globalAttributes = ncFile.getGlobalAttributes();
        i = 0;
        size = globalAttributes.size();
        for (Attribute a : globalAttributes) {
            out.print("\"");
            out.print(a.getName());
            out.print("\":\"");
            String s = (a.getStringValue() != null ? a.getStringValue().replaceAll("[\\r\\n]", " ") : "");
            out.print(s);
            out.print("\"");
            if (i < size - 1) {
                out.print(",");
            }
            i++;
        }
        out.print("}");
    }

    @Override
    public void outputDimensions() {
        int i;
        int size;
        
        out.print("\"dimensions\":{");
        List<Dimension> dimensions = this.ncFile.getDimensions();
        i = 0;
        size = dimensions.size();
        // TODO: Should we output the group? :)
        for (Dimension d : dimensions) {
            out.print("\"");
            out.print(d.getName());
            out.print("\":\"");
            out.print(d.getLength()); // What happens when it is unlimited?
            out.print("\"");
            if (i < size - 1) {
                out.print(",");
            }
            i++;
        }
        out.print("}");
    }

    @Override
    public void outputVariables() {
        int i, j;
        int size, sizej;

        out.print("\"variables\":{");

        List<Variable> variables = this.ncFile.getVariables();
        i = 0;
        size = variables.size();
        for (Variable v : variables) {
            out.print("\"");
            out.print(v.getFullNameEscaped());
            out.print("\":{");
            out.print("\"type\":\"");
            out.print(v.getDataType().toString());
            out.print("\",");
            out.print("\"description\":\"");
            out.print(v.getDescription());
            out.print("\",");
            out.print("\"units\":\"");
            out.print(v.getUnitsString());
            out.print("\",");
            out.print("\"dimensions\":\"");
            out.print(v.getDimensionsString()); // I know... a string is not very useful
            out.print("\",");
            out.print("\"rank\":");
            out.print(v.getRank());
            out.print(",");
            out.print("\"shape\":[");
            j = 0;
            sizej = v.getShapeAll().length;
            for (int s : v.getShapeAll()) {
                out.print(s);
                if (j < sizej - 1) {
                    out.print(",");
                }
                j++;
            }
            out.print("],");
            out.print("\"range\":{");
            j = 0;
            List<Range> ranges = v.getRanges();
            sizej = ranges.size();
            for (Range r : ranges) {
                out.print("\"first\":");
                out.print(r.first());
                out.print(",");
                out.print("\"stride\":");
                out.print(r.stride());
                out.print(",");
                out.print("\"last\":");
                out.print(r.last());
                out.print(",");
                out.print("\"string\":\"");
                out.print(r.toString());
                out.print("\"");
                if (j < sizej - 1) {
                    out.print(",");
                }
                j++;
            }
            out.print("},");
            out.print("\"attributes\":{");
            List<Attribute> attributes = v.getAttributes();
            j = 0;
            sizej = attributes.size();
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
                    if("NaN".equals(s)) {
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

            out.print("}");
            if (i < size - 1) {
                out.print(",");
            }
            i++;
        }
        out.print("}");
    }
    
    @Override
    public void outputSeparator() {
        out.println(",");
    }

    @Override
    public void outputEnd() {
        out.print("\"time elapsed\":\"");
        out.print(this.getTotalTime());
        out.print("ms\",");
        out.print("\"memory used\":\"");
        out.print(this.getTotalMemory());
        out.print(" bytes\"");
        out.print("}");
    }
    
}
