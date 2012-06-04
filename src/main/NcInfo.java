package main;

import exceptions.StandardException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import outputWriters.OutputInfoWriterJSON;
import outputWriters.OutputInfoWriterJSONP;
import ucar.nc2.NetcdfFile;
import ucar.nc2.dataset.NetcdfDataset;

/**
 *
 * @author josep
 */
public class NcInfo extends HttpServlet {

    HttpServletRequest req;
    HttpServletResponse res;
    ResourceBundle rb;
    // Parameters
    String source;
    String output = "application/json"; // default, must match with a content-type
    String callback = "callback"; // when output is jsonp
    // Netcdf resources
    NetcdfFile ncFile = null;
    PrintWriter out = null;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        req = request;
        res = response;
        debug();
        out = res.getWriter();
        try {
            gatherParameters();
            ncFile = NetcdfDataset.openFile(this.source, null);
            // null means task can not be cancelled
            this.render();
        } catch (StandardException e) {
            out.println(e.getMessage());
            e.printStackTrace(out);
        } finally {
            if(null != ncFile) {
                ncFile.close();
            }
        }
        out.flush();
        out.close();
    }

    private void gatherParameters() throws StandardException {
        source = req.getParameter("src");
        output = (req.getParameter("output") != null) ? req.getParameter("output") : output;
        callback = (req.getParameter("callback") != null) ? req.getParameter("callback") : callback;
        if (null == source) {
            throw new StandardException("Please, add parameter 'src', example: ");
        }
    }

    private void render() throws StandardException, IOException {
        rb = ResourceBundle.getBundle("LocalStrings", this.req.getLocale());
        res.setCharacterEncoding("UTF-8");
        if ("application/json".equals(output)) { // JSON
            res.setContentType(output + "; charset=utf-8");
            OutputInfoWriterJSON outInfoJSON = new OutputInfoWriterJSON(ncFile, out);
            renderJSON(outInfoJSON);
        } else if ("application/javascript".equals(output)) { // JSONP
            res.setContentType(output + "; charset=utf-8");
            OutputInfoWriterJSON outInfoJSON = new OutputInfoWriterJSONP(ncFile, out, callback);
            renderJSON(outInfoJSON);
        } else if ("application/xml".equals(output)) { // NcML
            res.setContentType(output + "; charset=utf-8");
            ncFile.writeNcML(out, source);
        } else if ("text/plain".equals(output)) { // CDL
            res.setContentType(output + "; charset=utf-8");
            ncFile.writeCDL(out, true);
        } else {
            // throw standard exception
            throw new StandardException("No valid Mime-type for parameter Output.");
        }
    }

    private void renderJSON(OutputInfoWriterJSON outInfoJSON) {
        outInfoJSON.outputStart();
        outInfoJSON.outputFileInfo();
        outInfoJSON.outputSeparator();
        outInfoJSON.outputGlobalAttributes();
        outInfoJSON.outputSeparator();
        outInfoJSON.outputDimensions();
        outInfoJSON.outputSeparator();
        outInfoJSON.outputVariables();
        outInfoJSON.outputSeparator();
        outInfoJSON.outputEnd();
    }

    private void debug() {
        Logger.getLogger(Info.class.getName()).log(Level.INFO, "DEBUG");

        Enumeration<String> e = this.req.getAttributeNames();
        while (e.hasMoreElements()) {
            String key = e.nextElement();
            Logger.getLogger(Info.class.getName()).log(Level.INFO, key + " -> " + this.req.getAttribute(key));
        }

        e = this.req.getParameterNames();
        while (e.hasMoreElements()) {
            String key = e.nextElement();
            Logger.getLogger(Info.class.getName()).log(Level.INFO, key + " -> " + this.req.getParameter(key));
        }
    }
}
