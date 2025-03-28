package org.utl.dsm.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.codec.digest.DigestUtils;
import org.utl.dsm.db.ConexionBD;
import org.utl.dsm.model.Ciudad;
import org.utl.dsm.model.Cliente;
import org.utl.dsm.model.Empleado;
import org.utl.dsm.model.Estado;
import org.utl.dsm.model.Persona;
import org.utl.dsm.model.Sucursal;
import org.utl.dsm.model.Usuario;

public class ControllerLogin {

    public Empleado validarLogin(String usuario, String contrasenia) throws SQLException {
        String sql = "SELECT * FROM viewUsuariosE WHERE usuario = ?";
        ConexionBD connMysql = new ConexionBD();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Empleado empleado = null;

        try {
            conn = connMysql.open();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, usuario);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                // Obtenemos el hash de la contraseña almacenada
                String contraseniaHash = rs.getString("contrasenia");

                // Verificamos si la contraseña proporcionada coincide con el hash
                if (SecurityUtil.verifyPassword(contrasenia, contraseniaHash)) {
                    empleado = fillFromView(rs);
                }
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
        return empleado;
    }

    private Empleado fillFromView(ResultSet rs) throws SQLException {
        Persona persona = new Persona();
        Ciudad ciudad = new Ciudad();
        Usuario usuario = new Usuario();
        Estado estado = new Estado();
        Empleado empleado = new Empleado();
        Sucursal sucursal = new Sucursal();

        empleado.setIdEmpleado(rs.getInt("idEmpleado"));
        usuario.setNombre(rs.getString("usuario"));
        usuario.setContrasenia(rs.getString("contrasenia"));
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

    public String checkUsers(String nombreU) throws Exception {
        String sql = "select * from usuario where nombre = '" + nombreU + "';";
        ConexionBD connMySQL = new ConexionBD();
        Connection conn = connMySQL.open();
        PreparedStatement pstmt = conn.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery();
        String name = null;
        String tok = null; // almacena el token de la BD (en caso de que exista)
        String tokenizer = null; // para almacenar el hash del token generado
        Date myDate = new Date(); // Fecha actual, para registrar fecha de acceso
        String fecha = new SimpleDateFormat("yyyy.MM.dd.HH:mm:ss").format(myDate);
        String sql2 = "";

        while (rs.next()) {
            name = rs.getString("nombre");
            tok = rs.getString(6); // lastToken
            tok = tok.trim(); // quitar espacios

            if (!tok.isEmpty()) { // Si tiene un hash
                tokenizer = tok; // solo asígnalo
                sql2 = "UPDATE usuario SET dateLastToken = '" + fecha + "' WHERE nombre = '" + name + "';";
            } else { // Si no, generale un token
                // Puedes darle la complejidad que quieras
                String token = "ZARAPE" + "." + name + "." + fecha;
                tokenizer = DigestUtils.md5Hex(token); // usamos el método más básico para que sea rápido
                sql2 = "UPDATE usuario SET lastToken= '" + tokenizer + "', dateLastToken = '" + fecha + "' WHERE nombre = '" + name + "';";
            }

            Connection connect = connMySQL.open();
            PreparedStatement ps = connect.prepareStatement(sql2);
            ps.executeUpdate();

            return tokenizer;
        }
        return name;
    }

    public boolean deleteToken(String nombreU) throws Exception {
        String sqlCheck = "SELECT lastToken FROM usuario WHERE nombre = ?;";
        String sqlUpdate = "UPDATE usuario SET lastToken = '' WHERE nombre = ?;";

        ConexionBD connMySQL = new ConexionBD();
        Connection conn = connMySQL.open();

        try (PreparedStatement pstmtCheck = conn.prepareStatement(sqlCheck)) {
            pstmtCheck.setString(1, nombreU);
            ResultSet rs = pstmtCheck.executeQuery();

            if (rs.next()) {
                String token = rs.getString("lastToken");

                if (token != null && !token.trim().isEmpty()) {
                    try (PreparedStatement pstmtUpdate = conn.prepareStatement(sqlUpdate)) {
                        pstmtUpdate.setString(1, nombreU);
                        int rowsUpdated = pstmtUpdate.executeUpdate();
                        return rowsUpdated > 0;
                    }
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } finally {
            conn.close();
        }
    }

    public boolean verificarToken(String usuario, String token) throws SQLException {
        String sql = "SELECT COUNT(*) FROM viewToken WHERE usuario = ? AND token = ?";
        ConexionBD connMysql = new ConexionBD();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        boolean valido = false;

        try {
            conn = connMysql.open();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, usuario);
            pstmt.setString(2, token);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                valido = rs.getInt(1) > 0;
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

        return valido;
    }

    public Cliente validarLoginC(String usuario, String contrasenia) throws SQLException {
        String sql = "SELECT * FROM viewUsuariosC WHERE usuario = ? AND contrasenia = ?";
        ConexionBD connMysql = new ConexionBD();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Cliente cliente = null;

        try {
            conn = connMysql.open();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, usuario);
            pstmt.setString(2, contrasenia);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                cliente = fillFromViewC(rs);
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
        return cliente;
    }

    private Cliente fillFromViewC(ResultSet rs) throws SQLException {
        Persona persona = new Persona();
        Ciudad ciudad = new Ciudad();
        Usuario usuario = new Usuario();
        Estado estado = new Estado();
        Cliente cliente = new Cliente();

        cliente.setIdCliente(rs.getInt("idCliente"));
        usuario.setNombre(rs.getString("usuario"));
        usuario.setContrasenia(rs.getString("contrasenia"));
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
}
