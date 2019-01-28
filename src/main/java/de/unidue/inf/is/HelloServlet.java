package de.unidue.inf.is;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.unidue.inf.is.domain.User;
import de.unidue.inf.is.utils.DBUtil;


public final class HelloServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private Connection con;


    private static List<User> userList = new ArrayList<>();


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Put the user list in request and let freemarker paint it.
        // Verbindet SERVLET class mit ftl

        

        try {
            userList.clear();

            con = DBUtil.getExternalConnection("insdb");
            String query = "SELECT * FROM dbp47.Benutzer";
            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while(resultSet.next()){
                User user = new User();
                user.setUsername(resultSet.getString("benutzername"));
                String name = resultSet.getString("name");
                String[] subStrings = name.split("\\s+");
                user.setFirstname(subStrings[0]);
                user.setLastname(subStrings[1]);
                userList.add(user);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }


        request.setAttribute("users", userList);

        request.getRequestDispatcher("/hello.ftl").forward(request, response);
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {

        Date currentDate = new Date();
        java.sql.Date sqlDate = new java.sql.Date(currentDate.getTime());

        String username = request.getParameter("username");
        String firstname = request.getParameter("firstname");
        String lastname = request.getParameter("lastname");

        if (null != username && null != firstname && null != lastname && !firstname.isEmpty() && !lastname.isEmpty() && !username.isEmpty()) {

            synchronized (userList) {

                try {
                    con = DBUtil.getExternalConnection("insdb");
                    con.setAutoCommit(false);
                    String insert = "INSERT INTO dbp47.Benutzer (benutzername, name, eintrittsdatum)" +
                            "VALUES (?,?,?)";
                    PreparedStatement preparedStatement = con.prepareStatement(insert);
                    preparedStatement.setString(1, username);
                    preparedStatement.setString(2, firstname + " " + lastname);
                    preparedStatement.setDate(3, sqlDate);
                    preparedStatement.execute();
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

        }

        doGet(request, response);
    }
}
