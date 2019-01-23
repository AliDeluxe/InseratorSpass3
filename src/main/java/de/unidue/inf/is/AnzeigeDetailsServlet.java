package de.unidue.inf.is;

import de.unidue.inf.is.domain.Anzeige;
import de.unidue.inf.is.utils.DBUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;


/**
 * Einfaches Beispiel, das die Vewendung der Template-Engine zeigt.
 */
public final class AnzeigeDetailsServlet extends HttpServlet {

    private Anzeige anzeige;

    private Connection con;
    private static final long serialVersionUID = 1L;


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {

            anzeige = new Anzeige();
            con = DBUtil.getConnection("insdb");
            String query = "SELECT * FROM dbp47.anzeige WHERE id = ?";
            PreparedStatement preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, request.getParameter("ID"));
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                anzeige.setId(resultSet.getShort("id"));
                anzeige.setTitel(resultSet.getString("titel"));
                anzeige.setPreis(resultSet.getDouble("preis"));
                anzeige.setBeschreibung(resultSet.getString("text"));
                anzeige.setErstellungsDatum(resultSet.getDate("erstellungsdatum"));
                anzeige.setErsteller(resultSet.getString("Ersteller"));
            }

            request.setAttribute("titel", anzeige.getTitel());
            request.setAttribute("preis", anzeige.getPreis());
            request.setAttribute("ersteller", anzeige.getErsteller());
            request.setAttribute("erstellungsdatum", anzeige.getErstellungsDatum());
            request.setAttribute("beschreibung", anzeige.getBeschreibung());
            request.setAttribute("id", anzeige.getId());


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }


        request.getRequestDispatcher("anzeige_details.ftl").forward(request, response);
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {


        if(request.getParameter("loeschen").equals("Löschen")){ //TODO richtig weiterleiten
            System.out.println("ES KLAPPT");
            doGet(request,response);
        }

//        kaufen();

//        doGet(request, response);

    }

    private void kaufen() {
        try {
            con = DBUtil.getConnection("insdb");
            final String query = "INSERT INTO dbp47.kauft VALUES (?,?,DEFAULT)";
            PreparedStatement preparedStatementAnzeige = con.prepareStatement(query);
            preparedStatementAnzeige.setString(1, "sonichu"); //TODO Benutzer hardcoded fix
            preparedStatementAnzeige.setShort(2, anzeige.getId());
            preparedStatementAnzeige.execute();


        } catch (SQLException e) {
            e.printStackTrace();

        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


}
