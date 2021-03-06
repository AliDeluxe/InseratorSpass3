package de.unidue.inf.is;

import de.unidue.inf.is.domain.Anzeige;
import de.unidue.inf.is.utils.CurrentUserUtil;
import de.unidue.inf.is.utils.DBUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


/**
 * Einfaches Beispiel, das die Vewendung der Template-Engine zeigt.
 */
public final class HauptseiteServlet extends HttpServlet {

    private static List<Anzeige> anzeigeListe = new ArrayList<>();
    private Connection con;
    private static final long serialVersionUID = 1L;
    private String currentUser ;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        if(request.getParameter("currentUser") != null){
            currentUser = request.getParameter("currentUser");
            CurrentUserUtil.currentuser = currentUser;
        }



        System.out.println("currentuser in hauptseite: " + currentUser);
        con = null;
        anzeigeListe.clear();

        ResultSet resultset = null;

        try {
            con = DBUtil.getExternalConnection("insdb");

            String sortieren = request.getParameter("sortieren");
            String filtern = request.getParameter("filtern");


            if(sortieren != null) {
                if (sortieren.equals("nicht")) {
                    sortieren = null;
                }
            }

            if(filtern != null) {
                if (filtern.equals("nicht")) {
                    filtern = null;
                }
            }

            if(sortieren != null && filtern != null) {

                String queryKategorie = "SELECT * FROM dbp47.hatKategorie WHERE kategorie = ?";
                PreparedStatement preparedStatementKategorie = con.prepareStatement(queryKategorie);
                preparedStatementKategorie.setString(1, filtern);
                ResultSet resultsKategorie = preparedStatementKategorie.executeQuery();

                while(resultsKategorie.next()) {

                    String query = "SELECT * FROM dbp47.anzeige WHERE status = 'aktiv' AND id = ? ORDER BY " + sortieren;
                    PreparedStatement preparedStatement = con.prepareStatement(query);
                    preparedStatement.setShort(1, resultsKategorie.getShort("anzeigeID"));
                    resultset = preparedStatement.executeQuery();

                }

            } else if(sortieren != null && filtern == null) {

                String query = "SELECT * FROM dbp47.anzeige WHERE status = 'aktiv' ORDER BY "+ sortieren;
                Statement statement = con.createStatement();
                resultset = statement.executeQuery(query);

            } else if(sortieren == null && filtern != null) {
                String queryKategorie = "SELECT * FROM dbp47.hatKategorie WHERE kategorie = ?";
                PreparedStatement preparedStatementKategorie = con.prepareStatement(queryKategorie);
                preparedStatementKategorie.setString(1, filtern);
                ResultSet resultsKategorie = preparedStatementKategorie.executeQuery();

                while(resultsKategorie.next()) {

                    String query = "SELECT * FROM dbp47.anzeige WHERE status = 'aktiv' AND id = ?";
                    PreparedStatement preparedStatement = con.prepareStatement(query);
                    preparedStatement.setShort(1, resultsKategorie.getShort("anzeigeID"));
                    resultset = preparedStatement.executeQuery();

                }

            } else {

                String query = "SELECT * FROM dbp47.anzeige WHERE status = 'aktiv'";
                Statement statement = con.createStatement();

                resultset = statement.executeQuery(query);

            }



            while (resultset.next()) {
                Anzeige anzeige = new Anzeige();
                anzeige.setId(resultset.getShort("id"));
                anzeige.setTitel(resultset.getString("titel"));
                anzeige.setPreis(resultset.getDouble("preis"));
                anzeige.setBeschreibung(resultset.getString("text"));
                anzeige.setErsteller(resultset.getString("ersteller"));
                anzeige.setErstellungsDatum(resultset.getDate("erstellungsdatum"));
                anzeigeListe.add(anzeige);

            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }


        request.setAttribute("currentUser", currentUser);
        request.setAttribute("anzeigeListe", anzeigeListe);
        request.getRequestDispatcher("hauptseite.ftl").forward(request, response);

    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {

    }
}
