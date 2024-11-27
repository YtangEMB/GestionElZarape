package org.utl.dsm.controller;

import com.mysql.cj.jdbc.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.utl.dsm.db.ConexionBD;
import org.utl.dsm.model.Sucursal;

public class ControllerSucursal {

    public List<Sucursal> getAllSucursalesFromView() throws SQLException {
        String sql = "SELECT * FROM viewSucursales;";
        ConexionBD connMysql = new ConexionBD();
        Connection conn = connMysql.open();
        PreparedStatement pstmt = conn.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery();

        List<Sucursal> listaSucursales = new ArrayList<>();
        while (rs.next()) {
            listaSucursales.add(fillFromView(rs));
        }
        rs.close();
        connMysql.close();
        return listaSucursales;
    }

    private Sucursal fillFromView(ResultSet rs) throws SQLException {
        Sucursal sucursal = new Sucursal();
        sucursal.setIdSucursal(rs.getInt("idSucursal"));
        sucursal.setNombre(rs.getString("SucursalNombre"));
        sucursal.setLatitud(rs.getDouble("latitud"));
        sucursal.setLongitud(rs.getDouble("longitud"));
        sucursal.setFoto(rs.getString("foto"));
        sucursal.setUrlWeb(rs.getString("urlWeb"));
        sucursal.setHorarios(rs.getString("horarios"));
        sucursal.setDireccion(rs.getString("direccion"));
        sucursal.setCiudadNombre(rs.getString("CiudadNombre"));
        sucursal.setEstadoNombre(rs.getString("EstadoNombre"));

        return sucursal;
    }
    
    public String insertSucursal(String nombre, double latitud, double longitud, String foto, String urlWeb,
                                  String horarios, String calle, String numCalle, String colonia, String nombreCiudad) throws SQLException {
        String result = "Sucursal insertada correctamente";

        ConexionBD connMysql = new ConexionBD();
        Connection conn = connMysql.open();

        String sql = "{CALL sp_insertSucursal(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";

        try (CallableStatement stmt = (CallableStatement) conn.prepareCall(sql)) {
            stmt.setString(1, nombre);
            stmt.setDouble(2, latitud);
            stmt.setDouble(3, longitud);
            stmt.setString(4, foto);
            stmt.setString(5, urlWeb);
            stmt.setString(6, horarios);
            stmt.setString(7, calle);
            stmt.setString(8, numCalle);
            stmt.setString(9, colonia);
            stmt.setString(10, nombreCiudad);

            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            result = "Error al insertar la sucursal: " + e.getMessage();
        } finally {
            connMysql.close();
        }

        return result;
    }
    
    public String deleteSucursal(int idSucursal) throws SQLException {
        String result = "Sucursal eliminada correctamente";

        ConexionBD connMysql = new ConexionBD();
        Connection conn = connMysql.open();

        String sql = "{CALL sp_deleteSucursal(?)}";

        try (CallableStatement stmt = (CallableStatement) conn.prepareCall(sql)) {
            stmt.setInt(1, idSucursal);
            stmt.execute();
        } catch (SQLException e) {
            if (e.getSQLState().equals("45000")) {
                result = "Error: " + e.getMessage();
            } else {
                throw e;
            }
        } finally {
            connMysql.close();
        }

        return result;
    }
    
    public String updateSucursal(
        int idSucursal, String nombre, double latitud, double longitud, 
        String foto, String urlWeb, String horarios, String calle, 
        String numCalle, String colonia, String nombreCiudad
    ) throws SQLException {
        String result = "Sucursal actualizada correctamente";

        ConexionBD connMysql = new ConexionBD();
        Connection conn = connMysql.open();

        String sql = "{CALL sp_updateSucursal(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";

        try (CallableStatement stmt = (CallableStatement) conn.prepareCall(sql)) {
            stmt.setInt(1, idSucursal);
            stmt.setString(2, nombre);
            stmt.setDouble(3, latitud);
            stmt.setDouble(4, longitud);
            stmt.setString(5, foto);
            stmt.setString(6, urlWeb);
            stmt.setString(7, horarios);
            stmt.setString(8, calle);
            stmt.setString(9, numCalle);
            stmt.setString(10, colonia);
            stmt.setString(11, nombreCiudad);

            stmt.execute();
        } catch (SQLException e) {
            if (e.getSQLState().equals("45000")) {
                result = "Error: " + e.getMessage();
            } else {
                throw e;
            }
        } finally {
            connMysql.close();
        }

        return result;
    }
}
