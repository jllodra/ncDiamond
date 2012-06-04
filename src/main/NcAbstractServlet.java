/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import exceptions.StandardException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ResourceBundle;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import ucar.nc2.NetcdfFile;
import ucar.nc2.dataset.NetcdfDataset;
/**
 *
 * @author josep
 */
public abstract class NcAbstractServlet extends HttpServlet {

    HttpServletRequest req;
    HttpServletResponse res;
    ResourceBundle rb;
    // Parameters
    String src;
    String output = "text/plain"; // default, must match with a content-type
    String callback = "callback"; // when output is jsonp
    // Netcdf resources
    NetcdfFile ncFile = null;
    PrintWriter out = null;
    
    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        req = request;
        res = response;
        out = res.getWriter();
        try {
            gatherParameters();
            ncFile = NetcdfDataset.openFile(src, null);
            // null means task can not be cancelled
            render();
        } catch (StandardException e) {
            out.println(e.getMessage());
            e.printStackTrace(out);
        } finally {
            if (null != ncFile) {
                ncFile.close();
            }
        }
        out.flush();
        out.close();
    }

    private void gatherParameters() throws StandardException {
        src = req.getParameter("src");
        output = (req.getParameter("output") != null) ? req.getParameter("output") : output;
        callback = (req.getParameter("callback") != null) ? req.getParameter("callback") : callback;
        if (null == src) {
            throw new StandardException("Please, add parameter 'src', example: ");
        }
    }
    
    protected abstract void render() throws StandardException, IOException;
    
    protected void debug() {
        out.print("NcAbstractServlet debug.");
    };
    
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Abstract servlet";
    }// </editor-fold>
}
