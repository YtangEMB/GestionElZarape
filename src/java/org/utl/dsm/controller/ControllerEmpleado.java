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

        // Asumiendo que los nombres de las columnas en la vista son correctos
        empleado.setIdEmpleado(rs.getInt("idPersona")); // Asegúrate que idEmpleado corresponde a idPersona
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
        empleado.setEstado(estado); // Si es necesario, dependiendo de tu diseño

        return empleado;
    }

    public String insertEmpleado(String nombre, String apellidos, String telefono, int idCiudad, String nombreUsuario, String contrasenia, int idSucursal, int idPersona, int idUsuario, int idEmpleado) throws SQLException {
        String result = "Empleado insertado correctamente";

        ConexionBD connMysql = new ConexionBD();
        Connection conn = connMysql.open();

        String sql = "{CALL insertarEmpleado(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
        try (CallableStatement stmt = (CallableStatement) conn.prepareCall(sql)) {
            stmt.setString(1, nombre);
            stmt.setString(2, apellidos);
            stmt.setString(3, telefono);
            stmt.setInt(4, idCiudad);
            stmt.setString(5, nombreUsuario);
            stmt.setString(6, contrasenia);
            stmt.setInt(7, idSucursal);
            stmt.setInt(8, idPersona);
            stmt.setInt(9, idUsuario);
            stmt.setInt(10, idEmpleado);

            stmt.execute();
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

    public String updateEmpleado(int idEmpleado, String nombre, String apellidos, String telefono, int idCiudad, String nombreUsuario, String contrasenia, int idSucursal) throws SQLException {
        String result = "Empleado actualizado correctamente";

        ConexionBD connMysql = new ConexionBD();
        Connection conn = connMysql.open();

        String sql = "{CALL updateEmpleado(?, ?, ?, ?, ?, ?, ?, ?)}";
        try (CallableStatement stmt = (CallableStatement) conn.prepareCall(sql)) {
            stmt.setInt(1, idEmpleado);
            stmt.setString(2, nombre);
            stmt.setString(3, apellidos);
            stmt.setString(4, telefono);
            stmt.setInt(5, idCiudad);
            stmt.setString(6, nombreUsuario);
            stmt.setString(7, contrasenia);
            stmt.setInt(8, idSucursal);

            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            result = "Error al actualizar al empleado: " + e.getMessage();
        } finally {
            connMysql.close();
        }
        return result;
    }

}
