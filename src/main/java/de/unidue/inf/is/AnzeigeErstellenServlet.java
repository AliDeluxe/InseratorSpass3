package de.unidue.inf.is;

import de.unidue.inf.is.domain.Anzeige;
import de.unidue.inf.is.utils.DBUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;
import java.util.Date;


/**
 * Einfaches Beispiel, das die Vewendung der Template-Engine zeigt.
 */
public final class AnzeigeErstellenServlet extends HttpServlet {

    private Connection con;
    private static final long serialVersionUID = 1L;
    private Anzeige anzeige = new Anzeige();
    private boolean gesetzt = false;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (gesetzt == false) { //TODO Falls was nicht richtig ist
            request.getRequestDispatcher("anzeige_erstellen.ftl").forward(request, response);
        } else {
            request.setAttribute("titel", anzeige.getTitel());
            request.setAttribute("erstellungsdatum", anzeige.getErstellungsDatum().toString());
            request.setAttribute("ersteller", anzeige.getErsteller());
            request.setAttribute("preis", anzeige.getPreis());
            request.setAttribute("beschreibung", anzeige.getBeschreibung());
            gesetzt = false;
            request.getRequestDispatcher("anzeige_details.ftl").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {

        String titel = request.getParameter("titel");
        Double preis = Double.parseDouble(request.getParameter("preis"));
        String beschreibung = request.getParameter("beschreibung");
        String[] kategorien = request.getParameterValues("kategorie");
        Date currentDate = new Date();
        java.sql.Date sqlDate = new java.sql.Date(currentDate.getTime());

        //TODO mit Anmeldung verbinden und aktuellen Benutzer speichern
        String currentUser = "sonichu";

        anzeige.setTitel(titel);
        anzeige.setPreis(preis);
        anzeige.setBeschreibung(beschreibung);
        anzeige.setKategorien(kategorien);
        anzeige.setErstellungsDatum(currentDate);
        anzeige.setErsteller(currentUser);


        con = null;

        try {
            con = DBUtil.getConnection("insdb");
            con.setAutoCommit(false);
            System.out.println("connected maybe");


            String generatedColumns[] = {"ID"};

            final String query = "INSERT INTO dbp47.anzeige VALUES (DEFAULT,?,?,?,?,?,?)";
            PreparedStatement preparedStatementAnzeige = con.prepareStatement(query, generatedColumns);
            preparedStatementAnzeige.setString(1, anzeige.getTitel());
            preparedStatementAnzeige.setString(2, anzeige.getBeschreibung());
            preparedStatementAnzeige.setDouble(3, anzeige.getPreis());
            preparedStatementAnzeige.setDate(4, sqlDate);
            preparedStatementAnzeige.setString(5, currentUser);
            preparedStatementAnzeige.setString(6, "aktiv");
            preparedStatementAnzeige.execute();

            ResultSet rs = preparedStatementAnzeige.getGeneratedKeys();
            int anzeigeId = 0;
            if (rs.next()) {
                anzeigeId = rs.getInt(1);
                //System.out.println("Inserted ID -" + anzeigeId); // display inserted record
            }


            for (String kategorie : kategorien) {
                final String statementKategorie = "INSERT INTO dbp47.HatKategorie VALUES(?,?)";
                PreparedStatement preparedStatementKategorie = con.prepareStatement(statementKategorie);
                preparedStatementKategorie.setInt(1, anzeigeId);
                preparedStatementKategorie.setString(2, kategorie);
                preparedStatementKategorie.execute();
            }
            con.commit();

        } catch (SQLException e) {
            try {
                con.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
            //TODO fehlerhandling
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        gesetzt = true;
        System.out.println("Und: " + gesetzt);
        doGet(request, response);

    }

}