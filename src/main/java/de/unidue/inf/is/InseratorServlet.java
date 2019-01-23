package de.unidue.inf.is;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.unidue.inf.is.utils.DBUtil;



/**
 * Ein Beispeil für die Einstiegsseite 
 */
public final class InseratorServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
                    throws ServletException, IOException {
  
	final String databaseToCheck = "insdb";
        boolean databaseExists = DBUtil.checkDatabaseExists(databaseToCheck);

        request.setAttribute("db2name", databaseToCheck);

        if (databaseExists) {
            request.setAttribute("db2exists", "vorhanden! Supi!");
        }
        else {
            request.setAttribute("db2exists", "nicht vorhanden :-(");
        }

        request.getRequestDispatcher("inserator.ftl").forward(request, response);
    }

}
