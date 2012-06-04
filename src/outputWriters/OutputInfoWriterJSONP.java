/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package outputWriters;

import java.io.PrintWriter;
import ucar.nc2.NetcdfFile;

/**
 *
 * @author josep
 */
public final class OutputInfoWriterJSONP extends OutputInfoWriterJSON {
    
    private String callback;
    
    public OutputInfoWriterJSONP(NetcdfFile ncFile, PrintWriter out, String callback) {
        super(ncFile, out);
        this.callback = callback;
    }

    @Override
    public void outputStart() {
        out.print(callback + "(");
        super.outputStart();
    }
    
    @Override
    public void outputEnd() {
        super.outputEnd();
        out.print(");");
    }
    
}
