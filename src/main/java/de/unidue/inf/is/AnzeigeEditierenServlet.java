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
import java.util.Date;


/**
 * Einfaches Beispiel, das die Vewendung der Template-Engine zeigt.
 */
public final class AnzeigeEditierenServlet extends HttpServlet {

    private Connection con;
    private static final long serialVersionUID = 1L;
    private Anzeige anzeige = new Anzeige();
    private boolean ersterAufruf = true;
    private boolean keineFehler;
    private String currentUser;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        currentUser = CurrentUserUtil.currentuser;
        request.setAttribute("anzeigeid",request.getParameter("ID"));

        con = null;

        try {
            con = DBUtil.getExternalConnection("insdb");
            con.setAutoCommit(false);

            String sqlQuery = "SELECT * FROM dbp47.anzeige WHERE id=?";
            PreparedStatement preparedStatementSelect = con.prepareStatement(sqlQuery);
            preparedStatementSelect.setString(1,request.getParameter("ID"));
            ResultSet resultsSelect = preparedStatementSelect.executeQuery();

            while(resultsSelect.next()) {

                request.setAttribute("titel", resultsSelect.getString("titel"));
                request.setAttribute("preis", resultsSelect.getDouble("preis"));
                request.setAttribute("beschreibung", resultsSelect.getString("text")+"-");

            }
            con.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            try{
                con.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }

        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        if(keineFehler){

            request.setAttribute("fehlermeldung", "");
            request.setAttribute("erfolgreich", "");
            request.getRequestDispatcher("anzeige_editieren.ftl").forward(request, response);

        } else {

            if (ersterAufruf) {
                request.setAttribute("fehlermeldung", "");
            }


            request.setAttribute("anzeigeid", "");
            request.setAttribute("erfolgreich", "hidden");
            request.getRequestDispatcher("anzeige_editieren.ftl").forward(request, response);

        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {

        keineFehler = true;
        ersterAufruf = false;
        con = null;

        try {
            con = DBUtil.getExternalConnection("insdb");
            con.setAutoCommit(false);


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

            anzeige.setTitel(titel);
            anzeige.setPreis(preis);
            anzeige.setBeschreibung(beschreibung);
            anzeige.setKategorien(kategorien);

            if(titel.length() > 100 || titel.length() < 1) {

                keineFehler = false;
                request.setAttribute("fehlermeldung", "Der Titel muss mindestens 1 und maximal 100 Zeichen haben");

            } else if(anzeige.getKategorien() == null) {
                keineFehler = false;
                request.setAttribute("fehlermeldung", "Wählen sie mindestens eine Kategorie aus");

            }

            if(keineFehler){

                final String query = "UPDATE dbp47.anzeige SET titel = ?, text= ?, preis = ? WHERE id = ?";
                PreparedStatement preparedStatementAnzeige = con.prepareStatement(query);
                preparedStatementAnzeige.setString(1, anzeige.getTitel());
                preparedStatementAnzeige.setString(2, anzeige.getBeschreibung());
                preparedStatementAnzeige.setDouble(3, anzeige.getPreis());
                preparedStatementAnzeige.setString(4, request.getParameter("ID"));
                preparedStatementAnzeige.execute();


                /*for (String kategorie : kategorien) {
                    final String statementKategorie = "INSERT INTO dbp47.HatKategorie VALUES(?,?)";
                    PreparedStatement preparedStatementKategorie = con.prepareStatement(statementKategorie);
                    preparedStatementKategorie.setInt(1, anzeigeId);
                    preparedStatementKategorie.setString(2, kategorie);
                    preparedStatementKategorie.execute();
                }
                request.setAttribute("anzeigeid", anzeigeId);*/
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