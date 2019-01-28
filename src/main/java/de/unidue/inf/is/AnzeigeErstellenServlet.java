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
    private boolean ersterAufruf = true;
    private boolean keineFehler;

    private String currentUser;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        currentUser = request.getParameter("currentUser");
        System.out.println("currentuser in anzeige erstellen is: " + currentUser);

         if(keineFehler){

            request.setAttribute("fehlermeldung", "");
            request.setAttribute("erfolgreich", "");
            request.getRequestDispatcher("anzeige_erstellen.ftl").forward(request, response);

        } else {

             if (ersterAufruf) {
                 request.setAttribute("fehlermeldung", "");
             }

            request.setAttribute("anzeigeid", "");
            request.setAttribute("erfolgreich", "hidden");
            request.getRequestDispatcher("anzeige_erstellen.ftl").forward(request, response);

        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {


        keineFehler = true;
        ersterAufruf = false;

        String titel = request.getParameter("titel");
        Double preis;

        if(!request.getParameter("preis").isEmpty()) {
            preis = Double.parseDouble(request.getParameter("preis"));
        } else {
            keineFehler = false;
            preis = 0d;
            request.setAttribute("fehlermeldung", "Preis darf nicht leer sein");
        }

        String beschreibung = request.getParameter("beschreibung");
        String[] kategorien = request.getParameterValues("kategorie");
        Date currentDate = new Date();
        java.sql.Date sqlDate = new java.sql.Date(currentDate.getTime());



        anzeige.setTitel(titel);
        anzeige.setPreis(preis);
        anzeige.setBeschreibung(beschreibung);
        anzeige.setKategorien(kategorien);
        anzeige.setErstellungsDatum(currentDate);
        anzeige.setErsteller(currentUser);


        con = null;

        try {
            con = DBUtil.getExternalConnection("insdb");
            con.setAutoCommit(false);

            if(titel.length() > 100 || titel.length() < 1) {

                keineFehler = false;
                request.setAttribute("fehlermeldung", "Der Titel muss mindestens 1 und maximal 100 Zeichen haben");

            } else if(anzeige.getKategorien() == null) {
                keineFehler = false;
                request.setAttribute("fehlermeldung", "Wählen sie mindestens eine Kategorie aus");

            }

            if(keineFehler){

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
                }


                for (String kategorie : kategorien) {
                    final String statementKategorie = "INSERT INTO dbp47.HatKategorie VALUES(?,?)";
                    PreparedStatement preparedStatementKategorie = con.prepareStatement(statementKategorie);
                    preparedStatementKategorie.setInt(1, anzeigeId);
                    preparedStatementKategorie.setString(2, kategorie);
                    preparedStatementKategorie.execute();
                }
                request.setAttribute("anzeigeid", anzeigeId);
                con.commit();
            }

        } catch (SQLException e) {

            try {
                con.rollback();
                keineFehler = false;
                request.setAttribute("fehlermeldung", "Ein unerwarteter Fehler ist aufgetreten, sorry");
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
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

        doGet(request, response);

    }

}