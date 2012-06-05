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
    protected Renderer getJSONRenderer() {
        return new Renderer() {

            @Override
            public void render() {
                OutputInfoWriterJSON outInfoJSON = new OutputInfoWriterJSON(ncFile, out);
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
        };
    }

    @Override
    protected Renderer getJSONPRenderer() {
        return new Renderer() {

            @Override
            public void render() {
                OutputInfoWriterJSONP outInfoJSONP = new OutputInfoWriterJSONP(ncFile, out, callback);
                outInfoJSONP.outputStart();
                outInfoJSONP.outputFileInfo();
                outInfoJSONP.outputSeparator();
                outInfoJSONP.outputGlobalAttributes();
                outInfoJSONP.outputSeparator();
                outInfoJSONP.outputDimensions();
                outInfoJSONP.outputSeparator();
                outInfoJSONP.outputVariables();
                outInfoJSONP.outputSeparator();
                outInfoJSONP.outputEnd();
            }
        };
    }

    @Override
    protected Renderer getNcMLRenderer() {
        return new Renderer() {
            @Override
            public void render() {
                try {
                    ncFile.writeNcML(out, src);
                } catch (IOException ex) {
                    Logger.getLogger(NcInfo.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };
    }

    @Override
    protected Renderer getCDLRenderer() {
        return new Renderer() {
            @Override
            public void render() {
                ncFile.writeCDL(out, true);
            }
        };
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
