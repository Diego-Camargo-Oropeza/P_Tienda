/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pck_connection;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;
import javax.swing.JOptionPane;

/**
 *
 * @author dieca
 */
public class DB_Connection {

    static final String DB = "shop";
    static final String URL = "jdbc:mysql://localhost:3306/" + DB;
    static final String USER = "root";
    static final String PASS = "";
    private static Connection con;

    public DB_Connection() {
        con = null;
    }

    public Connection getConnection() {
        try {
            con = DriverManager.getConnection(URL, USER, PASS);
        } catch (SQLException e) {
        }
        return con;
    }

    public void close() {
        try {
            con.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "XD YOU ARE A NIGGER");
        }
    }

}
