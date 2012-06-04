package main;

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

public class Info extends HttpServlet {

    HttpServletRequest req;
    HttpServletResponse res;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        this.req = request;
        this.res = response;
        this.debug();
        
        
        
        this.render();
    }

    private void render() {
        ResourceBundle rb = ResourceBundle.getBundle("LocalStrings", this.req.getLocale());

        this.res.setContentType("text/html");
        try {
            PrintWriter out = this.res.getWriter();
            
            out.println("<html>");
            out.println("<head>");

            String title = rb.getString("info.title");

            out.println("<title>" + title + "</title>");
            out.println("</head>");
            out.println("<body>");

            out.println("<h1>" + title + "</h1>");
            out.println("<p>This is an information servlet.</p>");
            out.println("<p>You are running Nc Diamond version 0.1 (replace for version)</p>");
            
            out.println("</body>");
            out.println("</html>");
        } catch (IOException ex) {
            Logger.getLogger(Info.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    private void debug() {
        Logger.getLogger(Info.class.getName()).log(Level.INFO, "DEBUG");
        Enumeration<String> e = this.req.getParameterNames();
        while(e.hasMoreElements()) {
            String key = e.nextElement();
            Logger.getLogger(Info.class.getName()).log(Level.INFO, key + " -> " + this.req.getParameter(key));
        }
    }
}
