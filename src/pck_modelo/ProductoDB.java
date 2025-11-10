/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pck_modelo;

import pck_connection.DB_Connection;
import java.sql.PreparedStatement;
import java.sql.*;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author dieca
 */
public class ProductoDB {

    private PreparedStatement ps;
    private final DB_Connection cnx;
    private DefaultTableModel DTM;
    private ResultSet rs;

    private final String INSERT = "INSERT INTO cg_products(idProducts, description, brand, content, category, price, status, prodDate, expDate) VALUES (?,?,?,?,?,?,?,?,?)";
    private final String SELECT = "SELECT * FROM cg_products";
    private String SELECT_WHERE;
    private String DELETE = "DELETE FROM cg_products WHERE idProducts = ";

    public ProductoDB() {
        this.ps = null;
        this.cnx = new DB_Connection();
    }

    public int insertarProducto(int id, String descripcion, String marca, String contenido, String categoria, Float precio, boolean estado, Date fechaE, Date fechaC) {
        int res = 0;
        try {
            ps = cnx.getConnection().prepareStatement(INSERT);
            ps.setInt(1, id);
            ps.setString(2, descripcion);
            ps.setString(3, marca);
            ps.setString(4, contenido);
            ps.setString(5, categoria);
            ps.setFloat(6, precio);
            ps.setBoolean(7, estado);
            ps.setDate(8, fechaE);
            ps.setDate(9, fechaC);
            res = ps.executeUpdate();
            if (res > 0) {
                JOptionPane.showMessageDialog(null, "Registro creado correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            JOptionPane.showMessageDialog(null, "ID Duplicada, pruebe con otro id " + e.getMessage(), "Error al crear!", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al crear registro, " + e.getMessage(), "Error al crear!", JOptionPane.ERROR_MESSAGE);
        } finally {
            ps = null;
            cnx.close();
        }
        return res;
    }

    private DefaultTableModel setTitulos() {

        DTM = new DefaultTableModel();
        DTM.addColumn("ID");
        DTM.addColumn("Descripción");
        DTM.addColumn("Marca");
        DTM.addColumn("Contenido");
        DTM.addColumn("Categoría");
        DTM.addColumn("Precio");
        DTM.addColumn("Estado");
        DTM.addColumn("Fecha de elaboración");
        DTM.addColumn("Fecha de caducidad");

        return DTM;
    }

    public DefaultTableModel getDatos() {
        
        Object[] fila = new Object[9];
        try {
            this.setTitulos();
            ps = cnx.getConnection().prepareStatement(SELECT);
            rs = ps.executeQuery();
            while (rs.next()) {
                fila[0] = rs.getInt(1);
                fila[1] = rs.getString(2);
                fila[2] = rs.getString(3);
                fila[3] = rs.getString(4);
                fila[4] = rs.getString(5);
                fila[5] = rs.getFloat(6);
                fila[6] = rs.getBoolean(7);
                fila[7] = rs.getDate(8);
                fila[8] = rs.getDate(9);
                DTM.addRow(fila);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al obtener datos" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            ps = null;
            rs = null;
            cnx.close();
        }

        return DTM;
    }

    public DefaultTableModel buscarProducto(int criterio, String parametro) {
        if (criterio == 0) {
            SELECT_WHERE = "SELECT * FROM cg_products WHERE idProducts = " + parametro;
        } else if (criterio == 1) {
            SELECT_WHERE = "SELECT * FROM cg_products WHERE description like '" + parametro + "%'";
        }

        try {
            this.setTitulos();
            ps = cnx.getConnection().prepareStatement(SELECT_WHERE);
            rs = ps.executeQuery();
            Object[] fila = new Object[9];
            while (rs.next()) {
                fila[0] = rs.getInt(1);
                fila[1] = rs.getString(2);
                fila[2] = rs.getString(3);
                fila[3] = rs.getString(4);
                fila[4] = rs.getString(5);
                fila[5] = rs.getFloat(6);
                fila[6] = rs.getBoolean(7);
                fila[7] = rs.getDate(8);
                fila[8] = rs.getDate(9);
                DTM.addRow(fila);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al consultar la base de de datos" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            ps = null;
            rs = null;
            cnx.close();
        }
        return DTM;
    }

    public int actualizarProducto(int id, String descripcion, String marca, String contenido, String categoria, Float precio, boolean estado, Date fechaE, Date fechaC) {

        int res = 0;
        String SQL_UPDATE = null;

        if (fechaC != null) {
            SQL_UPDATE = "UPDATE cg_products SET description = '" + descripcion + "',brand='" + marca + "',category='" + categoria + "',price='" + precio
                    + "',status=" + estado + ",prodDate='" + fechaE + "',expDate='" + fechaC
                    + "' WHERE idProducts= " + id;
        } else {
            SQL_UPDATE = "UPDATE cg_products SET description = '" + descripcion + "',brand='" + marca + "',category='" + categoria + "',price='" + precio
                    + "',status='" + estado + "',prodDate='" + fechaE
                    + "' WHERE idProducts= " + id;
        }
        try {
            ps = cnx.getConnection().prepareStatement(SQL_UPDATE);
            res = ps.executeUpdate();
            if (res > 0) {
                JOptionPane.showMessageDialog(null, "El registro fue actualizado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "No se actualizó ningun registro", "Advertencia", JOptionPane.WARNING_MESSAGE);

            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al intentar actualizar " + e.getMessage(), "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } finally {
            ps = null;
            cnx.close();
        }

        return res;

    }

    public boolean eliminarProducto(int idProducto) {
        String DELETE = "DELETE FROM cg_products WHERE idProducts = " + idProducto;
        try {
            ps = cnx.getConnection().prepareStatement(DELETE);
            int a = ps.executeUpdate();
            if (a > 0) {
                JOptionPane.showMessageDialog(null, "Registro borrado exitosamente");
                return true;
            } else {
                JOptionPane.showMessageDialog(null, DELETE);
                return false;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al intentar borrar" + e.getMessage());
            return false;
        } finally {
            ps = null;
            cnx.close();
        }
    }
}
