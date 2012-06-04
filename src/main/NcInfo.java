package main;

import exceptions.StandardException;
import java.io.IOException;
import java.util.Enumeration;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import outputWriters.OutputInfoWriterJSON;
import outputWriters.OutputInfoWriterJSONP;

/**
 *
 * @author josep
 */
public class NcInfo extends NcAbstractServlet {
    
    @Override
    protected void render() throws StandardException, IOException {
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
            ncFile.writeNcML(out, src);
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

    @Override
    protected void debug() {
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
