package org.utl.dsm.controller;

import com.mysql.cj.jdbc.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.sql.SQLException;
import org.utl.dsm.model.Usuario;
import org.utl.dsm.db.ConexionBD;
import org.utl.dsm.model.Ciudad;
import org.utl.dsm.model.Empleado;
import org.utl.dsm.model.Estado;
import org.utl.dsm.model.Persona;
import org.utl.dsm.model.Sucursal;

public class ControllerEmpleado {

    public List<Empleado> getAllEmpleadoFromView() throws SQLException {
        String sql = "SELECT * FROM viewEmpleado;";
        ConexionBD connMysql = new ConexionBD();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Empleado> listaEmpleado = new ArrayList<>();

        try {
            conn = connMysql.open();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                listaEmpleado.add(fillFromView(rs));
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (pstmt != null) {
                pstmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        }

        return listaEmpleado;
    }

    private Empleado fillFromView(ResultSet rs) throws SQLException {
        Persona persona = new Persona();
        Ciudad ciudad = new Ciudad();
        Usuario usuario = new Usuario();
        Estado estado = new Estado();
        Empleado empleado = new Empleado();
        Sucursal sucursal = new Sucursal();

        empleado.setIdEmpleado(rs.getInt("idPersona")); 
        usuario.setNombre(rs.getString("usuario"));
        persona.setNombre(rs.getString("nombre"));
        persona.setApellidos(rs.getString("apellidos"));
        persona.setTelefono(rs.getString("telefono"));
        estado.setNombre(rs.getString("estado"));
        ciudad.setNombre(rs.getString("ciudad"));
        sucursal.setNombre(rs.getString("sucursal"));

        empleado.setUsuario(usuario);
        empleado.setPersona(persona);
        empleado.setCiudad(ciudad);
        empleado.setSucursal(sucursal);
        empleado.setEstado(estado); 

        return empleado;
    }

    public String insertarEmpleado(String nombre, String apellidos, String telefono, String nombreCiudad, String nombreUsuario, String contrasenia, String nombreSucursal) throws SQLException {
        String result = "Empleado insertado correctamente";

        ConexionBD connMysql = new ConexionBD();
        Connection conn = connMysql.open();

        String sql = "{CALL insertarEmpleado(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
        try (CallableStatement stmt = (CallableStatement) conn.prepareCall(sql)) {
            stmt.setString(1, nombre);
            stmt.setString(2, apellidos);
            stmt.setString(3, telefono);
            stmt.setString(4, nombreCiudad);
            stmt.setString(5, nombreUsuario);
            stmt.setString(6, contrasenia);
            stmt.setString(7, nombreSucursal);

            stmt.registerOutParameter(8, java.sql.Types.INTEGER); 
            stmt.registerOutParameter(9, java.sql.Types.INTEGER); 
            stmt.registerOutParameter(10, java.sql.Types.INTEGER); 

            stmt.execute();

            int idPersona = stmt.getInt(8);
            int idUsuario = stmt.getInt(9);
            int idEmpleado = stmt.getInt(10);

            result = String.format("{\"result\":\"Empleado insertado\", \"idPersona\": %d, \"idUsuario\": %d, \"idEmpleado\": %d}", idPersona, idUsuario, idEmpleado);

        } catch (SQLException e) {
            e.printStackTrace();
            result = "Error al insertar al empleado: " + e.getMessage();
        } finally {
            connMysql.close();
        }

        return result;
    }

    public String deleteEmpleado(int idEmpleado) throws SQLException {
        String result = "Empleado eliminado correctamente";
        ConexionBD connMysql = new ConexionBD();
        Connection conn = null;

        String sql = "{CALL eliminarEmpleadoLogicamente(?)}";

        try {
            conn = connMysql.open();
            try (CallableStatement stmt = (CallableStatement) conn.prepareCall(sql)) {
                stmt.setInt(1, idEmpleado);
                stmt.execute();
            }
        } catch (SQLException e) {
            if (e.getErrorCode() == 45000) {
                result = "Error: " + e.getMessage();
            } else {
                result = "Error al eliminar al empleado: " + e.getMessage();
            }
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
        return result;
    }

    public String updateEmpleado(int idEmpleado, String nombre, String apellidos, String telefono, 
                                 String nombreCiudad, String nombreUsuario, String contrasenia, String nombreSucursal) throws SQLException {
        String result = "Empleado actualizado correctamente";

        ConexionBD connMysql = new ConexionBD();
        Connection conn = connMysql.open();

        String sql = "{CALL updateEmpleado(?, ?, ?, ?, ?, ?, ?, ?)}";

        try (CallableStatement stmt = (CallableStatement) conn.prepareCall(sql)) {
            stmt.setInt(1, idEmpleado);
            stmt.setString(2, nombre);
            stmt.setString(3, apellidos);
            stmt.setString(4, telefono);
            stmt.setString(5, nombreCiudad); 
            stmt.setString(6, nombreUsuario); 
            stmt.setString(7, contrasenia);   
            stmt.setString(8, nombreSucursal); 

            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            result = "Error al actualizar el empleado: " + e.getMessage();
        } finally {
            connMysql.close();
        }

        return result;
    }
}
