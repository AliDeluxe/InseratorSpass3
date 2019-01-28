package de.unidue.inf.is;

import de.unidue.inf.is.domain.Anzeige;
import de.unidue.inf.is.domain.Kommentar;
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
public final class AnzeigeDetailsServlet extends HttpServlet {

    //Muessen mit den ftl name tags ueberseinstimmen!
    public static final String LOESCHEN_BUTTON = "loeschen";
    public static final String KAUFEN_BUTTON = "kaufen";
    public static final String KOMMENTAR_BUTTON = "submitKommentar";


    private Anzeige anzeige;
    private boolean gelöscht = false;

    private String currentuser;

    private static List<Kommentar> kommentarList = new ArrayList<>();

    private Connection con;
    private static final long serialVersionUID = 1L;


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        currentuser = CurrentUserUtil.currentuser;
        System.out.println("currentuser in Anzeigedetails: " + currentuser);
        kommentarList.clear();

        if (gelöscht) {
            gelöscht = false;
            request.getRequestDispatcher("anzeige_gelöscht.ftl").forward(request, response);

        } else {

            try {

                anzeige = new Anzeige();
                con = DBUtil.getExternalConnection("insdb");
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

                String sqlQueryHatKommentar = "SELECT * FROM dbp47.hatKommentar WHERE anzeigeid= ?";
                PreparedStatement preparedStatementHatKommentar = con.prepareStatement(sqlQueryHatKommentar);
                preparedStatementHatKommentar.setString(1, request.getParameter("ID"));
                ResultSet resultsHatKommentar = preparedStatementHatKommentar.executeQuery();

                while (resultsHatKommentar.next()) {

                    Kommentar kommentar = new Kommentar();

                    String sqlQueryKommentar = "SELECT * FROM dbp47.kommentar WHERE id=?";
                    PreparedStatement preparedStatementKommentar = con.prepareStatement(sqlQueryKommentar);
                    preparedStatementKommentar.setShort(1, resultsHatKommentar.getShort("kommentarid"));
                    ResultSet resultsKommentar = preparedStatementKommentar.executeQuery();

                    while (resultsKommentar.next()) {

                        kommentar.setText(resultsKommentar.getString("text"));
                        kommentar.setKommentator(resultsHatKommentar.getString("benutzername"));
                        kommentarList.add(kommentar);
                    }
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

            request.setAttribute("kommentarListe", kommentarList);
            request.getRequestDispatcher("anzeige_details.ftl").forward(request, response);

        }
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {


        if (request.getParameter(LOESCHEN_BUTTON) != null) {
            if (anzeige.getErsteller().equals(currentuser)) {

                gelöscht = true;
                löschen();
            } else {
                System.out.println("loeschen fehlerhaft");
                System.out.println("Anzeigenersteller: " + anzeige.getErsteller());
                System.out.println("Currentuser: " + currentuser);
            }

        } else if (request.getParameter(KAUFEN_BUTTON) != null) {

            if (!anzeige.getErsteller().equals(currentuser)) {

                kaufen();

            } else {
                System.out.println("Du kannst nicht deine eigene Anzeige kaufen...");
            }


        } else if (request.getParameter(KOMMENTAR_BUTTON) != null) {

            try {

                con = DBUtil.getExternalConnection("insdb");
                con.setAutoCommit(false);

                String generatedColumns[] = {"ID"};

                final String queryKommentar = "INSERT INTO dbp47.kommentar VALUES (DEFAULT,?,DEFAULT)";
                PreparedStatement preparedStatementKommentar = con.prepareStatement(queryKommentar, generatedColumns);
                preparedStatementKommentar.setString(1, request.getParameter("kommentar"));
                preparedStatementKommentar.executeUpdate();

                ResultSet rs = preparedStatementKommentar.getGeneratedKeys();
                short kommentarId = 0;
                if (rs.next()) {
                    kommentarId = rs.getShort(1);
                }

                final String queryHatKommentar = "INSERT INTO dbp47.hatKommentar VALUES (?,?,?)";
                PreparedStatement preparedStatementHatKommentar = con.prepareStatement(queryHatKommentar);
                preparedStatementHatKommentar.setShort(1, kommentarId);
                preparedStatementHatKommentar.setString(2, currentuser);
                preparedStatementHatKommentar.setShort(3, Short.parseShort(request.getParameter("ID")));
                preparedStatementHatKommentar.executeUpdate();

                con.commit();

            } catch (SQLException e) {
                try {
                    con.rollback();
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

        }

        doGet(request, response);
    }

    private void kaufen() {
        try {
            con = DBUtil.getExternalConnection("insdb");
            con.setAutoCommit(false);
            final String query = "INSERT INTO dbp47.kauft VALUES (?,?,DEFAULT)";
            PreparedStatement preparedStatementAnzeige = con.prepareStatement(query);
            preparedStatementAnzeige.setString(1, currentuser);
            preparedStatementAnzeige.setShort(2, anzeige.getId());
            preparedStatementAnzeige.execute();
            con.commit();


        } catch (SQLException e) {
            e.printStackTrace();
            try {
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
    }

    private void löschen() {

        try {
            con = DBUtil.getExternalConnection("insdb");
            con.setAutoCommit(false);
            final String query = "DELETE FROM dbp47.anzeige WHERE id=?";
            PreparedStatement preparedStatementAnzeige = con.prepareStatement(query);
            preparedStatementAnzeige.setShort(1, anzeige.getId());
            preparedStatementAnzeige.execute();
            con.commit();

        } catch (SQLException e) {
            e.printStackTrace();
            try {
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

    }

}
