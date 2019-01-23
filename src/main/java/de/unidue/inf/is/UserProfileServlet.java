package de.unidue.inf.is;

import de.unidue.inf.is.domain.Anzeige;
import de.unidue.inf.is.utils.DBUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.plaf.nimbus.State;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


/**
 * Einfaches Beispiel, das die Vewendung der Template-Engine zeigt.
 */
public final class UserProfileServlet extends HttpServlet {

    String username;
    private static List<Anzeige> anzeigeListe = new ArrayList<>();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Connection con = null;
        anzeigeListe.clear();

        try {
            username = request.getParameter("Ersteller");
            con = DBUtil.getConnection("insdb");
            String sqlStatementBenutzer = "SELECT * FROM dbp47.benutzer WHERE benutzername = ?";
            PreparedStatement preparedStatementBenutzer = con.prepareStatement(sqlStatementBenutzer);
            preparedStatementBenutzer.setString(1, username);
            ResultSet resultsBenutzer = preparedStatementBenutzer.executeQuery();


            while (resultsBenutzer.next()) {

                request.setAttribute("username", username);
                request.setAttribute("name", resultsBenutzer.getString("name"));
                request.setAttribute("datum", resultsBenutzer.getString("eintrittsdatum"));

            }

            String sqlStatementVerkauft = "SELECT * FROM dbp47.anzeige WHERE status = ? AND ersteller = ?";
            PreparedStatement preparedStatementVerkauft = con.prepareStatement(sqlStatementVerkauft);
            preparedStatementVerkauft.setString(1, "verkauft");
            preparedStatementVerkauft.setString(2, username);
            ResultSet resultsVerkauft = preparedStatementVerkauft.executeQuery();

            int verkauft = 0;

            while (resultsVerkauft.next()) {
                verkauft++;
            }

            request.setAttribute("verkauft", verkauft);

            String sqlStatementAnzeigen = "SELECT * FROM dbp47.anzeige WHERE ersteller = ?";
            PreparedStatement preparedStatementAnzeigen = con.prepareStatement(sqlStatementAnzeigen);
            preparedStatementAnzeigen.setString(1, username);
            ResultSet resultsAnzeigen = preparedStatementAnzeigen.executeQuery();


            while (resultsAnzeigen.next()) {
                Anzeige anzeige = new Anzeige();
                anzeige.setTitel(resultsAnzeigen.getString("titel"));
                anzeige.setPreis(resultsAnzeigen.getDouble("preis"));
                anzeige.setErstellungsDatum(resultsAnzeigen.getDate("erstellungsdatum"));
                anzeige.setStatus(resultsAnzeigen.getString("status"));
                anzeigeListe.add(anzeige);
            }

            request.setAttribute("anzeigeListe", anzeigeListe);


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
        request.getRequestDispatcher("user_profile.ftl").forward(request, response);
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {

    }
}
