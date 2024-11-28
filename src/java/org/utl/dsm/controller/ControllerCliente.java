package org.utl.dsm.controller;

import com.mysql.cj.jdbc.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.sql.SQLException;
import org.utl.dsm.model.Cliente;
import org.utl.dsm.db.ConexionBD;
import org.utl.dsm.model.Ciudad;
import org.utl.dsm.model.Empleado;
import org.utl.dsm.model.Estado;
import org.utl.dsm.model.Persona;
import org.utl.dsm.model.Sucursal;
import org.utl.dsm.model.Usuario;

public class ControllerCliente {

    public List<Cliente> getAllClienteFromView() throws SQLException {
        String sql = "SELECT * FROM viewCliente;";
        ConexionBD connMysql = new ConexionBD();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Cliente> listaCliente = new ArrayList<>();

        try {
            conn = connMysql.open();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                listaCliente.add(fillFromView(rs));
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

        return listaCliente;
    }

    private Cliente fillFromView(ResultSet rs) throws SQLException {
        Persona persona = new Persona();
        Ciudad ciudad = new Ciudad();
        Usuario usuario = new Usuario();
        Estado estado = new Estado();
        Cliente cliente = new Cliente();

        cliente.setIdCliente(rs.getInt("idPersona")); 
        usuario.setNombre(rs.getString("usuario"));
        persona.setNombre(rs.getString("nombre"));
        persona.setApellidos(rs.getString("apellidos"));
        persona.setTelefono(rs.getString("telefono"));
        estado.setNombre(rs.getString("estado"));
        ciudad.setNombre(rs.getString("ciudad"));

        cliente.setUsuario(usuario);
        cliente.setPersona(persona);
        cliente.setCiudad(ciudad);
        cliente.setEstado(estado); 
        return cliente;
    }

    public String insertarCliente(String nombre, String apellidos, String telefono, String nombreCiudad, String nombreUsuario, String contrasenia) throws SQLException {
        String result = "Cliente insertado correctamente";

        ConexionBD connMysql = new ConexionBD();
        Connection conn = connMysql.open();

        String sql = "{CALL insertarCliente(?, ?, ?, ?, ?, ?, ?, ?, ?)}";
        try (CallableStatement stmt = (CallableStatement) conn.prepareCall(sql)) {
            stmt.setString(1, nombre);
            stmt.setString(2, apellidos);
            stmt.setString(3, telefono);
            stmt.setString(4, nombreCiudad);
            stmt.setString(5, nombreUsuario);
            stmt.setString(6, contrasenia);

            stmt.registerOutParameter(7, java.sql.Types.INTEGER); // ID Persona
            stmt.registerOutParameter(8, java.sql.Types.INTEGER); // ID Usuario
            stmt.registerOutParameter(9, java.sql.Types.INTEGER); // ID Cliente

            stmt.execute();

            int idPersona = stmt.getInt(7);
            int idUsuario = stmt.getInt(8);
            int idCliente = stmt.getInt(9);

            result = String.format("{\"result\":\"Cliente insertado\", \"idPersona\": %d, \"idUsuario\": %d, \"idCliente\": %d}", idPersona, idUsuario, idCliente);

        } catch (SQLException e) {
            e.printStackTrace();
            result = "Error al insertar al cliente: " + e.getMessage();
        } finally {
            connMysql.close();
        }

        return result;
    }

    public String deleteCliente(int idCliente) throws SQLException {
        String result = "Cliente eliminado correctamente";
        ConexionBD connMysql = new ConexionBD();
        Connection conn = null;

        String sql = "{CALL eliminarClienteLogicamente(?)}";

        try {
            conn = connMysql.open();
            try (CallableStatement stmt = (CallableStatement) conn.prepareCall(sql)) {
                stmt.setInt(1, idCliente);
                stmt.execute();
            }
        } catch (SQLException e) {
            result = "Error al eliminar al cliente: " + e.getMessage();
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
        return result;
    }

    public String updateCliente(int idCliente, String nombre, String apellidos, String telefono, 
                                 String nombreCiudad, String nombreUsuario, String contrasenia) throws SQLException {
        String result = "Cliente actualizado correctamente";

        ConexionBD connMysql = new ConexionBD();
        Connection conn = connMysql.open();

        // Llamada al procedimiento almacenado
        String sql = "{CALL actualizarCliente(?, ?, ?, ?, ?, ?, ?)}";

        try (CallableStatement stmt = (CallableStatement) conn.prepareCall(sql)) {
            stmt.setInt(1, idCliente);
            stmt.setString(2, nombre);
            stmt.setString(3, apellidos);
            stmt.setString(4, telefono);
            stmt.setString(5, nombreCiudad);  // Ciudad de cliente
            stmt.setString(6, nombreUsuario); // Nombre de usuario
            stmt.setString(7, contrasenia);   // Contraseña de usuario

            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            result = "Error al actualizar el cliente: " + e.getMessage();
        } finally {
            connMysql.close();
        }

        return result;
    }
}
