package org.utl.dsm.controller;

import com.mysql.cj.jdbc.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.utl.dsm.db.ConexionBD;
import org.utl.dsm.model.Categoria;
import org.utl.dsm.model.Producto;

public class ControllerBebida {

    public List<Producto> getAllBebidasFromView() throws SQLException {
        String sql = "SELECT * FROM viewBebidas;";
        ConexionBD connMysql = new ConexionBD();
        Connection conn = connMysql.open();
        PreparedStatement pstmt = conn.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery();

        List<Producto> listaProductos = new ArrayList<>();
        while (rs.next()) {
            listaProductos.add(fillFromView(rs));
        }
        rs.close();
        connMysql.close();
        return listaProductos;
    }

    private Producto fillFromView(ResultSet rs) throws SQLException {
        Categoria categoria = new Categoria();
        categoria.setDescripcion(rs.getString("categoria"));

        Producto producto = new Producto();
        producto.setIdProducto(rs.getInt("idProducto"));
        producto.setNombre(rs.getString("nombre"));
        producto.setDescripcion(rs.getString("descripcion"));
        producto.setFoto(rs.getString("foto"));
        producto.setPrecio((int) rs.getDouble("precio"));
        producto.setCategoria(categoria);

        return producto;
    }
    
    public String insertBebida(String nombre, String descripcion, String foto, int precio, String categoria) throws SQLException {
        String result = "Bebida insertada correctamente";
        
        ConexionBD connMysql = new ConexionBD();
        Connection conn = connMysql.open();
        
        String sql = "{CALL sp_insertBebida(?, ?, ?, ?, ?)}";
        try (CallableStatement stmt = (CallableStatement) conn.prepareCall(sql)) {
            stmt.setString(1, nombre);
            stmt.setString(2, descripcion);
            stmt.setString(3, foto);
            stmt.setInt(4, precio);
            stmt.setString(5, categoria);
            
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            result = "Error al insertar la bebida: " + e.getMessage();
        } finally {
            connMysql.close();
        }
        return result;
    }
    
    public String deleteBebida(int idProducto) throws SQLException {
        String result = "Bebida eliminada correctamente";
        
        ConexionBD connMysql = new ConexionBD();
        Connection conn = connMysql.open();
        
        String sql = "{CALL sp_deleteBebida(?)}";
        try (CallableStatement stmt = (CallableStatement) conn.prepareCall(sql)) {
            stmt.setInt(1, idProducto);
            
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            result = "Error al eliminar la bebida: " + e.getMessage();
        } finally {
            connMysql.close();
        }
        return result;
    }
    
    public String updateBebida(int idProducto, String nombre, String descripcion, String foto, int precio, String categoria) throws SQLException {
        String result = "Bebida actualizada correctamente";
        
        ConexionBD connMysql = new ConexionBD();
        Connection conn = connMysql.open();
        
        String sql = "{CALL sp_updateBebida(?, ?, ?, ?, ?, ?)}";
        try (CallableStatement stmt = (CallableStatement) conn.prepareCall(sql)) {
            stmt.setInt(1, idProducto);
            stmt.setString(2, nombre);
            stmt.setString(3, descripcion);
            stmt.setString(4, foto);
            stmt.setInt(5, precio);
            stmt.setString(6, categoria);
            
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            result = "Error al actualizar la bebida: " + e.getMessage();
        } finally {
            connMysql.close();
        }
        return result;
    }
    
}
